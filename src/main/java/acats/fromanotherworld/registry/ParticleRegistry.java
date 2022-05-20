package acats.fromanotherworld.registry;

import acats.fromanotherworld.FromAnotherWorld;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ParticleRegistry {

    public static final DefaultParticleType THING_GORE = FabricParticleTypes.simple();

    public static void register(){
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(FromAnotherWorld.MODID, "thing_gore"), THING_GORE);
    }
}
