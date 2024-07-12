package net.rawsome1234.fantasycoffee.data.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.rawsome1234.fantasycoffee.FantasyCoffee;
import net.rawsome1234.fantasycoffee.block.ModBlocks;
import net.rawsome1234.fantasycoffee.item.ModItems;

import javax.annotation.Nullable;

public class CoffeeMakerRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;
    private final int bottlesReq;
    private final int ticksReq;

    public CoffeeMakerRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems, int bottlesReq, int ticksReq) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
        this.bottlesReq = bottlesReq;
        this.ticksReq = ticksReq;
    }

    @Override
    public boolean matches(SimpleContainer inv, Level world) {
        if (world.isClientSide()){
            return false;
        }

        if(recipeItems.get(0).test((inv.getItem(1)))){
            if(recipeItems.size() == 1)
                return true;
            return recipeItems.get(1).test(inv.getItem(4))
                    && inv.getItem(2).getItem() == ModItems.MUG.get();
        }

        return false;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    @Override
    public ItemStack assemble(SimpleContainer p_77572_1_, RegistryAccess access) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
        return true;
    }

    public int getBottlesReq(){
        return bottlesReq;
    }

    public ItemStack getIcon(){
        return new ItemStack(ModBlocks.COFFEE_MAKER.get());
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public int getTicksReq(){
        return ticksReq;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.COFFEE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CoffeeRecipeType.INSTANCE;
    }

    public static class CoffeeRecipeType implements RecipeType<CoffeeMakerRecipe> {
        public static final CoffeeRecipeType INSTANCE = new CoffeeRecipeType();
        public static final String ID = "coffee";
    }

    public static class Serializer implements RecipeSerializer<CoffeeMakerRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(FantasyCoffee.MOD_ID, "coffee");


        @Override
        public CoffeeMakerRecipe fromJson(ResourceLocation recipeID, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            int bottles = GsonHelper.getAsInt(json, "bottles");
            int ticks = GsonHelper.getAsInt(json, "ticks");

            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(ingredients.size(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++){
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new CoffeeMakerRecipe(recipeID, output, inputs, bottles, ticks);
        }

        @Nullable
        @Override
        public CoffeeMakerRecipe fromNetwork(ResourceLocation recipeID, FriendlyByteBuf packet) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(packet.readInt(), Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++){
                inputs.set(i, Ingredient.fromNetwork(packet));
            }

            ItemStack output = packet.readItem();
            return new CoffeeMakerRecipe(recipeID, output, inputs, 0, 200);
        }

        @Override
        public void toNetwork(FriendlyByteBuf packet, CoffeeMakerRecipe recipe) {
            if (recipe != null){
                packet.writeInt(recipe.getIngredients().size());
                for (Ingredient ing : recipe.getIngredients()){
                    ing.toNetwork(packet);
                }
                packet.writeItemStack(recipe.getResultItem(null), false);
            }
        }
    }



}
