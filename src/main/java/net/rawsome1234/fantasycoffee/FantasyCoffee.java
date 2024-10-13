package net.rawsome1234.fantasycoffee;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.rawsome1234.fantasycoffee.block.ModBlocks;
import net.rawsome1234.fantasycoffee.block.tileentity.ModBlockEntities;
import net.rawsome1234.fantasycoffee.container.ModContainers;
import net.rawsome1234.fantasycoffee.data.recipes.ModRecipeTypes;
import net.rawsome1234.fantasycoffee.effects.ModEffects;
import net.rawsome1234.fantasycoffee.item.ModCreativeModeTabs;
import net.rawsome1234.fantasycoffee.item.ModItems;
import net.rawsome1234.fantasycoffee.loot.ModLootModifiers;
import net.rawsome1234.fantasycoffee.screen.CoffeeMakerScreen;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(FantasyCoffee.MOD_ID)
public class FantasyCoffee
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "fantasycoffee";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public FantasyCoffee()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModLootModifiers.register(modEventBus);
        ModEffects.register(modEventBus);

        ModBlockEntities.register(modEventBus);
        ModContainers.register(modEventBus);
        ModRecipeTypes.register(modEventBus);


        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event){

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MenuScreens.register(ModContainers.COFFEE_MAKER_CONTAINER.get(), CoffeeMakerScreen::new);
        }
    }
}
