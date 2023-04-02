package com.feed_the_beast.ftblib.lib.gui.misc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.HashMap;

import javax.annotation.Nullable;

import cpw.mods.fml.common.network.IGuiHandler;

/**
 * @author LatvianModder
 */
public class BlockGuiHandler implements IGuiHandler
{
	private final HashMap<Integer, BlockGuiSupplier> map = new HashMap<>();

	public void add(BlockGuiSupplier h)
	{
		map.put(h.id, h);
	}

	@Nullable
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		BlockGuiSupplier supplier = map.get(ID);

		if (supplier != null)
		{
			TileEntity tileEntity = world.getTileEntity(x, y, z);

			if (tileEntity != null)
			{
				return supplier.getContainer(player, tileEntity);
			}
		}

		return null;
	}

	@Nullable
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		BlockGuiSupplier supplier = map.get(ID);

		if (supplier != null)
		{
			TileEntity tileEntity = world.getTileEntity(x, y, z);

			if (tileEntity != null)
			{
				Container container = supplier.getContainer(player, tileEntity);
				return container == null ? null : supplier.getGui(container);
			}
		}

		return null;
	}
}