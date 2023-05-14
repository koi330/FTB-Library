package com.feed_the_beast.ftblib.command.team;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.events.team.ForgeTeamCreatedEvent;
import com.feed_the_beast.ftblib.lib.EnumTeamColor;
import com.feed_the_beast.ftblib.lib.command.CmdBase;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.TeamType;
import com.feed_the_beast.ftblib.lib.data.Universe;

/**
 * @author LatvianModder
 */
public class CmdCreateServerTeam extends CmdBase {

    public CmdCreateServerTeam() {
        super("create_server_team", Level.OP_OR_SP);
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        checkArgs(sender, args, 1);

        if (!CmdCreate.isValidTeamID(args[0])) {
            throw FTBLib.error(sender, "ftblib.lang.team.id_invalid");
        }

        if (Universe.get().getTeam(args[0]).isValid()) {
            throw FTBLib.error(sender, "ftblib.lang.team.id_already_exists");
        }

        Universe universe = Universe.get();
        universe.clearCache();
        ForgeTeam team = new ForgeTeam(universe, universe.generateTeamUID((short) 0), args[0], TeamType.SERVER);
        team.setTitle(team.getId());
        team.setColor(EnumTeamColor.NAME_MAP.getRandom(sender.getEntityWorld().rand));
        team.universe.addTeam(team);
        new ForgeTeamCreatedEvent(team).post();
        sender.addChatMessage(FTBLib.lang(sender, "ftblib.lang.team.created", team.getId()));
        team.markDirty();
    }
}
