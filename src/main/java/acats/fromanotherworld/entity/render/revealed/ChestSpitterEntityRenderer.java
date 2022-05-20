package acats.fromanotherworld.entity.render.revealed;

import acats.fromanotherworld.entity.model.revealed.ChestSpitterEntityModel;
import acats.fromanotherworld.entity.revealed.ChestSpitterEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ChestSpitterEntityRenderer extends GeoEntityRenderer<ChestSpitterEntity> {
    public ChestSpitterEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new ChestSpitterEntityModel());
        this.shadowRadius = 0.0F;
    }

    @Override
    public RenderLayer getRenderType(ChestSpitterEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityCutoutNoCull(this.getTextureLocation(animatable));
    }
}
