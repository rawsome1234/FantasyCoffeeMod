package net.rawsome1234.fantasycoffee.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.rawsome1234.fantasycoffee.item.ModItems;
import net.rawsome1234.fantasycoffee.util.ModEnumProperties;

import javax.annotation.Nullable;

public class FrolenCrop extends CropBlock {
    public static final EnumProperty<ModEnumProperties.BiomeTemperature> BIOME_TEMP = EnumProperty.create("biome_temp", ModEnumProperties.BiomeTemperature.class);

    public static final int MAX_AGE = 3;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;


    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D),
            Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D)
    };

    public EnumProperty<ModEnumProperties.BiomeTemperature> getBiomeTemp(){
        return BIOME_TEMP;
    }

    public FrolenCrop(Properties p_i48421_1_) {

        super(p_i48421_1_);
    }

    @Override
    protected ItemLike getBaseSeedId(){
        return ModItems.FROLEN_BEANS.get();
    }

    @Override
    public int getMaxAge() {
        return 3;
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    protected int getBonemealAgeIncrease(Level world) {
        return ((int) (Math.random()*2) ) + 1;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[state.getValue(this.getAgeProperty())/2];
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
        builder.add(BIOME_TEMP);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        ModEnumProperties.BiomeTemperature temp = ModEnumProperties.BiomeTemperature.toEnum(context.getLevel().getBiome(context.getClickedPos()).get().getBaseTemperature());
        if (temp == ModEnumProperties.BiomeTemperature.WARM) { return null; }
        this.registerDefaultState(this.stateDefinition.any().setValue(BIOME_TEMP,
                temp));
        return this.defaultBlockState();
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        // Combines the crop block and the bush block can survive to override the dimension
        boolean first = (pLevel.getRawBrightness(pPos, 0) >= 8 || pLevel.canSeeSky(pPos));

        BlockPos blockpos = pPos.below();
        boolean second = this.mayPlaceOn(pLevel.getBlockState(blockpos), pLevel, blockpos);

        return first && second;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter reader, BlockPos pos) {
        return state.is(Blocks.DIRT) || state.is(Blocks.GRASS_BLOCK);
    }
}
