package net.rawsome1234.fantasycoffee.effects;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionDefaults;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.core.jmx.Server;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

@Mod.EventBusSubscriber
public class TemporalAnchorEffect extends MobEffect {

//    public boolean initialLocation = false;
//    public boolean dimension = null;
//
//    public boolean lookVector = null;

    protected TemporalAnchorEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
//        System.out.println("Running effect tick");
        if (!pLivingEntity.getPersistentData().contains("ta_x")) {
//            System.out.println("Set initial position");
            Vec3 loc = pLivingEntity.position();
            CompoundTag tag = pLivingEntity.getPersistentData();
            tag.putDouble("ta_x", loc.x);
            tag.putDouble("ta_y", loc.y);
            tag.putDouble("ta_z", loc.z);
//            pLivingEntity.addAdditionalSaveData(tag);
//            initialLocation = true;
        }

        if(!pLivingEntity.getPersistentData().contains("ta_dim") && !pLivingEntity.level().isClientSide()){
            CompoundTag tag = pLivingEntity.getPersistentData();
            tag.putString("ta_dim", ((ServerLevel) pLivingEntity.level()).dimension().toString());
//            pLivingEntity.addAdditionalSaveData(tag);
//            dimension = true;
        }

        if(!pLivingEntity.getPersistentData().contains("ta_lookx")){
            Vec2 loc = pLivingEntity.getRotationVector();
            CompoundTag tag = pLivingEntity.getPersistentData();
            tag.putFloat("ta_lookx", loc.x);
            tag.putFloat("ta_looky", loc.y);
//            pLivingEntity.addAdditionalSaveData(tag);
//            lookVector = true;
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    }

    public static ServerLevel readDimensionFromTag(LivingEntity pLivingEntity, CompoundTag tag){
        if (tag.contains("ta_dim")){
            String id = tag.getString("ta_dim");
            for (ServerLevel level : pLivingEntity.getServer().getAllLevels()) {
                if(level.dimension().toString().equals(id)){
                    return level;
                }
            }
        }
        return null;
    }

    public static Vec3 readPositionFromTag(CompoundTag tag){
        double x = 0;
        double y = 0;
        double z = 0;
        if(tag.contains("ta_x")){
            x = tag.getDouble("ta_x");
        }
        if(tag.contains("ta_y")){
            y = tag.getDouble("ta_y");
        }
        if(tag.contains("ta_z")){
            z = tag.getDouble("ta_z");
        }
        return new Vec3(x,y,z);
    }

    public static Vec2 readLookVectorFromTag(CompoundTag tag){
        float x = 0;
        float y = 0;
        if(tag.contains("ta_lookx")){
            x = tag.getFloat("ta_lookx");
        }
        if(tag.contains("ta_looky")){
            y = tag.getFloat("ta_looky");
        }
        return new Vec2(x,y);
    }

    @SubscribeEvent
    public static void warpPosition(MobEffectEvent.Expired event){
        Level world = event.getEntity().level();
        if(!world.isClientSide() && event.getEffectInstance().getEffect() == ModEffects.TEMPORAL_ANCHOR.get() && !event.getEntity().isDeadOrDying()){
            TemporalAnchorEffect effectInstance = (TemporalAnchorEffect) event.getEffectInstance().getEffect();
            warpPlayer(effectInstance, event.getEntity());
        }
        else if (!world.isClientSide() && event.getEffectInstance().getEffect() == ModEffects.TEMPORAL_ANCHOR.get() && !event.getEntity().isDeadOrDying()){
            Vec3 location = TemporalAnchorEffect.readPositionFromTag(event.getEntity().getPersistentData());
            for(int i = 0; i < 50; i++){
                world.addParticle(ParticleTypes.PORTAL.getType(), location.x, location.y+1, location.z, (Math.random()/2f)-(1f/2), (Math.random()/2f)-(1f/2), (Math.random()/2f)-(1f/2));
            }
        }
    }

    public static void warpPlayer(TemporalAnchorEffect instance, LivingEntity pLivingEntity){
        Vec3 pos = TemporalAnchorEffect.readPositionFromTag(pLivingEntity.getPersistentData());
        ArrayList<RelativeMovement> movement = new ArrayList<>(RelativeMovement.ALL);
        ServerLevel level = TemporalAnchorEffect.readDimensionFromTag(pLivingEntity, pLivingEntity.getPersistentData());
        Vec2 lookVector = TemporalAnchorEffect.readLookVectorFromTag(pLivingEntity.getPersistentData());
        if(level == null){
            level = (ServerLevel) pLivingEntity.level();
        }
        pLivingEntity.teleportTo(level, pos.x, pos.y, pos.z, Set.copyOf(movement), lookVector.y, lookVector.x);
        pLivingEntity.sendSystemMessage(Component.translatable("effect.fantasycoffee.temporal_anchor_message").withStyle(ChatFormatting.BLUE));
        CompoundTag tag = pLivingEntity.getPersistentData();
        tag.remove("ta_x");
        tag.remove("ta_y");
        tag.remove("ta_z");
        tag.remove("ta_dim");
        tag.remove("ta_lookx");
        tag.remove("ta_looky");

    }

    @SubscribeEvent
    public static void warpFromDying(LivingDeathEvent event){
        Level world = event.getEntity().level();
//        System.out.println("Running from death");

        Vec3 location = event.getEntity().position();

        if(!world.isClientSide() && event.getEntity().hasEffect(ModEffects.TEMPORAL_ANCHOR.get())){
//            System.out.println("saving player..");
            TemporalAnchorEffect effectInstance = (TemporalAnchorEffect) event.getEntity().getEffect(ModEffects.TEMPORAL_ANCHOR.get()).getEffect();
            event.setCanceled(true);
            event.getEntity().setHealth(1);
            event.getEntity().addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 50, 49));
            warpPlayer(effectInstance, event.getEntity());
            event.getEntity().removeEffect(ModEffects.TEMPORAL_ANCHOR.get());
        }
        else if (world.isClientSide() && event.getEntity().hasEffect(ModEffects.TEMPORAL_ANCHOR.get())){
            world.addParticle(ParticleTypes.PORTAL.getType(), location.x, location.y+1, location.z, (Math.random()/4f)-(1f/4), (Math.random()/4f)-(1f/4), (Math.random()/4f)-(1f/4));
        }

    }

}
