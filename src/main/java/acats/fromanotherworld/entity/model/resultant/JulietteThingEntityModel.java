package acats.fromanotherworld.entity.model.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.resultant.JulietteThingEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class JulietteThingEntityModel extends AnimatedGeoModel<JulietteThingEntity> {
    @Override
    public Identifier getModelLocation(JulietteThingEntity object) {
        return new Identifier(FromAnotherWorld.MODID, "geo/entity/resultant/juliette_thing.geo.json");
    }

    @Override
    public Identifier getTextureLocation(JulietteThingEntity object) {
        String variant = switch (object.getVariant()) {
            case 1 -> "juliette_thing_stevetrousers";
            case 2 -> "juliette_thing_villagertrousers";
            default -> "juliette_thing";
        };
        return new Identifier(FromAnotherWorld.MODID, "textures/entity/resultant/juliette_thing/" + variant + ".png");
    }

    @Override
    public Identifier getAnimationFileLocation(JulietteThingEntity animatable) {
        return new Identifier(FromAnotherWorld.MODID, "animations/entity/resultant/juliette_thing.animation.json");
    }
}
