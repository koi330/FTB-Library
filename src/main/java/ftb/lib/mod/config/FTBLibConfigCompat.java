package ftb.lib.mod.config;

import latmod.lib.annotations.Info;
import ftb.lib.api.config.ConfigEntryBool;

public class FTBLibConfigCompat {

    @Info("When true: inventory buttons do not shift when potion effect(s) are active")
    public static final ConfigEntryBool compat_statusEffectHUD = new ConfigEntryBool("compat_statusEffectHUD", false);

}
