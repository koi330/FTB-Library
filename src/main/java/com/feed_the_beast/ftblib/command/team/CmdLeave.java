package com.feed_the_beast.ftblib.command.team;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.lib.command.CmdBase;
import com.feed_the_beast.ftblib.lib.command.CommandUtils;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;

/**
 * @author LatvianModder
 */
public class CmdLeave extends CmdBase {

    public CmdLeave() {
        super("leave", Level.ALL);
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        ForgePlayer p = CommandUtils.getForgePlayer(getCommandSenderAsPlayer(sender));

        if (!p.hasTeam()) {
            throw FTBLib.error(sender, "ftblib.lang.team.error.no_team");
        } else if (!p.team.removeMember(p)) {
            throw FTBLib.error(sender, "ftblib.lang.team.error.must_transfer_ownership");
        }
    }
}
