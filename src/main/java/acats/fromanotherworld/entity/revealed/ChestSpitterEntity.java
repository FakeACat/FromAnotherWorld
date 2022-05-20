package acats.fromanotherworld.entity.revealed;

import acats.fromanotherworld.entity.AbstractThingEntity;
import acats.fromanotherworld.entity.projectile.AssimilationLiquidEntity;
import acats.fromanotherworld.registry.ParticleRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.EnumSet;

public class ChestSpitterEntity extends AbstractThingEntity implements IAnimatable {

    private final AnimationFactory factory = new AnimationFactory(this);
    private static final int REVEAL_TIME = 100;
    private static final int ATTACK_TIME = 100;
    public Entity host;

    public ChestSpitterEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.addThingTargets(1);
        this.goalSelector.add(7, new ChestSpitterEntity.LookAtTargetGoal(this));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.age < REVEAL_TIME){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.chest_spitter.emerge", false));
        }
        else if (this.age < REVEAL_TIME + ATTACK_TIME){
            event.getController().transitionLengthTicks = 4;
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.chest_spitter.spit", true));
        }
        else{
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.chest_spitter.retract", false));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public static DefaultAttributeContainer.Builder createChestSpitterAttributes(){
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.0D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0.0D).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0D).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40.0D);
    }

    @Override
    public boolean saveNbt(NbtCompound nbt) {
        return false;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public void pushAwayFrom(Entity entity) {
    }
    @Override
    protected void pushAway(Entity entity) {
    }

    @Override
    public void tick() {
        super.tick();

        if (this.age > REVEAL_TIME + 10 && this.age < REVEAL_TIME + ATTACK_TIME && !this.world.isClient() && this.getTarget() != null){
            AssimilationLiquidEntity assimilationLiquid = new AssimilationLiquidEntity(world, this);
            assimilationLiquid.setVelocity(this, this.getPitch(), this.getYaw(), 0.0F, 2.5F, 10.0F);
            world.spawnEntity(assimilationLiquid);
        }

        if (this.age > 2 * REVEAL_TIME + ATTACK_TIME - 20){
            this.discard();
        }
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        if (this.host != null){
            Vec3d pos = this.host.getPos().add(0, (this.host.getHeight() * 0.55) - (this.getHeight() / 2), 0);
            this.setPosition(pos);
            LivingEntity e = (LivingEntity) this.host;
            e.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 6, false, false));
        }
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        this.world.addParticle(ParticleRegistry.THING_GORE, this.getParticleX(0.6D), this.getRandomBodyY(), this.getParticleZ(0.6D), 0, 0, 0);

    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return source != DamageSource.IN_WALL && super.damage(source, amount);
    }

    @Override
    public boolean shouldBeCounted() {
        return false;
    }

    static class LookAtTargetGoal extends Goal {
        private final ChestSpitterEntity spitter;

        public LookAtTargetGoal(ChestSpitterEntity spitter) {
            this.spitter = spitter;
            this.setControls(EnumSet.of(Control.LOOK));
        }

        public boolean canStart() {
            return true;
        }

        public boolean shouldRunEveryTick() {
            return true;
        }

        public void tick() {
            if (this.spitter.getTarget() != null) {
                LivingEntity vec3d = this.spitter.getTarget();
                double d = 64.0D;
                if (vec3d.squaredDistanceTo(this.spitter) < d*d) {
                    double e = vec3d.getX() - this.spitter.getX();
                    double f = vec3d.getZ() - this.spitter.getZ();
                    this.spitter.setYaw(-((float)MathHelper.atan2(e, f)) * 57.295776F);
                    this.spitter.bodyYaw = this.spitter.getYaw();
                }
            }
        }
    }
}
