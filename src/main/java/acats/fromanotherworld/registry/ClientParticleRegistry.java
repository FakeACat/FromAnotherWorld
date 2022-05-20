package acats.fromanotherworld.registry;

import acats.fromanotherworld.FromAnotherWorld;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.particle.RainSplashParticle;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

public class ClientParticleRegistry {

    public static void register(){
        ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register(((atlasTexture, registry) -> registry.register(new Identifier(FromAnotherWorld.MODID, "particle/thing_gore"))));
        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.THING_GORE, RainSplashParticle.Factory::new);
    }
}
