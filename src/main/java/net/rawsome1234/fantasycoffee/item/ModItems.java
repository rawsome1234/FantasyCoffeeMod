package net.rawsome1234.fantasycoffee.item;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rawsome1234.fantasycoffee.FantasyCoffee;
import net.rawsome1234.fantasycoffee.block.ModBlocks;
import net.rawsome1234.fantasycoffee.effects.ModEffects;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, FantasyCoffee.MOD_ID);

    public static final RegistryObject<Item> MUG = ITEMS.register("mug",
            () -> new BlockItem(ModBlocks.MUG_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<Item> COFFEE_BEANS = ITEMS.register("coffee_beans",
            () -> new BlockItem(ModBlocks.COFFEE_CROP.get(),
                    new Item.Properties().rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> MOLZEN_BEANS = ITEMS.register("molzen_beans",
            () -> new BlockItem(ModBlocks.MOLZEN_CROP.get(),
                    new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> FROLEN_BEANS = ITEMS.register("frolen_beans",
            () -> new BlockItem(ModBlocks.FROLEN_CROP.get(),
                    new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> HELLFIRE_BEANS = ITEMS.register("hellfire_beans",
            () -> new BlockItem(ModBlocks.HELLFIRE_CROP.get(),
                    new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> ELDRITCH_BEANS = ITEMS.register("eldritch_beans",
            () -> new BlockItem(ModBlocks.ELDRITCH_CROP.get(),
                    new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> COFFEE_ITEM = ITEMS.register("coffee_item",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(4).saturationMod(.375f)
                            .build())));

    public static final RegistryObject<Item> LATTE = ITEMS.register("latte",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(7).saturationMod(.7143f)
                            .build())));

    public static final RegistryObject<Item> ESPRESSO = ITEMS.register("espresso",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationMod(.625f)
                            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 0), 1f)
                            .build())));

    public static final RegistryObject<Item> GARBAGE = ITEMS.register("garbage",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(3).saturationMod(.5f)
                            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 100, 0), 1f)
                            .build())));

    public static final RegistryObject<Item> CHILLY_SWEET_COFFEE = ITEMS.register("chilly_sweet_coffee",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(4).saturationMod(.5f)
                            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 0), 1f)
                            .build())));
    public static final RegistryObject<Item> COOKIE_AND_CREAM_COFFEE = ITEMS.register("cookie_and_cream_coffee",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(7).saturationMod(.7143f)
                            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 0), 1f)
                            .build())));

    public static final RegistryObject<Item> FROSTY_BREW = ITEMS.register("frosty_brew",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(10).saturationMod(.8f)
                            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0), 1f)
                            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 0), 1f)
                            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 0), 1f)
                            .build())));

    public static final RegistryObject<Item> SUMMER_BREW = ITEMS.register("summer_brew",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(4).saturationMod(.5f)
                            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200, 0), 1f)
                            .build())));

    public static final RegistryObject<Item> REPOWERED_COFFEE = ITEMS.register("repowered_coffee",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(7).saturationMod(.7143f)
                            .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 200, 0), 1f)
                            .build())));

    public static final RegistryObject<Item> HEART_ATTACK = ITEMS.register("heart_attack",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(10).saturationMod(.8f)
                            .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 100, 0), 1f)
                            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 0), 1f)
                            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 0), 1f)
                            .build())));

    public static final RegistryObject<Item> INFERNAL_COFFEE = ITEMS.register("infernal_coffee",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationMod(.75f)
                            .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0), 1f)
                            .build())));

    public static final RegistryObject<Item> MOLTEN_COFFEE = ITEMS.register("molten_coffee",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(8).saturationMod(.5833f)
                            .effect(() -> new MobEffectInstance(ModEffects.EMBER_CLOAK.get(), 400, 0), 1f)
                            .alwaysEat().build())));

    public static final RegistryObject<Item> BLAZE_BREW = ITEMS.register("blaze_brew",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(16).saturationMod(.625f)
                            .effect(() -> new MobEffectInstance(ModEffects.FIRE_AURA.get(), 600, 0), 1f)
                            .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 400, 0), .5f)
                            .alwaysEat().build())));

    public static final RegistryObject<Item> CLOUD_COFFEE = ITEMS.register("cloud_coffee",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(5).saturationMod(.75f)
                            .effect(() -> new MobEffectInstance(MobEffects.LEVITATION, 100, 0), 1f)
                            .build())));

    public static final RegistryObject<Item> ETHEREAL_COFFEE = ITEMS.register("ethereal_coffee",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(12).saturationMod(.5833f)
                            .effect(() -> new MobEffectInstance(ModEffects.TEMPORAL_ANCHOR.get(), 1800, 0), 1f)
                            .alwaysEat().build())));

    public static final RegistryObject<Item> AMBROSIA_BLESSING = ITEMS.register("ambrosia_blessing",
            () -> new Item(new Item.Properties()
                    .food(new FoodProperties.Builder().nutrition(16).saturationMod(.625f)
                            .effect(() -> new MobEffectInstance(ModEffects.AMBROSIA.get(), 600, 1), 1f)
                            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 1), 1f)
                            .alwaysEat().build())));
    
    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }


    private static class Builder {
    }
}
