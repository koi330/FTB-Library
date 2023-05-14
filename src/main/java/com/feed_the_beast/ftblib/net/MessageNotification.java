package com.feed_the_beast.ftblib.net;

import com.feed_the_beast.ftblib.client.FTBLibClientEventHandler;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.net.MessageToClient;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import com.feed_the_beast.ftblib.lib.util.text_components.Notification;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IChatComponent;

public class MessageNotification extends MessageToClient {

    private Notification notification;

    public MessageNotification() {}

    public MessageNotification(Notification notification) {
        this.notification = notification;
    }

    @Override
    public NetworkWrapper getWrapper() {
        return FTBLibNetHandler.GENERAL;
    }

    @Override
    public void writeData(DataOut data) {
        data.writeTextComponent(notification);
    }

    @Override
    public void readData(DataIn data) {
        IChatComponent component = data.readTextComponent();
        if (component instanceof Notification) {
            notification = (Notification) component;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onMessage() {
        FTBLibClientEventHandler.INST.onNotify(notification);
    }
}
