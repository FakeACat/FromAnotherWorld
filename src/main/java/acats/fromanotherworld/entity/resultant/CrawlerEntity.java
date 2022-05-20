package acats.fromanotherworld.entity.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.AbstractThingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class CrawlerEntity extends AbstractThingEntity implements IAnimatable {

    private static final TrackedData<Integer> VARIANT;

    private final AnimationFactory factory = new AnimationFactory(this);

    public CrawlerEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0D));
        this.addThingTargets(2);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.isSleeping()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.crawler.sleeping", true));
        }
        else {
            if (event.isMoving()){
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.crawler.walk", true));
            }
            else{
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.crawler.idle", true));
            }
        }
        return PlayState.CONTINUE;
    }

    public static DefaultAttributeContainer.Builder createCrawlerAttributes(){
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.175D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0D);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 10, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
    private int tiredness;
    public boolean isSleeping(){
        return tiredness > 600;
    }
    @Override
    public void tick() {
        super.tick();
        this.tiredness++;
        if (this.isAttacking()){
            this.tiredness = 0;
        }
        if (this.isSleeping()){
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 6, false, false));
        }
    }

    @Override
    public float getSoundPitch() {
        return super.getSoundPitch() / 4;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return switch (this.getVariant()) {
            case 1, 3 -> SoundEvents.ENTITY_VILLAGER_AMBIENT;
            case 2 -> SoundEvents.ENTITY_VINDICATOR_AMBIENT;
            default -> super.getAmbientSound();
        };
    }

    @Override
    protected SoundEvent getDeathSound() {
        return switch (this.getVariant()) {
            case 1, 3 -> SoundEvents.ENTITY_VILLAGER_DEATH;
            case 2 -> SoundEvents.ENTITY_VINDICATOR_DEATH;
            default -> super.getDeathSound();
        };
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return switch (this.getVariant()) {
            case 1, 3 -> SoundEvents.ENTITY_VILLAGER_HURT;
            case 2 -> SoundEvents.ENTITY_VINDICATOR_HURT;
            default -> super.getHurtSound(source);
        };
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(VARIANT, 0);
    }

    public void setVariant(Entity e){
        int v;
        if (e instanceof VillagerEntity || e instanceof WitchEntity){
            v = 1;
        }
        else if (e instanceof IllagerEntity){
            v = 2;
        }
        else {
            v = 0;
        }
        this.setVariant(v);
    }

    public void setVariant(int variant){
        this.dataTracker.set(VARIANT, variant);
    }

    public int getVariant() {
        return this.dataTracker.get(VARIANT);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Tiredness", this.tiredness);
        nbt.putInt("Variant", this.getVariant());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.tiredness = nbt.getInt("Tiredness");
        this.setVariant(nbt.getInt("Variant"));
    }

    static {
        VARIANT = DataTracker.registerData(CrawlerEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }
}
