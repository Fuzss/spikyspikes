package fuzs.spikyspikes.world.item;

import fuzs.puzzleslib.api.util.v1.ComponentHelper;
import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.world.level.block.SpikeBlock;
import fuzs.spikyspikes.world.level.block.SpikeMaterial;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.Block;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.function.Consumer;

public class SpikeItem extends BlockItem {
    public static final DecimalFormat TOOLTIP_DAMAGE_FORMAT = Util.make(new DecimalFormat("0.0"),
            (DecimalFormat decimalFormat) -> {
                decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT));
            });

    public SpikeItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        if (this.getSpikeMaterial().dropsPlayerLoot()) {
            return !itemStack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).isEmpty();
        } else {
            return super.isFoil(itemStack);
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> componentConsumer, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, tooltipContext, tooltipDisplay, componentConsumer, tooltipFlag);
        if (tooltipContext != Item.TooltipContext.EMPTY) {
            if (!ComponentHelper.hasShiftDown()) {
                componentConsumer.accept(Component.translatable(TooltipComponent.MORE.getTranslationKey(),
                        Component.translatable(TooltipComponent.SHIFT.getTranslationKey())
                                .withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GRAY));
            } else {
                ComponentHelper.splitTooltipLines(Component.translatable(this.getDescriptionId() + ".description")
                        .withStyle(ChatFormatting.GRAY)).forEach(componentConsumer);
                componentConsumer.accept(Component.translatable(TooltipComponent.DAMAGE.getTranslationKey(),
                        Component.translatable(TooltipComponent.HEARTS.getTranslationKey(),
                                        Component.literal(String.valueOf(TOOLTIP_DAMAGE_FORMAT.format(
                                                this.getSpikeMaterial().damageAmount() / 2.0F))))
                                .withStyle(this.getSpikeMaterial().getTooltipStyle())).withStyle(ChatFormatting.GOLD));
            }
        }
    }

    public SpikeMaterial getSpikeMaterial() {
        return ((SpikeBlock) this.getBlock()).getSpikeMaterial();
    }

    public enum TooltipComponent implements StringRepresentable {
        MORE,
        SHIFT,
        DAMAGE,
        HEARTS;

        public String getTranslationKey() {
            return Util.makeDescriptionId(Registries.elementsDirPath(Registries.ITEM), SpikySpikes.id("spike")) +
                    ".tooltip." + this.getSerializedName();
        }

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}
