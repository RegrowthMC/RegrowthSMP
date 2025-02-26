package org.lushplugins.regrowthsmp.module.function;

import org.lushplugins.regrowthsmp.common.module.Module;
import org.lushplugins.regrowthsmp.common.plugin.RegrowthPlugin;
import org.lushplugins.regrowthsmp.module.function.command.FunctionCommand;
import org.lushplugins.regrowthsmp.module.function.function.FunctionManager;

public final class Functions extends Module {
    private static Functions instance;

    private final FunctionManager functionManager;

    public Functions(RegrowthPlugin plugin) {
        super("functions", plugin);

        if (instance == null) {
            instance = this;
        }

        this.functionManager = new FunctionManager();

        plugin.registerCommand(new FunctionCommand());
    }

    @Override
    public void onEnable() {
        functionManager.reloadConfig();
    }

    public FunctionManager getFunctionManager() {
        return functionManager;
    }

    public static Functions getInstance() {
        return instance;
    }
}
