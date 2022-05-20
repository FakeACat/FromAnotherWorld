package acats.fromanotherworld.registry;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.EntitySpawnPacket;
import acats.fromanotherworld.entity.render.resultant.CrawlerEntityRenderer;
import acats.fromanotherworld.entity.render.resultant.DogBeastEntityRenderer;
import acats.fromanotherworld.entity.render.resultant.JulietteThingEntityRenderer;
import acats.fromanotherworld.entity.render.revealed.ChestSpitterEntityRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

import java.util.UUID;

public class EntityRenderRegistry {
    public static void register(){
        EntityRendererRegistry.register(EntityRegistry.CRAWLER, CrawlerEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.CHEST_SPITTER, ChestSpitterEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.ASSIMILATION_LIQUID, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.JULIETTE_THING, JulietteThingEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.DOGBEAST, DogBeastEntityRenderer::new);

        receiveEntityPacket();
    }

    public static final Identifier PACKET_ID = new Identifier(FromAnotherWorld.MODID, "spawn_packet");

    public static void receiveEntityPacket() {
        ClientSidePacketRegistry.INSTANCE.register(PACKET_ID, (ctx, byteBuf) -> {
            EntityType<?> et = Registry.ENTITY_TYPE.get(byteBuf.readVarInt());
            UUID uuid = byteBuf.readUuid();
            int entityId = byteBuf.readVarInt();
            Vec3d pos = EntitySpawnPacket.PacketBufUtil.readVec3d(byteBuf);
            float pitch = EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
            float yaw = EntitySpawnPacket.PacketBufUtil.readAngle(byteBuf);
            ctx.getTaskQueue().execute(() -> {
                if (MinecraftClient.getInstance().world == null)
                    throw new IllegalStateException("Tried to spawn entity in a null world!");
                Entity e = et.create(MinecraftClient.getInstance().world);
                if (e == null)
                    throw new IllegalStateException("Failed to create instance of entity \"" + Registry.ENTITY_TYPE.getId(et) + "\"!");
                e.updateTrackedPosition(pos);
                e.setPos(pos.x, pos.y, pos.z);
                e.setPitch(pitch);
                e.setYaw(yaw);
                e.setId(entityId);
                e.setUuid(uuid);
                MinecraftClient.getInstance().world.addEntity(entityId, e);
            });
        });
    }
}
