package net.rawsome1234.fantasycoffee.util;

import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.rawsome1234.fantasycoffee.block.custom.MugBlock;
import net.rawsome1234.fantasycoffee.block.tileentity.CoffeeMakerTile;

public class ModIntProperties {

    public static IntegerProperty WATER_STORED = IntegerProperty.create("water_stored", 0, CoffeeMakerTile.maxWaterStore);
    public static IntegerProperty MUGS_HELD = IntegerProperty.create("mugs", 0, MugBlock.maxMugs);



}
