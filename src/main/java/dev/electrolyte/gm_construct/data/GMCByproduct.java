package dev.electrolyte.gm_construct.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import slimeknights.mantle.recipe.helper.FluidOutput;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.library.data.recipe.IByproduct;
import slimeknights.tconstruct.library.recipe.melting.IMeltingContainer.OreRateType;

import java.util.Locale;

@RequiredArgsConstructor
@Getter
public enum GMCByproduct implements IByproduct {
    COPPER    (true, TinkerFluids.moltenCopper),
    IRON      (true, TinkerFluids.moltenIron),
    GOLD      (true, TinkerFluids.moltenGold),
    SMALL_GOLD("gold", true, TinkerFluids.moltenGold, 16 * 3, OreRateType.METAL),
    COBALT    (true, TinkerFluids.moltenCobalt),
    STEEL     (true, TinkerFluids.moltenSteel),
    DEBRIS    ("netherite_scrap", true, TinkerFluids.moltenDebris, 144, OreRateType.METAL),

    TIN     (false, TinkerFluids.moltenTin),
    SILVER  (false, TinkerFluids.moltenSilver),
    NICKEL  (false, TinkerFluids.moltenNickel),
    LEAD    (false, TinkerFluids.moltenLead),
    PLATINUM(false, TinkerFluids.moltenPlatinum);

    private final String name;
    private final boolean alwaysPresent;
    private final FluidObject<?> fluid;
    private final int amount;
    private final OreRateType oreRate;

    GMCByproduct(boolean alwaysPresent, FluidObject<?> fluid, int amount, OreRateType oreRate) {
        this.name = name().toLowerCase(Locale.ROOT);
        this.alwaysPresent = alwaysPresent;
        this.fluid = fluid;
        this.amount = amount;
        this.oreRate = oreRate;
    }

    GMCByproduct(boolean alwaysPresent, FluidObject<?> fluid) {
        this(alwaysPresent, fluid, 144, OreRateType.METAL);
    }

    @Override
    public FluidOutput getFluid(float scale) {
        return fluid.result((int) (amount * scale));
    }
}
