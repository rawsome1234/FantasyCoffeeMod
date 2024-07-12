package net.rawsome1234.fantasycoffee.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import net.rawsome1234.fantasycoffee.FantasyCoffee;
import net.rawsome1234.fantasycoffee.item.ModItems;
import net.rawsome1234.fantasycoffee.loot.AddItemModifier;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(PackOutput output) {
        super(output, FantasyCoffee.MOD_ID);
    }

    @Override
    protected void start() {
        add("coffee_beans_from_village", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/village/village_plains_house")).build() ,
                LootItemRandomChanceCondition.randomChance(.6f).build()
        }, ModItems.COFFEE_BEANS.get()));
        add("hellfire_beans_from_nether_fortress", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/nether_bridge")).build() ,
                LootItemRandomChanceCondition.randomChance(.3f).build()
        }, ModItems.HELLFIRE_BEANS.get()));
        add("hellfire_beans_from_bastion_treasure", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/bastion_treasure")).build() ,
                LootItemRandomChanceCondition.randomChance(.3f).build()
        }, ModItems.HELLFIRE_BEANS.get()));
        add("eldritch_beans_from_end_city", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/end_city_treasure")).build() ,
                LootItemRandomChanceCondition.randomChance(.4f).build()
        }, ModItems.ELDRITCH_BEANS.get()));
    }
}
