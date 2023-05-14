package com.feed_the_beast.ftblib.command.team;

import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.lib.command.CmdBase;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftblib.lib.util.StringUtils;

/**
 * @author LatvianModder
 */
public class CmdInfo extends CmdBase {

    public CmdInfo() {
        super("info", Level.ALL);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            return matchFromIterable(args, Universe.get().getTeams());
        }

        return super.addTabCompletionOptions(sender, args);
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        checkArgs(sender, args, 1);
        ForgeTeam team = Universe.get().getTeam(args[0]);

        if (!team.isValid()) {
            throw FTBLib.error(sender, "ftblib.lang.team.error.not_found", args[0]);
        }

        sender.addChatMessage(
                FTBLib.lang(
                        sender,
                        "commands.team.info.id",
                        StringUtils.color(new ChatComponentText(team.getId()), EnumChatFormatting.BLUE)));
        sender.addChatMessage(
                FTBLib.lang(
                        sender,
                        "commands.team.info.uid",
                        StringUtils.color(
                                new ChatComponentText(team.getUID() + " / " + String.format("%04x", team.getUID())),
                                EnumChatFormatting.BLUE)));
        sender.addChatMessage(
                FTBLib.lang(
                        sender,
                        "commands.team.info.owner",
                        team.getOwner() == null ? "-"
                                : StringUtils.color(team.getOwner().getDisplayName(), EnumChatFormatting.BLUE)));

        IChatComponent component = new ChatComponentText("");
        component.getChatStyle().setColor(EnumChatFormatting.GOLD);
        boolean first = true;

        for (ForgePlayer player : team.getMembers()) {
            if (first) {
                first = false;
            } else {
                component.appendText(", ");
            }

            IChatComponent n = player.getDisplayName();
            n.getChatStyle().setColor(EnumChatFormatting.BLUE);
            component.appendSibling(n);
        }

        sender.addChatMessage(FTBLib.lang(sender, "commands.team.info.members", component));
    }
}
