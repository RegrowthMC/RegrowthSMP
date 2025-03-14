package org.lushplugins.regrowthsmp.module.recipes.recipe;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.lushlib.utils.DisplayItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class CraftingRecipe {
    private final NamespacedKey key;
    private final DisplayItemStack[] ingredients;
    private final DisplayItemStack result;
    private final boolean shapeless;
    private final boolean inRecipeBook;
    private final boolean registerCraft;

    private CraftingRecipe(NamespacedKey key, DisplayItemStack[] ingredients, DisplayItemStack result, boolean shapeless, boolean inRecipeBook, boolean registerCraft) {
        this.key = key;
        this.ingredients = ingredients;
        this.result = result;
        this.shapeless = shapeless;
        this.inRecipeBook = inRecipeBook;
        this.registerCraft = registerCraft;
    }

    public @NotNull NamespacedKey getKey() {
        return key;
    }

    public DisplayItemStack[] getIngredients() {
        return ingredients;
    }

    /**
     * @param slot crafting slot 0-8
     */
    public @Nullable DisplayItemStack getIngredient(int slot) {
        return ingredients[slot];
    }

    public DisplayItemStack getResult() {
        return result;
    }

    public boolean isShapeless() {
        return shapeless;
    }

    public boolean isCustom() {
        return !inRecipeBook || Arrays.stream(ingredients).anyMatch(DisplayItemStack::hasMeta);
    }

    public boolean shouldRegisterCraft() {
        return registerCraft;
    }

    public boolean matchesRecipe(ItemStack[] ingredients) {
        if (shapeless) {
            List<DisplayItemStack> unmatchedIngredients = Arrays.stream(this.ingredients)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));

            for (ItemStack ingredient : ingredients) {
                if (ingredient == null) {
                    continue;
                }

                boolean found = false;
                for (DisplayItemStack unmatchedIngredient : Collections.unmodifiableCollection(unmatchedIngredients)) {
                   if (unmatchedIngredient == null) {
                       continue;
                   }

                    if (unmatchedIngredient.isSimilar(ingredient)) {
                        found = true;
                        unmatchedIngredients.remove(unmatchedIngredient);
                        break;
                    }
                }

                if (!found) {
                    return false;
                }
            }

            return unmatchedIngredients.isEmpty();
        } else {
            for (int slot = 0; slot < ingredients.length; slot++) {
                ItemStack ingredient = ingredients[slot];
                DisplayItemStack recipeIngredient = this.ingredients[slot];

                if (ingredient == null) {
                    if (recipeIngredient != null) {
                        return false;
                    } else {
                        continue;
                    }
                }

                if (recipeIngredient == null) {
                    return false;
                }

                if (!recipeIngredient.isSimilar(ingredient)) {
                    return false;
                }
            }
        }

        return true;
    }

    public org.bukkit.inventory.CraftingRecipe createBukkitRecipe() {
        if (shapeless) {
            ShapelessRecipe recipe = new ShapelessRecipe(key, result.asItemStack());

            for (DisplayItemStack ingredient : ingredients) {
                if (ingredient == null) {
                    continue;
                }

                Material material = ingredient.getType();
                if (material == null || material.isAir()) {
                    continue;
                }

                recipe.addIngredient(material);
            }

            return recipe;
        } else {
            ShapedRecipe recipe = new ShapedRecipe(key, result.asItemStack());

            char currChar = 'a';
            String[] shape = new String[]{"", "", ""};
            Map<Character, Material> choiceMap = new HashMap<>();
            for (int slot = 0; slot < 9; slot++) {
                int row = slot / 3;
                DisplayItemStack ingredient = ingredients[slot];
                if (ingredient == null) {
                    shape[row] += ' ';
                    continue;
                }

                Material material = ingredient.getType();
                if (material == null || material.isAir()) {
                    shape[row] += ' ';
                    continue;
                }

                choiceMap.put(currChar, ingredient.getType());
                shape[row] += currChar;
                currChar++;
            }

            recipe.shape(shape);
            choiceMap.forEach(recipe::setIngredient);

            return recipe;
        }
    }

    public static Builder builder(NamespacedKey key) {
        return new Builder(key);
    }

    public static class Builder {
        private final NamespacedKey key;
        private final DisplayItemStack[] ingredients = new DisplayItemStack[9];
        private DisplayItemStack result;
        private boolean shapeless = false;
        private boolean inRecipeBook = true;
        private boolean registerCraft = true;

        private Builder(@NotNull NamespacedKey key) {
            this.key = key;
        }

        /**
         * @param ingredient the ingredient to add
         */
        public Builder addIngredient(@NotNull DisplayItemStack ingredient) {
            for (int i = 0; i < ingredients.length; i++) {
                if (ingredients[i] == null) {
                    ingredients[i] = ingredient;
                    return this;
                }
            }

            throw new IllegalArgumentException("No available slots");
        }

        /**
         * @param ingredient the ingredient to add
         * @param slot crafting slot 0-8
         */
        public Builder addIngredient(@NotNull DisplayItemStack ingredient, int slot) {
            if (slot < 0 || slot > 8) {
                throw new IllegalArgumentException("Slot out of bounds: " + slot);
            }

            ingredients[slot] = ingredient;
            return this;
        }

        public Builder result(@NotNull DisplayItemStack result) {
            this.result = result;
            return this;
        }

        /**
         * @param shapeless whether the recipe should be shapeless
         */
        public Builder shapeless(boolean shapeless) {
            this.shapeless = shapeless;
            return this;
        }

        /**
         * @param inRecipeBook whether the recipe should show in the recipe book
         */
        public Builder showInRecipeBook(boolean inRecipeBook) {
            this.inRecipeBook = inRecipeBook;
            return this;
        }

        /**
         * @param registerCraft whether the craft should be registered
         */
        public Builder registerCraft(boolean registerCraft) {
            this.registerCraft = registerCraft;
            return this;
        }

        /**
         * @return a built recipe
         */
        public CraftingRecipe build() {
            if (result == null) {
                throw new IllegalArgumentException("Crafting recipe requires a result");
            }

            return new CraftingRecipe(key, ingredients, result, shapeless, inRecipeBook, registerCraft);
        }
    }
}
