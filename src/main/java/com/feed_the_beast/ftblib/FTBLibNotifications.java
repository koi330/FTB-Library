package com.feed_the_beast.ftblib;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ResourceLocation;

import com.feed_the_beast.ftblib.lib.util.text_components.Notification;

/**
 * @author LatvianModder
 */
public class FTBLibNotifications {

    public static final ResourceLocation RELOAD_SERVER = new ResourceLocation(FTBLib.MOD_ID, "reload_server");
    public static final Notification NO_TEAM = Notification.of(
            new ResourceLocation(FTBLib.MOD_ID, "no_team"),
            new ChatComponentTranslation("ftblib.lang.team.error.no_team")).setError();
}
