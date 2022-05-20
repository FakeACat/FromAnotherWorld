package acats.fromanotherworld.entity.render.resultant;

import acats.fromanotherworld.entity.model.resultant.JulietteThingEntityModel;
import acats.fromanotherworld.entity.resultant.JulietteThingEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class JulietteThingEntityRenderer extends GeoEntityRenderer<JulietteThingEntity> {
    public JulietteThingEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new JulietteThingEntityModel());
        this.shadowRadius = 0.4F;
    }

    @Override
    public void render(JulietteThingEntity entity, float entityYaw, float partialTicks, MatrixStack stack, VertexConsumerProvider bufferIn, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        float g = 0.9375F;
        stack.scale(g, g, g);
    }

    @Override
    public RenderLayer getRenderType(JulietteThingEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityCutoutNoCull(this.getTextureLocation(animatable));
    }
}
