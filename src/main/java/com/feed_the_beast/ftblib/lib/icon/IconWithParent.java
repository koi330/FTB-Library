package com.feed_the_beast.ftblib.lib.icon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author LatvianModder
 */
public abstract class IconWithParent extends Icon {

    public final Icon parent;

    public IconWithParent(Icon i) {
        parent = i;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void bindTexture() {
        parent.bindTexture();
    }
}
