package org.lushplugins.regrowthsmp.module.bookreader;

import org.lushplugins.regrowthsmp.common.module.Module;
import org.lushplugins.regrowthsmp.common.plugin.RegrowthPlugin;
import org.lushplugins.regrowthsmp.module.bookreader.hook.WorldGuardHook;
import org.lushplugins.regrowthsmp.module.bookreader.listener.PlayerListener;

public class BookReader extends Module {
    private static BookReader instance;

    public BookReader(RegrowthPlugin plugin) {
        super("book_reader", plugin);

        if (instance == null) {
            instance = this;
        }
    }

    @Override
    public void onEnable() {
        this.getPlugin().registerListener(new PlayerListener());
    }

    public static BookReader getInstance() {
        return instance;
    }

    public static void prepare() {
        WorldGuardHook.prepare();
    }
}
