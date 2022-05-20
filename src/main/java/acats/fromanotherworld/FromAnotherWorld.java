package acats.fromanotherworld;

import acats.fromanotherworld.config.Config;
import acats.fromanotherworld.config.EntityClassification;
import acats.fromanotherworld.entity.AbstractThingEntity;
import acats.fromanotherworld.registry.EntityRegistry;
import acats.fromanotherworld.registry.ItemRegistry;
import acats.fromanotherworld.registry.ParticleRegistry;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import software.bernie.geckolib3.GeckoLib;

import java.util.List;

public class FromAnotherWorld implements ModInitializer {
    public static final String MODID = "fromanotherworld";
    @Override
    public void onInitialize() {
        GeckoLib.initialize();
        EntityRegistry.register();
        ItemRegistry.register();
        ParticleRegistry.register();
        Config.load();
    }

    public static boolean isThing(Entity e){
        return e instanceof AbstractThingEntity || isAssimilated(e);
    }
    public static boolean isAssimilated(Entity e){
        return e.writeNbt(new NbtCompound()).getBoolean("IsAssimilated");
    }
    public static boolean assimilate(Entity e){
        return assimilate(e, 1.0F);
    }
    public static boolean assimilate(Entity e, float supercellConcentration){
        if (!canAssimilate(e)){
            return false;
        }
        if (canAssimilate(e)){
            NbtCompound sim = e.writeNbt(new NbtCompound());
            if (sim.contains("SupercellConcentration")){
                sim.putFloat("SupercellConcentration", sim.getFloat("SupercellConcentration") + supercellConcentration);
            }
            else{
                sim.putFloat("SupercellConcentration", supercellConcentration);
            }
            e.readNbt(sim);
            if (e instanceof MobEntity e2){
                e2.setPersistent();
            }
        }
        return true;
    }
    public static boolean canAssimilate(Entity e){
        return !isThing(e) && (EntityClassification.isHumanoid(e) || EntityClassification.isLargeQuadruped(e) || EntityClassification.isQuadruped(e) || EntityClassification.isSmall(e));
    }
    public static boolean isVisiblyBeingAssimilated(Entity e){
        return e.writeNbt(new NbtCompound()).getFloat("SupercellConcentration") >= 1.0F;
    }
    public static int numThingsInList(List<LivingEntity> list){
        int t = 0;
        for (LivingEntity e:
             list) {
            if (isThing(e) && !(e instanceof AbstractThingEntity e2 && !e2.shouldBeCounted())){
                t++;
            }
        }
        return t;
    }
    public static int numAssimilablesInList(List<LivingEntity> list){
        int t = 0;
        for (LivingEntity e:
                list) {
            if (canAssimilate(e)){
                t++;
            }
        }
        return t;
    }
}
