package acats.fromanotherworld.entity.model.resultant;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.resultant.DogBeastEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DogBeastEntityModel extends AnimatedGeoModel<DogBeastEntity> {
    @Override
    public Identifier getModelLocation(DogBeastEntity object) {
        return new Identifier(FromAnotherWorld.MODID, "geo/entity/resultant/dogbeast.geo.json");
    }

    @Override
    public Identifier getTextureLocation(DogBeastEntity object) {
        return new Identifier(FromAnotherWorld.MODID, "textures/entity/resultant/dogbeast.png");
    }

    @Override
    public Identifier getAnimationFileLocation(DogBeastEntity animatable) {
        return new Identifier(FromAnotherWorld.MODID, "animations/entity/resultant/dogbeast.animation.json");
    }
}
