package dev.electrolyte.gm_construct.datagen;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import dev.electrolyte.gm_construct.data.GMCByproduct;
import dev.electrolyte.gm_construct.helper.GTMaterialHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.DifferenceIngredient;
import net.minecraftforge.common.crafting.conditions.*;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.mantle.recipe.condition.TagFilledCondition;
import slimeknights.mantle.recipe.data.ItemNameIngredient;
import slimeknights.mantle.recipe.data.ItemNameOutput;
import slimeknights.mantle.recipe.helper.FluidOutput;
import slimeknights.mantle.recipe.helper.ItemOutput;
import slimeknights.mantle.recipe.ingredient.EntityIngredient;
import slimeknights.mantle.recipe.ingredient.FluidIngredient;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.common.json.ConfigEnabledCondition;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.library.data.recipe.ISmelteryRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.SmelteryRecipeBuilder;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.alloying.AlloyRecipeBuilder;
import slimeknights.tconstruct.library.recipe.casting.ItemCastingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.casting.container.ContainerFillingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.entitymelting.EntityMeltingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.ingredient.MaterialIngredient;
import slimeknights.tconstruct.library.recipe.ingredient.NoContainerIngredient;
import slimeknights.tconstruct.library.recipe.melting.IMeltingContainer.OreRateType;
import slimeknights.tconstruct.library.recipe.melting.IMeltingRecipe;
import slimeknights.tconstruct.library.recipe.melting.MeltingRecipeBuilder;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.shared.TinkerMaterials;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.smeltery.block.component.SearedTankBlock.TankType;
import slimeknights.tconstruct.smeltery.data.Byproduct;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.TinkerToolParts;
import slimeknights.tconstruct.world.TinkerHeadType;
import slimeknights.tconstruct.world.TinkerWorld;

import java.util.function.Consumer;
import java.util.function.Function;

import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static slimeknights.mantle.Mantle.COMMON;
import static slimeknights.mantle.Mantle.commonResource;
import static slimeknights.tconstruct.library.data.recipe.SmelteryRecipeBuilder.*;

public class GMCSmelteryRecipeProvider implements ISmelteryRecipeHelper {

    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        addSmelteryRecipes(consumer);
        addFoundryRecipes(consumer);
        addCastingRecipes(consumer);
        addMeltingRecipes(consumer);
        addAlloyRecipes(consumer);
        addEntityMeltingRecipes(consumer);
        addTagRecipes(consumer);
        addCompatRecipes(consumer);
    }

    private void addSmelteryRecipes(Consumer<FinishedRecipe> consumer) {
        String castingFolder = "smeltery/casting/seared/";
        ItemCastingRecipeBuilder.retexturedBasinRecipe(ItemOutput.fromItem(TinkerSmeltery.smelteryController))
                .setCast(TinkerTags.Items.SMELTERY_BRICKS, true)
                .setFluidAndTime(new FluidStack(Copper.getFluid(), 144 * 4))
                .save(consumer, prefix(TinkerSmeltery.smelteryController, castingFolder));

        String meltingFolder = "smeltery/melting/seared/";
        MeltingRecipeBuilder.melting(NoContainerIngredient.of(TinkerSmeltery.searedLantern), TinkerFluids.searedStone, FluidValues.BRICK * 2, 1.0f)
                .addByproduct(TinkerFluids.moltenGlass.result(FluidValues.GLASS_PANE))
                .addByproduct(new FluidStack(Iron.getFluid(), 144 / 3))
                .save(consumer, location(meltingFolder + "lantern"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerSmeltery.searedFluidCannon), FluidOutput.fromFluid(Copper.getFluid(), 144 * 5), GTMaterialHelper.findTemp(Copper), 2.5f)
                .addByproduct(TinkerFluids.searedStone.result(FluidValues.BRICK * 4))
                .addByproduct(TinkerFluids.moltenGlass.result(FluidValues.GLASS_PANE * 5))
                .save(consumer, location(meltingFolder + "fluid_cannon"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerSmeltery.smelteryController), FluidOutput.fromFluid(Copper.getFluid(), 144 * 4), GTMaterialHelper.findTemp(Copper), 3.5f)
                .addByproduct(TinkerFluids.searedStone.result(FluidValues.BRICK * 4))
                .save(consumer, location("smeltery/melting/metal/copper/smeltery_controller"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerSmeltery.copperGauge), FluidOutput.fromFluid(Copper.getFluid(), 144), GTMaterialHelper.findTemp(Copper), 1f)
                .addByproduct(TinkerFluids.moltenGlass.result(FluidValues.GLASS_PANE / 5))
                .save(consumer, location("smeltery/melting/metal/copper/gauge"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerSmeltery.searedDrain, TinkerSmeltery.searedChute), FluidOutput.fromFluid(Copper.getFluid(), 144 * 2), GTMaterialHelper.findTemp(Copper), 2.5f)
                .addByproduct(TinkerFluids.searedStone.result(FluidValues.BRICK * 4))
                .save(consumer, location("smeltery/melting/metal/copper/smeltery_io"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerSmeltery.searedDuct), FluidOutput.fromFluid(Gold.getFluid(), 144 * 2), GTMaterialHelper.findTemp(Gold), 2.5f)
                .addByproduct(TinkerFluids.searedStone.result(FluidValues.BRICK * 4))
                .save(consumer, location("smeltery/melting/metal/cobalt/seared_duct"));
        //todo: check if still exists
        /*MeltingRecipeBuilder.melting(NoContainerIngredient.of(TinkerSmeltery.searedCastingTank), FluidOutput.fromFluid(Copper.getFluid(), 144 * 2), GTMaterialHelper.findTemp(Copper), 2.5f)
                .addByproduct(TinkerFluids.searedStone.result(FluidValues.BRICK * 4))
                .addByproduct(TinkerFluids.moltenGlass.result(FluidValues.GLASS_BLOCK * 3))
                .save(consumer, location(meltingFolder + "seared_casting_tank"));*/
    }

    private void addFoundryRecipes(Consumer<FinishedRecipe> consumer) {
        String meltingFolder = "smeltery/melting/scorched/";
        //todo:check
        /*MeltingRecipeBuilder.melting(NoContainerIngredient.of(TinkerSmeltery.scorchedLantern), TinkerFluids.scorchedStone, FluidValues.BRICK * 2, 1.0f)
                .addByproduct(TinkerFluids.moltenQuartz.result(FluidValues.GEM_SHARD))
                .addByproduct(FluidOutput.fromFluid(Iron.getFluid(), 16 * 3))
                .save(consumer, location(meltingFolder + "lantern"));*/
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerSmeltery.scorchedFluidCannon), FluidOutput.fromFluid(Cobalt.getFluid(), 144 * 5), GTMaterialHelper.findTemp(Cobalt), 3.5f)
                .addByproduct(TinkerFluids.scorchedStone.result(FluidValues.BRICK * 4))
                .addByproduct(TinkerFluids.moltenQuartz.result(FluidValues.GEM * 5))
                .save(consumer, location(meltingFolder + "fluid_cannon"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerSmeltery.scorchedDuct), FluidOutput.fromFluid(Gold.getFluid(), 144 * 2), GTMaterialHelper.findTemp(Gold), 2.5f)
                .addByproduct(TinkerFluids.scorchedStone.result(FluidValues.BRICK * 4))
                .save(consumer, location("smeltery/melting/metal/cobalt/scorched_duct"));
    }

    private void addCastingRecipes(Consumer<FinishedRecipe> consumer) {
        ItemCastingRecipeBuilder.tableRecipe(TinkerCommons.encyclopedia)
                .setFluidAndTime(new FluidStack(Gold.getFluid(), 144))
                .setCast(Items.BOOK, true)
                .save(consumer, prefix(TinkerCommons.encyclopedia, "common/"));
        String folder = "smeltery/casting/";
        //do not touch copper cans
        ContainerFillingRecipeBuilder.basinRecipe(TinkerSmeltery.searedTank.get(TankType.INGOT_TANK), 144)
                .save(consumer, location(folder + "filling/seared_ingot_tank"));
        ContainerFillingRecipeBuilder.basinRecipe(TinkerSmeltery.searedTank.get(TankType.INGOT_GAUGE), 144)
                .save(consumer, location(folder + "filling/seared_ingot_gauge"));
        ContainerFillingRecipeBuilder.tableRecipe(TinkerSmeltery.searedLantern, 16)
                .save(consumer, location(folder + "filling/seared_lantern_pixel"));

        ContainerFillingRecipeBuilder.basinRecipe(TinkerSmeltery.scorchedTank.get(TankType.INGOT_TANK), 144)
                .save(consumer, location(folder + "filling/scorched_ingot_tank"));
        ContainerFillingRecipeBuilder.basinRecipe(TinkerSmeltery.scorchedTank.get(TankType.INGOT_GAUGE), 144)
                .save(consumer, location(folder + "filling/scorched_ingot_gauge"));
        ContainerFillingRecipeBuilder.tableRecipe(TinkerSmeltery.scorchedLantern, 16)
                .save(consumer, location(folder + "filling/scorched_lantern_pixel"));

        //todo: slime casting?

        String castFolder = "smeltery/casts/";
        this.castCreation(consumer, CompoundIngredient.of(
                DifferenceIngredient.of(Ingredient.of(Tags.Items.INGOTS), Ingredient.of(TinkerToolParts.fakeIngot)),
                MaterialIngredient.of(TinkerToolParts.fakeIngot)
        ), TinkerSmeltery.ingotCast, castFolder, "ingots");
        this.castCreation(consumer, Tags.Items.NUGGETS, TinkerSmeltery.nuggetCast, castFolder);
        this.castCreation(consumer, Tags.Items.GEMS, TinkerSmeltery.gemCast, castFolder);
        this.castCreation(consumer, Tags.Items.RODS, TinkerSmeltery.rodCast, castFolder);

        this.castCreation(withCondition(consumer, tagCondition("plates")), getItemTag(COMMON, "plates"), TinkerSmeltery.plateCast, castFolder);
        this.castCreation(withCondition(consumer, tagCondition("gears")),  getItemTag(COMMON, "gears"), TinkerSmeltery.gearCast, castFolder);
        this.castCreation(withCondition(consumer, tagCondition("coins")),  getItemTag(COMMON, "coins"), TinkerSmeltery.coinCast, castFolder);
        this.castCreation(withCondition(consumer, tagCondition("wires")),  getItemTag(COMMON, "wires"), TinkerSmeltery.wireCast, castFolder);

        String metalFolder = folder + "metal/";
        ItemCastingRecipeBuilder.tableRecipe(TinkerCommons.goldBars)
                .setFluidAndTime(new FluidStack(Gold.getFluid(), 16 * 3))
                .save(consumer, location(metalFolder + "gold/bars"));
        ItemCastingRecipeBuilder.tableRecipe(Items.GOLDEN_APPLE)
                .setFluidAndTime(new FluidStack(Gold.getFluid(), 144 * 8))
                .setCast(Items.APPLE, true)
                .save(consumer, location(metalFolder + "gold/apple"));
        ItemCastingRecipeBuilder.tableRecipe(Items.GLISTERING_MELON_SLICE)
                .setFluidAndTime(new FluidStack(Gold.getFluid(), 16 * 8))
                .setCast(Items.MELON_SLICE, true)
                .save(consumer, location(metalFolder + "gold/melon"));
        ItemCastingRecipeBuilder.tableRecipe(Items.GOLDEN_CARROT)
                .setFluidAndTime(new FluidStack(Gold.getFluid(), 16 * 8))
                .setCast(Items.CARROT, true)
                .save(consumer, location(metalFolder + "gold/carrot"));
        ItemCastingRecipeBuilder.tableRecipe(Items.CLOCK)
                .setFluidAndTime(new FluidStack(Gold.getFluid(), 144 * 4))
                .setCast(Items.REDSTONE, true)
                .save(consumer, location(metalFolder + "gold/clock"));
        ItemCastingRecipeBuilder.tableRecipe(Blocks.IRON_BARS)
                .setFluidAndTime(new FluidStack(Iron.getFluid(), 16 * 3))
                .save(consumer, location(metalFolder + "iron/bars"));
        ItemCastingRecipeBuilder.tableRecipe(Items.LANTERN)
                .setFluidAndTime(new FluidStack(Iron.getFluid(), 16 * 8))
                .setCast(Blocks.TORCH, true)
                .save(consumer, location(metalFolder + "iron/lantern"));
        ItemCastingRecipeBuilder.tableRecipe(Items.SOUL_LANTERN)
                .setFluidAndTime(new FluidStack(Iron.getFluid(), 16 * 8))
                .setCast(Blocks.SOUL_TORCH, true)
                .save(consumer, location(metalFolder + "iron/soul_lantern"));
        ItemCastingRecipeBuilder.tableRecipe(Items.COMPASS)
                .setFluidAndTime(new FluidStack(Iron.getFluid(), 144 * 4))
                .setCast(Items.REDSTONE, true)
                .save(consumer, location(metalFolder + "iron/compass"));
    }

    private void addMeltingRecipes(Consumer<FinishedRecipe> consumer) {
        String metalFolder = "smeltery/melting/metal/";
        MeltingRecipeBuilder.melting(Ingredient.of(Tags.Items.ORES_NETHERITE_SCRAP), TinkerFluids.moltenDebris, 144, 2.0f)
                .setOre(OreRateType.METAL)
                .addByproduct(TinkerFluids.moltenNetherite.result(16 * 3))
                .save(consumer, location(metalFolder + "molten_debris/ore"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerTags.Items.INGOTS_NETHERITE_SCRAP), TinkerFluids.moltenDebris, 144, 1.0f)
                .save(consumer, location(metalFolder + "molten_debris/scrap"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerTags.Items.NUGGETS_NETHERITE_SCRAP), TinkerFluids.moltenDebris, 144, 1 / 3f)
                .save(consumer, location(metalFolder + "molten_debris/debris_nugget"));

        MeltingRecipeBuilder.melting(NoContainerIngredient.of(TinkerSmeltery.copperCan), FluidOutput.fromFluid(Copper.getFluid(), 144), GTMaterialHelper.findTemp(Copper), 1.0f)
                .save(consumer, location(metalFolder + "copper/can"));

        MeltingRecipeBuilder.melting(Ingredient.of(Items.ACTIVATOR_RAIL, Items.DETECTOR_RAIL, Blocks.STONECUTTER, Blocks.PISTON, Blocks.STICKY_PISTON), FluidOutput.fromFluid(Iron.getFluid(), 144), GTMaterialHelper.findTemp(Iron), IMeltingRecipe.calcTimeFactor(144))
                .save(consumer, location(metalFolder + "iron/ingot_1"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.HEAVY_WEIGHTED_PRESSURE_PLATE, Items.IRON_DOOR, Blocks.SMITHING_TABLE), FluidOutput.fromFluid(Iron.getFluid(), 144 * 2), GTMaterialHelper.findTemp(Iron), IMeltingRecipe.calcTimeFactor(144*2))
                .save(consumer, location(metalFolder + "iron/ingot_2"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.BUCKET), FluidOutput.fromFluid(Iron.getFluid(), 144 * 3), GTMaterialHelper.findTemp(Iron), IMeltingRecipe.calcTimeFactor(144*3))
                .save(consumer, location(metalFolder + "iron/bucket"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.COMPASS, Blocks.IRON_TRAPDOOR), FluidOutput.fromFluid(Iron.getFluid(), 144 * 4), GTMaterialHelper.findTemp(Iron), IMeltingRecipe.calcTimeFactor(144*4))
                .save(consumer, location(metalFolder + "iron/ingot_4"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.BLAST_FURNACE, Blocks.HOPPER, Items.MINECART), FluidOutput.fromFluid(Iron.getFluid(), 144 * 5), GTMaterialHelper.findTemp(Iron), IMeltingRecipe.calcTimeFactor(144*5))
                .save(consumer, location(metalFolder + "iron/ingot_5"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.CAULDRON), FluidOutput.fromFluid(Iron.getFluid(), 144 * 7), GTMaterialHelper.findTemp(Iron), IMeltingRecipe.calcTimeFactor(144*7))
                .save(consumer, location(metalFolder + "iron/cauldron"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.CHAIN), FluidOutput.fromFluid(Iron.getFluid(), 144 + 16 * 2), GTMaterialHelper.findTemp(Iron), IMeltingRecipe.calcTimeFactor(144 + 16 * 2))
                .save(consumer, location(metalFolder + "iron/chain"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.ANVIL, Blocks.CHIPPED_ANVIL, Blocks.DAMAGED_ANVIL), FluidOutput.fromFluid(Iron.getFluid(), 144 * 4 + 1296 * 3), GTMaterialHelper.findTemp(Iron), IMeltingRecipe.calcTimeFactor(144 * 4 + 1296 * 3))
                .save(consumer, location(metalFolder + "iron/anvil"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.IRON_BARS, Blocks.RAIL), FluidOutput.fromFluid(Iron.getFluid(), 16 * 3), GTMaterialHelper.findTemp(Iron), IMeltingRecipe.calcTimeFactor(16 * 3))
                .save(consumer, location(metalFolder + "iron/nugget_3"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerCommons.ironPlatform), FluidOutput.fromFluid(Iron.getFluid(), 16 * 10), GTMaterialHelper.findTemp(Iron), IMeltingRecipe.calcTimeFactor(16 * 10))
                .save(consumer, location(metalFolder + "iron/platform"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.TRIPWIRE_HOOK), FluidOutput.fromFluid(Iron.getFluid(), 16 * 4), GTMaterialHelper.findTemp(Iron), IMeltingRecipe.calcTimeFactor(16 * 4))
                .save(consumer, location(metalFolder + "iron/tripwire"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.LANTERN, Blocks.SOUL_LANTERN), FluidOutput.fromFluid(Iron.getFluid(), 16 * 8), GTMaterialHelper.findTemp(Iron), IMeltingRecipe.calcTimeFactor(16 * 8))
                .save(consumer, location(metalFolder + "iron/lantern"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerModifiers.ironReinforcement), FluidOutput.fromFluid(Iron.getFluid(), 144), GTMaterialHelper.findTemp(Iron), IMeltingRecipe.calcTimeFactor(144))
                .save(consumer, location(metalFolder + "iron/reinforcement"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.CROSSBOW), FluidOutput.fromFluid(Iron.getFluid(), 16 * 13), GTMaterialHelper.findTemp(Iron), IMeltingRecipe.calcTimeFactor(16 * 13))
                .setDamagable(16)
                .save(consumer, location(metalFolder + "iron/crossbow"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.IRON_HORSE_ARMOR), FluidOutput.fromFluid(Iron.getFluid(), 16 * 7), GTMaterialHelper.findTemp(Iron), IMeltingRecipe.calcTimeFactor(16 * 7))
                .save(consumer, location(metalFolder + "iron/horse_armor"));
        final int chainIron = 16 * 6;
        final int chainSteel = 16 * 3;
        MeltingRecipeBuilder.melting(Ingredient.of(Items.CHAINMAIL_HELMET), FluidOutput.fromFluid(Iron.getFluid(), chainIron * 5), GTMaterialHelper.findTemp(Iron), IMeltingRecipe.calcTimeFactor(chainIron * 5))
                .addByproduct(new FluidStack(Steel.getFluid(), chainSteel * 5))
                .setDamagable(16, 16)
                .save(consumer, location(metalFolder + "iron/chain_helmet"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.CHAINMAIL_CHESTPLATE), FluidOutput.fromFluid(Iron.getFluid(), chainIron * 5), GTMaterialHelper.findTemp(Iron), IMeltingRecipe.calcTimeFactor(chainIron * 8))
                .addByproduct(new FluidStack(Steel.getFluid(),chainSteel * 8))
                .setDamagable(16, 16)
                .save(consumer, location(metalFolder + "iron/chain_chestplate"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.CHAINMAIL_LEGGINGS), FluidOutput.fromFluid(Iron.getFluid(), chainIron * 5), GTMaterialHelper.findTemp(Iron), IMeltingRecipe.calcTimeFactor(chainIron * 7))
                .addByproduct(new FluidStack(Steel.getFluid(),chainSteel * 7))
                .setDamagable(16, 16)
                .save(consumer, location(metalFolder + "iron/chain_leggings"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.CHAINMAIL_BOOTS), FluidOutput.fromFluid(Iron.getFluid(), chainIron * 5), GTMaterialHelper.findTemp(Iron), IMeltingRecipe.calcTimeFactor(chainIron * 4))
                .addByproduct(new FluidStack(Steel.getFluid(),chainSteel * 4))
                .setDamagable(16, 16)
                .save(consumer, location(metalFolder + "iron/chain_boots"));

        MeltingRecipeBuilder.melting(Ingredient.of(TinkerTags.Items.GOLD_CASTS), FluidOutput.fromFluid(Gold.getFluid(), 144), GTMaterialHelper.findTemp(Gold), IMeltingRecipe.calcTimeFactor(144))
                .save(consumer, location(metalFolder + "gold/cast"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.POWERED_RAIL), FluidOutput.fromFluid(Gold.getFluid(), 144), GTMaterialHelper.findTemp(Gold), IMeltingRecipe.calcTimeFactor(144))
                .save(consumer, location(metalFolder + "gold/powered_rail"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE), FluidOutput.fromFluid(Gold.getFluid(), 144 * 2), GTMaterialHelper.findTemp(Gold), IMeltingRecipe.calcTimeFactor(144*2))
                .save(consumer, location(metalFolder + "gold/pressure_plate"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.CLOCK), FluidOutput.fromFluid(Gold.getFluid(), 144 * 4), GTMaterialHelper.findTemp(Gold), IMeltingRecipe.calcTimeFactor(144*4))
                .save(consumer, location(metalFolder + "gold/clock"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.GOLDEN_APPLE), FluidOutput.fromFluid(Gold.getFluid(), 144 * 8), GTMaterialHelper.findTemp(Gold), IMeltingRecipe.calcTimeFactor(144*8))
                .save(consumer, location(metalFolder + "gold/apple"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.GLISTERING_MELON_SLICE, Items.GOLDEN_CARROT), FluidOutput.fromFluid(Gold.getFluid(), 16 * 8), GTMaterialHelper.findTemp(Gold), IMeltingRecipe.calcTimeFactor(16*8))
                .save(consumer, location(metalFolder + "gold/produce"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerModifiers.goldReinforcement), FluidOutput.fromFluid(Gold.getFluid(), 144), GTMaterialHelper.findTemp(Gold), IMeltingRecipe.calcTimeFactor(144))
                .save(consumer, location(metalFolder + "gold/reinforcement"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerCommons.goldBars), FluidOutput.fromFluid(Gold.getFluid(), 16 * 3), GTMaterialHelper.findTemp(Gold), IMeltingRecipe.calcTimeFactor(16*3))
                .save(consumer, location(metalFolder + "gold/nugget_3"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerCommons.goldPlatform), FluidOutput.fromFluid(Gold.getFluid(), 16 * 10), GTMaterialHelper.findTemp(Gold), IMeltingRecipe.calcTimeFactor(16*10))
                .save(consumer, location(metalFolder + "gold/platform"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.GOLDEN_HORSE_ARMOR), FluidOutput.fromFluid(Gold.getFluid(), 144 * 7), GTMaterialHelper.findTemp(Gold), IMeltingRecipe.calcTimeFactor(144*7))
                .save(consumer, location(metalFolder + "gold/horse_armor"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.ENCHANTED_GOLDEN_APPLE), FluidOutput.fromFluid(Gold.getFluid(), 1296 * 8), GTMaterialHelper.findTemp(Gold), IMeltingRecipe.calcTimeFactor(1296*8))
                .save(consumer, location(metalFolder + "gold/enchanted_apple"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.NETHER_GOLD_ORE), FluidOutput.fromFluid(Gold.getFluid(), 144), GTMaterialHelper.findTemp(Gold), IMeltingRecipe.calcTimeFactor(144))
                .addByproduct(new FluidStack(Copper.getFluid(), 144))
                .setOre(OreRateType.METAL)
                .save(consumer, location(metalFolder + "gold/nether_gold_ore"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.GILDED_BLACKSTONE), FluidOutput.fromFluid(Gold.getFluid(), 16 * 3), GTMaterialHelper.findTemp(Gold), IMeltingRecipe.calcTimeFactor(16*3))
                .addByproduct(new FluidStack(Copper.getFluid(), 144))
                .setOre(OreRateType.METAL)
                .save(consumer, location(metalFolder + "gold/gilded_blackstone"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.BELL), FluidOutput.fromFluid(Gold.getFluid(), 144 * 4), GTMaterialHelper.findTemp(Gold), IMeltingRecipe.calcTimeFactor(144*4))
                .save(consumer, location(metalFolder + "gold/bell"));

        MeltingRecipeBuilder.melting(Ingredient.of(
                                Blocks.EXPOSED_COPPER, Blocks.WEATHERED_COPPER, Blocks.OXIDIZED_COPPER,
                                Blocks.WAXED_COPPER_BLOCK, Blocks.WAXED_EXPOSED_COPPER, Blocks.WAXED_WEATHERED_COPPER, Blocks.WAXED_OXIDIZED_COPPER),
                        FluidOutput.fromFluid(Copper.getFluid(), 1296), GTMaterialHelper.findTemp(Copper), IMeltingRecipe.calcTimeFactor(1296))
                .save(consumer, location(metalFolder + "copper/decorative_block"));
        MeltingRecipeBuilder.melting(Ingredient.of(
                                Blocks.CUT_COPPER, Blocks.EXPOSED_CUT_COPPER, Blocks.WEATHERED_CUT_COPPER, Blocks.OXIDIZED_CUT_COPPER,
                                Blocks.CUT_COPPER_STAIRS, Blocks.EXPOSED_CUT_COPPER_STAIRS, Blocks.WEATHERED_CUT_COPPER_STAIRS, Blocks.OXIDIZED_CUT_COPPER_STAIRS,
                                Blocks.WAXED_CUT_COPPER, Blocks.WAXED_EXPOSED_CUT_COPPER, Blocks.WAXED_WEATHERED_CUT_COPPER, Blocks.WAXED_OXIDIZED_CUT_COPPER,
                                Blocks.WAXED_CUT_COPPER_STAIRS, Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS, Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS, Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS),
                        FluidOutput.fromFluid(Copper.getFluid(), 16 * 20), GTMaterialHelper.findTemp(Copper), IMeltingRecipe.calcTimeFactor(16*20))
                .save(consumer, location(metalFolder + "copper/cut_block"));
        MeltingRecipeBuilder.melting(Ingredient.of(
                                Blocks.CUT_COPPER_SLAB, Blocks.EXPOSED_CUT_COPPER_SLAB, Blocks.WEATHERED_CUT_COPPER_SLAB, Blocks.OXIDIZED_CUT_COPPER_SLAB,
                                Blocks.WAXED_CUT_COPPER_SLAB, Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB, Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB, Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB),
                        FluidOutput.fromFluid(Copper.getFluid(), 16 * 10), GTMaterialHelper.findTemp(Copper), IMeltingRecipe.calcTimeFactor(16*10))
                .save(consumer, location(metalFolder + "copper/cut_slab"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.LIGHTNING_ROD), FluidOutput.fromFluid(Copper.getFluid(), 144 * 3), GTMaterialHelper.findTemp(Copper), IMeltingRecipe.calcTimeFactor(144*3))
                .save(consumer, location(metalFolder + "copper/lightning_rod"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerTags.Items.COPPER_PLATFORMS), FluidOutput.fromFluid(Copper.getFluid(), 16 * 10), GTMaterialHelper.findTemp(Copper), IMeltingRecipe.calcTimeFactor(16*10))
                .save(consumer, location(metalFolder + "copper/platform"));

        MeltingRecipeBuilder.melting(Ingredient.of(Items.SPYGLASS), TinkerFluids.moltenAmethyst, FluidValues.GEM)
                .addByproduct(Copper.getFluid(144 * 2))
                .save(consumer, location("smeltery/melting/amethyst/spyglass"));

        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.LODESTONE), TinkerFluids.moltenNetherite, 144)
                .save(consumer, location(metalFolder + "netherite/lodestone"));
        
        int[] netheriteSizes = {16, FluidValues.GEM_SHARD};
        MeltingRecipeBuilder.melting(Ingredient.of(Items.NETHERITE_HELMET), TinkerFluids.moltenNetherite, 144)
                .setDamagable(netheriteSizes)
                .addByproduct(TinkerFluids.moltenDiamond.result(FluidValues.GEM * 5))
                .save(consumer, location(metalFolder + "netherite/helmet"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.NETHERITE_CHESTPLATE), TinkerFluids.moltenNetherite, 144)
                .setDamagable(netheriteSizes)
                .addByproduct(TinkerFluids.moltenDiamond.result(FluidValues.GEM * 8))
                .save(consumer, location(metalFolder + "netherite/chestplate"));
        MeltingRecipeBuilder.melting(Ingredient.of(getItemTag(TConstruct.MOD_ID, "melting/netherite/tools_costing_" + 7)), TinkerFluids.moltenNetherite, 144)
                .setDamagable(netheriteSizes)
                .addByproduct(TinkerFluids.moltenDiamond.result(FluidValues.GEM * 7))
                .save(consumer, location(metalFolder + "netherite/leggings"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.NETHERITE_BOOTS), TinkerFluids.moltenNetherite, 144)
                .setDamagable(netheriteSizes)
                .addByproduct(TinkerFluids.moltenDiamond.result(FluidValues.GEM * 4))
                .save(consumer, location(metalFolder + "netherite/boots"));
        MeltingRecipeBuilder.melting(Ingredient.of(getItemTag(TConstruct.MOD_ID, "melting/netherite/tools_costing_" + 3)), TinkerFluids.moltenNetherite, 144)
                .setDamagable(netheriteSizes)
                .addByproduct(TinkerFluids.moltenDiamond.result(FluidValues.GEM * 3))
                .save(consumer, location(metalFolder + "netherite/axes"));
        MeltingRecipeBuilder.melting(Ingredient.of(getItemTag(TConstruct.MOD_ID, "melting/netherite/tools_costing_" + 2)), TinkerFluids.moltenNetherite, 144)
                .setDamagable(netheriteSizes)
                .addByproduct(TinkerFluids.moltenDiamond.result(FluidValues.GEM * 2))
                .save(consumer, location(metalFolder + "netherite/sword"));
        MeltingRecipeBuilder.melting(Ingredient.of(getItemTag(TConstruct.MOD_ID, "melting/netherite/tools_costing_" + 1)), TinkerFluids.moltenNetherite, 144)
                .setDamagable(netheriteSizes)
                .addByproduct(TinkerFluids.moltenDiamond.result(FluidValues.GEM))
                .save(consumer, location(metalFolder + "netherite/shovel"));
        MeltingRecipeBuilder.melting(ItemNameIngredient.from(new ResourceLocation("tools_complement", "netherite_excavator")), TinkerFluids.moltenNetherite, 144)
                .setDamagable(netheriteSizes)
                .addByproduct(TinkerFluids.moltenDiamond.result(FluidValues.GEM * 11))
                .save(withCondition(consumer, new ItemExistsCondition("tools_complement", "netherite_excavator")), location(metalFolder + "netherite/excavator"));
        MeltingRecipeBuilder.melting(ItemNameIngredient.from(new ResourceLocation("tools_complement", "netherite_hammer")), TinkerFluids.moltenNetherite, 144)
                .setDamagable(netheriteSizes)
                .addByproduct(TinkerFluids.moltenDiamond.result(FluidValues.GEM * 13))
                .save(withCondition(consumer, new ItemExistsCondition("tools_complement", "netherite_hammer")), location(metalFolder + "netherite/hammer"));

        MeltingRecipeBuilder.melting(Ingredient.of(TinkerModifiers.silkyCloth), FluidOutput.fromFluid(RoseGold.getFluid(), 144), GTMaterialHelper.findTemp(RoseGold), IMeltingRecipe.calcTimeFactor(144))
                .save(consumer, location(metalFolder + "rose_gold/silky_cloth"));

        MeltingRecipeBuilder.melting(Ingredient.of(TinkerModifiers.slimesteelReinforcement), TinkerFluids.moltenSlimesteel, 16 * 3)
                .addByproduct(TinkerFluids.moltenObsidian.result(FluidValues.GLASS_PANE))
                .save(consumer, location(metalFolder + "slimesteel/reinforcement"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerModifiers.cobaltReinforcement), FluidOutput.fromFluid(Cobalt.getFluid(), 144), GTMaterialHelper.findTemp(Cobalt), IMeltingRecipe.calcTimeFactor(144))
                .save(consumer, location(metalFolder + "cobalt/reinforcement"));

        MeltingRecipeBuilder.melting(Ingredient.of(TinkerCommons.cobaltPlatform), FluidOutput.fromFluid(Cobalt.getFluid(), 16 * 10), GTMaterialHelper.findTemp(Copper), IMeltingRecipe.calcTimeFactor(16*10))
                .save(consumer, location(metalFolder + "cobalt/platform"));
    }

    private void addAlloyRecipes(Consumer<FinishedRecipe> consumer) {
        String folder = "smeltery/alloys/";
        AlloyRecipeBuilder.alloy(TinkerFluids.moltenSlimesteel, 144 * 2)
                .addInput(Iron.getFluid(), 144)
                .addInput(TinkerFluids.skySlime.ingredient(FluidValues.SLIMEBALL))
                .addInput(TinkerFluids.searedStone.ingredient(FluidValues.BRICK))
                .save(consumer, prefix(TinkerFluids.moltenSlimesteel, folder));

        AlloyRecipeBuilder.alloy(TinkerFluids.moltenAmethystBronze, 144)
                .addInput(Copper.getFluid(), 144)
                .addInput(TinkerFluids.moltenAmethyst.ingredient(FluidValues.GEM))
                .save(consumer, prefix(TinkerFluids.moltenAmethystBronze, folder));

        AlloyRecipeBuilder.alloy(FluidOutput.fromFluid(RoseGold.getFluid(), 144 * 2), GTMaterialHelper.findTemp(RoseGold))
                .addInput(Copper.getFluid(), 144)
                .addInput(Gold.getFluid(), 144)
                .save(consumer, prefix(TinkerFluids.moltenRoseGold, folder));

        AlloyRecipeBuilder.alloy(TinkerFluids.moltenPigIron, 144 * 2)
                .addInput(Copper.getFluid(), 144)
                .addInput(TinkerFluids.meatSoup.ingredient(FluidValues.SLIMEBALL * 2))
                .addInput(TinkerFluids.honey.ingredient(FluidValues.BOTTLE))
                .save(consumer, prefix(TinkerFluids.moltenPigIron, folder));

        AlloyRecipeBuilder.alloy(TinkerFluids.moltenCinderslime, 144 * 2)
                .addInput(Gold.getFluid(), 144)
                .addInput(TinkerFluids.ichor.ingredient(FluidValues.SLIMEBALL))
                .addInput(TinkerFluids.scorchedStone.ingredient(FluidValues.BRICK))
                .save(consumer, prefix(TinkerFluids.moltenCinderslime, folder));

        AlloyRecipeBuilder.alloy(TinkerFluids.moltenQueensSlime, 144 * 2)
                .addInput(Cobalt.getFluid(), 144)
                .addInput(Gold.getFluid(), 144)
                .addInput(TinkerFluids.magma.ingredient(FluidValues.SLIMEBALL))
                .save(consumer, prefix(TinkerFluids.moltenQueensSlime, folder));

        AlloyRecipeBuilder.alloy(TinkerFluids.moltenManyullyn, 144 * 4)
                .addInput(Cobalt.getFluid(), 144 * 3)
                .addInput(TinkerFluids.moltenDebris.ingredient(144))
                .save(consumer, prefix(TinkerFluids.moltenManyullyn, folder));

        AlloyRecipeBuilder.alloy(TinkerFluids.moltenHepatizon, 144 * 2)
                .addInput(Copper.getFluid(), 144 * 2)
                .addInput(Cobalt.getFluid(), 144)
                .addInput(TinkerFluids.moltenQuartz.ingredient(FluidValues.GEM))
                .save(consumer, prefix(TinkerFluids.moltenHepatizon, folder));

        ConditionalRecipe.builder()
                .addCondition(ConfigEnabledCondition.CHEAPER_NETHERITE_ALLOY)
                .addRecipe(
                        AlloyRecipeBuilder.alloy(TinkerFluids.moltenNetherite, 16)
                                .addInput(TinkerFluids.moltenDebris.ingredient(16 * 4))
                                .addInput(Gold.getFluid(), 16 * 2)::save)
                .addCondition(TrueCondition.INSTANCE)
                .addRecipe(
                        AlloyRecipeBuilder.alloy(TinkerFluids.moltenNetherite, 16)
                                .addInput(TinkerFluids.moltenDebris.ingredient(16 * 4))
                                .addInput(Gold.getFluid(),16 * 4)::save)
                .build(consumer, prefix(TinkerFluids.moltenNetherite, folder));

        Consumer<FinishedRecipe> wrapped;
        wrapped = withCondition(consumer, tagCondition("ingots/tin"));
        AlloyRecipeBuilder.alloy(FluidOutput.fromFluid(Bronze.getFluid(), 144 * 4), GTMaterialHelper.findTemp(Bronze))
                .addInput(Copper.getFluid(), 144 * 3)
                .addInput(Tin.getFluid(), 144)
                .save(wrapped, prefix(TinkerFluids.moltenBronze, folder));

        wrapped = withCondition(consumer, tagCondition("ingots/zinc"));
        AlloyRecipeBuilder.alloy(FluidOutput.fromFluid(Brass.getFluid(), 144 * 2), GTMaterialHelper.findTemp(Brass))
                .addInput(Copper.getFluid(), 144)
                .addInput(Zinc.getFluid(), 144)
                .save(wrapped, prefix(TinkerFluids.moltenBrass, folder));

        wrapped = withCondition(consumer, tagCondition("ingots/silver"));
        AlloyRecipeBuilder.alloy(FluidOutput.fromFluid(Electrum.getFluid(), 144 * 2), GTMaterialHelper.findTemp(Electrum))
                .addInput(Gold.getFluid(),144)
                .addInput(Silver.getFluid(), 144)
                .save(wrapped, prefix(TinkerFluids.moltenElectrum, folder));

        wrapped = withCondition(consumer, tagCondition("ingots/nickel"));
        AlloyRecipeBuilder.alloy(FluidOutput.fromFluid(Invar.getFluid(), 144 * 3), GTMaterialHelper.findTemp(Invar))
                .addInput(Iron.getFluid(), 144 * 2)
                .addInput(Nickel.getFluid(), 144)
                .save(wrapped, prefix(TinkerFluids.moltenInvar, folder));

        wrapped = withCondition(consumer, tagCondition("ingots/nickel"));
        AlloyRecipeBuilder.alloy(TinkerFluids.moltenConstantan, 144 * 2)
                .addInput(Copper.getFluid(), 144)
                .addInput(Nickel.getFluid(), 144)
                .save(wrapped, prefix(TinkerFluids.moltenConstantan, folder));

        ICondition lead = tagCondition("ingots/lead");
        ICondition tin = tagCondition("ingots/tin");
        ConditionalRecipe.builder()
                .addCondition(new AndCondition(lead, tin))
                .addRecipe(
                        AlloyRecipeBuilder.alloy(TinkerFluids.moltenPewter, 144 * 4)
                                .addInput(Tin.getFluid(), 144 * 3)
                                .addInput(Lead.getFluid(), 144)::save)
                .addCondition(tin)
                .addRecipe(
                        AlloyRecipeBuilder.alloy(TinkerFluids.moltenPewter, 144 * 4)
                                .addInput(Iron.getFluid(),144)
                                .addInput(Tin.getFluid(), 144 * 3)::save)
                .addCondition(lead)
                .addRecipe(
                        AlloyRecipeBuilder.alloy(TinkerFluids.moltenPewter, 144 * 2)
                                .addInput(Iron.getFluid(),144)
                                .addInput(Lead.getFluid(), 144)::save)
                .build(withCondition(consumer, new OrCondition(ConfigEnabledCondition.ALLOW_INGOTLESS_ALLOYS, tagCondition("ingots/pewter"))), prefix(TinkerFluids.moltenPewter, folder));

        Function<String, ICondition> fluidTagLoaded = name -> new TagFilledCondition<>(Registries.FLUID, commonResource(name));
        Function<String,TagKey<Fluid>> fluidTag = name -> FluidTags.create(commonResource(name));

        wrapped = withCondition(consumer, tagCondition("ingots/enderium"), tagCondition("ingots/lead"));
        AlloyRecipeBuilder.alloy(TinkerFluids.moltenEnderium, 144 * 2)
                .addInput(Lead.getFluid(), 144 * 3)
                .addInput(TinkerFluids.moltenDiamond.ingredient(FluidValues.GEM))
                .addInput(TinkerFluids.moltenEnder.ingredient(FluidValues.SLIMEBALL * 2))
                .save(wrapped, prefix(TinkerFluids.moltenEnderium, folder));

        wrapped = withCondition(consumer, tagCondition("ingots/lumium"), tagCondition("ingots/tin"), tagCondition("ingots/silver"), fluidTagLoaded.apply("glowstone"));
        AlloyRecipeBuilder.alloy(TinkerFluids.moltenLumium, 144 * 4)
                .addInput(Tin.getFluid(), 144 * 3)
                .addInput(Silver.getFluid(), 144)
                .addInput(FluidIngredient.of(fluidTag.apply("glowstone"), FluidValues.SLIMEBALL * 2))
                .save(wrapped, prefix(TinkerFluids.moltenLumium, folder));

        wrapped = withCondition(consumer, tagCondition("ingots/signalum"), tagCondition("ingots/copper"), tagCondition("ingots/silver"), fluidTagLoaded.apply("redstone"));
        AlloyRecipeBuilder.alloy(TinkerFluids.moltenSignalum, 144 * 4)
                .addInput(Copper.getFluid(), 144 * 3)
                .addInput(Silver.getFluid(), 144)
                .addInput(Redstone.getFluid(), 144 * 4)
                .save(wrapped, prefix(TinkerFluids.moltenSignalum, folder));


        wrapped = withCondition(consumer, tagCondition("ingots/refined_obsidian"), tagCondition("ingots/osmium"));
        AlloyRecipeBuilder.alloy(TinkerFluids.moltenRefinedObsidian, 144)
                .addInput(TinkerFluids.moltenObsidian.ingredient(FluidValues.GLASS_PANE))
                .addInput(TinkerFluids.moltenDiamond.ingredient(FluidValues.GEM))
                .addInput(Osmium.getFluid(), 144)
                .save(wrapped, prefix(TinkerFluids.moltenRefinedObsidian, folder));

        wrapped = withCondition(consumer, tagCondition("ingots/nicrosil"));
        ConditionalRecipe.builder()
                .addCondition(new AndCondition(tagCondition("ingots/chromium"), tagCondition("ingots/nickel")))
                .addRecipe(AlloyRecipeBuilder.alloy(TinkerFluids.moltenNicrosil, 144 * 4)
                        .addInput(Nickel.getFluid(), 144 * 2)
                        .addInput(Chromium.getFluid(), 144)
                        .addInput(TinkerFluids.moltenQuartz.ingredient(FluidValues.GEM))::save)

                .addCondition(new AndCondition(tagCondition("ingots/tin"), tagCondition("ingots/nickel")))
                .addRecipe(AlloyRecipeBuilder.alloy(TinkerFluids.moltenNicrosil, 144 * 4)
                        .addInput(Nickel.getFluid(), 144 * 2)
                        .addInput(Tin.getFluid(), 144)
                        .addInput(TinkerFluids.moltenQuartz.ingredient(FluidValues.GEM))::save)

                .addCondition(tagCondition("ingots/chromium"))
                .addRecipe(AlloyRecipeBuilder.alloy(TinkerFluids.moltenNicrosil, 144 * 4)
                        .addInput(Chromium.getFluid(), 144 * 2)
                        .addInput(Iron.getFluid(), 144)
                        .addInput(TinkerFluids.moltenQuartz.ingredient(FluidValues.GEM))::save)

                .addCondition(tagCondition("ingots/tin"))
                .addRecipe(AlloyRecipeBuilder.alloy(TinkerFluids.moltenNicrosil, 144 * 4)
                        .addInput(Tin.getFluid(), 144 * 2)
                        .addInput(Iron.getFluid(), 144)
                        .addInput(TinkerFluids.moltenQuartz.ingredient(FluidValues.GEM))::save)
                .build(wrapped, prefix(TinkerFluids.moltenNicrosil, folder));

        // duralumin
        wrapped = withCondition(consumer, tagCondition("ingots/duralumin"), tagCondition("ingots/aluminum"));
        AlloyRecipeBuilder.alloy(TinkerFluids.moltenDuralumin, 144 * 4)
                .addInput(TinkerFluids.moltenAluminum.ingredient(144 * 3))
                .addInput(Copper.getFluid(), 144)
                .save(wrapped, prefix(TinkerFluids.moltenDuralumin, folder));

        // bendalloy
        wrapped = withCondition(consumer, tagCondition("ingots/bendalloy"), tagCondition("ingots/tin"), tagCondition("ingots/lead"), tagCondition("ingots/cadmium"));
        AlloyRecipeBuilder.alloy(TinkerFluids.moltenBendalloy, 144 * 4)
                .addInput(Tin.getFluid(), 144 * 2)
                .addInput(Lead.getFluid(), 144)
                .addInput(TinkerFluids.moltenCadmium.ingredient(144))
                .save(wrapped, prefix(TinkerFluids.moltenBendalloy, folder));
    }
    
    private void addEntityMeltingRecipes(Consumer<FinishedRecipe> consumer) {
        String folder = "smeltery/entity_melting/";
        String headFolder = "smeltery/entity_melting/heads/";
        EntityMeltingRecipeBuilder.melting(EntityIngredient.of(EntityType.ZOMBIE, EntityType.HUSK, EntityType.ZOMBIE_HORSE), Iron.getFluid(16), 4)
                .save(consumer, location(folder + "zombie"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.ZOMBIE_HEAD, TinkerWorld.heads.get(TinkerHeadType.HUSK)), FluidOutput.fromFluid(Iron.getFluid(), 144),
                        GTMaterialHelper.findTemp(Iron), IMeltingRecipe.calcTimeFactor(144))
                .save(consumer, location(headFolder + "zombie"));
        
        EntityMeltingRecipeBuilder.melting(EntityIngredient.of(EntityType.DROWNED), Copper.getFluid(16), 4)
                .save(consumer, location(folder + "drowned"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerWorld.heads.get(TinkerHeadType.DROWNED)), FluidOutput.fromFluid(Copper.getFluid(), 144),
                        GTMaterialHelper.findTemp(Copper), IMeltingRecipe.calcTimeFactor(144))
                .save(consumer, location(headFolder + "drowned"));
        
        EntityMeltingRecipeBuilder.melting(EntityIngredient.of(EntityType.PIGLIN, EntityType.PIGLIN_BRUTE, EntityType.ZOMBIFIED_PIGLIN), Gold.getFluid(16), 4)
                .save(consumer, location(folder + "piglin"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.PIGLIN_HEAD, TinkerWorld.heads.get(TinkerHeadType.PIGLIN_BRUTE), TinkerWorld.heads.get(TinkerHeadType.ZOMBIFIED_PIGLIN)), FluidOutput.fromFluid(Gold.getFluid(), 144),
                        GTMaterialHelper.findTemp(Gold), IMeltingRecipe.calcTimeFactor(144))
                .save(consumer, location(headFolder + "piglin"));

        EntityMeltingRecipeBuilder.melting(EntityIngredient.of(EntityType.IRON_GOLEM), Iron.getFluid(16), 4)
                .save(consumer, location(folder + "iron_golem"));
    }

    private void addTagRecipes(Consumer<FinishedRecipe> consumer) {
        ToolItemMelting EXCAVATOR = new ToolItemMelting(11, "tools_complement", "excavator");
        ToolItemMelting HAMMER = new ToolItemMelting(13, "tools_complement", "hammer");
        CommonRecipe[] TOOLS_COMPLEMENT = { SmelteryRecipeBuilder.SHOVEL_PLUS, SWORD, AXES, EXCAVATOR, HAMMER };
        ToolItemMelting MEKANISM_SHIELD = new ToolItemMelting(6, "mekanism", "shield");
        CommonRecipe[] MEKANISM_ARMOR = {
                SmelteryRecipeBuilder.HELMET, SmelteryRecipeBuilder.CHESTPLATE, SmelteryRecipeBuilder.LEGGINGS_PLUS, SmelteryRecipeBuilder.BOOTS, MEKANISM_SHIELD};
        CommonRecipe FLAKES = new MetalMelting(1/3f, "allomancy", "flakes");

        metal(consumer, Copper).ore(GMCByproduct.SMALL_GOLD   ).metal().dust().plate().gear().coin().sheetmetal().geore().oreberry().wire().common(SWORD, AXES, EXCAVATOR, HAMMER, FLAKES).common(ARMOR).toolCostMelting(1, "shovel", false);
        metal(consumer, Iron  ).ore(GMCByproduct.STEEL        ).metal().dust().plate().gear().coin().sheetmetal().geore().oreberry().minecraftTools().toolCostMelting(11, "tools_costing_11").common(HAMMER, FLAKES).rod();
        metal(consumer, Cobalt).ore(Byproduct.SMALL_DIAMOND).metal().dust();
        metal(consumer, Steel ).metal().dust().plate().gear().coin().sheetmetal().common(SHOVEL_PLUS, SWORD, AXES, MEKANISM_SHIELD, FLAKES).common(ARMOR_PLUS).wire().rod().rawOre(GMCByproduct.IRON)
                .toolItemMelting(11, "railcraft", "spike_maul")
                .melting(1 / 9f, "raw_nugget", 1 / 2f, false);
        metal(consumer, Gold).metal().ore(GMCByproduct.COBALT).dust().plate().gear().coin().sheetmetal().geore().oreberry().minecraftTools("golden", true).common(EXCAVATOR, HAMMER, FLAKES).rawOre().singularOre(2).denseOre(6);

        molten(consumer, TinkerFluids.moltenDiamond).ore(GMCByproduct.DEBRIS ).largeGem().dust().gear().geore().minecraftTools("diamond", true).toolCostMelting(11, "tools_costing_11").common(HAMMER);
        molten(consumer, TinkerFluids.moltenQuartz ).ore(GMCByproduct.IRON   ).smallGem().dust().gear().geore();

        metal(consumer, TinkerFluids.moltenNetherite).metal().dust().plate().gear().coin();

        metal(consumer, TinkerFluids.moltenSlimesteel    ).metal();
        metal(consumer, TinkerFluids.moltenAmethystBronze).metal().dust();
        metal(consumer, RoseGold      ).metal().dust().plate().coin().gear();
        metal(consumer, TinkerFluids.moltenPigIron       ).metal();

        metal(consumer, TinkerFluids.moltenManyullyn  ).metal();
        metal(consumer, TinkerFluids.moltenHepatizon  ).metal();
        metal(consumer, TinkerFluids.moltenCinderslime).metal();
        metal(consumer, TinkerFluids.moltenQueensSlime).metal();

        String tf = "twilightforest";
        CommonRecipe tfHelmet     = new ToolItemMelting(5, tf, "helmet");
        CommonRecipe tfChestplate = new ToolItemMelting(8, tf, "chestplate");
        CommonRecipe tfBoots      = new ToolItemMelting(4, tf, "boots");
        CommonRecipe tfSword      = new ToolItemMelting(2, tf, "sword");
        metal(consumer, TinkerFluids.moltenKnightmetal).metal().common(AXES, tfHelmet, tfChestplate, LEGGINGS_PLUS, tfBoots, tfSword)
                .metalMelting(4, tf, "ring", false)
                .itemMelting(16, tf, "block_and_chain", true)
                .melting(1, "raw", "raw_materials", false, true)
                .melting(1 / 9f, "raw_nugget", 1 / 2f, false);

        metal(consumer, Tin     ).ore(GMCByproduct.NICKEL, GMCByproduct.COPPER).optional().metal().dust().oreberry().plate().gear().coin().common(TOOLS_COMPLEMENT).common(ARMOR).common(FLAKES);
        metal(consumer, TinkerFluids.moltenAluminum).ore(GMCByproduct.IRON                    ).optional().metal().dust().oreberry().plate().gear().coin().sheetmetal().wire().rod().common(FLAKES);
        metal(consumer, Lead    ).ore(GMCByproduct.SILVER, GMCByproduct.GOLD  ).optional().metal().dust().oreberry().plate().gear().coin().common(TOOLS_COMPLEMENT).common(ARMOR).common(FLAKES).sheetmetal().wire();
        metal(consumer, Silver  ).ore(GMCByproduct.LEAD, GMCByproduct.GOLD    ).optional().metal().dust().oreberry().plate().gear().coin().common(TOOLS_COMPLEMENT).common(ARMOR).common(FLAKES).sheetmetal();
        metal(consumer, Nickel  ).ore(GMCByproduct.PLATINUM, GMCByproduct.IRON).optional().metal().dust().oreberry().plate().gear().coin().common(TOOLS_COMPLEMENT).common(ARMOR).sheetmetal();
        metal(consumer, Zinc    ).ore(GMCByproduct.TIN, GMCByproduct.COPPER   ).optional().metal().dust().oreberry().plate().gear().geore().common(FLAKES);
        metal(consumer, Platinum).ore(GMCByproduct.GOLD                    ).optional().metal().dust();
        metal(consumer, Tungsten).ore(GMCByproduct.PLATINUM, GMCByproduct.GOLD).optional().metal().dust();
        metal(consumer, Chromium).ore(Byproduct.ALUMINUM, GMCByproduct.IRON).optional().metal().dust().common(FLAKES);
        metal(consumer, TinkerFluids.moltenCadmium ).ore(GMCByproduct.LEAD, GMCByproduct.COPPER  ).optional().metal().dust().common(FLAKES);
        metal(consumer, Osmium  ).ore(GMCByproduct.IRON                    ).optional().metal().dust().oreberry().common(TOOLS).common(MEKANISM_ARMOR);
        metal(consumer, Uranium238 ).ore(GMCByproduct.LEAD, GMCByproduct.COPPER  ).optional().metal().dust().oreberry().plate().gear().coin().sheetmetal();

        metal(consumer, Bronze    ).optional().metal().dust().plate().gear().coin().common(TOOLS_COMPLEMENT).common(MEKANISM_ARMOR);
        metal(consumer, Brass     ).optional().metal().dust().plate().gear();
        metal(consumer, Electrum  ).optional().metal().dust().plate().gear().coin().common(TOOLS_COMPLEMENT).common(ARMOR).sheetmetal().wire();
        metal(consumer, Invar     ).optional().metal().dust().plate().gear().coin().common(TOOLS_COMPLEMENT).common(ARMOR);
        metal(consumer, TinkerFluids.moltenConstantan).optional().metal().dust().plate().gear().coin().common(TOOLS_COMPLEMENT).common(ARMOR).sheetmetal();
        metal(consumer, TinkerFluids.moltenPewter    ).optional().metal().dust().common(FLAKES).rawOre(GMCByproduct.TIN, GMCByproduct.LEAD, GMCByproduct.IRON);
        metal(consumer, TinkerFluids.moltenNicrosil  ).optional().metal().dust().common(FLAKES).rawOre(GMCByproduct.CHROMIUM);
        metal(consumer, TinkerFluids.moltenDuralumin ).optional().metal().dust().common(FLAKES).rawOre(Byproduct.ALUMINUM, GMCByproduct.COPPER);
        metal(consumer, TinkerFluids.moltenBendalloy ).optional().metal().dust().common(FLAKES).rawOre(Byproduct.CADMIUM);

        metal(consumer, TinkerFluids.moltenEnderium).optional().metal().dust().plate().gear().coin();
        metal(consumer, TinkerFluids.moltenLumium  ).optional().metal().dust().plate().gear().coin();
        metal(consumer, TinkerFluids.moltenSignalum).optional().metal().dust().plate().gear().coin();
        metal(consumer, TinkerFluids.moltenRefinedObsidian ).optional().metal().common(TOOLS).common(MEKANISM_ARMOR);
        metal(consumer, TinkerFluids.moltenRefinedGlowstone).optional().metal().common(TOOLS).common(MEKANISM_ARMOR);

        TagKey<Fluid> dawnstone = getFluidTag(COMMON, "molten_dawnstone");
        metal(withCondition(consumer, new TagFilledCondition<>(dawnstone)), "dawnstone", dawnstone).temperature(900).optional().metal().plate();

        CommonRecipe tfLeggings = new ToolItemMelting(7, tf, "leggings");
        CommonRecipe tfShovel = new ToolItemMelting(1, tf, "shovel");
        metal(consumer, TinkerFluids.moltenSteeleaf).optional().metal()
                .common(AXES, SWORD, tfShovel, tfHelmet, tfChestplate, tfLeggings, tfBoots);
        fluid(consumer, "fiery", TinkerFluids.fieryLiquid).optional()
                .baseUnit(FluidValues.BOTTLE).damageUnit(FluidValues.SIP).unitByproducts(GMCByproduct.IRON)
                .melting(9, "block", "storage_blocks", 3.0f, false, false)
                .blockCasting(9, Ingredient.of(Tags.Items.STORAGE_BLOCKS_IRON), false)
                .meltingCasting(1, "ingot", "iron", 1, false)
                .common(tfSword, tfHelmet, tfChestplate, tfLeggings, tfBoots)
                .metalMelting(3, tf, "pickaxe", true);
        fluid(consumer, "ironwood", Iron.getFluid()).optional()
                .baseUnit(FluidValues.INGOT).damageUnit(FluidValues.NUGGET).unitByproducts(GMCByproduct.TINY_GOLD)
                .melting(9, "block", "storage_blocks", 3.0f, false, false)
                .melting(1, "ingot", 1f, false)
                .melting(1, "raw", "raw_materials", false, false)
                .common(AXES, SWORD, tfShovel, tfHelmet, tfChestplate, tfLeggings, tfBoots);
    }

    private void addCompatRecipes(Consumer<FinishedRecipe> consumer) {
        String folder = "compat/";
        ItemOutput andesiteAlloy = ItemNameOutput.fromName(new ResourceLocation("create", "andesite_alloy"));
        Consumer<FinishedRecipe> createConsumer = withCondition(consumer, new ModLoadedCondition("create"));
        ItemCastingRecipeBuilder.basinRecipe(andesiteAlloy)
                .setCast(Blocks.ANDESITE, true)
                .setFluidAndTime(new FluidStack(Iron.getFluid(), 16))
                .save(createConsumer, location(folder + "create/andesite_alloy_iron"));
        ItemCastingRecipeBuilder.basinRecipe(andesiteAlloy)
                .setCast(Blocks.ANDESITE, true)
                .setFluidAndTime(new FluidStack(Zinc.getFluid(), 16))
                .save(createConsumer, location(folder + "create/andesite_alloy_zinc"));

        int goldPerBlock = 16;
        String ceramics = "ceramics";
        String ceramicsFolder = folder + ceramics + "/";
        Function<String,ResourceLocation> ceramicsId = name -> new ResourceLocation(ceramics, name);
        Function<String,Ingredient> ceramicsItem = name -> ItemNameIngredient.from(new ResourceLocation(ceramics, name));
        Function<String,ItemOutput> ceramicsOutput = name -> ItemNameOutput.fromName(new ResourceLocation(ceramics, name));
        Consumer<FinishedRecipe> ceramicsConsumer = withCondition(consumer, new ModLoadedCondition(ceramics));
        String porcelainFolder = ceramicsFolder + "porcelain/";
        MeltingRecipeBuilder.melting(ceramicsItem.apply("golden_bricks_slab"), TinkerFluids.moltenPorcelain, FluidValues.BRICK * 2, 1.33f)
                .addByproduct(new FluidStack(Gold.getFluid(), goldPerBlock / 2))
                .save(ceramicsConsumer, location(porcelainFolder + "golden_bricks_slab"));
        MeltingRecipeBuilder.melting(ItemNameIngredient.from(
                        ceramicsId.apply("golden_bricks"), ceramicsId.apply("golden_bricks_stairs"), ceramicsId.apply("golden_bricks_wall")
                ), TinkerFluids.moltenPorcelain, FluidValues.BRICK * 4, 2f)
                .addByproduct(new FluidStack(Gold.getFluid(), goldPerBlock))
                .save(ceramicsConsumer, location(porcelainFolder + "golden_bricks_block"));

        String castingFolder = ceramicsFolder + "casting/";
        ItemCastingRecipeBuilder.basinRecipe(ceramicsOutput.apply("golden_bricks"))
                .setCast(ceramicsItem.apply("porcelain_bricks"), true)
                .setFluidAndTime(new FluidStack(Gold.getFluid(), goldPerBlock))
                .save(ceramicsConsumer, location(castingFolder + "golden_bricks"));
        ItemCastingRecipeBuilder.basinRecipe(ceramicsOutput.apply("golden_bricks_slab"))
                .setCast(ceramicsItem.apply("porcelain_bricks_slab"), true)
                .setFluidAndTime(new FluidStack(Gold.getFluid(), goldPerBlock / 2))
                .save(ceramicsConsumer, location(castingFolder + "golden_bricks_slab"));
        ItemCastingRecipeBuilder.basinRecipe(ceramicsOutput.apply("golden_bricks_stairs"))
                .setCast(ceramicsItem.apply("porcelain_bricks_stairs"), true)
                .setFluidAndTime(new FluidStack(Gold.getFluid(), goldPerBlock))
                .save(ceramicsConsumer, location(castingFolder + "golden_bricks_stairs"));
        ItemCastingRecipeBuilder.basinRecipe(ceramicsOutput.apply("golden_bricks_wall"))
                .setCast(ceramicsItem.apply("porcelain_bricks_wall"), true)
                .setFluidAndTime(new FluidStack(Gold.getFluid(), goldPerBlock))
                .save(ceramicsConsumer, location(castingFolder + "golden_bricks_wall"));

        Consumer<FinishedRecipe> wrapped = withCondition(consumer, tagCondition("ingots/refined_glowstone"), tagCondition("ingots/osmium"));
        ItemCastingRecipeBuilder.tableRecipe(ItemOutput.fromTag(getItemTag(COMMON, "ingots/refined_glowstone")))
                .setCast(Tags.Items.DUSTS_GLOWSTONE, true)
                .setFluidAndTime(new FluidStack(Osmium.getFluid(), 144))
                .save(wrapped, location(folder + "refined_glowstone_ingot"));
        wrapped = withCondition(consumer, tagCondition("ingots/refined_obsidian"), tagCondition("ingots/osmium"));
        ItemCastingRecipeBuilder.tableRecipe(ItemOutput.fromTag(getItemTag(COMMON, "ingots/refined_obsidian")))
                .setCast(getItemTag(COMMON, "dusts/refined_obsidian"), true)
                .setFluidAndTime(new FluidStack(Osmium.getFluid(), 144))
                .save(wrapped, location(folder + "refined_obsidian_ingot"));
        ItemCastingRecipeBuilder.tableRecipe(TinkerMaterials.necroniumBone)
                .setFluidAndTime(new FluidStack(Uranium238.getFluid(), 144))
                .setCast(TinkerTags.Items.WITHER_BONES, true)
                .save(withCondition(consumer, tagCondition("ingots/uranium")), location(folder + "necronium_bone"));
    }

    @Override
    public void castCreation(Consumer<FinishedRecipe> consumer, Ingredient input, CastItemObject cast, String folder, String name) {
        ItemCastingRecipeBuilder.tableRecipe(cast)
                .setFluidAndTime(new FluidStack(Gold.getFluid(), 144))
                .setCast(input, true)
                .setSwitchSlots()
                .save(consumer, location(folder + "gold/" + name));
    }

    public SmelteryRecipeBuilder metal(Consumer<FinishedRecipe> consumer, FluidObject<?> fluid) {
        return molten(consumer, fluid).castingFolder("smeltery/casting/metal").meltingFolder("smeltery/melting/metal");
    }

    public SmelteryRecipeBuilder metal(Consumer<FinishedRecipe> consumer, Material material) {
        return SmelteryRecipeBuilder.fluid(consumer, location(material.getName()), material.getFluid()).castingFolder("smeltery/casting/metal").meltingFolder("smeltery/melting/metal")
                .temperature(GTMaterialHelper.findTemp(material));
    }

    public SmelteryRecipeBuilder metal(Consumer<FinishedRecipe> consumer, String name, TagKey<Fluid> fluid) {
        return SmelteryRecipeBuilder.fluid(consumer, location(name), fluid).castingFolder("smeltery/casting/metal").meltingFolder("smeltery/melting/metal");
    }

    @Override
    public SmelteryRecipeBuilder molten(Consumer<FinishedRecipe> consumer, FluidObject<?> fluid) {
        return ISmelteryRecipeHelper.super.molten(consumer, fluid).castingFolder("smeltery/casting").meltingFolder("smeltery/melting");
    }

    public SmelteryRecipeBuilder fluid(Consumer<FinishedRecipe> consumer, String name, Fluid fluid) {
        return SmelteryRecipeBuilder.fluid(consumer, location(name), fluid);
    }

    @Override
    public String getModId() {
        return TConstruct.MOD_ID;
    }
}
