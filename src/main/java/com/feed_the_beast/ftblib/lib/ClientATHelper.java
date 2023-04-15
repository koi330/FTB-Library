package com.feed_the_beast.ftblib.lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * @author LatvianModder
 */
public final class ClientATHelper
{
	@Nullable
	public static ResourceLocation getFontUnicodePage(int page)
	{
		return FontRenderer.unicodePageLocations[page];
	}

	// public static Map<ChatType, List<IChatListener>> getChatListeners()
	// {
	// 	return Minecraft.getMinecraft().ingameGUI.chatListeners;
	// }

	public static Map<String, TextureAtlasSprite> getRegisteredSpritesMap()
	{
		return Minecraft.getMinecraft().getTextureMapBlocks().mapRegisteredSprites;
	}
}
