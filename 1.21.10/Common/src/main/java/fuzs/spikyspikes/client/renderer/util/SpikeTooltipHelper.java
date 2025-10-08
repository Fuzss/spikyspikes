package fuzs.spikyspikes.client.renderer.util;

import fuzs.puzzleslib.api.util.v1.CommonHelper;
import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.world.level.block.SpikeBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SpikeTooltipHelper {

    public static List<Component> appendHoverText(SpikeBlock spikeBlock) {
        List<Component> tooltipLines = new ArrayList<>();
        if (!CommonHelper.hasShiftDown()) {
            tooltipLines.add(Component.translatable(SpikeTooltipHelper.TooltipComponent.ADDITIONAL.getTranslationKey(),
                    Component.translatable(SpikeTooltipHelper.TooltipComponent.SHIFT.getTranslationKey())
                            .withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GRAY));
        } else {
            tooltipLines.add(spikeBlock.getDescriptionComponent());
            tooltipLines.add(Component.translatable(SpikeTooltipHelper.TooltipComponent.DAMAGE.getTranslationKey(),
                    spikeBlock.getSpikeMaterial().getDamageComponent()).withStyle(ChatFormatting.GOLD));
        }

        return tooltipLines;
    }

    public enum TooltipComponent implements StringRepresentable {
        ADDITIONAL,
        SHIFT,
        DAMAGE;

        public String getTranslationKey() {
            return Util.makeDescriptionId(Registries.elementsDirPath(Registries.ITEM), SpikySpikes.id("spike"))
                    + ".tooltip." + this.getSerializedName();
        }

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}
