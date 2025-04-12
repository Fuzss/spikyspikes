package fuzs.spikyspikes.client.renderer.util;

import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.world.level.block.SpikeBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.Locale;
import java.util.function.Consumer;

public class SpikeTooltipHelper {

    public static void appendHoverText(SpikeBlock spikeBlock, ItemStack itemStack, Item.TooltipContext tooltipContext, TooltipFlag tooltipFlag, Consumer<Component> tooltipLineConsumer) {
        if (!Screen.hasShiftDown()) {
            tooltipLineConsumer.accept(Component.translatable(SpikeTooltipHelper.TooltipComponent.ADDITIONAL.getTranslationKey(),
                    Component.translatable(SpikeTooltipHelper.TooltipComponent.SHIFT.getTranslationKey())
                            .withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GRAY));
        } else {
            tooltipLineConsumer.accept(spikeBlock.getDescriptionComponent());
            tooltipLineConsumer.accept(Component.translatable(SpikeTooltipHelper.TooltipComponent.DAMAGE.getTranslationKey(),
                    spikeBlock.getSpikeMaterial().getDamageComponent()).withStyle(ChatFormatting.GOLD));
        }
    }

    public enum TooltipComponent implements StringRepresentable {
        ADDITIONAL,
        SHIFT,
        DAMAGE;

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
