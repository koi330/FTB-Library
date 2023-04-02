package com.feed_the_beast.ftblib.command.team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.FTBLibGameRules;
import com.feed_the_beast.ftblib.events.team.ForgeTeamChangedEvent;
import com.feed_the_beast.ftblib.lib.command.CmdBase;
import com.feed_the_beast.ftblib.lib.command.CommandUtils;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * @author LatvianModder
 */
public class CmdJoin extends CmdBase {
	public CmdJoin() {
		super("join", Level.ALL);
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
		if (args.length == 1) {
			if (!FTBLibGameRules.canJoinTeam(sender.getEntityWorld())) {
				return Collections.emptyList();
			}

			List<String> list = new ArrayList<>();

			try {
				ForgePlayer player = CommandUtils.getForgePlayer(sender);

				for (ForgeTeam team : Universe.get().getTeams()) {
					if (team.addMember(player, true)) {
						list.add(team.getId());
					}
				}

				if (list.size() > 1) {
					list.sort(null);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			return getListOfStringsFromIterableMatchingLastWord(args, list);
		}

		return super.addTabCompletionOptions(sender, args);
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if (!FTBLibGameRules.canJoinTeam(sender.getEntityWorld())) {
			throw FTBLib.error(sender, "feature_disabled_server");
		}

		EntityPlayerMP player = getCommandSenderAsPlayer(sender);
		ForgePlayer p = CommandUtils.getForgePlayer(player);

		checkArgs(sender, args, 1);

		ForgeTeam team = CommandUtils.getTeam(sender, args[0]);

		if (team.addMember(p, true)) {
			if (p.team.isOwner(p)) {
				new ForgeTeamChangedEvent(team, p.team).post();
				p.team.removeMember(p);
			} else if (p.hasTeam()) {
				throw FTBLib.error(sender, "ftblib.lang.team.error.must_leave");
			}

			team.addMember(p, false);
		} else {
			throw FTBLib.error(sender, "ftblib.lang.team.error.not_member", p.getDisplayName());
		}
	}
}
