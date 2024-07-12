package net.rawsome1234.fantasycoffee.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import net.rawsome1234.fantasycoffee.block.tileentity.CoffeeMakerTile;
import net.rawsome1234.fantasycoffee.block.tileentity.ModBlockEntities;
import net.rawsome1234.fantasycoffee.util.ModBoolProperties;
import net.rawsome1234.fantasycoffee.util.ModIntProperties;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CoffeeMakerBlock extends BaseEntityBlock {

    BlockPos pos;
    List<ItemStack> drops = new ArrayList<ItemStack>();

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final BooleanProperty HAS_BEANS = ModBoolProperties.HAS_BEANS;
    public static final BooleanProperty BREWED = ModBoolProperties.BREWED;
    public static final IntegerProperty WATER_STORED = ModIntProperties.WATER_STORED;


    private static final VoxelShape SHAPE_N = Stream.of(
            Block.box(1, 0, 1, 15, 2, 15),
            Block.box(2, 2, 11, 14, 12, 14),
            Block.box(0, 12, 0, 16, 14, 16),
            Block.box(10, 2, 2, 14, 12, 11),
            Block.box(4, 10, 5, 6, 12, 7),
            Block.box(9, 14, 1, 10, 16, 15),
            Block.box(14, 14, 1, 15, 16, 15),
            Block.box(10, 14, 1, 14, 16, 2),
            Block.box(10, 14, 14, 14, 16, 15),
            Block.box(2, 14, 14, 8, 16, 15),
            Block.box(2, 14, 9, 8, 16, 10),
            Block.box(2, 14, 10, 3, 16, 14),
            Block.box(7, 14, 10, 8, 16, 14),
            Block.box(3, 14, 10, 7, 15, 14),
            Block.box(10, 14, 2, 14, 15, 14)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_W = Stream.of(
            Block.box(1, 0, 1, 15, 2, 15),
            Block.box(11, 2, 2, 14, 12, 14),
            Block.box(0, 12, 0, 16, 14, 16),
            Block.box(2, 2, 2, 11, 12, 6),
            Block.box(5, 10, 10, 7, 12, 12),
            Block.box(1, 14, 6, 15, 16, 7),
            Block.box(1, 14, 1, 15, 16, 2),
            Block.box(1, 14, 2, 2, 16, 6),
            Block.box(14, 14, 2, 15, 16, 6),
            Block.box(14, 14, 8, 15, 16, 14),
            Block.box(9, 14, 8, 10, 16, 14),
            Block.box(10, 14, 13, 14, 16, 14),
            Block.box(10, 14, 8, 14, 16, 9),
            Block.box(10, 14, 9, 14, 15, 13),
            Block.box(2, 14, 2, 14, 15, 6)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_S = Stream.of(
            Block.box(1, 0, 1, 15, 2, 15),
            Block.box(2, 2, 2, 14, 12, 5),
            Block.box(0, 12, 0, 16, 14, 16),
            Block.box(2, 2, 5, 6, 12, 14),
            Block.box(10, 10, 9, 12, 12, 11),
            Block.box(6, 14, 1, 7, 16, 15),
            Block.box(1, 14, 1, 2, 16, 15),
            Block.box(2, 14, 14, 6, 16, 15),
            Block.box(2, 14, 1, 6, 16, 2),
            Block.box(8, 14, 1, 14, 16, 2),
            Block.box(8, 14, 6, 14, 16, 7),
            Block.box(13, 14, 2, 14, 16, 6),
            Block.box(8, 14, 2, 9, 16, 6),
            Block.box(9, 14, 2, 13, 15, 6),
            Block.box(2, 14, 2, 6, 15, 14)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    private static final VoxelShape SHAPE_E = Stream.of(
            Block.box(1, 0, 1, 15, 2, 15),
            Block.box(2, 2, 2, 5, 12, 14),
            Block.box(0, 12, 0, 16, 14, 16),
            Block.box(5, 2, 10, 14, 12, 14),
            Block.box(9, 10, 4, 11, 12, 6),
            Block.box(1, 14, 9, 15, 16, 10),
            Block.box(1, 14, 14, 15, 16, 15),
            Block.box(14, 14, 10, 15, 16, 14),
            Block.box(1, 14, 10, 2, 16, 14),
            Block.box(1, 14, 2, 2, 16, 8),
            Block.box(6, 14, 2, 7, 16, 8),
            Block.box(2, 14, 2, 6, 16, 3),
            Block.box(2, 14, 7, 6, 16, 8),
            Block.box(2, 14, 3, 6, 15, 7),
            Block.box(2, 14, 10, 14, 15, 14)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();



    public BlockState rotate(BlockState pState, Rotation pRot) {
        return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
    }

    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }


    public CoffeeMakerBlock(Properties p_i48440_1_) {

        super(p_i48440_1_);
        this.registerDefaultState(this.stateDefinition.any().setValue(HAS_BEANS, Boolean.valueOf(false)));
        this.registerDefaultState(this.stateDefinition.any().setValue(BREWED, Boolean.valueOf(false)));
        this.registerDefaultState(this.stateDefinition.any().setValue(WATER_STORED, Integer.valueOf(0)));
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof CoffeeMakerTile) {
                ((CoffeeMakerTile) blockEntity).drops();
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }



    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if(entity instanceof CoffeeMakerTile) {
                NetworkHooks.openScreen(((ServerPlayer)pPlayer), (CoffeeMakerTile)entity, pPos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }



    @Override
    public VoxelShape getShape(BlockState state, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        switch (state.getValue(FACING)){
            case WEST: return SHAPE_W;
            case SOUTH: return SHAPE_S;
            case EAST: return SHAPE_E;
            default: return SHAPE_N;
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(WATER_STORED);
        builder.add(BREWED);
        builder.add(HAS_BEANS);
//        builder.add()
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(HAS_BEANS, false).setValue(WATER_STORED, 0).setValue(BREWED, false);
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return ModBlockEntities.COFFEE_MAKER_TILE.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()) {
            return null;
        }

        return createTickerHelper(pBlockEntityType, ModBlockEntities.COFFEE_MAKER_TILE.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1));
    }

}
