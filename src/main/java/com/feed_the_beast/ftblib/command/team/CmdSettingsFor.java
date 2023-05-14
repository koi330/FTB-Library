package com.feed_the_beast.ftblib.command.team;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import com.feed_the_beast.ftblib.lib.command.CmdBase;
import com.feed_the_beast.ftblib.lib.command.CommandUtils;
import com.feed_the_beast.ftblib.lib.data.FTBLibAPI;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;

/**
 * @author LatvianModder
 */
public class CmdSettingsFor extends CmdBase {

    public CmdSettingsFor() {
        super("settings_for", Level.OP);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> list = new ArrayList<>();

            for (ForgeTeam team : Universe.get().getTeams()) {
                if (team.type.isServer) {
                    list.add(team.getId());
                }
            }

            return getListOfStringsFromIterableMatchingLastWord(args, list);
        }

        return super.addTabCompletionOptions(sender, args);
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        checkArgs(sender, args, 1);
        ForgeTeam team = CommandUtils.getTeam(sender, args[0]);

        if (team.type.isServer) {
            FTBLibAPI.editServerConfig(getCommandSenderAsPlayer(sender), team.getSettings(), team);
        }
    }
}
