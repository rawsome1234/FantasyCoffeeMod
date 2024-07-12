package net.rawsome1234.fantasycoffee.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rawsome1234.fantasycoffee.FantasyCoffee;
import net.rawsome1234.fantasycoffee.block.custom.*;
//import net.rawsome1234.fantasycoffee.block.custom.CoffeeMakerBlock;
import net.rawsome1234.fantasycoffee.item.ModItems;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS
            = DeferredRegister.create(ForgeRegistries.BLOCKS, FantasyCoffee.MOD_ID);

    public static final RegistryObject<Block> COFFEE_MAKER = registerBlock("coffee_maker",
            () -> new CoffeeMakerBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .sound(SoundType.ANVIL)
                    .strength(5, 6).requiresCorrectToolForDrops().dynamicShape()));

    public static final RegistryObject<Block> COFFEE_CROP = BLOCKS.register("coffee_crop",
            () -> new CoffeeCrop(BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).noCollission().noOcclusion()));

    public static final RegistryObject<Block> MOLZEN_CROP = BLOCKS.register("molzen_crop",
            () -> new MolzenCrop(BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).noCollission().noOcclusion()));

    public static final RegistryObject<Block> FROLEN_CROP = BLOCKS.register("frolen_crop",
            () -> new FrolenCrop(BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).noCollission().noOcclusion()));

    public static final RegistryObject<Block> HELLFIRE_CROP = BLOCKS.register("hellfire_crop",
            () -> new HellfireCrop(BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).noCollission().noOcclusion()));

    public static final RegistryObject<Block> ELDRITCH_CROP = BLOCKS.register("eldritch_crop",
            () -> new EldritchCrop(BlockBehaviour.Properties.copy(Blocks.SWEET_BERRY_BUSH).noCollission().noOcclusion()));


    public static final RegistryObject<Block> MUG_BLOCK = BLOCKS.register("mug_block",
            () -> new MugBlock(BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));


    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block){
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties()));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }

}
