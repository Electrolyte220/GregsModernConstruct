package dev.electrolyte.gm_construct.mixin;

import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.smeltery.data.Byproduct;
import slimeknights.tconstruct.smeltery.data.SmelteryCompat;

@Mixin(SmelteryCompat.class)
public class SmelteryCompatMixin {

    @Shadow
    @Final
    private static SmelteryCompat[] $VALUES;

    @Invoker(value = "<init>", remap = false)
    public static SmelteryCompat create(String name, int ordinal, FluidObject<?> fluid, Byproduct... byproducts) {
        throw new IllegalStateException("Unreachable");
    }

    static {
        SmelteryCompat TIN = create("GMC_TIN", $VALUES.length, TinkerFluids.moltenTin, Enum.valueOf(Byproduct.class, "GMC_COPPER"));
        SmelteryCompat ALUMINUM = create("GMC_ALUMINUM", $VALUES.length + 1, TinkerFluids.moltenAluminum, Enum.valueOf(Byproduct.class, "GMC_IRON"));
        SmelteryCompat LEAD = create("GMC_LEAD", $VALUES.length + 2, TinkerFluids.moltenLead, Enum.valueOf(Byproduct.class, "GMC_SILVER"), Enum.valueOf(Byproduct.class, "GMC_GOLD"));
        SmelteryCompat SILVER = create("GMC_SILVER", $VALUES.length + 3, TinkerFluids.moltenSilver, Enum.valueOf(Byproduct.class, "GMC_LEAD"), Enum.valueOf(Byproduct.class, "GMC_GOLD"));
        SmelteryCompat NICKEL = create("GMC_NICKEL", $VALUES.length + 4, TinkerFluids.moltenNickel, Enum.valueOf(Byproduct.class, "GMC_PLATINUM"), Enum.valueOf(Byproduct.class, "GMC_IRON"));
        SmelteryCompat ZINC = create("GMC_ZINC", $VALUES.length + 5, TinkerFluids.moltenZinc, Enum.valueOf(Byproduct.class, "GMC_TIN"), Enum.valueOf(Byproduct.class, "GMC_COPPER"));
        SmelteryCompat PLATINUM = create("GMC_PLATINUM", $VALUES.length + 6, TinkerFluids.moltenPlatinum, Enum.valueOf(Byproduct.class, "GMC_GOLD"));
        SmelteryCompat TUNGSTEN = create("GMC_TUNGSTEN", $VALUES.length + 7, TinkerFluids.moltenTungsten, Enum.valueOf(Byproduct.class, "GMC_PLATINUM"), Enum.valueOf(Byproduct.class, "GMC_GOLD"));
        SmelteryCompat OSMIUM = create("GMC_OSMIUM", $VALUES.length + 8, TinkerFluids.moltenOsmium, Enum.valueOf(Byproduct.class, "GMC_IRON"));
        SmelteryCompat URANIUM = create("GMC_URANIUM", $VALUES.length + 9, TinkerFluids.moltenUranium, Enum.valueOf(Byproduct.class, "GMC_LEAD"), Enum.valueOf(Byproduct.class, "GMC_COPPER"));

        //noinspection ShadowFinalModification
        $VALUES = ArrayUtils.addAll($VALUES, TIN, ALUMINUM, LEAD, SILVER, NICKEL, ZINC, PLATINUM, TUNGSTEN, OSMIUM, URANIUM);
    }
}
