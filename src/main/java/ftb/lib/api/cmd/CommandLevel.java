package ftb.lib.api.cmd;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import ftb.lib.FTBLib;

public enum CommandLevel {

    NONE,
    ALL,
    OP;

    public static final CommandLevel[] VALUES = { NONE, ALL, OP };
    public static final String[] LEVEL_STRINGS = { "NONE", "ALL", "OP" };

    public static CommandLevel get(String s) {
        if (s.toUpperCase().equals("ALL")) return ALL;
        if (s.toUpperCase().equals("OP")) return OP;
        return NONE;
    }

    public boolean isEnabled() {
        return this != NONE;
    }

    public boolean isOP() {
        return this == OP;
    }

    public int requiredPermsLevel() {
        if (this == NONE) return 5;
        if (this == ALL) return 0;
        if (this == OP) return 2;
        return 0;
    }

    public boolean hasLevel(ICommandSender s) {
        if (this == NONE) return false;
        else if (this == ALL) return true;
        else {
            if (s instanceof MinecraftServer) return true;
            else if (s instanceof EntityPlayerMP) return FTBLib.isOP(((EntityPlayerMP) s).getGameProfile());
            else return true;
        }
    }
}
