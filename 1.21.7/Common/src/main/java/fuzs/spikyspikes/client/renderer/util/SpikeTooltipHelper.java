package fuzs.spikyspikes.client.renderer.util;

import fuzs.spikyspikes.SpikySpikes;
import fuzs.spikyspikes.world.level.block.SpikeBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class SpikeTooltipHelper {

    public static List<Component> appendHoverText(SpikeBlock spikeBlock) {
        List<Component> tooltipLines = new ArrayList<>();
        if (!Screen.hasShiftDown()) {
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
