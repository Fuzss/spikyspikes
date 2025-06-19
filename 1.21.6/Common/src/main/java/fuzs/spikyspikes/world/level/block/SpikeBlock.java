package fuzs.spikyspikes.world.level.block;

import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.spikyspikes.init.ModRegistry;
import fuzs.spikyspikes.world.damagesource.SpikeDamageSource;
import fuzs.spikyspikes.world.level.block.entity.SpikeBlockEntity;
import fuzs.spikyspikes.world.phys.shapes.CustomOutlineShape;
import fuzs.spikyspikes.world.phys.shapes.VoxelUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Code for facing copied from {@link AmethystClusterBlock}.
 */
public class SpikeBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final MapCodec<SpikeBlock> CODEC = spikeCodec(SpikeBlock::new);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    public static final BooleanProperty ENCHANTED = BooleanProperty.create("enchanted");
    private static final Map<Direction, VoxelShape> SHAPE_BY_DIRECTION = Arrays.stream(Direction.values())
            .collect(Maps.<Direction, Direction, VoxelShape>toImmutableEnumMap(Function.identity(),
                    SpikeBlock::makeVisualShape));
    private static final Map<Direction, VoxelShape> COLLISION_SHAPE_BY_DIRECTION = Arrays.stream(Direction.values())
            .collect(Maps.<Direction, Direction, VoxelShape>toImmutableEnumMap(Function.identity(),
                    direction -> makeCollisionShape(direction, false)));
    private static final Map<Direction, VoxelShape> INTERACTION_SHAPE_BY_DIRECTION = Arrays.stream(Direction.values())
            .collect(Maps.<Direction, Direction, VoxelShape>toImmutableEnumMap(Function.identity(),
                    direction -> makeCollisionShape(direction, true)));

    private final SpikeMaterial spikeMaterial;

    public SpikeBlock(SpikeMaterial spikeMaterial, Properties properties) {
        super(properties);
        this.spikeMaterial = spikeMaterial;
        this.registerDefaultState(this.defaultBlockState()
                .setValue(WATERLOGGED, Boolean.FALSE)
                .setValue(FACING, Direction.UP)
                .setValue(ENCHANTED, Boolean.FALSE));
    }

    protected static <T extends SpikeBlock> MapCodec<T> spikeCodec(BiFunction<SpikeMaterial, Properties, T> factory) {
        return RecordCodecBuilder.mapCodec((instance) -> {
            return instance.group(SpikeMaterial.CODEC.fieldOf("material").forGetter(SpikeBlock::getSpikeMaterial),
                    propertiesCodec()).apply(instance, factory);
        });
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public SpikeMaterial getSpikeMaterial() {
        return this.spikeMaterial;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return state.getValue(ENCHANTED) ? RenderShape.INVISIBLE : RenderShape.MODEL;
    }

    private static VoxelShape makeVisualShape(Direction direction) {
        VoxelShape shape = makeStaircasePyramid(direction, 8, 2.0);
        Vec3[] outlineVectors = VoxelUtils.makePyramidEdges(VoxelUtils.makeVectors(0.0,
                0.0,
                0.0,
                16.0,
                0.0,
                0.0,
                16.0,
                0.0,
                16.0,
                0.0,
                0.0,
                16.0,
                8.0,
                16.0,
                8.0));
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
    protected VoxelShape getOcclusionShape(BlockState state) {
        // block entity renderer won't render when far away, shouldn't occlude other block faces then
        return this.getRenderShape(state) != RenderShape.INVISIBLE ? super.getOcclusionShape(state) : Shapes.empty();
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
    protected BlockState updateShape(BlockState blockState, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos blockPos, Direction direction, BlockPos neighborBlockPos, BlockState neighborBlockState, RandomSource randomSource) {
        if (blockState.getValue(WATERLOGGED)) {
            scheduledTickAccess.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.getTickDelay(levelReader));
        }
        return direction == blockState.getValue(FACING).getOpposite() && !this.canSurvive(blockState,
                levelReader,
                blockPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState,
                levelReader,
                scheduledTickAccess,
                blockPos,
                direction,
                neighborBlockPos,
                neighborBlockState,
                randomSource);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = this.defaultBlockState()
                .setValue(WATERLOGGED, level.getFluidState(blockPos).getType() == Fluids.WATER)
                .setValue(ENCHANTED,
                        !context.getItemInHand()
                                .getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY)
                                .isEmpty());
        for (Direction direction : context.getNearestLookingDirections()) {
            blockState = blockState.setValue(FACING, direction.getOpposite());
            if (blockState.canSurvive(level, blockPos)) {
                return blockState;
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
        builder.add(WATERLOGGED, FACING, ENCHANTED);
    }

    @Override
    public boolean isPathfindable(BlockState blockState, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    protected void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity, InsideBlockEffectApplier insideBlockEffectApplier) {
        if (level instanceof ServerLevel serverLevel && entity instanceof LivingEntity livingEntity
                && livingEntity.isAlive()) {
            if (!(livingEntity instanceof Player player)
                    || !player.getAbilities().instabuild && !player.getAbilities().invulnerable) {
                if ((this.spikeMaterial.dealsFinalBlow()
                        || livingEntity.getHealth() > this.spikeMaterial.damageAmount()) && (
                        this.spikeMaterial.hurtsPlayers() || !(livingEntity instanceof Player))) {
                    if (this.spikeMaterial.dropsPlayerLoot()) {
                        // this is handled by the block entity as there used to be one player per placed spike (no longer using fake players though)
                        if (level.getBlockEntity(blockPos) instanceof SpikeBlockEntity blockEntity) {
                            SpikeBlockEntity.attack((ServerLevel) level,
                                    blockPos,
                                    level.getBlockState(blockPos),
                                    blockEntity,
                                    livingEntity,
                                    this.spikeMaterial);
                        }
                    } else {
                        // cancelling drops via forge event works too, but also cancels equipment drops (e.g. saddles, not spawned equipment) which is not good
                        boolean doMobLoot = serverLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT);
                        if (!this.spikeMaterial.dropsMobLoot()) {
                            serverLevel.getGameRules().getRule(GameRules.RULE_DOMOBLOOT).set(false, level.getServer());
                        }
                        livingEntity.hurtServer(serverLevel,
                                SpikeDamageSource.source(ModRegistry.SPIKE_DAMAGE_TYPE, level, blockPos),
                                this.spikeMaterial.damageAmount());
                        if (!this.spikeMaterial.dropsMobLoot()) {
                            serverLevel.getGameRules()
                                    .getRule(GameRules.RULE_DOMOBLOOT)
                                    .set(doMobLoot, level.getServer());
                        }
                        // similar to zombified piglins, so we don't have to use a fake player just to get xp
                        if (!livingEntity.isAlive() && this.spikeMaterial.dropsExperience()) {
                            livingEntity.setLastHurtByPlayer((EntityReference<Player>) null, 100);
                            livingEntity.dropExperience(serverLevel, null);
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
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        ItemStack itemStack = super.getCloneItemStack(level, pos, state, includeData);
        if (this.spikeMaterial.dropsPlayerLoot() && level.getBlockEntity(pos) instanceof SpikeBlockEntity blockEntity) {
            itemStack.applyComponents(blockEntity.collectComponents());
        }

        return itemStack;
    }

    public Component getDescriptionComponent() {
        return Component.translatable(this.getDescriptionId() + ".description").withStyle(ChatFormatting.GRAY);
    }
}
