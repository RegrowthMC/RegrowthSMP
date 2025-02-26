package org.lushplugins.regrowthsmp.module.function.function;

import org.bukkit.configuration.file.YamlConfiguration;
import org.lushplugins.regrowthsmp.module.function.Functions;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;

public class FunctionManager {
    private static final File FUNCTIONS_FOLDER = new File(Functions.getInstance().getPlugin().getDataFolder(), "modules/functions");

    private final HashMap<String, Function> functions = new HashMap<>();

    public FunctionManager() {
        if (!FUNCTIONS_FOLDER.exists()) {
            FUNCTIONS_FOLDER.mkdirs();
            Functions.getInstance().getPlugin().saveDefaultResource("modules/functions/example.yml");
        }
    }

    public void reloadConfig() {
        unregisterAll();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(FUNCTIONS_FOLDER.toPath())) {
            stream.forEach(entry -> {
                File functionFile = entry.toFile();
                YamlConfiguration functionConfig = YamlConfiguration.loadConfiguration(functionFile);

                String id = functionFile.getName().split("\\.")[0];
                Function function = new Function(id, functionConfig);
                registerFunction(id, function);
            });
        } catch (IOException e) {
            Functions.getInstance().getPlugin().log(Level.SEVERE, "Something went wrong whilst reading modules files", e);
        }
    }

    public void registerFunction(String name, Function function) {
        functions.put(name, function);
    }

    public void unregisterFunction(Function function) {
        unregisterFunction(function.getId());
    }

    public void unregisterFunction(String name) {
        functions.remove(name);
    }

    public void unregisterAll() {
        functions.clear();
    }

    public Set<String> getFunctionNames() {
        return functions.keySet();
    }

    public Optional<Function> getFunction(String name) {
        return Optional.ofNullable(functions.get(name));
    }
}
