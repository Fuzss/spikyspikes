package fuzs.spikyspikes.world.damagesource;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class SpikeEntityDamageSource extends EntityDamageSource {
    private static final String SPIKE_DAMAGE_SOURCE_KEY = "spike";
    public static final DamageSource SPIKE_DAMAGE_SOURCE = new DamageSource(SPIKE_DAMAGE_SOURCE_KEY);

    public final int lootingLevel;

    private SpikeEntityDamageSource(Player player, int lootingLevel) {
        super(SPIKE_DAMAGE_SOURCE_KEY, player);
        this.lootingLevel = lootingLevel;
    }

    @Override
    public Component getLocalizedDeathMessage(LivingEntity p_19343_) {
        // remove the player (/killer) part as it'll show the spike which is already mentioned in the message itself
        String s = "death.attack." + this.msgId;
        return new TranslatableComponent(s, p_19343_.getDisplayName());
    }

    public static DamageSource spikeEntity(Player player, int lootingLevel) {
        return new SpikeEntityDamageSource(player, lootingLevel);
    }
}
