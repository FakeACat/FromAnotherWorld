package acats.fromanotherworld.entity.model.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.resultant.CrawlerEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CrawlerEntityModel extends AnimatedGeoModel<CrawlerEntity> {
    @Override
    public Identifier getModelLocation(CrawlerEntity object) {
        String variant = switch (object.getVariant()) {
            case 1, 2 -> "crawler_villager";
            default -> "crawler";
        };
        return new Identifier(FromAnotherWorld.MODID, "geo/entity/resultant/crawler/" + variant + ".geo.json");
    }

    @Override
    public Identifier getTextureLocation(CrawlerEntity object) {
        String variant = switch (object.getVariant()) {
            case 1 -> "crawler_villager";
            case 2 -> "crawler_illager";
            case 3 -> "crawler_juliette_thing";
            default -> "crawler";
        };
        return new Identifier(FromAnotherWorld.MODID, "textures/entity/resultant/crawler/" + variant + ".png");
    }

    @Override
    public Identifier getAnimationFileLocation(CrawlerEntity animatable) {
        return new Identifier(FromAnotherWorld.MODID, "animations/entity/resultant/crawler.animation.json");
    }
}
