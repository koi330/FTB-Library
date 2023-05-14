package com.feed_the_beast.ftblib;

import net.minecraft.entity.player.EntityPlayerMP;

import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftblib.lib.util.ServerUtils;
import com.feed_the_beast.ftblib.net.MessageSyncData;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

/**
 * @author LatvianModder
 */
public class FTBLibEventHandler {

    public static final FTBLibEventHandler INST = new FTBLibEventHandler();

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player.ticksExisted % 5 == 2 && event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;
            byte opState = player.getEntityData().getByte("FTBLibOP");
            byte newOpState = ServerUtils.isOP(player) ? (byte) 2 : (byte) 1;

            if (opState != newOpState) {
                player.getEntityData().setByte("FTBLibOP", newOpState);
                Universe.get().clearCache();
                ForgePlayer forgePlayer = Universe.get().getPlayer(player.getGameProfile());
                if (forgePlayer != null) {
                    new MessageSyncData(false, player, forgePlayer).sendTo(player);
                }
            }
        }
    }
}
