package net.rawsome1234.fantasycoffee.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.rawsome1234.fantasycoffee.FantasyCoffee;
import net.rawsome1234.fantasycoffee.block.ModBlocks;

public class ModCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FantasyCoffee.MOD_ID);

    public static final RegistryObject<CreativeModeTab> COFFEE_TAB = CREATIVE_MODE_TABS.register("fantasy_coffee_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.COFFEE_ITEM.get()))
                    .title(Component.translatable("creativetab.fantasy_coffee_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.MUG.get());
                        pOutput.accept(ModItems.COFFEE_BEANS.get());
                        pOutput.accept(ModItems.MOLZEN_BEANS.get());
                        pOutput.accept(ModItems.FROLEN_BEANS.get());
                        pOutput.accept(ModItems.HELLFIRE_BEANS.get());
                        pOutput.accept(ModItems.ELDRITCH_BEANS.get());
                        pOutput.accept(ModItems.COFFEE_ITEM.get());
                        pOutput.accept(ModItems.LATTE.get());
                        pOutput.accept(ModItems.ESPRESSO.get());
                        pOutput.accept(ModItems.GARBAGE.get());
                        pOutput.accept(ModItems.CHILLY_SWEET_COFFEE.get());
                        pOutput.accept(ModItems.COOKIE_AND_CREAM_COFFEE.get());
                        pOutput.accept(ModItems.FROSTY_BREW.get());
                        pOutput.accept(ModItems.SUMMER_BREW.get());
                        pOutput.accept(ModItems.REPOWERED_COFFEE.get());
                        pOutput.accept(ModItems.HEART_ATTACK.get());
                        pOutput.accept(ModItems.INFERNAL_COFFEE.get());
                        pOutput.accept(ModItems.MOLTEN_COFFEE.get());
                        pOutput.accept(ModItems.BLAZE_BREW.get());
                        pOutput.accept(ModItems.CLOUD_COFFEE.get());
                        pOutput.accept(ModItems.ETHEREAL_COFFEE.get());
                        pOutput.accept(ModItems.AMBROSIA_BLESSING.get());

                        pOutput.accept(ModBlocks.COFFEE_MAKER.get());
                    })
                    .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }


}
