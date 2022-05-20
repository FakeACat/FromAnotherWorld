package acats.fromanotherworld;

import acats.fromanotherworld.registry.ClientParticleRegistry;
import acats.fromanotherworld.registry.EntityRenderRegistry;
import net.fabricmc.api.ClientModInitializer;

public class FromAnotherWorldClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRenderRegistry.register();
        ClientParticleRegistry.register();
    }
}
