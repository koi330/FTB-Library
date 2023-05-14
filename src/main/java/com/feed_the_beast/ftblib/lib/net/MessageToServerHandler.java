package com.feed_the_beast.ftblib.lib.net;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.FTBLibConfig;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

/**
 * @author LatvianModder
 */
enum MessageToServerHandler implements IMessageHandler<MessageToServer, IMessage> {

    INSTANCE;

    @Override
    public IMessage onMessage(MessageToServer message, MessageContext context) {
        if (FTBLibConfig.debugging.log_network) {
            FTBLib.LOGGER.info("Net TX: " + message.getClass().getName());
        }

        message.onMessage(context.getServerHandler().playerEntity);

        return null;
    }
}
