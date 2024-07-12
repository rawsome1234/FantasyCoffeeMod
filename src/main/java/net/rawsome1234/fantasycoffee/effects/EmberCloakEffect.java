package net.rawsome1234.fantasycoffee.effects;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EmberCloakEffect extends MobEffect {
    protected int currentTick = 0;
    protected int tickPause = 20;

    protected EmberCloakEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        Level level = pLivingEntity.level();
        if (!level.isClientSide()){
            currentTick += pAmplifier+1;
        }
//        System.out.println(currentTick);
        if (currentTick >= tickPause || currentTick > 100){
            if(level.isClientSide())
                currentTick = 0;

            Vec3 pPos = pLivingEntity.position();
            AABB aabb = new AABB(-5 + pPos.x,-5 + pPos.y,-5+pPos.z, 5+pPos.x, 5+pPos.y, 5+pPos.z);

            List<LivingEntity> entitiesAround = level.getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(),
                    pLivingEntity, aabb);

            if(!level.isClientSide()) {
                spawnDamageParticles(entitiesAround, (ServerLevel) level, pLivingEntity);
                for(LivingEntity e: entitiesAround){
                    if((e instanceof Monster && !(e instanceof NeutralMob)) || (e instanceof NeutralMob && ((NeutralMob) e).isAngryAt(pLivingEntity))){
                        e.hurt(pLivingEntity.damageSources().playerAttack((Player) pLivingEntity), 2);
                    }
                }
            }
        }
    }

    public void spawnDamageParticles(List<LivingEntity> entities, ServerLevel level, LivingEntity target){
        for(LivingEntity e: entities){
            if((e instanceof Monster && !(e instanceof NeutralMob)) || (e instanceof NeutralMob && ((NeutralMob) e).isAngryAt(target))){
                for(int i = 0; i < 100; i++){
                    level.sendParticles(ParticleTypes.FLAME.getType(), e.getX(), e.getY()+1, e.getZ(), 1,(Math.random())-.5f, (Math.random())-.5f, (Math.random())-.5f, (Math.random())-.5f);
                }
            }
        }
    }


    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public boolean isInstantenous() {
        return false;
    }
}
