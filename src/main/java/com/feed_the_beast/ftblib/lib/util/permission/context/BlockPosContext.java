/*
 * Minecraft Forge
 * Copyright (c) 2016-2020.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package com.feed_the_beast.ftblib.lib.util.permission.context;

import com.google.common.base.Preconditions;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCoordIntPair;

import javax.annotation.Nullable;

public class BlockPosContext extends PlayerContext
{
    private final int xpos;
    private final int ypos;
    private final int zpos;
    private EnumFacing facing;

    public BlockPosContext(EntityPlayer ep, int x, int y, int z, @Nullable EnumFacing f)
    {
        super(ep);
        xpos = x;
        ypos = y;
        zpos = z;
        facing = f;
    }

    public BlockPosContext(EntityPlayer ep, ChunkCoordIntPair pos)
    {
        this(ep, pos.getCenterXPos(), 0, pos.getCenterZPosition(), null);
    }

    @Override
    @Nullable
    public <T> T get(ContextKey<T> key)
    {
        if(key.equals(ContextKeys.POS))
        {
            return (T) Vec3.createVectorHelper(xpos, ypos, zpos);
        }
        // else if(key.equals(ContextKeys.BLOCK_STATE))
        // {
        //     if(blockState == null)
        //     {
        //         blockState = getWorld().getBlockState(blockPos);
        //     }

        //     return (T) blockState;
        // }
        else if(key.equals(ContextKeys.FACING))
        {
            return (T) facing;
        }

        return super.get(key);
    }

    @Override
    protected boolean covers(ContextKey<?> key)
    {
        return key.equals(ContextKeys.POS) || /*key.equals(ContextKeys.BLOCK_STATE) ||*/ (facing != null && key.equals(ContextKeys.FACING));
    }
}