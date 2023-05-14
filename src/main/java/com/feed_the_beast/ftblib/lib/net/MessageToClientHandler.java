package com.feed_the_beast.ftblib.lib.net;

import com.feed_the_beast.ftblib.FTBLib;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

/**
 * @author LatvianModder
 */
enum MessageToClientHandler implements IMessageHandler<MessageToClient, IMessage> {

    INSTANCE;

    @Override
    public IMessage onMessage(MessageToClient message, MessageContext context) {
        FTBLib.PROXY.handleClientMessage(message);
        return null;
    }
}
