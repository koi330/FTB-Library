package com.feed_the_beast.ftblib.lib.data;

import com.feed_the_beast.ftblib.lib.icon.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentTranslation;

/**
 * @author LatvianModder
 */
public abstract class TeamAction extends Action {
	public TeamAction(String mod, String id, Icon icon, int order) {
		super(new ResourceLocation(mod, id), new ChatComponentTranslation("team_action." + mod + "." + id), icon,
				order);
	}

	@Override
	public TeamAction setTitle(IChatComponent t) {
		super.setTitle(t);
		return this;
	}

	@Override
	public TeamAction setRequiresConfirm() {
		super.setRequiresConfirm();
		return this;
	}
}