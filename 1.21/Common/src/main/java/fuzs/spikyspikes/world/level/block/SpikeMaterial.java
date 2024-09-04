package fuzs.spikyspikes.world.level.block;

import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.config.ServerConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.function.DoubleSupplier;

public enum SpikeMaterial implements StringRepresentable {
    WOOD("wood", 0, () -> SpikySpikes.CONFIG.get(ServerConfig.class).woodenSpikeDamage),
    STONE("stone", 1, () -> SpikySpikes.CONFIG.get(ServerConfig.class).stoneSpikeDamage),
    IRON("iron", 2, () -> SpikySpikes.CONFIG.get(ServerConfig.class).ironSpikeDamage),
    GOLD("gold", 3, () -> SpikySpikes.CONFIG.get(ServerConfig.class).goldenSpikeDamage),
    DIAMOND("diamond", 4, () -> SpikySpikes.CONFIG.get(ServerConfig.class).diamondSpikeDamage),
    NETHERITE("netherite", 5, () -> SpikySpikes.CONFIG.get(ServerConfig.class).netheriteSpikeDamage);

    public static final StringRepresentable.StringRepresentableCodec<SpikeMaterial> CODEC = StringRepresentable.fromEnum(SpikeMaterial::values);

    private final String materialName;
    private final int materialTier;
    private final DoubleSupplier damageAmount;

    SpikeMaterial(String materialName, int materialTier, DoubleSupplier damageAmount) {
        this.materialName = materialName;
        this.materialTier = materialTier;
        this.damageAmount = damageAmount;
    }

    public float damageAmount() {
        return (float) this.damageAmount.getAsDouble();
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

    @Override
    public String getSerializedName() {
        return this.materialName;
    }
}
