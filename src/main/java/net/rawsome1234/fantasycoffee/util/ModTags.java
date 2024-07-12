package net.rawsome1234.fantasycoffee.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.rawsome1234.fantasycoffee.FantasyCoffee;

public class ModTags {
    public static class Blocks {

        private static TagKey<Block> tag(String name){
            return BlockTags.create(new ResourceLocation(FantasyCoffee.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> BEANS = tag("beans");
        public static final TagKey<Item> COFFEE_BUCKET = tag("coffee_buckets");
        public static final TagKey<Item> COFFEE_TYPES = tag("coffees");

        private static TagKey<Item> tag(String name){
            return ItemTags.create(new ResourceLocation(FantasyCoffee.MOD_ID, name));
        }
    }


}
