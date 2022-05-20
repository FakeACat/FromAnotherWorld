package acats.fromanotherworld.mixin.client;

import acats.fromanotherworld.FromAnotherWorld;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {
    @Inject(at = @At("HEAD"), method = "isShaking", cancellable = true)
    private void isShaking(T entity, CallbackInfoReturnable<Boolean> cir){
        if (FromAnotherWorld.isVisiblyBeingAssimilated(entity)){
            cir.setReturnValue(true);
        }
    }
}
