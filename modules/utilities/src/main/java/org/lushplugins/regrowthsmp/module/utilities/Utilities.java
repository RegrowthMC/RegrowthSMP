package org.lushplugins.regrowthsmp.module.utilities;

import org.lushplugins.regrowthsmp.common.module.Module;
import org.lushplugins.regrowthsmp.common.plugin.RegrowthPlugin;
import org.lushplugins.regrowthsmp.module.utilities.command.TimerCommand;

public class Utilities extends Module {
    private static Utilities instance;

    public Utilities(RegrowthPlugin plugin) {
        super("utilities", plugin);

        if (instance == null) {
            instance = this;
        }

        plugin.registerCommand(new TimerCommand());
    }

    public static Utilities getInstance() {
        return instance;
    }
}
