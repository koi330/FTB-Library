package com.feed_the_beast.ftblib.events.client;

import com.feed_the_beast.ftblib.events.FTBLibEvent;

import cpw.mods.fml.common.eventhandler.Cancelable;
import net.minecraft.util.ResourceLocation;

/**
 * @author LatvianModder
 */
@Cancelable
public class CustomClickEvent extends FTBLibEvent
{
	private final ResourceLocation id;

	public CustomClickEvent(ResourceLocation _id)
	{
		id = _id;
	}

	public ResourceLocation getID()
	{
		return id;
	}
}