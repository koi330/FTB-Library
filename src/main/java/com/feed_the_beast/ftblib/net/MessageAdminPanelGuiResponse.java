package com.feed_the_beast.ftblib.net;

import java.util.Collection;

import net.minecraft.client.resources.I18n;

import com.feed_the_beast.ftblib.lib.data.Action;
import com.feed_the_beast.ftblib.lib.gui.misc.GuiActionList;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.net.MessageToClient;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author LatvianModder
 */
public class MessageAdminPanelGuiResponse extends MessageToClient {

    private Collection<Action.Inst> actions;

    public MessageAdminPanelGuiResponse() {}

    public MessageAdminPanelGuiResponse(Collection<Action.Inst> a) {
        actions = a;
    }

    @Override
    public NetworkWrapper getWrapper() {
        return FTBLibNetHandler.GENERAL;
    }

    @Override
    public void writeData(DataOut data) {
        data.writeCollection(actions, Action.Inst.SERIALIZER);
    }

    @Override
    public void readData(DataIn data) {
        actions = data.readCollection(Action.Inst.DESERIALIZER);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onMessage() {
        new GuiActionList(
                I18n.format("sidebar_button.ftblib.admin_panel"),
                actions,
                id -> new MessageAdminPanelAction(id).sendToServer()).openGui();
    }
}
