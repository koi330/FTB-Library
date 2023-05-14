package com.feed_the_beast.ftblib.command.team;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.lib.command.CmdBase;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;

/**
 * @author LatvianModder
 */
public class CmdList extends CmdBase {

    public CmdList() {
        super("list", Level.ALL);
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        sender.addChatMessage(FTBLib.lang(sender, "commands.team.list.teams", Universe.get().getTeams().size()));

        for (ForgeTeam team : Universe.get().getTeams()) {
            sender.addChatMessage(team.getCommandTitle());
        }
    }
}
