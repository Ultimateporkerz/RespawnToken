package net.ultimporks.resptoken.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.ultimporks.resptoken.block.blockentity.DeathChestBlockEntity;
import net.ultimporks.resptoken.init.ModBlockEntities;

import java.util.Objects;
import java.util.function.Supplier;

public class DeathChestBlock extends AbstractChestBlock<DeathChestBlockEntity> implements SimpleWaterloggedBlock {
    public static final MapCodec<DeathChestBlock> CODEC1 = simpleCodec(props -> new DeathChestBlock(props, ModBlockEntities.DEATH_CHEST_BE::get));
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape AABB = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);

    @Override
    public MapCodec<? extends DeathChestBlock> codec() {
        return CODEC1;
    }

    public DeathChestBlock(Properties properties, Supplier<BlockEntityType<? extends DeathChestBlockEntity>> blockEntityType) {
        super(properties, blockEntityType);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, Boolean.FALSE));
    }

    @Override
    public boolean canDropFromExplosion(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return false;
    }

    public BlockEntityType<? extends DeathChestBlockEntity> blockEntityType() {
        return this.blockEntityType.get();
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            MenuProvider menuProvider = this.getMenuProvider(pState, pLevel, pPos);
            if (menuProvider != null) {
                pPlayer.openMenu(menuProvider);
            }
        }
        return InteractionResult.SUCCESS;
    }
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            // If the block is being replaced/destroyed (not just a state change)
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof DeathChestBlockEntity deathChest) {
                // Drop all items from the chest
                Containers.dropContents(level, pos, deathChest);
                level.updateNeighbourForOutputSignal(pos, this); // Optional: for redstone
            }
        }
        super.onRemove(state, level, pos, newState, isMoving); // Call super last
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Get default state from super and set facing direction
        return Objects.requireNonNull(super.getStateForPlacement(context))
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new DeathChestBlockEntity(pPos, pState);
    }
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? createTickerHelper(pBlockEntityType, this.blockEntityType(), DeathChestBlockEntity::lidAnimateTick) : null;
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, WATERLOGGED);
    }
    @Override
    public DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combine(BlockState state, Level level, BlockPos pos, boolean override) {
        return DoubleBlockCombiner.Combiner::acceptNone;
    }
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AABB;
    }
    @Override
    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return 150000.0F;
    }

}
