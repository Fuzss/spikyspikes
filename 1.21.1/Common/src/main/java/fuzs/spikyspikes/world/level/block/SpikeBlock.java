package fuzs.spikyspikes.world.level.block;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.puzzleslib.api.core.v1.Proxy;
import fuzs.spikyspikes.init.ModRegistry;
import fuzs.spikyspikes.mixin.accessor.LivingEntityAccessor;
import fuzs.spikyspikes.world.damagesource.SpikeDamageSource;
import fuzs.spikyspikes.world.level.block.entity.SpikeBlockEntity;
import fuzs.spikyspikes.world.phys.shapes.CustomOutlineShape;
import fuzs.spikyspikes.world.phys.shapes.VoxelUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Code for facing copied from {@link AmethystClusterBlock}.
 */
public class SpikeBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final MapCodec<SpikeBlock> CODEC = spikeCodec(SpikeBlock::new);
    public static final DecimalFormat TOOLTIP_DAMAGE_FORMAT = Util.make(new DecimalFormat("0.0"),
            (DecimalFormat decimalFormat) -> {
                decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT));
            }
    );
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    private static final Map<Direction, VoxelShape> SHAPE_BY_DIRECTION = Arrays.stream(Direction.values()).collect(
            Maps.<Direction, Direction, VoxelShape>toImmutableEnumMap(Function.identity(),
                    SpikeBlock::makeVisualShape
            ));
    private static final Map<Direction, VoxelShape> COLLISION_SHAPE_BY_DIRECTION = Arrays.stream(Direction.values())
            .collect(Maps.<Direction, Direction, VoxelShape>toImmutableEnumMap(Function.identity(),
                    direction -> makeCollisionShape(direction, false)
            ));
    private static final Map<Direction, VoxelShape> INTERACTION_SHAPE_BY_DIRECTION = Arrays.stream(Direction.values())
            .collect(Maps.<Direction, Direction, VoxelShape>toImmutableEnumMap(Function.identity(),
                    direction -> makeCollisionShape(direction, true)
            ));

    public final SpikeMaterial spikeMaterial;

    public SpikeBlock(SpikeMaterial spikeMaterial, Properties properties) {
        super(properties);
        this.spikeMaterial = spikeMaterial;
        this.registerDefaultState(
                this.defaultBlockState().setValue(WATERLOGGED, Boolean.FALSE).setValue(FACING, Direction.UP));
    }

    protected static <T extends SpikeBlock> MapCodec<T> spikeCodec(BiFunction<SpikeMaterial, Properties, T> factory) {
        return RecordCodecBuilder.mapCodec((instance) -> {
            return instance.group(SpikeMaterial.CODEC.fieldOf("material").forGetter(SpikeBlock::getSpikeMaterial),
                    propertiesCodec()
            ).apply(instance, factory);
        });
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public SpikeMaterial getSpikeMaterial() {
        return this.spikeMaterial;
    }

    private static VoxelShape makeVisualShape(Direction direction) {
        VoxelShape shape = makeStaircasePyramid(direction, 8, 2.0);
        Vec3[] outlineVectors = VoxelUtils.makePyramidEdges(
                VoxelUtils.makeVectors(0.0, 0.0, 0.0, 16.0, 0.0, 0.0, 16.0, 0.0, 16.0, 0.0, 0.0, 16.0, 8.0, 16.0, 8.0));
        return new CustomOutlineShape(shape, VoxelUtils.scale(VoxelUtils.rotate(direction, outlineVectors)));
    }

    private static VoxelShape makeStaircasePyramid(Direction direction, int layers, double layerHeight) {
        Vec3[] vectors = new Vec3[layers * 2];
        for (int i = 0, j = layers; i < layers; i++, j--) {
            vectors[2 * i] = new Vec3(8.0 - j, i * layerHeight, 8.0 - j);
            vectors[2 * i + 1] = new Vec3(8.0 + j, (i + 1) * layerHeight, 8.0 + j);
        }
        return VoxelUtils.makeCombinedShape(VoxelUtils.rotate(direction, vectors));
    }

    private static VoxelShape makeCollisionShape(Direction direction, boolean fullHeight) {
        // less than full block since entity needs to be inside to receive damage
        // height of 11 is just enough for items to fit through this and a block above, but does not allow the player to walk up the block
        Vec3[] vectors = VoxelUtils.makeVectors(1.0, 0.0, 1.0, 15.0, fullHeight ? 16.0 : 11.0, 15.0);
        return VoxelUtils.makeCombinedShape(VoxelUtils.rotate(direction, vectors));
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos blockPos, CollisionContext context) {
        return SHAPE_BY_DIRECTION.get(blockState.getValue(FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter level, BlockPos blockPos, CollisionContext context) {
        return COLLISION_SHAPE_BY_DIRECTION.get(blockState.getValue(FACING));
    }

    @Override
    public VoxelShape getVisualShape(BlockState blockState, BlockGetter level, BlockPos blockPos, CollisionContext context) {
        return this.getShape(blockState, level, blockPos, context);
    }

    @Override
    public VoxelShape getInteractionShape(BlockState blockState, BlockGetter level, BlockPos blockPos) {
        return INTERACTION_SHAPE_BY_DIRECTION.get(blockState.getValue(FACING));
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState blockState, BlockGetter level, BlockPos blockPos) {
        return Shapes.empty();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos otherPos = pos.relative(direction.getOpposite());
        if (direction == Direction.UP) {
            return canSupportRigidBlock(level, otherPos);
        } else {
            return level.getBlockState(otherPos).isFaceSturdy(level, otherPos, direction);
        }
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos blockPos, BlockPos neighborPos) {
        if (blockState.getValue(WATERLOGGED)) {
            level.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return direction == blockState.getValue(FACING).getOpposite() && !blockState.canSurvive(level, blockPos) ?
                Blocks.AIR.defaultBlockState() : super.updateShape(blockState, direction, neighborState, level,
                blockPos, neighborPos
        );
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = this.defaultBlockState().setValue(WATERLOGGED,
                level.getFluidState(pos).getType() == Fluids.WATER
        );
        for (Direction direction : context.getNearestLookingDirections()) {
            Direction opposite = direction.getOpposite();
            state = state.setValue(FACING, opposite);
            if (state.canSurvive(level, pos)) {
                return state;
            }
        }
        return null;
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, FACING);
    }

    @Override
    public boolean isPathfindable(BlockState blockState, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity instanceof LivingEntity livingEntity && livingEntity.isAlive()) {
            if (!(livingEntity instanceof Player player) ||
                    !player.getAbilities().instabuild && !player.getAbilities().invulnerable) {
                if ((this.spikeMaterial.dealsFinalBlow() ||
                        livingEntity.getHealth() > this.spikeMaterial.damageAmount()) &&
                        (this.spikeMaterial.hurtsPlayers() || !(livingEntity instanceof Player))) {
                    if (this.spikeMaterial.dropsPlayerLoot()) {
                        // this is handled by the block entity as there used to be one player per placed spike (no longer using fake players though)
                        if (level.getBlockEntity(pos) instanceof SpikeBlockEntity blockEntity) {
                            SpikeBlockEntity.attack((ServerLevel) level, pos, level.getBlockState(pos), blockEntity,
                                    livingEntity, this.spikeMaterial
                            );
                        }
                    } else {
                        // cancelling drops via forge event works too, but also cancels equipment drops (e.g. saddles, not spawned equipment) which is not good
                        boolean doMobLoot = level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT);
                        if (!this.spikeMaterial.dropsMobLoot()) {
                            level.getGameRules().getRule(GameRules.RULE_DOMOBLOOT).set(false, level.getServer());
                        }
                        livingEntity.hurt(SpikeDamageSource.source(ModRegistry.SPIKE_DAMAGE_TYPE, level, pos),
                                this.spikeMaterial.damageAmount()
                        );
                        if (!this.spikeMaterial.dropsMobLoot()) {
                            level.getGameRules().getRule(GameRules.RULE_DOMOBLOOT).set(doMobLoot, level.getServer());
                        }
                        // similar to zombified piglins, so we don't have to use a fake player just to get xp
                        if (!livingEntity.isAlive() && this.spikeMaterial.dropsExperience()) {
                            ((LivingEntityAccessor) livingEntity).spikyspikes$setLastHurtByPlayerTime(100);
                            ((LivingEntityAccessor) livingEntity).spikyspikes$callDropExperience(null);
                            livingEntity.skipDropExperience();
                        }
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SpikeBlockEntity(pos, state);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, context, tooltipComponents, tooltipFlag);
        if (context != Item.TooltipContext.EMPTY) {
            if (!Proxy.INSTANCE.hasShiftDown()) {
                tooltipComponents.add(Component.translatable("item.spikyspikes.spike.tooltip.more",
                        Component.translatable("item.spikyspikes.spike.tooltip.shift").withStyle(ChatFormatting.YELLOW)
                ).withStyle(ChatFormatting.GRAY));
            } else {
                tooltipComponents.addAll(Proxy.INSTANCE.splitTooltipLines(
                        Component.translatable(this.getDescriptionId() + ".description")
                                .withStyle(ChatFormatting.GRAY)));
                tooltipComponents.add(Component.translatable("item.spikyspikes.spike.tooltip.damage",
                        Component.translatable("item.spikyspikes.spike.tooltip.hearts", Component.literal(
                                        String.valueOf(TOOLTIP_DAMAGE_FORMAT.format(this.spikeMaterial.damageAmount() / 2.0F))))
                                .withStyle(this.spikeMaterial.getTooltipStyle())
                ).withStyle(ChatFormatting.GOLD));
            }
        }
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState blockState) {
        ItemStack itemStack = super.getCloneItemStack(level, pos, blockState);
        if (this.spikeMaterial.dropsPlayerLoot() && level.getBlockEntity(
                pos) instanceof SpikeBlockEntity blockEntity) {
            itemStack.applyComponents(blockEntity.collectComponents());
        }

        return itemStack;
    }
}
