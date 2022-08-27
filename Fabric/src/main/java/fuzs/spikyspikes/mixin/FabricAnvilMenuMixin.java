package fuzs.spikyspikes.mixin;

import fuzs.spikyspikes.api.event.AnvilUpdateCallback;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(AnvilMenu.class)
public abstract class FabricAnvilMenuMixin extends ItemCombinerMenu {
    @Shadow
    private int repairItemCountCost;
    @Shadow
    private String itemName;
    @Shadow
    @Final
    private DataSlot cost;

    public FabricAnvilMenuMixin(@Nullable MenuType<?> menuType, int i, Inventory inventory, ContainerLevelAccess containerLevelAccess) {
        super(menuType, i, inventory, containerLevelAccess);
    }

    @Inject(method = "createResult", at = @At(value = "FIELD", target = "Lnet/minecraft/world/inventory/AnvilMenu;repairItemCountCost:I", shift = At.Shift.AFTER, ordinal = 0), cancellable = true)
    public void createResult(CallbackInfo callback) {
        ItemStack itemstack = this.inputSlots.getItem(0);
        ItemStack itemstack2 = this.inputSlots.getItem(1);
        MutableObject<ItemStack> output = new MutableObject<>(ItemStack.EMPTY);
        int j = itemstack.getBaseRepairCost() + (itemstack2.isEmpty() ? 0 : itemstack2.getBaseRepairCost());
        MutableInt cost = new MutableInt(j);
        MutableInt materialCost = new MutableInt(0);
        Optional<Unit> result = AnvilUpdateCallback.EVENT.invoker().onAnvilUpdate(itemstack, itemstack2, output, this.itemName, cost, materialCost, this.player);
        if (result.isPresent()) {
            callback.cancel();
            return;
        }
        if (output.getValue().isEmpty()) return;
        this.resultSlots.setItem(0, output.getValue());
        this.cost.set(cost.intValue());
        this.repairItemCountCost = materialCost.intValue();
        callback.cancel();
    }
}
