package com.feed_the_beast.ftblib.command.team;

import java.util.Collections;
import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.lib.EnumTeamStatus;
import com.feed_the_beast.ftblib.lib.command.CmdBase;
import com.feed_the_beast.ftblib.lib.command.CommandUtils;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;

/**
 * @author LatvianModder
 */
public class CmdStatus extends CmdBase {

    public CmdStatus() {
        super("status", Level.ALL);
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("set_status");
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 2) {
            return matchFromIterable(args, EnumTeamStatus.VALID_VALUES);
        }

        return super.addTabCompletionOptions(sender, args);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        ForgePlayer p = CommandUtils.getForgePlayer(getCommandSenderAsPlayer(sender));

        if (!p.hasTeam()) {
            throw FTBLib.error(sender, "ftblib.lang.team.error.no_team");
        } else if (!p.team.isModerator(p)) {
            throw new CommandException("commands.generic.permission");
        }

        checkArgs(sender, args, 1);
        ForgePlayer p1 = CommandUtils.getForgePlayer(sender, args[0]);

        if (args.length == 1) {
            sender.addChatMessage(EnumTeamStatus.NAME_MAP.getDisplayName(sender, p.team.getHighestStatus(p1)));
            return;
        }

        if (p.team.isOwner(p1)) {
            throw FTBLib.error(sender, "ftblib.lang.team.permission.owner");
        } else if (!p.team.isModerator(p)) {
            throw new CommandException("commands.generic.permission");
        }

        EnumTeamStatus status = EnumTeamStatus.NAME_MAP.get(args[1].toLowerCase());

        if (status.canBeSet()) {
            p.team.setStatus(p1, status);
            sender.addChatMessage(
                    FTBLib.lang(
                            sender,
                            "commands.team.status.set",
                            p1.getDisplayName(),
                            EnumTeamStatus.NAME_MAP.getDisplayName(sender, status)));
        } else {
            sender.addChatMessage(FTBLib.lang(sender, "commands.team.status.cant_set"));
        }
    }
}
