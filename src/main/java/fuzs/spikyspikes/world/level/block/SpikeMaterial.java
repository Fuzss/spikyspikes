package fuzs.spikyspikes.world.level.block;

import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.config.ServerConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.function.ToDoubleFunction;

public enum SpikeMaterial {
    WOOD(0, config -> config.woodenSpikeDamage), STONE(1, config -> config.stoneSpikeDamage), IRON(2, config -> config.ironSpikeDamage), GOLD(3, config -> config.goldenSpikeDamage), DIAMOND(4, config -> config.diamondSpikeDamage), NETHERITE(5, config -> config.netheriteSpikeDamage);

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
