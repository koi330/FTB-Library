package com.feed_the_beast.ftblib.net;

import net.minecraft.entity.player.EntityPlayerMP;

import com.feed_the_beast.ftblib.FTBLibGameRules;
import com.feed_the_beast.ftblib.lib.data.FTBLibAPI;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftblib.lib.net.MessageToServer;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;

/**
 * @author LatvianModder
 */
public class MessageMyTeamGui extends MessageToServer {

    @Override
    public NetworkWrapper getWrapper() {
        return FTBLibNetHandler.MY_TEAM;
    }

    @Override
    public void onMessage(EntityPlayerMP player) {
        if (!FTBLibGameRules.canCreateTeam(player.worldObj) && !FTBLibGameRules.canJoinTeam(player.worldObj)) {
            FTBLibAPI.sendCloseGuiPacket(player);
        } else {
            ForgePlayer p = Universe.get().getPlayer(player);
            (p.hasTeam() ? new MessageMyTeamGuiResponse(p)
                    : new MessageSelectTeamGui(p, FTBLibGameRules.canCreateTeam(player.worldObj))).sendTo(player);
        }
    }
}
