package ftb.lib.mod.cmd;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.IChatComponent;

import cpw.mods.fml.relauncher.*;
import ftb.lib.api.cmd.*;
import ftb.lib.mod.client.FTBLibModClient;
import ftb.lib.mod.net.MessageReload;

@SideOnly(Side.CLIENT)
public class CmdReloadClient extends CommandLM {

    public CmdReloadClient() {
        super(FTBLibModClient.reload_client_cmd.getAsString(), CommandLevel.OP);
    }

    public IChatComponent onCommand(ICommandSender ics, String[] args) {
        MessageReload.reloadClient(0L, true);
        return null;
    }
}
