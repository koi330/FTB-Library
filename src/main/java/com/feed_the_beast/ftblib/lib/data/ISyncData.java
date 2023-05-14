package com.feed_the_beast.ftblib.lib.data;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author LatvianModder
 */
public interface ISyncData {

    NBTTagCompound writeSyncData(EntityPlayerMP player, ForgePlayer forgePlayer);

    @SideOnly(Side.CLIENT)
    void readSyncData(NBTTagCompound nbt);
}
