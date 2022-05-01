package fuzs.spikyspikes.world.level.block;

import com.google.common.collect.Maps;
import fuzs.puzzleslib.proxy.IProxy;
import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.config.ServerConfig;
import fuzs.spikyspikes.core.world.phys.shapes.CustomOutlineShape;
import fuzs.spikyspikes.core.world.phys.shapes.VoxelUtils;
import fuzs.spikyspikes.mixin.accessor.LivingEntityAccessor;
import fuzs.spikyspikes.world.damagesource.SpikePlayerDamageSource;
import fuzs.spikyspikes.world.level.block.entity.SpikeBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
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
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

/**
 * code for facing copied from {@link AmethystClusterBlock}
 */
@SuppressWarnings("deprecation")
public class SpikeBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final DecimalFormat TOOLTIP_DAMAGE_FORMAT = Util.make(new DecimalFormat("0.0"), (p_41704_) -> {
        p_41704_.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT));
    });
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    private static final Map<Direction, VoxelShape> SHAPE_BY_DIRECTION = Arrays.stream(Direction.values())
            .collect(Maps.<Direction, Direction, VoxelShape>toImmutableEnumMap(Function.identity(), SpikeBlock::makeVisualShape));
    private static final Map<Direction, VoxelShape> COLLISION_SHAPE_BY_DIRECTION = Arrays.stream(Direction.values())
            .collect(Maps.<Direction, Direction, VoxelShape>toImmutableEnumMap(Function.identity(), SpikeBlock::makeCollisionShape));

    public final SpikeMaterial spikeMaterial;

    public SpikeBlock(SpikeMaterial spikeMaterial, Properties p_49795_) {
        super(p_49795_);
        this.spikeMaterial = spikeMaterial;
        this.registerDefaultState(this.defaultBlockState().setValue(WATERLOGGED, Boolean.FALSE).setValue(FACING, Direction.UP));
    }

    private static VoxelShape makeVisualShape(Direction direction) {
        VoxelShape shape = makeStaircasePyramid(direction, 8, 2.0);
        Vec3[] outlineVectors = VoxelUtils.makePyramidEdges(VoxelUtils.makeVectors(0.0, 0.0, 0.0, 16.0, 0.0, 0.0, 16.0, 0.0, 16.0, 0.0, 0.0, 16.0, 8.0, 16.0, 8.0));
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

    private static VoxelShape makeCollisionShape(Direction direction) {
        // less than full block since entity needs to be inside to receive damage
        // height of 11 is just enough for items to fit through this and a block above, but does not allow the player to walk up the block
        Vec3[] vectors = VoxelUtils.makeVectors(1.0, 0.0, 1.0, 15.0, 11.0, 15.0);
        return VoxelUtils.makeCombinedShape(VoxelUtils.rotate(direction, vectors));
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE_BY_DIRECTION.get(p_60555_.getValue(FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState p_52357_, BlockGetter p_52358_, BlockPos p_52359_, CollisionContext p_52360_) {
        return COLLISION_SHAPE_BY_DIRECTION.get(p_52357_.getValue(FACING));
    }

    @Override
    public VoxelShape getVisualShape(BlockState p_60479_, BlockGetter p_60480_, BlockPos p_60481_, CollisionContext p_60482_) {
        return this.getShape(p_60479_, p_60480_, p_60481_, p_60482_);
    }

    @Override
    public VoxelShape getInteractionShape(BlockState p_60547_, BlockGetter p_60548_, BlockPos p_60549_) {
        return COLLISION_SHAPE_BY_DIRECTION.get(p_60547_.getValue(FACING));
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState p_60581_, BlockGetter p_60582_, BlockPos p_60583_) {
        return Shapes.empty();
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
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity p_51151_) {
        if (!level.isClientSide && p_51151_ instanceof LivingEntity entity && entity.isAlive()) {
            if (!(entity instanceof Player player) || !player.getAbilities().instabuild && !player.getAbilities().invulnerable) {
                SpikeMaterial material = this.spikeMaterial;
                if ((material.dealsFinalBlow() || entity.getHealth() > material.damageAmount()) && (material.hurtsPlayers() || !(entity instanceof Player))) {
                    if (material.dropsPlayerLoot()) {
                        // this is handled by the block entity so we can have one player per placed spike
                        if (level.getBlockEntity(pos) instanceof SpikeBlockEntity blockEntity) {
                            blockEntity.attack(entity, material.damageAmount());
                        }
                    } else {
                        // cancelling drops via forge event works too, but also cancels equipment drops (e.g. saddles, not spawned equipment) which is not good
                        boolean doMobLoot = level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT);
                        if (!material.dropsLoot()) {
                            level.getGameRules().getRule(GameRules.RULE_DOMOBLOOT).set(false, level.getServer());
                        }
                        entity.hurt(SpikePlayerDamageSource.SPIKE_DAMAGE_SOURCE, material.damageAmount());
                        if (!material.dropsLoot()) {
                            level.getGameRules().getRule(GameRules.RULE_DOMOBLOOT).set(doMobLoot, level.getServer());
                        }
                        // similar to zombified piglins, so we don't have to use a fake player just to get xp
                        if (!entity.isAlive() && material.dropsJustExperience()) {
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
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new SpikeBlockEntity(p_153215_, p_153216_);
    }

    @Override
    public void setPlacedBy(Level p_55179_, BlockPos p_55180_, BlockState p_55181_, @javax.annotation.Nullable LivingEntity p_55182_, ItemStack stack) {
        super.setPlacedBy(p_55179_, p_55180_, p_55181_, p_55182_, stack);
        if (p_55179_.getBlockEntity(p_55180_) instanceof SpikeBlockEntity blockEntity) {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.deserializeEnchantments(stack.getEnchantmentTags());
            blockEntity.setEnchantmentData(enchantments, stack.getBaseRepairCost());
        }
    }

    @Override
    public void appendHoverText(ItemStack p_56193_, @Nullable BlockGetter p_56194_, List<Component> tooltip, TooltipFlag p_56196_) {
        super.appendHoverText(p_56193_, p_56194_, tooltip, p_56196_);
        if (p_56194_ == null) return;
        if (!IProxy.INSTANCE.hasShiftDown()) {
            tooltip.add(new TranslatableComponent("item.spikyspikes.spike.tooltip.more", new TranslatableComponent("item.spikyspikes.spike.tooltip.shift").withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(new TranslatableComponent(this.getDescriptionId() + ".description").withStyle(ChatFormatting.GRAY));
            tooltip.add(new TranslatableComponent("item.spikyspikes.spike.tooltip.damage", new TranslatableComponent("item.spikyspikes.spike.tooltip.hearts", new TextComponent(String.valueOf(TOOLTIP_DAMAGE_FORMAT.format(this.spikeMaterial.damageAmount() / 2.0F)))).withStyle(this.spikeMaterial.tooltipStyle())).withStyle(ChatFormatting.GOLD));
        }
    }

    @Nullable
    @Override
    public BlockPathTypes getAiPathNodeType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob entity) {
        return BlockPathTypes.DAMAGE_CACTUS;
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState p_49825_) {
        ItemStack stack = super.getCloneItemStack(level, pos, p_49825_);
        if (this.spikeMaterial.acceptsEnchantments()) {
            if (level.getBlockEntity(pos) instanceof SpikeBlockEntity blockEntity) {
                stack.setTag(blockEntity.saveWithoutMetadata());
            }
        }
        return stack;
    }

    public enum SpikeMaterial {
        WOOD(0, config -> config.woodenSpikeDamage),
        STONE(1, config -> config.stoneSpikeDamage),
        IRON(2, config -> config.ironSpikeDamage),
        GOLD(3, config -> config.goldenSpikeDamage),
        DIAMOND(4, config -> config.diamondSpikeDamage),
        NETHERITE(5, config -> config.netheriteSpikeDamage);

        private final int materialTier;
        private final ToDoubleFunction<ServerConfig> damageAmount;

        SpikeMaterial(int materialTier, ToDoubleFunction<ServerConfig> damageAmount) {
            this.materialTier = materialTier;
            this.damageAmount = damageAmount;
        }

        public float damageAmount() {
            return (float) this.damageAmount.applyAsDouble(SpikySpikes.CONFIG.server());
        }

        public ChatFormatting tooltipStyle() {
            if (this.isAtLeast(DIAMOND)) return ChatFormatting.GREEN;
            if (this.isAtLeast(IRON)) return ChatFormatting.AQUA;
            return ChatFormatting.RED;
        }

        public Item swordItem() {
            return switch (this) {
                case WOOD -> Items.WOODEN_SWORD;
                case STONE -> Items.STONE_SWORD;
                case IRON -> Items.IRON_SWORD;
                case GOLD -> Items.GOLDEN_SWORD;
                case DIAMOND -> Items.DIAMOND_SWORD;
                case NETHERITE -> Items.NETHERITE_SWORD;
            };
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

        public boolean hurtsPlayers() {
            return this != NETHERITE;
        }

        private boolean isAtLeast(SpikeMaterial material) {
            return this.materialTier >= material.materialTier;
        }
    }
}
