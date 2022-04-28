package fuzs.spikyspikes.world.level.block;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import fuzs.puzzleslib.proxy.IProxy;
import fuzs.spikyspikes.mixin.accessor.LivingEntityAccessor;
import fuzs.spikyspikes.world.damagesource.SpikeDamageSource;
import fuzs.spikyspikes.world.level.block.entity.SpikeBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.function.Function;

@SuppressWarnings("deprecation")
public class SpikeBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final DecimalFormat TOOLTIP_DAMAGE_FORMAT = Util.make(new DecimalFormat("0.0"), (p_41704_) -> {
        p_41704_.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT));
    });
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    private static final Map<Direction, VoxelShape> SHAPE_BY_DIRECTION = Arrays.stream(Direction.values())
            .collect(Maps.<Direction, Direction, VoxelShape>toImmutableEnumMap(Function.identity(), direction -> makeShape(direction, true)));
    private static final Map<Direction, VoxelShape> COLLISION_SHAPE_BY_DIRECTION = Arrays.stream(Direction.values())
            .collect(Maps.<Direction, Direction, VoxelShape>toImmutableEnumMap(Function.identity(), direction -> makeShape(direction, false)));

    public final SpikeMaterial spikeMaterial;
    private final UUID fakePlayerUUID = UUID.randomUUID();

    public SpikeBlock(SpikeMaterial spikeMaterial, Properties p_49795_) {
        super(p_49795_);
        this.spikeMaterial = spikeMaterial;
        this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, Boolean.FALSE).setValue(FACING, Direction.UP));
    }

    private static VoxelShape makeShape(Direction direction, boolean fullHeight) {
        int height = fullHeight ? 8 : 7;
        VoxelShape fullShape = null;
        // one less so entities will be inside of shape when standing on top/pushing in from the side
        for (int i = 0; i < height; i++) {
            int startX = startCoordinate(i, direction.getStepX(), direction.getAxisDirection());
            int startY = startCoordinate(i, direction.getStepY(), direction.getAxisDirection());
            int startZ = startCoordinate(i, direction.getStepZ(), direction.getAxisDirection());
            int endX = endCoordinate(i, direction.getStepX(), direction.getAxisDirection());
            int endY = endCoordinate(i, direction.getStepY(), direction.getAxisDirection());
            int endZ = endCoordinate(i, direction.getStepZ(), direction.getAxisDirection());
            VoxelShape layer = Block.box(Math.min(startX, endX), Math.min(startY, endY), Math.min(startZ, endZ), Math.max(startX, endX), Math.max(startY, endY), Math.max(startZ, endZ));
            if (i == 0) {
                fullShape = layer;
            } else {
                fullShape = Shapes.or(fullShape, layer);
            }
        }
        return fullShape;
    }

//    private static VoxelShape makeVisualShape(Direction direction, boolean fullHeight) {
//        VoxelUtils.createVectorArray(0.0, 0.0, 0.0, 16.0, 0.0, 0.0, 0.0, 0.0, 16.0, 16.0, 0.0, 16.0, 8.0, 16.0, 8.0);
//    }

    private static int startCoordinate(int i, int step, Direction.AxisDirection axisDirection) {
        int coordinate;
        if (step != 0) {
            coordinate = 2 * i * step;
        } else {
            coordinate = i * axisDirection.getStep();
        }
        // addition as value will be negative
        return axisDirection == Direction.AxisDirection.NEGATIVE ? 16 + coordinate : coordinate;
    }

    private static int endCoordinate(int i, int step, Direction.AxisDirection axisDirection) {
        if (step != 0) {
            return startCoordinate(i + 1, step, axisDirection);
        } else {
            return startCoordinate(i, 0, axisDirection.opposite());
        }
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE_BY_DIRECTION.get(p_60555_.getValue(FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState p_52357_, BlockGetter p_52358_, BlockPos p_52359_, CollisionContext p_52360_) {
        // items and xp shouldn't get stuck inside, good enough for all non-living entities
        if (p_52360_ instanceof EntityCollisionContext context && !(context.getEntity() instanceof LivingEntity)) {
            return Shapes.block();
        }
        return COLLISION_SHAPE_BY_DIRECTION.get(p_52357_.getValue(FACING));
    }

    @Override
    public boolean canSurvive(BlockState p_152026_, LevelReader p_152027_, BlockPos p_152028_) {
        Direction direction = p_152026_.getValue(FACING);
        BlockPos blockpos = p_152028_.relative(direction.getOpposite());
        return p_152027_.getBlockState(blockpos).isFaceSturdy(p_152027_, blockpos, direction);
    }

    @Override
    public BlockState updateShape(BlockState p_152036_, Direction p_152037_, BlockState p_152038_, LevelAccessor p_152039_, BlockPos p_152040_, BlockPos p_152041_) {
        if (p_152036_.getValue(WATERLOGGED)) {
            p_152039_.scheduleTick(p_152040_, Fluids.WATER, Fluids.WATER.getTickDelay(p_152039_));
        }

        return p_152037_ == p_152036_.getValue(FACING).getOpposite() && !p_152036_.canSurvive(p_152039_, p_152040_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_152036_, p_152037_, p_152038_, p_152039_, p_152040_, p_152041_);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext p_152019_) {
        LevelAccessor levelaccessor = p_152019_.getLevel();
        BlockPos blockpos = p_152019_.getClickedPos();
        return this.defaultBlockState().setValue(WATERLOGGED, levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER).setValue(FACING, p_152019_.getClickedFace());
    }

    @Override
    public BlockState rotate(BlockState p_152033_, Rotation p_152034_) {
        return p_152033_.setValue(FACING, p_152034_.rotate(p_152033_.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState p_152030_, Mirror p_152031_) {
        return p_152030_.rotate(p_152031_.getRotation(p_152030_.getValue(FACING)));
    }

    @Override
    public FluidState getFluidState(BlockState p_152045_) {
        return p_152045_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_152045_);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_152043_) {
        p_152043_.add(WATERLOGGED, FACING);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState p_152047_) {
        return PushReaction.DESTROY;
    }

    @Override
    public boolean isPathfindable(BlockState p_60475_, BlockGetter p_60476_, BlockPos p_60477_, PathComputationType p_60478_) {
        return false;
    }

    @Override
    public void entityInside(BlockState p_51148_, Level p_51149_, BlockPos p_51150_, Entity p_51151_) {
        if (!p_51151_.level.isClientSide && p_51151_ instanceof LivingEntity entity && entity.isAlive()) {
            if ((!(entity instanceof Player playerTarget) || !playerTarget.getAbilities().instabuild && !playerTarget.getAbilities().invulnerable) && (this.spikeMaterial.dealsFinalBlow() || entity.getHealth() > this.spikeMaterial.damageAmount)) {
                if (this.spikeMaterial.dropsPlayerLoot()) {
                    GameProfile profile = new GameProfile(this.fakePlayerUUID, this.getName().getString());
                    Player player = FakePlayerFactory.get((ServerLevel) entity.level, profile);
                    player.setSilent(true);
                    if (player.getAttackStrengthScale(0.5F) > 0.9F) {
                        player.attack(entity);
                    }
                } else {
                    ResourceLocation location = ForgeRegistries.BLOCKS.getKey(this);
                    DamageSource damageSource = new SpikeDamageSource(location.getPath(), this.spikeMaterial);
                    if (p_51148_.getValue(FACING) == Direction.DOWN) {
                        damageSource.damageHelmet();
                    }
                    entity.hurt(damageSource, this.spikeMaterial.damageAmount);
                    if (!entity.isAlive() && this.spikeMaterial.dropsJustExperience()) {
                        int lastHurtByPlayerTime = ((LivingEntityAccessor) entity).getLastHurtByPlayerTime();
                        if (lastHurtByPlayerTime <= 0) {
                            ((LivingEntityAccessor) entity).setLastHurtByPlayerTime(100);
                            ((LivingEntityAccessor) entity).callDropExperience();
                            ((LivingEntityAccessor) entity).setLastHurtByPlayerTime(lastHurtByPlayerTime);
                        }
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new SpikeBlockEntity(p_153215_, p_153216_);
    }

    @Override
    public void appendHoverText(ItemStack p_56193_, @Nullable BlockGetter p_56194_, List<Component> tooltip, TooltipFlag p_56196_) {
        super.appendHoverText(p_56193_, p_56194_, tooltip, p_56196_);
        if (p_56194_ == null) return;
        if (!IProxy.INSTANCE.hasShiftDown()) {
            tooltip.add(new TranslatableComponent("item.spikyspikes.spike.tooltip.more", new TranslatableComponent("item.spikyspikes.spike.tooltip.shift").withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(new TranslatableComponent(this.getDescriptionId() + ".description").withStyle(ChatFormatting.GRAY));
            tooltip.add(new TranslatableComponent("item.spikyspikes.spike.tooltip.damage", new TranslatableComponent("item.spikyspikes.spike.tooltip.hearts", new TextComponent(String.valueOf(TOOLTIP_DAMAGE_FORMAT.format(this.spikeMaterial.damageAmount / 2.0F)))).withStyle(this.spikeMaterial.tooltipStyle())).withStyle(ChatFormatting.GOLD));
        }
    }

    public enum SpikeMaterial {
        WOOD(0, 1.0F),
        STONE(1, 2.0F),
        IRON(2, 4.0F),
        GOLD(3, 6.0F),
        DIAMOND(4, 8.0F),
        NETHERITE(5, 12.0F);

        private final int materialTier;
        public final float damageAmount;

        SpikeMaterial(int materialTier, float damageAmount) {
            this.materialTier = materialTier;
            this.damageAmount = damageAmount;
        }

        public ChatFormatting tooltipStyle() {
            if (this.isAtLeast(DIAMOND)) return ChatFormatting.GREEN;
            if (this.isAtLeast(IRON)) return ChatFormatting.AQUA;
            return ChatFormatting.RED;
        }

        public boolean dealsFinalBlow() {
            return this.isAtLeast(STONE);
        }

        public boolean dropsLoot() {
            return this != STONE && this != GOLD;
        }

        public boolean dropsJustExperience() {
            return this == GOLD;
        }

        public boolean dropsPlayerLoot() {
            return this.isAtLeast(DIAMOND);
        }

        public boolean acceptsEnchantments() {
            return this.isAtLeast(DIAMOND);
        }

        private boolean isAtLeast(SpikeMaterial material) {
            return this.materialTier >= material.materialTier;
        }
    }
}
