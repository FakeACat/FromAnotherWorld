package acats.fromanotherworld.entity.model.revealed;

import acats.fromanotherworld.FromAnotherWorld;
import acats.fromanotherworld.entity.revealed.ChestSpitterEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ChestSpitterEntityModel extends AnimatedGeoModel<ChestSpitterEntity> {
    @Override
    public Identifier getModelLocation(ChestSpitterEntity object) {
        return new Identifier(FromAnotherWorld.MODID, "geo/entity/revealed/chest_spitter.geo.json");
    }

    @Override
    public Identifier getTextureLocation(ChestSpitterEntity object) {
        return new Identifier(FromAnotherWorld.MODID, "textures/entity/revealed/chest_spitter/chest_spitter.png");
    }

    @Override
    public Identifier getAnimationFileLocation(ChestSpitterEntity animatable) {
        return new Identifier(FromAnotherWorld.MODID, "animations/entity/revealed/chest_spitter.animation.json");
    }
}
