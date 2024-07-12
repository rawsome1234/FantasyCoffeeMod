package net.rawsome1234.fantasycoffee.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.rawsome1234.fantasycoffee.item.ModItems;
import net.rawsome1234.fantasycoffee.util.ModIntProperties;
import net.rawsome1234.fantasycoffee.util.ModUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MugBlock extends Block{

    public static final IntegerProperty MUGS = ModIntProperties.MUGS_HELD;
    public static final int maxMugs = 8;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public int currentMugs = 1;

    public static VoxelShape MUG_1 = Block.box(5, 0, 5, 10, 4, 9);

    public static VoxelShape MUG_2 = Block.box(3, 0, 3, 11, 4, 12);

    public static VoxelShape MUG_3 = Block.box(1, 0, 1, 13, 4, 13);

    public static VoxelShape MUG_4 = Block.box(3, 0, 3, 13, 8, 12);

    public static VoxelShape MUG_5 = Block.box(2, 0, 2, 13, 8, 13);

    public static VoxelShape MUG_6 = Block.box(0, 0, 1, 16, 8, 14);

    public static VoxelShape MUG_7 = Block.box(0, 0, 1, 16, 8, 14);

    public static VoxelShape MUG_8 = Block.box(0, 0, 2, 16, 8, 15);

    public MugBlock(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public VoxelShape getShape(BlockState state, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction rot = switch (state.getValue(FACING)) {
            case WEST -> Direction.EAST;
            case SOUTH -> Direction.WEST;
            case EAST -> Direction.SOUTH;
            default -> Direction.NORTH;
        };

//        System.out.println(rot);
        return switch (state.getValue(MUGS)) {
            case 2 -> ModUtils.rotateShape(Direction.NORTH, rot, MUG_2);
            case 3 -> ModUtils.rotateShape(Direction.NORTH, rot, MUG_3);
            case 4 -> ModUtils.rotateShape(Direction.NORTH, rot, MUG_4);
            case 5 -> ModUtils.rotateShape(Direction.NORTH, rot, MUG_5);
            case 6 -> ModUtils.rotateShape(Direction.NORTH, rot, MUG_6);
            case 7 -> ModUtils.rotateShape(Direction.NORTH, rot, MUG_7);
            case 8 -> ModUtils.rotateShape(Direction.NORTH, rot, MUG_8);
            default -> ModUtils.rotateShape(Direction.NORTH, rot, MUG_1);
        };
    }

    public BlockState rotate(BlockState pState, Rotation pRot) {
        return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
    }

    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(MUGS);
//        builder.add()
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(MUGS, 1);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(pState.getValue(MUGS) >= maxMugs){
            return InteractionResult.FAIL;
        }

        ItemStack handStack = pPlayer.getItemInHand(pHand);

        if(handStack.is(ModItems.MUG.get())){
            if(!pPlayer.isCreative()){
                handStack.setCount(handStack.getCount()-1);
            }
            pState = pState.setValue(MUGS, pState.getValue(MUGS)+1);
            pLevel.setBlock(pPos, pState, 3);
        }


        return InteractionResult.SUCCESS;
    }

    @Override
    public List<ItemStack> getDrops(BlockState pState, LootParams.Builder pParams) {
        List<ItemStack> mug = new ArrayList<>();

        mug.add(new ItemStack(ModItems.MUG.get(), pState.getValue(MUGS)));

        return mug;
    }
}
