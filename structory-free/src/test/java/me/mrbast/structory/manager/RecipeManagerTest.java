package me.mrbast.structory.manager;

import me.mrbast.structory.crafting.recipe.DeterministicItem;
import me.mrbast.structory.crafting.recipe.Recipe;
import org.bukkit.NamespacedKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeManagerTest {
    private RecipeManager manager;

    @BeforeEach
    void setUp() {
        manager = new RecipeManager();
    }

    @Test
    void indexesANonDeterministicRecipeByFullAndShortKey() {
        Recipe recipe = recipe("simple", false, Set.of());

        manager.registerRecipe(recipe);

        assertSame(recipe, manager.getRecipe(recipe.getKey()).orElseThrow());
        assertSame(recipe, manager.getRecipe("simple").orElseThrow());
        assertTrue(manager.getNonDeterministicRecipes().contains(recipe));
    }

    @Test
    void clearRemovesDeterministicIndexesToo() {
        DeterministicItem item = mock(DeterministicItem.class);
        Set<DeterministicItem> deterministicItems = Set.of(item);
        Recipe recipe = recipe("deterministic", true, deterministicItems);
        manager.registerRecipe(recipe);

        manager.clear();

        assertTrue(manager.getRecipe("deterministic").isEmpty());
        assertTrue(manager.getDeterministicRecipes(deterministicItems).isEmpty());
        assertTrue(manager.getNonDeterministicRecipes().isEmpty());
    }

    private static Recipe recipe(String keyValue, boolean deterministic, Set<DeterministicItem> items) {
        NamespacedKey key = mock(NamespacedKey.class);
        when(key.getKey()).thenReturn(keyValue);
        Recipe recipe = mock(Recipe.class);
        when(recipe.getKey()).thenReturn(key);
        when(recipe.hasDeterministic()).thenReturn(deterministic);
        when(recipe.getDeterministicItems()).thenReturn(items);
        return recipe;
    }
}
