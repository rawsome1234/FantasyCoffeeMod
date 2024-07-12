package net.rawsome1234.fantasycoffee.block.tileentity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rawsome1234.fantasycoffee.FantasyCoffee;
import net.rawsome1234.fantasycoffee.block.ModBlocks;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, FantasyCoffee.MOD_ID);

    public static RegistryObject<BlockEntityType<CoffeeMakerTile>> COFFEE_MAKER_TILE =
            BLOCK_ENTITES.register("coffee_maker_tile", ()-> BlockEntityType.Builder.of(
                    CoffeeMakerTile::new, ModBlocks.COFFEE_MAKER.get()).build(null));

    public static void register(IEventBus eventBus){
        BLOCK_ENTITES.register(eventBus);
    }

}
