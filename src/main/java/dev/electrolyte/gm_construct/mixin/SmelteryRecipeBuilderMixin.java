package dev.electrolyte.gm_construct.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.data.recipe.SmelteryRecipeBuilder;

@Mixin(SmelteryRecipeBuilder.class)
public abstract class SmelteryRecipeBuilderMixin {

    @Shadow private int baseUnit;
    @Shadow private int damageUnit;

    @Inject(method = "metal", at = @At(value = "INVOKE", target = "Lslimeknights/tconstruct/library/data/recipe/SmelteryRecipeBuilder;melting(FLjava/lang/String;Ljava/lang/String;FZZ)Lslimeknights/tconstruct/library/data/recipe/SmelteryRecipeBuilder;"), remap = false)
    private void gmc$oldValue(CallbackInfoReturnable<SmelteryRecipeBuilder> cir) {
        baseUnit = 144;
        damageUnit = 16;
    }

    @WrapOperation(method = "oreberry", at = @At(value = "INVOKE", target = "Lslimeknights/tconstruct/library/data/recipe/SmelteryRecipeBuilder;itemMelting(ILjava/lang/String;FLnet/minecraft/resources/ResourceLocation;Z)V"), remap = false)
    private void gmc$oldOreberry(SmelteryRecipeBuilder instance, int amount, String output, float factor, ResourceLocation itemName, boolean damagable, Operation<Void> original) {
        original.call(instance, 16, output, factor, itemName, damagable);
    }

    @WrapOperation(method = "metal", at = @At(value = "INVOKE", target = "Lslimeknights/tconstruct/library/data/recipe/SmelteryRecipeBuilder;basinCasting(ILjava/lang/String;Ljava/lang/String;Z)V"), remap = false)
    private void gmc$BasinCasting(SmelteryRecipeBuilder instance, int amount, String output, String tagName, boolean forceOptional, Operation<Void> original) {
        original.call(instance, 1296, output, tagName, forceOptional);
    }
}