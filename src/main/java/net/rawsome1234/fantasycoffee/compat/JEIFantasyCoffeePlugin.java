package net.rawsome1234.fantasycoffee.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeManager;
import net.rawsome1234.fantasycoffee.FantasyCoffee;
import net.rawsome1234.fantasycoffee.container.CoffeeMakerContainer;
import net.rawsome1234.fantasycoffee.data.recipes.CoffeeMakerRecipe;
import net.rawsome1234.fantasycoffee.screen.CoffeeMakerScreen;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

@JeiPlugin
public class JEIFantasyCoffeePlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(FantasyCoffee.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new CoffeeMakerCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();
        List<CoffeeMakerRecipe> coffeeRecipes = manager.getAllRecipesFor(CoffeeMakerRecipe.CoffeeRecipeType.INSTANCE);
        registration.addRecipes(CoffeeMakerCategory.COFFEE_MAKER_TYPE, coffeeRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration)
    {
        registration.addRecipeClickArea(CoffeeMakerScreen.class, 111, 17, 23, 51, CoffeeMakerCategory.COFFEE_MAKER_TYPE);
    }

//    @Override
//    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
//        IRecipeTransferHandler handler = new IRecipeTransferHandler() {
//            @Override
//            public Class getContainerClass() {
//                return CoffeeMakerContainer.class;
//            }
//
//            @Override
//            public Optional<MenuType> getMenuType() {
//                return Optional.of(CoffeeMakerContainer.T);
//            }
//
//            @Override
//            public RecipeType getRecipeType() {
//                return null;
//            }
//
//            @Override
//            public @Nullable IRecipeTransferError transferRecipe(AbstractContainerMenu container, Object recipe, IRecipeSlotsView recipeSlots, Player player, boolean maxTransfer, boolean doTransfer) {
//                return null;
//            }
//        }
//        registration.addRecipeTransferHandler();
//    }
}

