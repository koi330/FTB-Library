package com.feed_the_beast.ftblib.command.team;

import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.lib.command.CmdBase;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;

/**
 * @author LatvianModder
 */
public class CmdDelete extends CmdBase {

    public CmdDelete() {
        super("delete", Level.OP_OR_SP);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            return matchFromIterable(args, Universe.get().getTeams());
        }

        return super.addTabCompletionOptions(sender, args);
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        checkArgs(sender, args, 1);

        ForgeTeam team = Universe.get().getTeam(args[0]);

        if (!team.isValid()) {
            throw FTBLib.error(sender, "ftblib.lang.team.error.not_found", args[0]);
        }

        ForgePlayer o = team.getOwner();

        for (ForgePlayer player : team.getMembers()) {
            if (player != o) {
                team.removeMember(player);
            }
        }

        if (o != null) {
            team.removeMember(o);
        }

        team.delete();
    }
}
