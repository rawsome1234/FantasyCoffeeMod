package net.rawsome1234.fantasycoffee.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.items.SlotItemHandler;
import net.rawsome1234.fantasycoffee.FantasyCoffee;
import net.rawsome1234.fantasycoffee.block.ModBlocks;
import net.rawsome1234.fantasycoffee.data.recipes.CoffeeMakerRecipe;
import net.rawsome1234.fantasycoffee.item.ModItems;
import org.checkerframework.checker.units.qual.C;

import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CoffeeMakerCategory implements IRecipeCategory<CoffeeMakerRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(FantasyCoffee.MOD_ID, "coffee");
    public static final ResourceLocation TEXTURE = new ResourceLocation(FantasyCoffee.MOD_ID, "textures/gui/coffee_maker_gui.png");

    public static final RecipeType<CoffeeMakerRecipe> COFFEE_MAKER_TYPE = new RecipeType<>(UID, CoffeeMakerRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;
    private IDrawable watericon;

    public CoffeeMakerCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 5, 5, 164, 74);
        this.watericon = helper.createDrawable(TEXTURE, 176, 1, 18, 52);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.COFFEE_MAKER.get()));
    }


    @Override
    public RecipeType<CoffeeMakerRecipe> getRecipeType() {
        return COFFEE_MAKER_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("screen.fantasycoffee.coffee_maker");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CoffeeMakerRecipe recipe, IFocusGroup focuses) {
        // water
        ItemStack waterBottle = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);

        IDrawable shownWater = new IDrawable() {
            @Override
            public int getWidth() {
                return 0;
            }

            @Override
            public int getHeight() {
                return 0;
            }

            @Override
            public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
                float l = ((float) recipe.getBottlesReq()) / 12;
                int pixelHeight = Math.round(l * 52);
                guiGraphics.blit(TEXTURE, xOffset, yOffset+52-pixelHeight, 176, 53-pixelHeight, 18, pixelHeight);
            }
        };

        IDrawable overlaySlot = new IDrawable() {
            @Override
            public int getWidth() {
                return 0;
            }

            @Override
            public int getHeight() {
                return 0;
            }

            @Override
            public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
                guiGraphics.blit(TEXTURE, xOffset, yOffset, 176, 104, 18, 18);
            }
        };


        builder.addSlot(RecipeIngredientRole.CATALYST, 32, 30)
                .setBackground(shownWater, -23, -19)
                .addItemStack(new ItemStack(Items.WATER_BUCKET))
                .addItemStack(waterBottle)
                .addTooltipCallback(new IRecipeSlotTooltipCallback() {
            @Override
            public void onTooltip(IRecipeSlotView recipeSlotView, List<Component> tooltip) {
                tooltip.clear();
                Optional<ITypedIngredient<?>> displayedIngredient = recipeSlotView.getDisplayedIngredient();
                tooltip.add(Component.translatable("screen.fantasycoffee.jei_bottle_clue", recipe.getBottlesReq()));
                if (displayedIngredient.isPresent()){
                    if (displayedIngredient.get().getItemStack().get().is(new ItemStack(Items.WATER_BUCKET).getItem())){
                        tooltip.add(Component.translatable("screen.fantasycoffee.jei_bottle_inform", 3).withStyle(ChatFormatting.ITALIC, ChatFormatting.BLUE));
                    }
                    else{
                        tooltip.add(Component.translatable("screen.fantasycoffee.jei_bottle_inform", 1).withStyle(ChatFormatting.ITALIC, ChatFormatting.BLUE));
                    }
                }
            }
        });
        // Beans
        builder.addSlot(RecipeIngredientRole.INPUT, 90, 7).addIngredients(recipe.getIngredients().get(0)).setBackground(overlaySlot, -1, -1);
        // Mug input
        builder.addSlot(RecipeIngredientRole.INPUT,90, 53).addItemStack(new ItemStack(ModItems.MUG.get()));
        // Mug Output
        builder.addSlot(RecipeIngredientRole.OUTPUT, 134, 30).addItemStack(recipe.getResultItem(null));
        // Extra Ingredient
        builder.addSlot(RecipeIngredientRole.INPUT, 90, 30).addIngredients(recipe.getIngredients().get(1));
    }
}
