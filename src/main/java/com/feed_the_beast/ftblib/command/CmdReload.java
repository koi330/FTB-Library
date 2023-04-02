package com.feed_the_beast.ftblib.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.feed_the_beast.ftblib.FTBLibCommon;
import com.feed_the_beast.ftblib.FTBLibConfig;
import com.feed_the_beast.ftblib.events.ServerReloadEvent;
import com.feed_the_beast.ftblib.lib.EnumReloadType;
import com.feed_the_beast.ftblib.lib.command.CmdBase;
import com.feed_the_beast.ftblib.lib.data.FTBLibAPI;
import com.feed_the_beast.ftblib.lib.data.Universe;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ResourceLocation;

/**
 * @author LatvianModder
 */
public class CmdReload extends CmdBase {
	private Collection<String> tab;

	public CmdReload(String id, Level l) {
		super(id, l);

		tab = new HashSet<>();
		tab.add("*");

		for (ResourceLocation r : FTBLibCommon.RELOAD_IDS.keySet()) {
			tab.add(r.toString());
			tab.add(r.getResourceDomain() + ":*");
		}

		tab = new ArrayList<>(tab);
		((ArrayList<String>) tab).sort(null);
	}

	public CmdReload() {
		this(FTBLibConfig.general.replace_reload_command ? "reload" : "ftb_reload", Level.OP_OR_SP);
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
		if (args.length == 1) {
			return getListOfStringsFromIterableMatchingLastWord(args, tab);
		}

		return super.addTabCompletionOptions(sender, args);
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		ResourceLocation id = ServerReloadEvent.ALL;

		if (args.length >= 1) {
			if (args[0].indexOf(':') != -1) {
				id = new ResourceLocation(args[0]);
			} else if (!args[0].equals("*")) {
				id = new ResourceLocation(args[0] + ":*");
			}
		}

		FTBLibAPI.reloadServer(Universe.get(), sender, EnumReloadType.RELOAD_COMMAND, id);
	}
}