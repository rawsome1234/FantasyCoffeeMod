package net.rawsome1234.fantasycoffee.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.rawsome1234.fantasycoffee.FantasyCoffee;
import net.rawsome1234.fantasycoffee.block.ModBlocks;
import net.rawsome1234.fantasycoffee.block.custom.CoffeeCrop;
import net.rawsome1234.fantasycoffee.block.custom.FrolenCrop;
import net.rawsome1234.fantasycoffee.block.custom.MolzenCrop;
import org.jetbrains.annotations.Debug;

import java.util.function.Function;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, FantasyCoffee.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
//        System.out.println("Registering models");

        makeCoffeeCrop((CropBlock) ModBlocks.COFFEE_CROP.get(), "coffee_stage", "coffee_stage");
        makeFrolenCrop((CropBlock) ModBlocks.FROLEN_CROP.get(), "frolen_stage", "frolen_stage");
        makeMolzenCrop((CropBlock) ModBlocks.MOLZEN_CROP.get(), "molzen_stage", "molzen_stage");
    }


    public void makeCoffeeCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> coffeeStates(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    public void makeFrolenCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> frolenStates(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    public void makeMolzenCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> molzenStates(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] coffeeStates(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((CoffeeCrop) block).getAgeProperty()),
                new ResourceLocation(FantasyCoffee.MOD_ID, "block/" + textureName + state.getValue(((CoffeeCrop) block).getAgeProperty()))).renderType("cutout"));

        return models;
    }

    private ConfiguredModel[] frolenStates(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((FrolenCrop) block).getAgeProperty()),
                new ResourceLocation(FantasyCoffee.MOD_ID, "block/" + textureName + state.getValue(((FrolenCrop) block).getAgeProperty()))).renderType("cutout"));

        return models;
    }

    private ConfiguredModel[] molzenStates(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((MolzenCrop) block).getAgeProperty()),
                new ResourceLocation(FantasyCoffee.MOD_ID, "block/" + textureName + state.getValue(((MolzenCrop) block).getAgeProperty()))).renderType("cutout"));

        return models;
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
