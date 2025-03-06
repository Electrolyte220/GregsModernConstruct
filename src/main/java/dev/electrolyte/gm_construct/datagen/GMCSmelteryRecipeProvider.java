package dev.electrolyte.gm_construct.datagen;

import dev.electrolyte.gm_construct.data.GMCByproduct;
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
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ItemExistsCondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.TrueCondition;
import slimeknights.mantle.recipe.condition.TagFilledCondition;
import slimeknights.mantle.recipe.data.ItemNameIngredient;
import slimeknights.mantle.recipe.data.ItemNameOutput;
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
import slimeknights.tconstruct.library.data.recipe.SmelteryRecipeBuilder.*;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.alloying.AlloyRecipeBuilder;
import slimeknights.tconstruct.library.recipe.casting.ItemCastingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.casting.container.ContainerFillingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.entitymelting.EntityMeltingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.ingredient.NoContainerIngredient;
import slimeknights.tconstruct.library.recipe.melting.IMeltingContainer.OreRateType;
import slimeknights.tconstruct.library.recipe.melting.MeltingRecipeBuilder;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.shared.TinkerMaterials;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.smeltery.block.component.SearedTankBlock.TankType;
import slimeknights.tconstruct.smeltery.data.Byproduct;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.world.TinkerHeadType;
import slimeknights.tconstruct.world.TinkerWorld;

import java.util.function.Consumer;
import java.util.function.Function;

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
        String castingFolder = "smeltery/casing/seared/";
        ItemCastingRecipeBuilder.retexturedBasinRecipe(ItemOutput.fromItem(TinkerSmeltery.smelteryController))
                .setCast(TinkerTags.Items.SMELTERY_BRICKS, true)
                .setFluidAndTime(TinkerFluids.moltenCopper, 144 * 4)
                .save(consumer, prefix(TinkerSmeltery.smelteryController, castingFolder));

        String meltingFolder = "smeltery/melting/seared/";
        MeltingRecipeBuilder.melting(NoContainerIngredient.of(TinkerSmeltery.searedLantern), TinkerFluids.searedStone, FluidValues.BRICK * 2, 1.0f)
                .addByproduct(TinkerFluids.moltenGlass.result(FluidValues.GLASS_PANE))
                .addByproduct(TinkerFluids.moltenIron.result(144 / 3))
                .save(consumer, location(meltingFolder + "lantern"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerSmeltery.smelteryController), TinkerFluids.moltenCopper, 144 * 4, 3.5f)
                .addByproduct(TinkerFluids.searedStone.result(FluidValues.BRICK * 4))
                .save(consumer, location("smeltery/melting/metal/copper/smeltery_controller"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerSmeltery.searedDrain, TinkerSmeltery.searedChute), TinkerFluids.moltenCopper, 144 * 2, 2.5f)
                .addByproduct(TinkerFluids.searedStone.result(FluidValues.BRICK * 4))
                .save(consumer, location("smeltery/melting/metal/copper/smeltery_io"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerSmeltery.searedDuct), TinkerFluids.moltenGold, 144 * 2, 2.5f)
                .addByproduct(TinkerFluids.searedStone.result(FluidValues.BRICK * 4))
                .save(consumer, location("smeltery/melting/metal/cobalt/seared_duct"));
    }

    private void addFoundryRecipes(Consumer<FinishedRecipe> consumer) {
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerSmeltery.scorchedDuct), TinkerFluids.moltenGold, 144 * 2, 2.5f)
                .addByproduct(TinkerFluids.scorchedStone.result(FluidValues.BRICK * 4))
                .save(consumer, location("smeltery/melting/metal/cobalt/scorched_duct"));
    }

    private void addCastingRecipes(Consumer<FinishedRecipe> consumer) {
        ItemCastingRecipeBuilder.tableRecipe(TinkerCommons.encyclopedia)
                .setFluidAndTime(TinkerFluids.moltenGold, 144)
                .setCast(Items.BOOK, true)
                .save(consumer, prefix(TinkerCommons.encyclopedia, "common/"));
        String folder = "smeltery/casting/";
        ContainerFillingRecipeBuilder.tableRecipe(TinkerSmeltery.copperCan, 144)
                .save(consumer, location(folder + "filling/copper_can"));
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


        String castFolder = "smeltery/casts/";

        this.castCreation(consumer, Tags.Items.INGOTS, TinkerSmeltery.ingotCast, castFolder);
        this.castCreation(consumer, Tags.Items.NUGGETS, TinkerSmeltery.nuggetCast, castFolder);
        this.castCreation(consumer, Tags.Items.GEMS, TinkerSmeltery.gemCast, castFolder);
        this.castCreation(consumer, Tags.Items.RODS, TinkerSmeltery.rodCast, castFolder);

        this.castCreation(withCondition(consumer, tagCondition("plates")), getItemTag(COMMON, "plates"), TinkerSmeltery.plateCast, castFolder);
        this.castCreation(withCondition(consumer, tagCondition("gears")),  getItemTag(COMMON, "gears"), TinkerSmeltery.gearCast, castFolder);
        this.castCreation(withCondition(consumer, tagCondition("coins")),  getItemTag(COMMON, "coins"), TinkerSmeltery.coinCast, castFolder);
        this.castCreation(withCondition(consumer, tagCondition("wires")),  getItemTag(COMMON, "wires"), TinkerSmeltery.wireCast, castFolder);

        String metalFolder = folder + "metal/";
        ItemCastingRecipeBuilder.tableRecipe(TinkerCommons.goldBars)
                .setFluidAndTime(TinkerFluids.moltenGold, 16 * 3)
                .save(consumer, location(metalFolder + "gold/bars"));
        ItemCastingRecipeBuilder.tableRecipe(Items.GOLDEN_APPLE)
                .setFluidAndTime(TinkerFluids.moltenGold, 144 * 8)
                .setCast(Items.APPLE, true)
                .save(consumer, location(metalFolder + "gold/apple"));
        ItemCastingRecipeBuilder.tableRecipe(Items.GLISTERING_MELON_SLICE)
                .setFluidAndTime(TinkerFluids.moltenGold, 16 * 8)
                .setCast(Items.MELON_SLICE, true)
                .save(consumer, location(metalFolder + "gold/melon"));
        ItemCastingRecipeBuilder.tableRecipe(Items.GOLDEN_CARROT)
                .setFluidAndTime(TinkerFluids.moltenGold, 16 * 8)
                .setCast(Items.CARROT, true)
                .save(consumer, location(metalFolder + "gold/carrot"));
        ItemCastingRecipeBuilder.tableRecipe(Items.CLOCK)
                .setFluidAndTime(TinkerFluids.moltenGold, 144 * 4)
                .setCast(Items.REDSTONE, true)
                .save(consumer, location(metalFolder + "gold/clock"));
        ItemCastingRecipeBuilder.tableRecipe(Blocks.IRON_BARS)
                .setFluidAndTime(TinkerFluids.moltenIron, 16 * 3)
                .save(consumer, location(metalFolder + "iron/bars"));
        ItemCastingRecipeBuilder.tableRecipe(Items.LANTERN)
                .setFluidAndTime(TinkerFluids.moltenIron, 16 * 8)
                .setCast(Blocks.TORCH, true)
                .save(consumer, location(metalFolder + "iron/lantern"));
        ItemCastingRecipeBuilder.tableRecipe(Items.SOUL_LANTERN)
                .setFluidAndTime(TinkerFluids.moltenIron, 16 * 8)
                .setCast(Blocks.SOUL_TORCH, true)
                .save(consumer, location(metalFolder + "iron/soul_lantern"));
        ItemCastingRecipeBuilder.tableRecipe(Items.COMPASS)
                .setFluidAndTime(TinkerFluids.moltenIron, 144 * 4)
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

        MeltingRecipeBuilder.melting(NoContainerIngredient.of(TinkerSmeltery.copperCan), TinkerFluids.moltenCopper, 144, 1.0f)
                .save(consumer, location(metalFolder + "copper/can"));

        MeltingRecipeBuilder.melting(Ingredient.of(Items.ACTIVATOR_RAIL, Items.DETECTOR_RAIL, Blocks.STONECUTTER, Blocks.PISTON, Blocks.STICKY_PISTON), TinkerFluids.moltenIron, 144)
                .save(consumer, location(metalFolder + "iron/ingot_1"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.HEAVY_WEIGHTED_PRESSURE_PLATE, Items.IRON_DOOR, Blocks.SMITHING_TABLE), TinkerFluids.moltenIron, 144 * 2)
                .save(consumer, location(metalFolder + "iron/ingot_2"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.BUCKET), TinkerFluids.moltenIron, 144 * 3)
                .save(consumer, location(metalFolder + "iron/bucket"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.COMPASS, Blocks.IRON_TRAPDOOR), TinkerFluids.moltenIron, 14 * 4)
                .save(consumer, location(metalFolder + "iron/ingot_4"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.BLAST_FURNACE, Blocks.HOPPER, Items.MINECART), TinkerFluids.moltenIron, 144 * 5)
                .save(consumer, location(metalFolder + "iron/ingot_5"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.CAULDRON), TinkerFluids.moltenIron, 144 * 7)
                .save(consumer, location(metalFolder + "iron/cauldron"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.CHAIN), TinkerFluids.moltenIron, 144 + 16 * 2)
                .save(consumer, location(metalFolder + "iron/chain"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.ANVIL, Blocks.CHIPPED_ANVIL, Blocks.DAMAGED_ANVIL), TinkerFluids.moltenIron, 144 * 4 + 1296 * 3)
                .save(consumer, location(metalFolder + "iron/anvil"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.IRON_BARS, Blocks.RAIL), TinkerFluids.moltenIron, 16 * 3)
                .save(consumer, location(metalFolder + "iron/nugget_3"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerCommons.ironPlatform), TinkerFluids.moltenIron, 16 * 10)
                .save(consumer, location(metalFolder + "iron/platform"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.TRIPWIRE_HOOK), TinkerFluids.moltenIron, 16 * 4)
                .save(consumer, location(metalFolder + "iron/tripwire"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.LANTERN, Blocks.SOUL_LANTERN), TinkerFluids.moltenIron, 16 * 8)
                .save(consumer, location(metalFolder + "iron/lantern"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerModifiers.ironReinforcement), TinkerFluids.moltenIron, 144)
                .save(consumer, location(metalFolder + "iron/reinforcement"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.CROSSBOW), TinkerFluids.moltenIron, 16 * 13) // tripwire hook is 4 nuggets, ingot is 9 nuggets
                .setDamagable(16)
                .save(consumer, location(metalFolder + "iron/crossbow"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.IRON_HORSE_ARMOR), TinkerFluids.moltenIron, 16 * 7)
                .save(consumer, location(metalFolder + "iron/horse_armor"));
        
        final int chainIron = 16 * 6;
        final int chainSteel = 16 * 3;
        MeltingRecipeBuilder.melting(Ingredient.of(Items.CHAINMAIL_HELMET), TinkerFluids.moltenIron, chainIron * 5)
                .addByproduct(TinkerFluids.moltenSteel.result(chainSteel * 5))
                .setDamagable(16)
                .save(consumer, location(metalFolder + "iron/chain_helmet"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.CHAINMAIL_CHESTPLATE), TinkerFluids.moltenIron, chainIron * 8)
                .addByproduct(TinkerFluids.moltenSteel.result(chainSteel * 8))
                .setDamagable(16)
                .save(consumer, location(metalFolder + "iron/chain_chestplate"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.CHAINMAIL_LEGGINGS), TinkerFluids.moltenIron, chainIron * 7)
                .addByproduct(TinkerFluids.moltenSteel.result(chainSteel * 7))
                .setDamagable(16)
                .save(consumer, location(metalFolder + "iron/chain_leggings"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.CHAINMAIL_BOOTS), TinkerFluids.moltenIron, chainIron * 4)
                .addByproduct(TinkerFluids.moltenSteel.result(chainSteel * 4))
                .setDamagable(16)
                .save(consumer, location(metalFolder + "iron/chain_boots"));

        MeltingRecipeBuilder.melting(Ingredient.of(TinkerTags.Items.GOLD_CASTS), TinkerFluids.moltenGold, 144)
                .save(consumer, location(metalFolder + "gold/cast"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.POWERED_RAIL), TinkerFluids.moltenGold, 144)
                .save(consumer, location(metalFolder + "gold/powered_rail"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE), TinkerFluids.moltenGold, 144 * 2)
                .save(consumer, location(metalFolder + "gold/pressure_plate"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.CLOCK), TinkerFluids.moltenGold, 144 * 4)
                .save(consumer, location(metalFolder + "gold/clock"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.GOLDEN_APPLE), TinkerFluids.moltenGold, 144 * 8)
                .save(consumer, location(metalFolder + "gold/apple"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.GLISTERING_MELON_SLICE, Items.GOLDEN_CARROT), TinkerFluids.moltenGold, 16 * 8)
                .save(consumer, location(metalFolder + "gold/produce"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerModifiers.goldReinforcement), TinkerFluids.moltenGold, 144)
                .save(consumer, location(metalFolder + "gold/reinforcement"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerCommons.goldBars), TinkerFluids.moltenGold, 16 * 3)
                .save(consumer, location(metalFolder + "gold/nugget_3"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerCommons.goldPlatform), TinkerFluids.moltenGold, 16 * 10)
                .save(consumer, location(metalFolder + "gold/platform"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.GOLDEN_HORSE_ARMOR), TinkerFluids.moltenGold, 144 * 7)
                .save(consumer, location(metalFolder + "gold/horse_armor"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.ENCHANTED_GOLDEN_APPLE), TinkerFluids.moltenGold, FluidValues.METAL_BLOCK * 8)
                .save(consumer, location(metalFolder + "gold/enchanted_apple"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.NETHER_GOLD_ORE), TinkerFluids.moltenGold, 144)
                .addByproduct(TinkerFluids.moltenCopper.result(144))
                .setOre(OreRateType.METAL)
                .save(consumer, location(metalFolder + "gold/nether_gold_ore"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.GILDED_BLACKSTONE), TinkerFluids.moltenGold, 16 * 3)
                .addByproduct(TinkerFluids.moltenCopper.result(144))
                .setOre(OreRateType.METAL)
                .save(consumer, location(metalFolder + "gold/gilded_blackstone"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.BELL), TinkerFluids.moltenGold, 144 * 4)
                .save(consumer, location(metalFolder + "gold/bell"));

        MeltingRecipeBuilder.melting(Ingredient.of(
                                Blocks.EXPOSED_COPPER, Blocks.WEATHERED_COPPER, Blocks.OXIDIZED_COPPER,
                                Blocks.WAXED_COPPER_BLOCK, Blocks.WAXED_EXPOSED_COPPER, Blocks.WAXED_WEATHERED_COPPER, Blocks.WAXED_OXIDIZED_COPPER),
                        TinkerFluids.moltenCopper, 1296)
                .save(consumer, location(metalFolder + "copper/decorative_block"));
        MeltingRecipeBuilder.melting(Ingredient.of(
                                Blocks.CUT_COPPER, Blocks.EXPOSED_CUT_COPPER, Blocks.WEATHERED_CUT_COPPER, Blocks.OXIDIZED_CUT_COPPER,
                                Blocks.CUT_COPPER_STAIRS, Blocks.EXPOSED_CUT_COPPER_STAIRS, Blocks.WEATHERED_CUT_COPPER_STAIRS, Blocks.OXIDIZED_CUT_COPPER_STAIRS,
                                Blocks.WAXED_CUT_COPPER, Blocks.WAXED_EXPOSED_CUT_COPPER, Blocks.WAXED_WEATHERED_CUT_COPPER, Blocks.WAXED_OXIDIZED_CUT_COPPER,
                                Blocks.WAXED_CUT_COPPER_STAIRS, Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS, Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS, Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS),
                        TinkerFluids.moltenCopper, 16 * 20)
                .save(consumer, location(metalFolder + "copper/cut_block"));
        MeltingRecipeBuilder.melting(Ingredient.of(
                                Blocks.CUT_COPPER_SLAB, Blocks.EXPOSED_CUT_COPPER_SLAB, Blocks.WEATHERED_CUT_COPPER_SLAB, Blocks.OXIDIZED_CUT_COPPER_SLAB,
                                Blocks.WAXED_CUT_COPPER_SLAB, Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB, Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB, Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB),
                        TinkerFluids.moltenCopper, 16 * 10)
                .save(consumer, location(metalFolder + "copper/cut_slab"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.LIGHTNING_ROD), TinkerFluids.moltenCopper, 144 * 3)
                .save(consumer, location(metalFolder + "copper/lightning_rod"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerTags.Items.COPPER_PLATFORMS), TinkerFluids.moltenCopper, 16 * 10)
                .save(consumer, location(metalFolder + "copper/platform"));

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

        MeltingRecipeBuilder.melting(Ingredient.of(TinkerModifiers.silkyCloth), TinkerFluids.moltenRoseGold, 144)
                .save(consumer, location(metalFolder + "rose_gold/silky_cloth"));

        MeltingRecipeBuilder.melting(Ingredient.of(TinkerModifiers.slimesteelReinforcement), TinkerFluids.moltenSlimesteel, 16 * 3)
                .addByproduct(TinkerFluids.moltenObsidian.result(FluidValues.GLASS_PANE))
                .save(consumer, location(metalFolder + "slimesteel/reinforcement"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerModifiers.cobaltReinforcement), TinkerFluids.moltenCobalt, 144)
                .save(consumer, location(metalFolder + "cobalt/reinforcement"));

        MeltingRecipeBuilder.melting(Ingredient.of(TinkerCommons.cobaltPlatform), TinkerFluids.moltenCobalt, 16 * 10)
                .save(consumer, location(metalFolder + "cobalt/platform"));
    }

    private void addAlloyRecipes(Consumer<FinishedRecipe> consumer) {
        String folder = "smeltery/alloys/";
        AlloyRecipeBuilder.alloy(TinkerFluids.moltenSlimesteel, 144 * 2)
                .addInput(TinkerFluids.moltenIron.ingredient(144))
                .addInput(TinkerFluids.skySlime.ingredient(FluidValues.SLIMEBALL))
                .addInput(TinkerFluids.searedStone.ingredient(FluidValues.BRICK))
                .save(consumer, prefix(TinkerFluids.moltenSlimesteel, folder));

        AlloyRecipeBuilder.alloy(TinkerFluids.moltenAmethystBronze, 144)
                .addInput(TinkerFluids.moltenCopper.ingredient(144))
                .addInput(TinkerFluids.moltenAmethyst.ingredient(FluidValues.GEM))
                .save(consumer, prefix(TinkerFluids.moltenAmethystBronze, folder));

        AlloyRecipeBuilder.alloy(TinkerFluids.moltenRoseGold, 144 * 2)
                .addInput(TinkerFluids.moltenCopper.ingredient(144))
                .addInput(TinkerFluids.moltenGold.ingredient(144))
                .save(consumer, prefix(TinkerFluids.moltenRoseGold, folder));

        AlloyRecipeBuilder.alloy(TinkerFluids.moltenPigIron, 144 * 2)
                .addInput(TinkerFluids.moltenIron.ingredient(144))
                .addInput(TinkerFluids.meatSoup.ingredient(FluidValues.SLIMEBALL * 2))
                .addInput(TinkerFluids.honey.ingredient(FluidValues.BOTTLE))
                .save(consumer, prefix(TinkerFluids.moltenPigIron, folder));

        AlloyRecipeBuilder.alloy(TinkerFluids.moltenCinderslime, 144 * 2)
                .addInput(TinkerFluids.moltenGold.ingredient(144))
                .addInput(TinkerFluids.ichor.ingredient(FluidValues.SLIMEBALL))
                .addInput(TinkerFluids.scorchedStone.ingredient(FluidValues.BRICK))
                .save(consumer, prefix(TinkerFluids.moltenCinderslime, folder));

        AlloyRecipeBuilder.alloy(TinkerFluids.moltenQueensSlime, 144 * 2)
                .addInput(TinkerFluids.moltenCobalt.ingredient(144))
                .addInput(TinkerFluids.moltenGold.ingredient(144))
                .addInput(TinkerFluids.magma.ingredient(FluidValues.SLIMEBALL))
                .save(consumer, prefix(TinkerFluids.moltenQueensSlime, folder));

        AlloyRecipeBuilder.alloy(TinkerFluids.moltenManyullyn, 144 * 4)
                .addInput(TinkerFluids.moltenCobalt.ingredient(144 * 3))
                .addInput(TinkerFluids.moltenDebris.ingredient(144))
                .save(consumer, prefix(TinkerFluids.moltenManyullyn, folder));

        AlloyRecipeBuilder.alloy(TinkerFluids.moltenHepatizon, 144 * 2)
                .addInput(TinkerFluids.moltenCopper.ingredient(144 * 2))
                .addInput(TinkerFluids.moltenCobalt.ingredient(144))
                .addInput(TinkerFluids.moltenQuartz.ingredient(FluidValues.GEM * 4))
                .save(consumer, prefix(TinkerFluids.moltenHepatizon, folder));

        ConditionalRecipe.builder()
                .addCondition(ConfigEnabledCondition.CHEAPER_NETHERITE_ALLOY)
                .addRecipe(
                        AlloyRecipeBuilder.alloy(TinkerFluids.moltenNetherite, 16)
                                .addInput(TinkerFluids.moltenDebris.ingredient(16 * 4))
                                .addInput(TinkerFluids.moltenGold.ingredient(16 * 2))::save)
                .addCondition(TrueCondition.INSTANCE) // fallback
                .addRecipe(
                        AlloyRecipeBuilder.alloy(TinkerFluids.moltenNetherite, 16)
                                .addInput(TinkerFluids.moltenDebris.ingredient(16 * 4))
                                .addInput(TinkerFluids.moltenGold.ingredient(16 * 4))::save)
                .build(consumer, prefix(TinkerFluids.moltenNetherite, folder));

        Consumer<FinishedRecipe> wrapped;
        wrapped = withCondition(consumer, tagCondition("ingots/tin"));
        AlloyRecipeBuilder.alloy(TinkerFluids.moltenBronze, 144 * 4)
                .addInput(TinkerFluids.moltenCopper.ingredient(144 * 3))
                .addInput(TinkerFluids.moltenTin.ingredient(144))
                .save(wrapped, prefix(TinkerFluids.moltenBronze, folder));

        wrapped = withCondition(consumer, tagCondition("ingots/zinc"));
        AlloyRecipeBuilder.alloy(TinkerFluids.moltenBrass, 144 * 2)
                .addInput(TinkerFluids.moltenCopper.ingredient(144))
                .addInput(TinkerFluids.moltenZinc.ingredient(144))
                .save(wrapped, prefix(TinkerFluids.moltenBrass, folder));

        wrapped = withCondition(consumer, tagCondition("ingots/silver"));
        AlloyRecipeBuilder.alloy(TinkerFluids.moltenElectrum, 144 * 2)
                .addInput(TinkerFluids.moltenGold.ingredient(144))
                .addInput(TinkerFluids.moltenSilver.ingredient(144))
                .save(wrapped, prefix(TinkerFluids.moltenElectrum, folder));

        wrapped = withCondition(consumer, tagCondition("ingots/nickel"));
        AlloyRecipeBuilder.alloy(TinkerFluids.moltenInvar, 144 * 3)
                .addInput(TinkerFluids.moltenIron.ingredient(144 * 2))
                .addInput(TinkerFluids.moltenNickel.ingredient(144))
                .save(wrapped, prefix(TinkerFluids.moltenInvar, folder));

        wrapped = withCondition(consumer, tagCondition("ingots/nickel"));
        AlloyRecipeBuilder.alloy(TinkerFluids.moltenConstantan, 144 * 2)
                .addInput(TinkerFluids.moltenCopper.ingredient(144))
                .addInput(TinkerFluids.moltenNickel.ingredient(144))
                .save(wrapped, prefix(TinkerFluids.moltenConstantan, folder));

        wrapped = withCondition(consumer, tagCondition("ingots/pewter"), tagCondition("ingots/lead"));
        ConditionalRecipe.builder()
                .addCondition(tagCondition("ingots/tin"))
                .addRecipe(
                        AlloyRecipeBuilder.alloy(TinkerFluids.moltenPewter, 144 * 3)
                                .addInput(TinkerFluids.moltenTin.ingredient(144 * 2))
                                .addInput(TinkerFluids.moltenLead.ingredient(144))::save)
                .addCondition(TrueCondition.INSTANCE)
                .addRecipe(
                        AlloyRecipeBuilder.alloy(TinkerFluids.moltenPewter, 144 * 2)
                                .addInput(TinkerFluids.moltenIron.ingredient(144))
                                .addInput(TinkerFluids.moltenLead.ingredient(144))::save)
                .build(wrapped, prefix(TinkerFluids.moltenPewter, folder));

        Function<String, ICondition> fluidTagLoaded = name -> new TagFilledCondition<>(Registries.FLUID, commonResource(name));
        Function<String,TagKey<Fluid>> fluidTag = name -> FluidTags.create(commonResource(name));

        wrapped = withCondition(consumer, tagCondition("ingots/enderium"), tagCondition("ingots/lead"));
        AlloyRecipeBuilder.alloy(TinkerFluids.moltenEnderium, 144 * 2)
                .addInput(TinkerFluids.moltenLead.ingredient(144 * 3))
                .addInput(TinkerFluids.moltenDiamond.ingredient(FluidValues.GEM))
                .addInput(TinkerFluids.moltenEnder.ingredient(FluidValues.SLIMEBALL * 2))
                .save(wrapped, prefix(TinkerFluids.moltenEnderium, folder));

        wrapped = withCondition(consumer, tagCondition("ingots/lumium"), tagCondition("ingots/tin"), tagCondition("ingots/silver"), fluidTagLoaded.apply("glowstone"));
        AlloyRecipeBuilder.alloy(TinkerFluids.moltenLumium, 144 * 4)
                .addInput(TinkerFluids.moltenTin.ingredient(144 * 3))
                .addInput(TinkerFluids.moltenSilver.ingredient(144))
                .addInput(FluidIngredient.of(fluidTag.apply("glowstone"), FluidValues.SLIMEBALL * 2))
                .save(wrapped, prefix(TinkerFluids.moltenLumium, folder));

        wrapped = withCondition(consumer, tagCondition("ingots/signalum"), tagCondition("ingots/copper"), tagCondition("ingots/silver"), fluidTagLoaded.apply("redstone"));
        AlloyRecipeBuilder.alloy(TinkerFluids.moltenSignalum, 144 * 4)
                .addInput(TinkerFluids.moltenCopper.ingredient(144 * 3))
                .addInput(TinkerFluids.moltenSilver.ingredient(144))
                .addInput(FluidIngredient.of(fluidTag.apply("redstone"), 400))
                .save(wrapped, prefix(TinkerFluids.moltenSignalum, folder));


        wrapped = withCondition(consumer, tagCondition("ingots/refined_obsidian"), tagCondition("ingots/osmium"));
        AlloyRecipeBuilder.alloy(TinkerFluids.moltenRefinedObsidian, 144)
                .addInput(TinkerFluids.moltenObsidian.ingredient(FluidValues.GLASS_PANE))
                .addInput(TinkerFluids.moltenDiamond.ingredient(FluidValues.GEM))
                .addInput(TinkerFluids.moltenOsmium.ingredient(144))
                .save(wrapped, prefix(TinkerFluids.moltenRefinedObsidian, folder));
    }
    
    private void addEntityMeltingRecipes(Consumer<FinishedRecipe> consumer) {
        String folder = "smeltery/entity_melting/";
        String headFolder = "smeltery/entity_melting/heads/";
        EntityMeltingRecipeBuilder.melting(EntityIngredient.of(EntityType.ZOMBIE, EntityType.HUSK, EntityType.ZOMBIE_HORSE), TinkerFluids.moltenIron.result(16), 4)
                .save(consumer, location(folder + "zombie"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.ZOMBIE_HEAD, TinkerWorld.heads.get(TinkerHeadType.HUSK)), TinkerFluids.moltenIron, 144)
                .save(consumer, location(headFolder + "zombie"));
        
        EntityMeltingRecipeBuilder.melting(EntityIngredient.of(EntityType.DROWNED), TinkerFluids.moltenCopper.result(16), 4)
                .save(consumer, location(folder + "drowned"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerWorld.heads.get(TinkerHeadType.DROWNED)), TinkerFluids.moltenCopper, 144)
                .save(consumer, location(headFolder + "drowned"));
        
        EntityMeltingRecipeBuilder.melting(EntityIngredient.of(EntityType.PIGLIN, EntityType.PIGLIN_BRUTE, EntityType.ZOMBIFIED_PIGLIN), TinkerFluids.moltenGold.result(16), 4)
                .save(consumer, location(folder + "piglin"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.PIGLIN_HEAD, TinkerWorld.heads.get(TinkerHeadType.PIGLIN_BRUTE), TinkerWorld.heads.get(TinkerHeadType.ZOMBIFIED_PIGLIN)), TinkerFluids.moltenGold, 144)
                .save(consumer, location(headFolder + "piglin"));

        EntityMeltingRecipeBuilder.melting(EntityIngredient.of(EntityType.IRON_GOLEM), TinkerFluids.moltenIron.result(16), 4)
                .save(consumer, location(folder + "iron_golem"));
    }

    private void addTagRecipes(Consumer<FinishedRecipe> consumer) {
        ToolItemMelting EXCAVATOR = new ToolItemMelting(11, "tools_complement", "excavator");
        ToolItemMelting HAMMER = new ToolItemMelting(13, "tools_complement", "hammer");
        CommonRecipe[] TOOLS_COMPLEMENT = { SmelteryRecipeBuilder.SHOVEL_PLUS, SWORD, AXES, EXCAVATOR, HAMMER };
        CommonRecipe[] MEKANISM_ARMOR = {
                SmelteryRecipeBuilder.HELMET, SmelteryRecipeBuilder.CHESTPLATE, SmelteryRecipeBuilder.LEGGINGS_PLUS, SmelteryRecipeBuilder.BOOTS,
                new ToolItemMelting(6, "mekanism", "shield")
        };

        metal(consumer, TinkerFluids.moltenCopper).ore(GMCByproduct.SMALL_GOLD   ).metal().dust().plate().gear().coin().sheetmetal().geore().oreberry().wire().common(SWORD, AXES, EXCAVATOR, HAMMER).common(ARMOR).toolCostMelting(1, "shovel", false);
        metal(consumer, TinkerFluids.moltenIron  ).ore(GMCByproduct.STEEL        ).metal().dust().plate().gear().coin().sheetmetal().geore().oreberry().minecraftTools().toolCostMelting(11, "tools_costing_11").common(HAMMER).rod();
        metal(consumer, TinkerFluids.moltenCobalt).ore(Byproduct.SMALL_DIAMOND).metal().dust();
        metal(consumer, TinkerFluids.moltenSteel ).metal().dust().plate().gear().coin().sheetmetal().common(TOOLS).common(MEKANISM_ARMOR).wire().rod().toolItemMelting(11, "railcraft", "spike_maul");
        metal(consumer, TinkerFluids.moltenGold).metal().ore(GMCByproduct.COBALT).dust().plate().gear().coin().sheetmetal().geore().oreberry().minecraftTools("golden").common(EXCAVATOR, HAMMER).rawOre().singularOre(2).denseOre(6);
        metal(consumer, TinkerFluids.moltenNetherite).metal().dust().plate().gear().coin();

        metal(consumer, TinkerFluids.moltenSlimesteel    ).metal();
        metal(consumer, TinkerFluids.moltenAmethystBronze).metal().dust();
        metal(consumer, TinkerFluids.moltenRoseGold      ).metal().dust().plate().coin().gear();
        metal(consumer, TinkerFluids.moltenPigIron       ).metal();

        metal(consumer, TinkerFluids.moltenManyullyn  ).metal();
        metal(consumer, TinkerFluids.moltenHepatizon  ).metal();
        metal(consumer, TinkerFluids.moltenCinderslime).metal();
        metal(consumer, TinkerFluids.moltenQueensSlime).metal();

        metal(consumer, TinkerFluids.moltenTin     ).ore(GMCByproduct.NICKEL, GMCByproduct.COPPER).optional().metal().dust().oreberry().plate().gear().coin().common(TOOLS_COMPLEMENT).common(ARMOR);
        metal(consumer, TinkerFluids.moltenAluminum).ore(GMCByproduct.IRON                    ).optional().metal().dust().oreberry().plate().gear().coin().sheetmetal().wire().rod();
        metal(consumer, TinkerFluids.moltenLead    ).ore(GMCByproduct.SILVER, GMCByproduct.GOLD  ).optional().metal().dust().oreberry().plate().gear().coin().common(TOOLS_COMPLEMENT).common(ARMOR).sheetmetal().wire();
        metal(consumer, TinkerFluids.moltenSilver  ).ore(GMCByproduct.LEAD, GMCByproduct.GOLD    ).optional().metal().dust().oreberry().plate().gear().coin().common(TOOLS_COMPLEMENT).common(ARMOR).sheetmetal();
        metal(consumer, TinkerFluids.moltenNickel  ).ore(GMCByproduct.PLATINUM, GMCByproduct.IRON).optional().metal().dust().oreberry().plate().gear().coin().common(TOOLS_COMPLEMENT).common(ARMOR).sheetmetal();
        metal(consumer, TinkerFluids.moltenZinc    ).ore(GMCByproduct.TIN, GMCByproduct.COPPER   ).optional().metal().dust().oreberry().plate().gear().geore();
        metal(consumer, TinkerFluids.moltenPlatinum).ore(GMCByproduct.GOLD                    ).optional().metal().dust();
        metal(consumer, TinkerFluids.moltenTungsten).ore(GMCByproduct.PLATINUM, GMCByproduct.GOLD).optional().metal().dust();
        metal(consumer, TinkerFluids.moltenOsmium  ).ore(GMCByproduct.IRON                    ).optional().metal().dust().oreberry().common(TOOLS).common(MEKANISM_ARMOR);
        metal(consumer, TinkerFluids.moltenUranium ).ore(GMCByproduct.LEAD, GMCByproduct.COPPER  ).optional().metal().dust().oreberry().plate().gear().coin().sheetmetal();

        metal(consumer, TinkerFluids.moltenBronze    ).optional().metal().dust().plate().gear().coin().common(TOOLS_COMPLEMENT).common(MEKANISM_ARMOR);
        metal(consumer, TinkerFluids.moltenBrass     ).optional().metal().dust().plate().gear();
        metal(consumer, TinkerFluids.moltenElectrum  ).optional().metal().dust().plate().gear().coin().common(TOOLS_COMPLEMENT).common(ARMOR).sheetmetal().wire();
        metal(consumer, TinkerFluids.moltenInvar     ).optional().metal().dust().plate().gear().coin().common(TOOLS_COMPLEMENT).common(ARMOR);
        metal(consumer, TinkerFluids.moltenConstantan).optional().metal().dust().plate().gear().coin().common(TOOLS_COMPLEMENT).common(ARMOR).sheetmetal();
        metal(consumer, TinkerFluids.moltenPewter    ).optional().metal().dust();

        metal(consumer, TinkerFluids.moltenEnderium).optional().metal().dust().plate().gear().coin();
        metal(consumer, TinkerFluids.moltenLumium  ).optional().metal().dust().plate().gear().coin();
        metal(consumer, TinkerFluids.moltenSignalum).optional().metal().dust().plate().gear().coin();
        metal(consumer, TinkerFluids.moltenRefinedObsidian ).optional().metal().common(TOOLS).common(MEKANISM_ARMOR);
        metal(consumer, TinkerFluids.moltenRefinedGlowstone).optional().metal().common(TOOLS).common(MEKANISM_ARMOR);

        TagKey<Fluid> dawnstone = getFluidTag(COMMON, "molten_dawnstone");
        metal(withCondition(consumer, new TagFilledCondition<>(dawnstone)), "dawnstone", dawnstone).temperature(900).optional().metal().plate();
    }

    private void addCompatRecipes(Consumer<FinishedRecipe> consumer) {
        String folder = "compat/";
        ItemOutput andesiteAlloy = ItemNameOutput.fromName(new ResourceLocation("create", "andesite_alloy"));
        Consumer<FinishedRecipe> createConsumer = withCondition(consumer, new ModLoadedCondition("create"));
        ItemCastingRecipeBuilder.basinRecipe(andesiteAlloy)
                .setCast(Blocks.ANDESITE, true)
                .setFluidAndTime(TinkerFluids.moltenIron, 16)
                .save(createConsumer, location(folder + "create/andesite_alloy_iron"));
        ItemCastingRecipeBuilder.basinRecipe(andesiteAlloy)
                .setCast(Blocks.ANDESITE, true)
                .setFluidAndTime(TinkerFluids.moltenZinc, 16)
                .save(createConsumer, location(folder + "create/andesite_alloy_zinc"));

        Consumer<FinishedRecipe> wrapped = withCondition(consumer, tagCondition("ingots/refined_glowstone"), tagCondition("ingots/osmium"));
        ItemCastingRecipeBuilder.tableRecipe(ItemOutput.fromTag(getItemTag(COMMON, "ingots/refined_glowstone")))
                .setCast(Tags.Items.DUSTS_GLOWSTONE, true)
                .setFluidAndTime(TinkerFluids.moltenOsmium, 144)
                .save(wrapped, location(folder + "refined_glowstone_ingot"));
        wrapped = withCondition(consumer, tagCondition("ingots/refined_obsidian"), tagCondition("ingots/osmium"));
        ItemCastingRecipeBuilder.tableRecipe(ItemOutput.fromTag(getItemTag(COMMON, "ingots/refined_obsidian")))
                .setCast(getItemTag(COMMON, "dusts/refined_obsidian"), true)
                .setFluidAndTime(TinkerFluids.moltenOsmium, 144)
                .save(wrapped, location(folder + "refined_obsidian_ingot"));
        ItemCastingRecipeBuilder.tableRecipe(TinkerMaterials.necroniumBone)
                .setFluidAndTime(TinkerFluids.moltenUranium, 144)
                .setCast(TinkerTags.Items.WITHER_BONES, true)
                .save(withCondition(consumer, tagCondition("ingots/uranium")), location(folder + "necronium_bone"));
    }

    @Override
    public void castCreation(Consumer<FinishedRecipe> consumer, Ingredient input, CastItemObject cast, String folder, String name) {
        ItemCastingRecipeBuilder.tableRecipe(cast)
                .setFluidAndTime(TinkerFluids.moltenGold, 144)
                .setCast(input, true)
                .setSwitchSlots()
                .save(consumer, location(folder + "gold/" + name));
    }

    public SmelteryRecipeBuilder metal(Consumer<FinishedRecipe> consumer, FluidObject<?> fluid) {
        return molten(consumer, fluid).castingFolder("smeltery/casting/metal").meltingFolder("smeltery/melting/metal");
    }

    public SmelteryRecipeBuilder metal(Consumer<FinishedRecipe> consumer, String name, TagKey<Fluid> fluid) {
        return SmelteryRecipeBuilder.fluid(consumer, location(name), fluid).castingFolder("smeltery/casting/metal").meltingFolder("smeltery/melting/metal");
    }

    @Override
    public String getModId() {
        return TConstruct.MOD_ID;
    }
}
