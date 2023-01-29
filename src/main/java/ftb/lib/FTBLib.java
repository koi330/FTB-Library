package ftb.lib;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

import latmod.lib.*;
import latmod.lib.net.*;

import net.minecraft.block.Block;
import net.minecraft.command.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.Container;
import net.minecraft.item.*;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.*;

import org.apache.logging.log4j.*;

import com.google.gson.JsonElement;
import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.*;
import cpw.mods.fml.relauncher.Side;
import ftb.lib.api.*;
import ftb.lib.api.config.ConfigRegistry;
import ftb.lib.api.notification.Notification;
import ftb.lib.api.tile.IGuiTile;
import ftb.lib.mod.*;
import ftb.lib.mod.net.*;

public class FTBLib {

    public static final boolean DEV_ENV = ((Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment"))
            .booleanValue();
    public static boolean userIsLatvianModder = false;
    public static final Logger dev_logger = LogManager.getLogger("FTBLibDev");
    public static final String FORMATTING = "\u00a7";
    public static final Pattern textFormattingPattern = Pattern.compile("(?i)" + FORMATTING + "[0-9A-FK-OR]");
    private static final HashMap<String, UUID> cachedUUIDs = new HashMap<>();
    public static FTBUIntegration ftbu = null;

    public static final EnumChatFormatting[] chatColors = new EnumChatFormatting[] { EnumChatFormatting.BLACK,
            EnumChatFormatting.DARK_BLUE, EnumChatFormatting.DARK_GREEN, EnumChatFormatting.DARK_AQUA,
            EnumChatFormatting.DARK_RED, EnumChatFormatting.DARK_PURPLE, EnumChatFormatting.GOLD,
            EnumChatFormatting.GRAY, EnumChatFormatting.DARK_GRAY, EnumChatFormatting.BLUE, EnumChatFormatting.GREEN,
            EnumChatFormatting.AQUA, EnumChatFormatting.RED, EnumChatFormatting.LIGHT_PURPLE, EnumChatFormatting.YELLOW,
            EnumChatFormatting.WHITE, };

    public static File folderConfig;
    public static File folderMinecraft;
    public static File folderModpack;
    public static File folderLocal;
    public static File folderWorld = null;

    public static void init(File configFolder) {
        folderConfig = configFolder;
        folderMinecraft = folderConfig.getParentFile();
        folderModpack = new File(folderMinecraft, "modpack/");
        folderLocal = new File(folderMinecraft, "local/");

        if (!folderModpack.exists()) folderModpack.mkdirs();
        if (!folderLocal.exists()) folderLocal.mkdirs();

        if (dev_logger instanceof org.apache.logging.log4j.core.Logger) {
            if (DEV_ENV) ((org.apache.logging.log4j.core.Logger) dev_logger).setLevel(Level.ALL);
            else((org.apache.logging.log4j.core.Logger) dev_logger).setLevel(Level.OFF);
        } else FTBLibMod.logger
                .info("DevLogger isn't org.apache.logging.log4j.core.Logger! It's " + dev_logger.getClass().getName());
    }

    public static void reload(ICommandSender sender, boolean printMessage, boolean reloadClient) {
        if (FTBWorld.server == null) return;

        long ms = LMUtils.millis();
        ConfigRegistry.reload();
        GameModes.reload();

        EventFTBReload event = new EventFTBReload(FTBWorld.server, sender, reloadClient);
        if (ftbu != null) ftbu.onReloaded(event);
        event.post();

        if (printMessage) printChat(
                BroadcastSender.inst,
                FTBLibMod.mod.chatComponent("reloaded_server", ((LMUtils.millis() - ms) + "ms")));
        new MessageReload(FTBWorld.server, 2).sendTo(null);
    }

    public static IChatComponent getChatComponent(Object o) {
        return (o != null && o instanceof IChatComponent) ? (IChatComponent) o : new ChatComponentText("" + o);
    }

    public static void addItem(Item i, String name) {
        GameRegistry.registerItem(i, name);
    }

    public static void addBlock(Block b, Class<? extends ItemBlock> c, String name) {
        GameRegistry.registerBlock(b, c, name);
    }

    public static void addBlock(Block b, String name) {
        addBlock(b, ItemBlock.class, name);
    }

    public static void addTileEntity(Class<? extends TileEntity> c, String s, String... alt) {
        if (alt == null || alt.length == 0) GameRegistry.registerTileEntity(c, s);
        else GameRegistry.registerTileEntityWithAlternatives(c, s, alt);
    }

    public static void addEntity(Class<? extends Entity> c, String s, int id, Object mod) {
        EntityRegistry.registerModEntity(c, s, id, mod, 50, 1, true);
    }

    public static void addWorldGenerator(IWorldGenerator i, int w) {
        GameRegistry.registerWorldGenerator(i, w);
    }

    public static Fluid addFluid(Fluid f) {
        Fluid f1 = FluidRegistry.getFluid(f.getName());
        if (f1 != null) return f1;
        FluidRegistry.registerFluid(f);
        return f;
    }

    public static void addCommand(FMLServerStartingEvent e, ICommand c) {
        if (c != null && !c.getCommandName().isEmpty()) e.registerServerCommand(c);
    }

    /**
     * Prints message to chat (doesn't translate it)
     */
    public static void printChat(ICommandSender ep, Object o) {
        if (ep == null) ep = FTBLibMod.proxy.getClientPlayer();
        if (ep != null) ep.addChatMessage(getChatComponent(o));
        else System.out.println(o);
    }

    public static MinecraftServer getServer() {
        return FMLCommonHandler.instance().getMinecraftServerInstance();
    }

    public static Side getEffectiveSide() {
        return FMLCommonHandler.instance().getEffectiveSide();
    }

    public static boolean isDedicatedServer() {
        MinecraftServer mcs = getServer();
        return (mcs == null) ? false : mcs.isDedicatedServer();
    }

    public static String getPath(ResourceLocation res) {
        return "/assets/" + res.getResourceDomain() + "/" + res.getResourcePath();
    }

    public static boolean resourceExists(ResourceLocation res) {
        return FTBLib.class.getResource(getPath(res)) != null;
    }

    public static boolean hasOnlinePlayers() {
        return !getServer().getConfigurationManager().playerEntityList.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public static List<EntityPlayerMP> getAllOnlinePlayers(EntityPlayerMP except) {
        ArrayList<EntityPlayerMP> l = new ArrayList<>();
        if (getEffectiveSide().isClient()) return l;

        if (hasOnlinePlayers()) {
            l.addAll(getServer().getConfigurationManager().playerEntityList);
            if (except != null) l.remove(except);
        }
        return l;
    }

    public static Map<UUID, EntityPlayerMP> getAllOnlinePlayersMap() {
        HashMap<UUID, EntityPlayerMP> m = new HashMap<>();

        if (!hasOnlinePlayers()) return m;

        for (int i = 0; i < getServer().getConfigurationManager().playerEntityList.size(); i++) {
            EntityPlayerMP ep = (EntityPlayerMP) getServer().getConfigurationManager().playerEntityList.get(i);
            m.put(ep.getUniqueID(), ep);
        }

        return m;
    }

    public static EntityPlayerMP getPlayerMP(UUID id) {
        if (!hasOnlinePlayers()) return null;

        for (int i = 0; i < getServer().getConfigurationManager().playerEntityList.size(); i++) {
            EntityPlayerMP ep = (EntityPlayerMP) getServer().getConfigurationManager().playerEntityList.get(i);
            if (ep.getUniqueID().equals(id)) return ep;
        }

        return null;
    }

    public static List<String> getPlayerNames(boolean online) {
        if (ftbu != null) return Arrays.asList(ftbu.getPlayerNames(online));
        ArrayList<String> l = new ArrayList<>();

        if (online) {
            List<EntityPlayerMP> players = getAllOnlinePlayers(null);
            for (int j = 0; j < players.size(); j++) l.add(players.get(j).getGameProfile().getName());
        } else l.addAll(UsernameCache.getMap().values());
        return l;
    }

    public static boolean remap(FMLMissingMappingsEvent.MissingMapping m, String id, Item i) {
        if (m.type == GameRegistry.Type.ITEM && id.equals(m.name)) {
            m.remap(i);
            return true;
        }

        return false;
    }

    public static boolean remap(FMLMissingMappingsEvent.MissingMapping m, String id, Block b) {
        if (id.equals(m.name)) {
            if (m.type == GameRegistry.Type.BLOCK) m.remap(b);
            else if (m.type == GameRegistry.Type.ITEM) m.remap(Item.getItemFromBlock(b));
            return true;
        }

        return false;
    }

    public static boolean isModInstalled(String s) {
        return Loader.isModLoaded(s);
    }

    public static String removeFormatting(String s) {
        if (s == null) return null;
        if (s.isEmpty()) return "";
        return textFormattingPattern.matcher(s).replaceAll("");
    }

    public static WorldServer getServerWorld() {
        MinecraftServer ms = getServer();
        if (ms == null || ms.worldServers.length == 0) return null;
        return ms.worldServers[0];
    }

    public static int runCommand(ICommandSender ics, String s) throws CommandException {
        return getServer().getCommandManager().executeCommand(ics, s);
    }

    public static int runCommand(ICommandSender ics, String cmd, String[] args) throws CommandException {
        StringBuilder sb = new StringBuilder();
        sb.append(cmd);
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                sb.append(' ');
                sb.append(args[i]);
            }
        }

        return runCommand(ics, sb.toString());
    }

    public static IChatComponent setColor(EnumChatFormatting e, IChatComponent c) {
        c.getChatStyle().setColor(e);
        return c;
    }

    public static void openGui(EntityPlayer ep, IGuiTile t, NBTTagCompound data) {
        if (t == null || !(t instanceof TileEntity) || ep instanceof FakePlayer) return;
        else if (ep instanceof EntityPlayerMP) {
            Container c = t.getContainer(ep, data);
            if (c == null) return;

            EntityPlayerMP epM = (EntityPlayerMP) ep;
            epM.getNextWindowId();
            epM.closeContainer();
            epM.openContainer = c;
            epM.openContainer.windowId = epM.currentWindowId;
            epM.openContainer.addCraftingToCrafters(epM);
            new MessageOpenGuiTile((TileEntity) t, data, epM.currentWindowId).sendTo(epM);
        } else if (!getEffectiveSide().isServer())
            FTBLibMod.proxy.openClientTileGui((ep == null) ? FTBLibMod.proxy.getClientPlayer() : ep, t, data);
    }

    public static void addCallback(ServerTickCallback c) {
        if (c.maxTick == 0) c.onCallback();
        else FTBLibEventHandler.pendingCallbacks.add(c);
    }

    public static boolean isOP(GameProfile p) {
        return !isDedicatedServer() || LMStringUtils
                .contains(getServer().getConfigurationManager().func_152603_m().func_152685_a(), p.getName());
    }

    public static void notifyPlayer(EntityPlayerMP ep, Notification n) {
        new MessageNotifyPlayer(n).sendTo(ep);
    }

    @SuppressWarnings("all")
    public static List<ICommand> getAllCommands(ICommandSender sender) {
        ArrayList<ICommand> commands = new ArrayList<>();
        ArrayList<String> cmdIDs = new ArrayList<>();

        for (Object o : getServer().getCommandManager().getPossibleCommands(sender)) {
            ICommand c = (ICommand) o;
            if (!cmdIDs.contains(c.getCommandName())) {
                commands.add(c);
                cmdIDs.add(c.getCommandName());
            }
        }

        return commands;
    }

    public static UUID getPlayerID(String s) {
        if (s == null || s.isEmpty()) return null;

        String key = s.trim().toLowerCase();

        if (!cachedUUIDs.containsKey(key)) {
            cachedUUIDs.put(key, null);

            try {
                String json = new LMURLConnection(
                        RequestMethod.GET,
                        "https://api.mojang.com/users/profiles/minecraft/" + s).connect().asString();
                JsonElement e = LMJsonUtils.fromJson(json);
                cachedUUIDs.put(key, LMUtils.fromString(e.getAsJsonObject().get("id").getAsString()));
            } catch (Exception e) {}
        }

        return cachedUUIDs.get(key);
    }
}
