package acats.fromanotherworld.entity.render.resultant;

import acats.fromanotherworld.entity.model.resultant.DogBeastEntityModel;
import acats.fromanotherworld.entity.resultant.DogBeastEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class DogBeastEntityRenderer extends GeoEntityRenderer<DogBeastEntity> {

    public DogBeastEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new DogBeastEntityModel());
        this.shadowRadius = 0.5F;
    }

    @Override
    public RenderLayer getRenderType(DogBeastEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityCutoutNoCull(this.getTextureLocation(animatable));
    }
}
