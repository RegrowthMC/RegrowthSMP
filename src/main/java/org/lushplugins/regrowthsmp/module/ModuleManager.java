package org.lushplugins.regrowthsmp.module;

import org.lushplugins.lushlib.manager.Manager;
import org.lushplugins.regrowthsmp.RegrowthSMP;
import org.lushplugins.regrowthsmp.common.module.Module;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class ModuleManager extends Manager {
    private final HashMap<String, Module> modules = new HashMap<>();

    @Override
    public void onDisable() {
        for (Module module : modules.values()) {
            module.disable();
        }

        modules.clear();
    }

    public Set<String> getModuleTypes() {
        return modules.keySet();
    }

    public Collection<Module> getModules() {
        return modules.values();
    }

    public Module getModule(String moduleId) {
        return modules.get(moduleId);
    }

    public void enableModule(String moduleId) {
        Module module = modules.get(moduleId);
        if (module == null) {
            module = RegrowthSMP.getInstance().getModuleRegistry().constructModule(moduleId);
            modules.put(moduleId, module);
        }

        module.reload();
    }

    public void disableModule(String moduleId) {
        Module module = modules.get(moduleId);
        if (module != null) {
            module.disable();
        }
    }
}
