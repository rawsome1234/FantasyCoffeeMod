package net.rawsome1234.fantasycoffee.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FireAuraEffect extends MobEffect {

    protected final float multiplier;
    protected boolean onFire = false;

    protected FireAuraEffect(MobEffectCategory pCategory, int pColor, float pMultiplier) {
        super(pCategory, pColor);
        this.multiplier = pMultiplier;
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        Level level = pLivingEntity.level();
//        if (!pLivingEntity.isOnFire())
        if(pLivingEntity.getRemainingFireTicks() < 21 || !pLivingEntity.isOnFire()){
            pLivingEntity.setSecondsOnFire(3);
        }
        onFire = pLivingEntity.isOnFire();

        Vec3 pPos = pLivingEntity.position();

        AABB aabb = new AABB(-5 + pPos.x,-5 + pPos.y,-5+pPos.z, 5+pPos.x, 5+pPos.y, 5+pPos.z);
        if(level != null && !level.isClientSide()){
//            System.out.println("Getting Surrounding Entities");
            List<LivingEntity> entitiesAround = level.getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(),
                    pLivingEntity, aabb);
//            System.out.println("Found entity around: " + (entitiesAround.size() > 0));
            for(LivingEntity e: entitiesAround){
                if(e.getRemainingFireTicks() < 21 || !e.isOnFire()){
                    e.setSecondsOnFire(3);
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    public double getAttributeModifierValue(int pAmplifier, AttributeModifier pModifier) {
        if (onFire){
            return this.multiplier * (double)(pAmplifier + 1);
        }
        return 0;
    }
}
