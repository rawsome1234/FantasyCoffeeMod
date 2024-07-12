package net.rawsome1234.fantasycoffee.effects;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.rawsome1234.fantasycoffee.effects.ModEffects;
import net.rawsome1234.fantasycoffee.effects.TemporalAnchorEffect;
import org.apache.logging.log4j.core.jmx.Server;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

@Mod.EventBusSubscriber
public class AmbrosiaEffect extends MobEffect {

    protected static int cooldown_ticks = 3;

    protected static MinecraftServer server;

    public AmbrosiaEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
//        jumps = 1;
//        cooldown = 0;
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (server == null && !pLivingEntity.level().isClientSide()){
            server = pLivingEntity.getServer();
        }
        CompoundTag tag = pLivingEntity.getPersistentData();
        int jumps = tag.getInt("ambr_jumps");
        if (pLivingEntity.onGround()){
            jumps = pAmplifier+1;
        }
//        System.out.println(jumps);
        int cooldown = tag.getInt("ambr_cool");
        if(cooldown > 0){
            cooldown -= 1;
        }

        pLivingEntity.resetFallDistance();

//        System.out.println("Jumps: " + jumps);
//        System.out.println("Attempting jump");
//        Player p = Minecraft.getInstance().player;
        if (Minecraft.getInstance().options.keyJump.isDown() && pLivingEntity.level() == Minecraft.getInstance().level) {

//            System.out.println("Jumped last tick: " + wasJumping + " and cooldown is : " + cooldown);

            if((!tag.contains("ambr_was_jump") && tag.getBoolean("ambr_was_jump")) || cooldown > 0){
                return;
            }

            tag.putBoolean("ambr_was_jump", true);
            cooldown = cooldown_ticks;
//            System.out.println("Attempting jump with " + jumps + " jumps while onground is " + pLivingEntity.onGround());

            if(jumps > 0 && (!pLivingEntity.onGround())){
                Vector3f direction = pLivingEntity.getLookAngle().toVector3f();
                if(Math.abs(pLivingEntity.getDeltaMovement().x()) < .01d){
                    direction = new Vector3f(0,0,0);
                }
                pLivingEntity.setDeltaMovement(new Vec3(direction.x *.7f, .7f, direction.z *.7f));
                jumps -= 1;
                pLivingEntity.hurtMarked = true;
//                System.out.println(direction);

                spawnParticles(pLivingEntity.position(), pLivingEntity.level().dimension());
            }
        }
        else{
            tag.putBoolean("ambr_was_jump", false);
        }

        tag.putInt("ambr_jumps", jumps);
        tag.putInt("ambr_cool", cooldown);
    }
//
//    @SubscribeEvent
//    public void detectJump(InputEvent.Key event){
//        if (event.getKey())
//    }

    public void spawnParticles(Vec3 location, ResourceKey<Level> levelKey){
        for(int i = 0; i < 10; i++){
            server.getLevel(levelKey).sendParticles(ParticleTypes.CLOUD.getType(), location.x, location.y, location.z, 1, (Math.random()/8f)-(1f/8), (Math.random()/8f)-(1f/8), (Math.random()/8f)-(1f/8), (Math.random()/8f)-(1f/8));
//                        pLivingEntity.level().addParticle(ParticleTypes.CLOUD.getType(), location.x, location.y, location.z, (Math.random()/8f)-(1f/8), (Math.random()/8f)-(1f/8), (Math.random()/8f)-(1f/8));
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public void applyInstantenousEffect(@Nullable Entity pSource, @Nullable Entity pIndirectSource, LivingEntity pLivingEntity, int pAmplifier, double pHealth) {
        pLivingEntity.setAbsorptionAmount(20);
    }

    //    @SubscribeEvent
//    public static void fixFall(MobEffectEvent.Expired event){
//        Level world = event.getEntity().level();
//        if(!world.isClientSide() && event.getEffectInstance().getEffect() == ModEffects.AMBROSIA.get() && !event.getEntity().isDeadOrDying()){
//            AmbrosiaEffect effectInstance = (AmbrosiaEffect) event.getEffectInstance().getEffect();
//            event.getEntity().fall
//        }
//    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        pLivingEntity.setAbsorptionAmount(pLivingEntity.getAbsorptionAmount() - (float)(20));
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    }

    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        pLivingEntity.setAbsorptionAmount(pLivingEntity.getAbsorptionAmount() + (float)(20));
        super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    }

}
