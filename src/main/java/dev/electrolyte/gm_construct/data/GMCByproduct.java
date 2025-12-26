package dev.electrolyte.gm_construct.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.level.material.Fluid;
import slimeknights.mantle.recipe.helper.FluidOutput;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.library.data.recipe.IByproduct;
import slimeknights.tconstruct.library.recipe.melting.IMeltingContainer.OreRateType;

import java.util.Locale;

import static com.gregtechceu.gtceu.common.data.GTMaterials.*;

@RequiredArgsConstructor
@Getter
public enum GMCByproduct implements IByproduct {
    COPPER    (false, Copper.getFluid()),
    IRON      (false, Iron.getFluid()),
    GOLD      (false, Gold.getFluid()),
    SMALL_GOLD("gold", false, Gold.getFluid(), 16 * 3, OreRateType.METAL),
    TINY_GOLD("gold", false, Gold.getFluid(), 16, OreRateType.NONE),
    COBALT    (false, Cobalt.getFluid()),
    STEEL     (false, Steel.getFluid()),
    DEBRIS    ("netherite_scrap", false, TinkerFluids.moltenDebris.get(), 144, OreRateType.METAL),

    TIN     (false, Tin.getFluid()),
    SILVER  (false, Silver.getFluid()),
    NICKEL  (false, Nickel.getFluid()),
    LEAD    (false, Lead.getFluid()),
    PLATINUM(false, Platinum.getFluid()),
    CHROMIUM(false, Chromium.getFluid());

    private final String name;
    private final boolean alwaysPresent;
    private final Fluid fluid;
    private final int amount;
    private final OreRateType oreRate;

    GMCByproduct(boolean alwaysPresent, Fluid fluid, int amount, OreRateType oreRate) {
        this.name = name().toLowerCase(Locale.ROOT);
        this.alwaysPresent = alwaysPresent;
        this.fluid = fluid;
        this.amount = amount;
        this.oreRate = oreRate;
    }

    GMCByproduct(boolean alwaysPresent, Fluid fluid) {
        this(alwaysPresent, fluid, 144, OreRateType.METAL);
    }

    @Override
    public FluidOutput getFluid(float scale) {
        return FluidOutput.fromFluid(fluid, (int) (amount * scale));
    }
}
