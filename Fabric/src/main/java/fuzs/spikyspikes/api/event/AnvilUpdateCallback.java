package fuzs.spikyspikes.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.Optional;

@FunctionalInterface
public interface AnvilUpdateCallback {
    Event<AnvilUpdateCallback> EVENT = EventFactory.createArrayBacked(AnvilUpdateCallback.class, listeners -> (ItemStack left, ItemStack right, MutableObject<ItemStack> output, String name, MutableInt cost, MutableInt materialCost, Player player) -> {
        for (AnvilUpdateCallback event : listeners) {
            if (event.onAnvilUpdate(left, right, output, name, cost, materialCost, player).isPresent()) {
                return Optional.of(Unit.INSTANCE);
            }
        }
        return Optional.empty();
    });

    Optional<Unit> onAnvilUpdate(ItemStack left, ItemStack right, MutableObject<ItemStack> output, String name, MutableInt cost, MutableInt materialCost, Player player);
}
