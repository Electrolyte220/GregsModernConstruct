package dev.electrolyte.gm_construct.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.data.recipe.SmelteryRecipeBuilder;

@Mixin(SmelteryRecipeBuilder.class)
public abstract class SmelteryRecipeBuilderMixin {

    @Shadow(remap = false) private int baseUnit;
    @Shadow(remap = false) private int damageUnit;
    @Shadow @Final private ResourceLocation name;
    @Shadow(remap = false) private void itemMelting(float scale, String output, float factor, ResourceLocation itemName, boolean damagable) {throw new IllegalStateException("Mixin did not apply!");}


    @Inject(method = "metal", at = @At(value = "INVOKE", target = "Lslimeknights/tconstruct/library/data/recipe/SmelteryRecipeBuilder;melting(FLjava/lang/String;Ljava/lang/String;FZZ)Lslimeknights/tconstruct/library/data/recipe/SmelteryRecipeBuilder;"), remap = false)
    private void gmc$oldValue(CallbackInfoReturnable<SmelteryRecipeBuilder> cir) {
        baseUnit = 144;
        damageUnit = 16;
    }

    @WrapMethod(method = "oreberry", remap = false)
    private SmelteryRecipeBuilder gmc$oldOreberry(Operation<SmelteryRecipeBuilder> original) {
        baseUnit = 144;
        itemMelting(1 / 9f, "oreberry", 1 / 3f, new ResourceLocation("oreberriesreplanted", name.getPath() + "_oreberry"), false);
        return ((SmelteryRecipeBuilder) (Object) this);
    }
}