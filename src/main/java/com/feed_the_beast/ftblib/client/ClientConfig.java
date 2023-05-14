package com.feed_the_beast.ftblib.client;

import javax.annotation.Nullable;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.util.JsonUtils;
import com.google.gson.JsonObject;

/**
 * @author LatvianModder
 */
public class ClientConfig {

    public final String id;
    public final IChatComponent name;
    public final Icon icon;

    public ClientConfig(String _id, @Nullable IChatComponent _name, Icon _icon) {
        id = _id;
        name = _name == null ? new ChatComponentText(id) : _name;
        icon = _icon;
    }

    public ClientConfig(JsonObject o) {
        this(o.get("id").getAsString(), JsonUtils.deserializeTextComponent(o.get("name")), Icon.getIcon(o.get("icon")));
    }
}
