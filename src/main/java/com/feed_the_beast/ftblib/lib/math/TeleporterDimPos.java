package com.feed_the_beast.ftblib.lib.math;

import javax.annotation.Nullable;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.FTBLibConfig;
import com.feed_the_beast.ftblib.lib.util.ITeleporter;
import com.feed_the_beast.ftblib.lib.util.ServerUtils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

/**
 * @author LatvianModder
 */
public class TeleporterDimPos implements ITeleporter {
	public final double posX, posY, posZ;
	public final int dim;

	public static TeleporterDimPos of(double x, double y, double z, int dim) {
		return new TeleporterDimPos(x, y, z, dim);
	}

	public static TeleporterDimPos of(Entity entity) {
		return new TeleporterDimPos(entity.posX, entity.posY, entity.posZ, entity.dimension);
	}

	public static TeleporterDimPos of(int posx, int posy, int posz, int dim) {
		return new TeleporterDimPos(posx + 0.5D, posy + 0.1D, posz + 0.5D, dim);
	}

	private TeleporterDimPos(double x, double y, double z, int d) {
		posX = x;
		posY = y;
		posZ = z;
		dim = d;
	}

	public BlockDimPos block() {
		return new BlockDimPos(posX, posY, posZ, dim);
	}

	@Override
	public void placeEntity(World world, Entity entity, float yaw) {
		entity.motionX = entity.motionY = entity.motionZ = 0D;
		entity.fallDistance = 0F;

		if (entity instanceof EntityPlayerMP && ((EntityPlayerMP) entity).playerNetServerHandler != null) {
			((EntityPlayerMP) entity).playerNetServerHandler.setPlayerLocation(posX, posY, posZ, yaw, entity.rotationPitch);
		} else {
			entity.setLocationAndAngles(posX, posY, posZ, yaw, entity.rotationPitch);
		}
	}

	@Nullable
	public Entity teleport(@Nullable Entity entity) {
		if (entity == null || entity.worldObj.isRemote) {
			return entity;
		}

		if (FTBLibConfig.debugging.log_teleport) {
			FTBLib.LOGGER.info("Teleporting '" + entity.getCommandSenderName() + "' to [" + posX + ',' + posY + ',' + posZ + "] in "
					+ ServerUtils.getDimensionName(dim).getUnformattedText());
		}

		if (dim != entity.dimension) {
			entity.travelToDimension(dim);
			return entity;
		}

		placeEntity(entity.worldObj, entity, entity.rotationYaw);
		return entity;
	}
}