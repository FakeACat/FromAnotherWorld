package acats.fromanotherworld.mixin;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.config.EntityClassification;
import acats.fromanotherworld.entity.resultant.CrawlerEntity;
import acats.fromanotherworld.entity.resultant.DogBeastEntity;
import acats.fromanotherworld.entity.resultant.JulietteThingEntity;
import acats.fromanotherworld.entity.revealed.ChestSpitterEntity;
import acats.fromanotherworld.registry.EntityRegistry;
import acats.fromanotherworld.registry.ParticleRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Box;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    private static final TrackedData<Float> SUPERCELL_CONCENTRATION;
    private boolean assimilated;

    @Shadow public abstract void kill();
    @Shadow public abstract void heal(float amount);

    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("HEAD"), method = "writeCustomDataToNbt")
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci){
        nbt.putBoolean("IsAssimilated", this.assimilated);
        nbt.putFloat("SupercellConcentration", this.getSupercellConcentration());
    }

    @Inject(at = @At("HEAD"), method = "readCustomDataFromNbt")
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci){
        if (nbt.contains("IsAssimilated")){
            this.assimilated = nbt.getBoolean("IsAssimilated");
        }
        if (nbt.contains("SupercellConcentration")){
            this.setSupercellConcentration(nbt.getFloat("SupercellConcentration"));
        }
    }

    @Inject(at = @At("HEAD"), method = "initDataTracker")
    private void simInitDataTracker(CallbackInfo ci){
        this.dataTracker.startTracking(SUPERCELL_CONCENTRATION, 0.0F);
    }

    private void setSupercellConcentration(float i){
        this.dataTracker.set(SUPERCELL_CONCENTRATION, i);
    }

    private float getSupercellConcentration(){
        return this.dataTracker.get(SUPERCELL_CONCENTRATION);
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void simTick(CallbackInfo ci){
        if (this.getSupercellConcentration() > 0){
            if (!this.world.isClient() && !this.isOnFire()){
                this.heal(1.0F);
            }
            this.setSupercellConcentration(this.getSupercellConcentration() * 1.005F);
            if (this.getSupercellConcentration() >= 100){
                this.assimilated = true;
                this.setSupercellConcentration(0);
            }
            if (this.getSupercellConcentration() >= 1.0F){
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 6, false, false));
            }
        }
        else if (this.assimilated){
            if (this.world.getDifficulty() == Difficulty.PEACEFUL){
                this.discard();
            }
            if (random.nextInt(600) == 0 && random.nextInt(1 + world.getLightLevel(this.getBlockPos())) == 0){
                this.tryReveal();
            }
            if (random.nextInt(6000) == 0){
                this.tryBecomeResultant();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "tickMovement")
    private void conversionEffects(CallbackInfo ci){
        if (this.getSupercellConcentration() >= 1.0F && this.world.isClient) {
            for(int i = 0; i < this.getSupercellConcentration() / 10; ++i) {
                this.world.addParticle(ParticleRegistry.THING_GORE, this.getParticleX(0.6D), this.getRandomBodyY(), this.getParticleZ(0.6D), 0, 0, 0);
            }
        }
    }

    private void tryReveal(){
        int entityCheckDist = 16;
        int playerCheckDist = 32;
        PlayerEntity p = world.getClosestPlayer(this, playerCheckDist);
        if ((p == null || p.isSpectator() || p.isCreative()) && !this.world.isClient()){
            List<LivingEntity> nearbyEntities = world.getNonSpectatingEntities(LivingEntity.class, new Box(this.getX() - entityCheckDist, this.getY() - entityCheckDist, this.getZ() - entityCheckDist, this.getX() + entityCheckDist, this.getY() + entityCheckDist, this.getZ() + entityCheckDist));
            int assimilables = FromAnotherWorld.numAssimilablesInList(nearbyEntities);
            int things = FromAnotherWorld.numThingsInList(nearbyEntities);
            if (assimilables > 0 && assimilables < things * 5){
                this.reveal();
            }
        }
    }

    private void tryBecomeResultant(){
        int entityCheckDist = 16;
        List<LivingEntity> nearbyEntities = world.getNonSpectatingEntities(LivingEntity.class, new Box(this.getX() - entityCheckDist, this.getY() - entityCheckDist, this.getZ() - entityCheckDist, this.getX() + entityCheckDist, this.getY() + entityCheckDist, this.getZ() + entityCheckDist));
        int assimilables = FromAnotherWorld.numAssimilablesInList(nearbyEntities);
        int things = FromAnotherWorld.numThingsInList(nearbyEntities);
        if (things >= 4 && things >= assimilables){
            this.becomeResultant();
        }
    }

    @Inject(at = @At("HEAD"), method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", cancellable = true)
    private void canSimTarget(LivingEntity target, CallbackInfoReturnable<Boolean> cir){
        if (this.assimilated && FromAnotherWorld.isThing(target)){
            cir.setReturnValue(false);
        }
    }

    @Inject(at = @At("HEAD"), method = "pushAway")
    private void assimilateOnPush(Entity entity, CallbackInfo ci){
        if (this.assimilated && random.nextInt(6000) == 0){
            FromAnotherWorld.assimilate(entity, 0.01F);
        }
    }

    @Inject(at = @At("HEAD"), method = "onDeath")
    private void onSimDeath(DamageSource source, CallbackInfo ci){
        if (this.assimilated){
            this.becomeResultant();
        }
    }

    private void reveal(){
        ChestSpitterEntity chestSpitterEntity = EntityRegistry.CHEST_SPITTER.create(this.world);
        if (chestSpitterEntity != null){
            chestSpitterEntity.setPosition(this.getPos());
            chestSpitterEntity.host = this;
            this.world.spawnEntity(chestSpitterEntity);
        }
    }

    private void becomeResultant(){
        if (this.world.isClient()) {
            return;
        }
        if (EntityClassification.isHumanoid(this)){
            switch (random.nextInt(2)) {
                case 0 -> {
                    CrawlerEntity crawlerEntity = EntityRegistry.CRAWLER.create(this.world);
                    if (crawlerEntity != null) {
                        crawlerEntity.setPosition(this.getPos());
                        crawlerEntity.setVariant(this);
                        this.world.spawnEntity(crawlerEntity);
                    }
                }
                case 1 -> {
                    JulietteThingEntity julietteThingEntity = EntityRegistry.JULIETTE_THING.create(this.world);
                    if (julietteThingEntity != null) {
                        julietteThingEntity.setPosition(this.getPos());
                        julietteThingEntity.setVariant(this);
                        this.world.spawnEntity(julietteThingEntity);
                    }
                }
            }
        }
        else if (EntityClassification.isLargeQuadruped(this)){

        }
        else if (EntityClassification.isQuadruped(this)){
            DogBeastEntity dogBeastEntity = EntityRegistry.DOGBEAST.create(this.world);
            if (dogBeastEntity != null) {
                dogBeastEntity.setPosition(this.getPos());
                this.world.spawnEntity(dogBeastEntity);
            }
        }
        else if (EntityClassification.isSmall(this)){

        }
        this.discard();
    }


    static {
        SUPERCELL_CONCENTRATION = DataTracker.registerData(LivingEntityMixin.class, TrackedDataHandlerRegistry.FLOAT);
    }
}
