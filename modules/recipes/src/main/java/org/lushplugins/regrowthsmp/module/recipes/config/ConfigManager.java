package org.lushplugins.regrowthsmp.module.recipes.config;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;
import org.lushplugins.lushlib.gui.inventory.GuiFormat;
import org.lushplugins.lushlib.utils.DisplayItemStack;
import org.lushplugins.lushlib.utils.YamlUtils;
import org.lushplugins.lushlib.utils.converter.YamlConverter;
import org.lushplugins.regrowthsmp.module.recipes.Recipes;
import org.lushplugins.regrowthsmp.module.recipes.recipe.CraftingRecipe;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class ConfigManager {
    private HashMap<NamespacedKey, CraftingRecipe> recipes;
    private boolean showInRecipeBook;
    private String guiTitle;
    private GuiFormat recipesGuiFormat;
    private GuiFormat recipeGuiFormat;

    public ConfigManager() {
        Recipes.getInstance().getPlugin().saveDefaultResource("modules/recipes.yml");
    }

    public void reloadConfig() {
        ConfigurationSection config = Recipes.getInstance().getPlugin().getConfigResource("modules/recipes.yml");
        this.showInRecipeBook = config.getBoolean("show-in-recipe-book", true);

        this.recipes = new HashMap<>();
        List<ConfigurationSection> recipeSections = YamlUtils.getConfigurationSections(config, "recipes");
        for (ConfigurationSection recipeSection : recipeSections) {
            NamespacedKey key = NamespacedKey.fromString(recipeSection.getName());
            CraftingRecipe.Builder recipeBuilder = CraftingRecipe.builder(key);

            boolean shapeless = recipeSection.getBoolean("shapeless");
            recipeBuilder.shapeless(shapeless);
            recipeBuilder.showInRecipeBook(showInRecipeBook);
            recipeBuilder.registerCraft(recipeSection.getBoolean("register-craft", true));

            List<ConfigurationSection> ingredientSections = YamlUtils.getConfigurationSections(recipeSection, "ingredients");
            for (ConfigurationSection ingredientSection : ingredientSections) {
                DisplayItemStack ingredient = YamlConverter.getDisplayItem(ingredientSection);
                if (shapeless) {
                    recipeBuilder.addIngredient(ingredient);
                } else {
                    int slot = Integer.parseInt(ingredientSection.getName());
                    recipeBuilder.addIngredient(ingredient, slot);
                }
            }

            ConfigurationSection resultSection = recipeSection.getConfigurationSection("result");
            if (resultSection != null) {
                recipeBuilder.result(YamlConverter.getDisplayItem(resultSection));
            }

            try {
                this.recipes.put(key, recipeBuilder.build());
            } catch (IllegalArgumentException e) {
                Recipes.getInstance().getPlugin().log(Level.WARNING, "Failed to load recipe: " + recipeSection.getName(), e);
            }
        }

        applyRecipes();

        guiTitle = config.getString("recipes-gui.title", "Recipes");
        recipesGuiFormat = new GuiFormat(config.getStringList("recipes-gui.format"));
        for (ConfigurationSection itemSection : YamlUtils.getConfigurationSections(config, "recipes-gui.items")) {
            recipesGuiFormat.setItemReference(itemSection.getName().charAt(0), YamlConverter.getDisplayItem(itemSection));
        }

        recipeGuiFormat = new GuiFormat(config.getStringList("recipe-gui.format"));
        for (ConfigurationSection itemSection : YamlUtils.getConfigurationSections(config, "recipe-gui.items")) {
            recipeGuiFormat.setItemReference(itemSection.getName().charAt(0), YamlConverter.getDisplayItem(itemSection));
        }
    }

    public void applyRecipes() {
        Collection<CraftingRecipe> recipes = this.recipes.values();

        for (CraftingRecipe recipe : recipes) {
            NamespacedKey key = recipe.getKey();
            Recipe bukkitRecipe = Bukkit.getRecipe(key);
            if (bukkitRecipe != null) {
                Bukkit.removeRecipe(key);
            }

            if (recipe.shouldRegisterCraft()) {
                bukkitRecipe = recipe.createBukkitRecipe();
                Bukkit.addRecipe(bukkitRecipe, true);
            }
        }

        if (showInRecipeBook) {
            Collection<NamespacedKey> recipeKeys = recipes.stream().map(CraftingRecipe::getKey).toList();
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.discoverRecipes(recipeKeys);
            }
        }
    }

    public CraftingRecipe getRecipe(NamespacedKey key) {
        return this.recipes.get(key);
    }

    public Set<NamespacedKey> getRecipeKeys() {
        return this.recipes.keySet();
    }

    public Collection<CraftingRecipe> getRecipes() {
        return this.recipes.values();
    }

    public boolean showInRecipeBook() {
        return showInRecipeBook;
    }

    public String getGuiTitle() {
        return guiTitle;
    }

    public GuiFormat getRecipesGuiFormat() {
        return recipesGuiFormat;
    }

    public GuiFormat getRecipeGuiFormat() {
        return recipeGuiFormat;
    }
}
