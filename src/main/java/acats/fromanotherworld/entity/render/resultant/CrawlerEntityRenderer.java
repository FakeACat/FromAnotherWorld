package acats.fromanotherworld.entity.render.resultant;

import acats.fromanotherworld.entity.model.resultant.CrawlerEntityModel;
import acats.fromanotherworld.entity.resultant.CrawlerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class CrawlerEntityRenderer extends GeoEntityRenderer<CrawlerEntity> {

    public CrawlerEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new CrawlerEntityModel());
        this.shadowRadius = 0.4F;
    }

    @Override
    public void render(CrawlerEntity entity, float entityYaw, float partialTicks, MatrixStack stack, VertexConsumerProvider bufferIn, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        float g = 0.9375F;
        stack.scale(g, g, g);
    }
}
