package com.feed_the_beast.ftblib;

import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftblib.lib.util.ServerUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * @author LatvianModder
 */
// @Mod.EventBusSubscriber(modid = FTBLib.MOD_ID)
public class FTBLibEventHandler
{
	public static final FTBLibEventHandler INST = new FTBLibEventHandler();
	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event)
	{
		if (event.player.ticksExisted % 5 == 2 && event.player instanceof EntityPlayerMP)
		{
			byte opState = event.player.getEntityData().getByte("FTBLibOP");
			byte newOpState = ServerUtils.isOP((EntityPlayerMP) event.player) ? (byte) 2 : (byte) 1;

			if (opState != newOpState)
			{
				event.player.getEntityData().setByte("FTBLibOP", newOpState);
				Universe.get().clearCache();
			}
		}
	}
}
