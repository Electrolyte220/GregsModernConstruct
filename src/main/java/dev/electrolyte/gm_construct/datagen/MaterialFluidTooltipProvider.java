package dev.electrolyte.gm_construct.datagen;

import dev.electrolyte.gm_construct.GMConstruct;
import net.minecraft.Util;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.mantle.fluid.tooltip.AbstractFluidTooltipProvider;
import slimeknights.tconstruct.TConstruct;

public class MaterialFluidTooltipProvider extends AbstractFluidTooltipProvider {
    public MaterialFluidTooltipProvider(PackOutput output) {
        super(output, GMConstruct.MOD_ID);
    }

    @Override
    protected void addFluids() {
        add("metal", ForgeRegistries.FLUIDS.tags().createTagKey(GMConstruct.id("tooltips/metal")))
                .addUnitRaw(Util.makeDescriptionId("gui", TConstruct.getResource("fluid.block")), 1296)
                .addUnitRaw(Util.makeDescriptionId("gui", TConstruct.getResource("fluid.ingot")), 144)
                .addUnitRaw(Util.makeDescriptionId("gui", TConstruct.getResource("fluid.nugget")), 16);
        addRedirect(new ResourceLocation(TConstruct.MOD_ID,"metal"), GMConstruct.id("metal"));
    }

    @Override
    public String getName() {
        return GMConstruct.MOD_ID + " Fluid Tooltips";
    }
}
