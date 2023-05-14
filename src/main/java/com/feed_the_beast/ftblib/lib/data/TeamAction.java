package com.feed_the_beast.ftblib.lib.data;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;

import com.feed_the_beast.ftblib.lib.icon.Icon;

/**
 * @author LatvianModder
 */
public abstract class TeamAction extends Action {

    public TeamAction(String mod, String id, Icon icon, int order) {
        super(
                new ResourceLocation(mod, id),
                new ChatComponentTranslation("team_action." + mod + "." + id),
                icon,
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
