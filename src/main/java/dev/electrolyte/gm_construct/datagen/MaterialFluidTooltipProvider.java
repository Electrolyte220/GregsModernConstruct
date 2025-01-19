package dev.electrolyte.gm_construct.datagen;

import dev.electrolyte.gm_construct.GMConstruct;
import net.minecraft.Util;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import slimeknights.mantle.fluid.tooltip.AbstractFluidTooltipProvider;
import slimeknights.tconstruct.TConstruct;

import static slimeknights.tconstruct.common.TinkerTags.Fluids.METAL_TOOLTIPS;

public class MaterialFluidTooltipProvider extends AbstractFluidTooltipProvider {
    public MaterialFluidTooltipProvider(PackOutput output) {
        super(output, GMConstruct.MOD_ID);
    }

    @Override
    protected void addFluids() {
        add("metal", METAL_TOOLTIPS)
                .addUnitRaw(Util.makeDescriptionId("gui", new ResourceLocation(TConstruct.MOD_ID, "fluid.block")), 1296)
                .addUnitRaw(Util.makeDescriptionId("gui", new ResourceLocation(TConstruct.MOD_ID, "fluid.ingot")), 144)
                .addUnitRaw(Util.makeDescriptionId("gui", new ResourceLocation(TConstruct.MOD_ID, "fluid.nugget")), 16);
        addRedirect(new ResourceLocation(TConstruct.MOD_ID,"metal"), GMConstruct.id("metal"));
    }

    @Override
    public String getName() {
        return GMConstruct.MOD_ID + " Fluid Tooltips";
    }
}
