package org.lushplugins.regrowthsmp.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.regrowthsmp.RegrowthSMP;
import org.lushplugins.regrowthsmp.common.data.UserData;
import org.lushplugins.regrowthsmp.common.module.Module;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SMPUser implements org.lushplugins.regrowthsmp.common.data.SMPUser {
    private final UUID uuid;
    private final String username;
    private final Map<String, UserData> moduleData = new HashMap<>();

    public SMPUser(UUID uuid, @Nullable JsonObject json) {
        this.uuid = uuid;

        Player player = Bukkit.getPlayer(uuid);
        if (json != null) {
            if (player != null) {
                this.username = player.getName();
            } else {
                JsonElement usernameElement = json.get("username");
                this.username = usernameElement != null ? usernameElement.getAsString() : null;
            }

            JsonObject modules = json.get("modules").getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : modules.entrySet()) {
                String moduleId = entry.getKey().toLowerCase();
                JsonObject moduleDataObj = entry.getValue().getAsJsonObject();

                Module module = RegrowthSMP.getInstance().getModuleManager().getModule(moduleId);
                if (module != null && module.storesUserData()) {
                    UserData userData = module.createUserData(uuid, moduleDataObj);
                    if (userData != null) {
                        moduleData.put(moduleId, userData);
                        continue;
                    }
                }

                moduleData.put(moduleId, new JsonUserData(uuid, moduleDataObj));
            }
        } else {
            // Handle users with no data (We create a default user as it is assumed there is no data for this user)
            this.username = player != null ? player.getName() : null;

            Collection<Module> modules = RegrowthSMP.getInstance().getModuleManager().getModules();
            for (Module module : modules) {
                if (module.storesUserData()) {
                    UserData userData = module.createUserData(uuid, null);
                    if (userData != null) {
                        moduleData.put(module.getId(), userData);
                    }
                }
            }
        }
    }

    public UUID getUUID() {
        return uuid;
    }

    public @Nullable String getUsername() {
        return username;
    }

    @Override
    public UserData getUserData(String moduleId) {
        return moduleData.get(moduleId);
    }

    public JsonObject toJsonObject() {
        JsonObject json = new JsonObject();

        json.addProperty("username", username);

        JsonObject modules = new JsonObject();
        for (Map.Entry<String, UserData> entry : moduleData.entrySet()) {
            modules.add(entry.getKey(), entry.getValue().toJsonObject());
        }

        json.add("modules", modules);
        return json;
    }
}
