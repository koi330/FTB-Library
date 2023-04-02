package com.feed_the_beast.ftblib.lib.util;

import com.feed_the_beast.ftblib.lib.EnumDyeColor;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public class EnumDyeColorHelper // ItemDye
{
	public static final EnumDyeColorHelper[] HELPERS = new EnumDyeColorHelper[EnumDyeColor.values().length];

	static
	{
		for (EnumDyeColor c : EnumDyeColor.values())
		{
			HELPERS[c.ordinal()] = new EnumDyeColorHelper(c);
		}
	}

	private final EnumDyeColor dye;
	private final String langKey;
	private final String oreName;

	private EnumDyeColorHelper(EnumDyeColor col)
	{
		dye = col;
		langKey = "item.fireworksCharge." + col.unlocalizedName;
		oreName = StringUtils.firstUppercase(col.unlocalizedName);
	}

	public static EnumDyeColorHelper get(EnumDyeColor dye)
	{
		return HELPERS[dye.ordinal()];
	}

	public ItemStack getDye(int s)
	{
		return dye.getDye(s);
	}

	@Override
	public String toString()
	{
		return dye.name;
	}

	@Override
	public int hashCode()
	{
		return dye.ordinal();
	}

	public EnumDyeColor getDye()
	{
		return dye;
	}

	public String getLangKey()
	{
		return langKey;
	}

	public String getOreName()
	{
		return oreName;
	}

	public String getDyeName()
	{
		return dye.dyeName;
	}

	public String getGlassName()
	{
		return dye.glassName;
	}

	public String getPaneName()
	{
		return dye.paneName;
	}
}