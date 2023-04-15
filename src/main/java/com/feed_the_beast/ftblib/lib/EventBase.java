package com.feed_the_beast.ftblib.lib;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.FTBLibConfig;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;

/**
 * @author LatvianModder
 */
public class EventBase extends Event
{
	private boolean canPost = true;

	public boolean post()
	{
		if (canPost)
		{
			canPost = false;
			boolean b = MinecraftForge.EVENT_BUS.post(this);

			if (FTBLibConfig.debugging.log_events)
			{
				FTBLib.LOGGER.info("Event " + getClass().getName() + " fired, cancelled: " + b);
			}

			return b;
		}

		return false;
	}
}
