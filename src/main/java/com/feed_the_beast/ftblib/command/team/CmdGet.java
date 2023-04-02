package com.feed_the_beast.ftblib.command.team;

import java.util.List;

import com.feed_the_beast.ftblib.lib.command.CmdBase;
import com.feed_the_beast.ftblib.lib.command.CommandUtils;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.Universe;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

/**
 * @author LatvianModder
 */
public class CmdGet extends CmdBase {
	public CmdGet() {
		super("get", Level.ALL);
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
		if (args.length == 1) {
			return getListOfStringsFromIterableMatchingLastWord(args, Universe.get().getPlayers());
		}

		return super.addTabCompletionOptions(sender, args);
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		checkArgs(sender, args, 1);
		ForgePlayer player = CommandUtils.getSelfOrOther(sender, args, 0);
		IChatComponent component = new ChatComponentText("");
		component.appendSibling(player.getDisplayName());
		component.appendText(": ");
		component.appendSibling(player.team.getCommandTitle());
		sender.addChatMessage(component);
	}
}
