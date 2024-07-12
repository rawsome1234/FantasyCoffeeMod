package net.rawsome1234.fantasycoffee.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.rawsome1234.fantasycoffee.FantasyCoffee;

public class ModEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, FantasyCoffee.MOD_ID);

    public static final RegistryObject<MobEffect> FIRE_AURA = MOB_EFFECTS.register("fire_aura",
            () -> new FireAuraEffect(MobEffectCategory.NEUTRAL, 16736634, 3.0f)
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE, "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", 0.0D,AttributeModifier.Operation.ADDITION));

    public static final RegistryObject<MobEffect> EMBER_CLOAK = MOB_EFFECTS.register("ember_cloak",
            () -> new EmberCloakEffect(MobEffectCategory.NEUTRAL, 16736634));

    public static final RegistryObject<MobEffect> TEMPORAL_ANCHOR = MOB_EFFECTS.register("temporal_anchor",
            () -> new TemporalAnchorEffect(MobEffectCategory.NEUTRAL, 108324733));

    public static final RegistryObject<MobEffect> AMBROSIA = MOB_EFFECTS.register("ambrosia",
            () -> new AmbrosiaEffect(MobEffectCategory.BENEFICIAL, 327876193));

    public static void register(IEventBus eventBus){
        MOB_EFFECTS.register(eventBus);
    }

}
