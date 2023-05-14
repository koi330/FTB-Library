package com.feed_the_beast.ftblib.command.client;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import com.feed_the_beast.ftblib.client.GuiClientConfig;
import com.feed_the_beast.ftblib.lib.command.CmdBase;

/**
 * @author LatvianModder
 */
public class CommandClientConfig extends CmdBase {

    public CommandClientConfig() {
        super("client_config", Level.ALL);
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        new GuiClientConfig().openGuiLater();
    }
}
