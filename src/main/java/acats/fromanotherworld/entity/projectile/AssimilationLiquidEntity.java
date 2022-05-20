package acats.fromanotherworld.entity.projectile;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.AbstractThingEntity;
import acats.fromanotherworld.entity.EntitySpawnPacket;
import acats.fromanotherworld.registry.EntityRegistry;
import acats.fromanotherworld.registry.EntityRenderRegistry;
import acats.fromanotherworld.registry.ItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class AssimilationLiquidEntity extends ThrownItemEntity {

    public AssimilationLiquidEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public AssimilationLiquidEntity(World world, LivingEntity owner) {
        super(EntityRegistry.ASSIMILATION_LIQUID, owner, world);
    }

    public AssimilationLiquidEntity(World world, double x, double y, double z) {
        super(EntityRegistry.ASSIMILATION_LIQUID, x, y, z, world);
    }
    @Override
    protected Item getDefaultItem() {
        return ItemRegistry.ASSIMILATION_LIQUID;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (!this.world.isClient()){
            Entity e = entityHitResult.getEntity();
            if (!FromAnotherWorld.assimilate(e) && !FromAnotherWorld.isThing(e)){
                e.damage(DamageSource.mob((LivingEntity) this.getOwner()), 3.0F);
            }
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.world.isClient) {
            this.discard();
        }
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return EntitySpawnPacket.create(this, EntityRenderRegistry.PACKET_ID);
    }
}
