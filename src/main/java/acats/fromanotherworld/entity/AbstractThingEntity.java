package acats.fromanotherworld.entity;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.config.EntityClassification;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class AbstractThingEntity extends HostileEntity {
    protected AbstractThingEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = STRONG_MONSTER_XP;
    }

    @Override
    public boolean canTarget(LivingEntity target) {
        if (FromAnotherWorld.isThing(target) || !FromAnotherWorld.canAssimilate(target) && !EntityClassification.isAttackableButNotAssimilable(target)){
            return false;
        }
        return super.canTarget(target);
    }

    public void addThingTargets(int priority){
        this.targetSelector.add(priority, new ActiveTargetGoal<>(this, PlayerEntity.class, false));
        this.targetSelector.add(priority, new ActiveTargetGoal<>(this, LivingEntity.class, false));
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    public boolean shouldBeCounted(){
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.world.isClient() && !this.isOnFire()){
            this.heal(1.0F);
        }
    }

    @Override
    public boolean tryAttack(Entity target) {
        if (FromAnotherWorld.assimilate(target)){
            target.damage(DamageSource.mob(this), 0.0F);
            return false;
        }
        return super.tryAttack(target);
    }
}
