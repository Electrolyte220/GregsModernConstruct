package dev.electrolyte.gm_construct.mixin;

import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.smeltery.data.Byproduct;

@Mixin(Byproduct.class)
public class ByproductMixin {

    @Shadow
    @Final
    private static Byproduct[] $VALUES;

    @Invoker(value = "<init>", remap = false)
    public static Byproduct create(String name, int ordinal, String id, boolean alwaysPresent, FluidObject<?> fluid, int amount) {
        throw new IllegalStateException("Unreachable");
    }

    static {
        Byproduct COPPER = create("GMC_COPPER", $VALUES.length, "gmc_copper", true, TinkerFluids.moltenCopper, 144);
        Byproduct IRON = create("GMC_IRON", $VALUES.length + 1, "gmc_iron", true, TinkerFluids.moltenIron, 144);
        Byproduct GOLD = create("GMC_GOLD", $VALUES.length + 2, "gmc_gold", true, TinkerFluids.moltenGold, 144);
        Byproduct SMALL_GOLD = create("GMC_SMALL_GOLD", $VALUES.length + 2, "gmc_small_gold", true, TinkerFluids.moltenGold, 16 * 3);
        Byproduct COBALT = create("GMC_COBALT", $VALUES.length + 3, "gmc_cobalt", true, TinkerFluids.moltenCobalt, 144);

        Byproduct TIN = create("GMC_TIN", $VALUES.length + 4, "gmc_tin", false, TinkerFluids.moltenTin, 144);
        Byproduct SILVER = create("GMC_SILVER", $VALUES.length + 5, "gmc_silver", false, TinkerFluids.moltenSilver, 144);
        Byproduct NICKEL = create("GMC_NICKEL", $VALUES.length + 6, "gmc_nickel", false, TinkerFluids.moltenNickel, 144);
        Byproduct LEAD = create("GMC_LEAD", $VALUES.length + 7, "gmc_lead",  false, TinkerFluids.moltenLead, 144);
        Byproduct PLATINUM = create("GMC_PLATINUM", $VALUES.length + 8, "gmc_platinum", false, TinkerFluids.moltenPlatinum, FluidValues.NUGGET * 3);

        //noinspection ShadowFinalModification
        $VALUES = ArrayUtils.addAll($VALUES, COPPER, IRON, GOLD, SMALL_GOLD, COBALT, TIN, SILVER, NICKEL, LEAD, PLATINUM);
    }
}
