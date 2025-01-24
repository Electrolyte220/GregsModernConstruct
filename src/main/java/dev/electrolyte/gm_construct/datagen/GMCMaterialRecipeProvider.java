package dev.electrolyte.gm_construct.datagen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.*;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.recipe.data.IRecipeHelper;
import slimeknights.mantle.recipe.data.ItemNameOutput;
import slimeknights.mantle.recipe.helper.FluidOutput;
import slimeknights.mantle.recipe.helper.ItemOutput;
import slimeknights.mantle.recipe.helper.TagEmptyCondition;
import slimeknights.mantle.recipe.ingredient.EntityIngredient;
import slimeknights.mantle.recipe.ingredient.FluidIngredient;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.common.json.ConfigEnabledCondition;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.library.data.recipe.IByproduct;
import slimeknights.tconstruct.library.data.recipe.ICastCreationHelper;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.ISmelteryRecipeHelper;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.alloying.AlloyRecipeBuilder;
import slimeknights.tconstruct.library.recipe.casting.ItemCastingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.casting.container.ContainerFillingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.entitymelting.EntityMeltingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.ingredient.MaterialIngredient;
import slimeknights.tconstruct.library.recipe.ingredient.NoContainerIngredient;
import slimeknights.tconstruct.library.recipe.melting.IMeltingContainer.OreRateType;
import slimeknights.tconstruct.library.recipe.melting.MeltingRecipeBuilder;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.shared.TinkerMaterials;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.smeltery.block.component.SearedTankBlock.TankType;
import slimeknights.tconstruct.smeltery.data.Byproduct;
import slimeknights.tconstruct.smeltery.data.SmelteryCompat;
import slimeknights.tconstruct.tables.TinkerTables;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.TinkerToolParts;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import slimeknights.tconstruct.world.TinkerHeadType;
import slimeknights.tconstruct.world.TinkerWorld;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

import static slimeknights.mantle.Mantle.COMMON;
import static slimeknights.mantle.Mantle.commonResource;
import static slimeknights.tconstruct.library.recipe.melting.IMeltingRecipe.getTemperature;

public class GMCMaterialRecipeProvider extends RecipeProvider implements IMaterialRecipeHelper, IConditionBuilder, IRecipeHelper, ICastCreationHelper, ISmelteryRecipeHelper {
    public GMCMaterialRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ItemCastingRecipeBuilder.tableRecipe(TinkerCommons.encyclopedia)
                .setFluidAndTime(TinkerFluids.moltenGold, 144)
                .setCast(Items.BOOK, true)
                .save(consumer, prefix(TinkerCommons.encyclopedia, "common/"));
        String folder = "tools/materials/";
        materialMeltingCasting(consumer, MaterialIds.iron, TinkerFluids.moltenIron, folder);
        materialMeltingCasting(consumer, MaterialIds.copper, TinkerFluids.moltenCopper, folder);

        materialMeltingCasting(consumer, MaterialIds.slimesteel, TinkerFluids.moltenSlimesteel, folder);
        materialMeltingCasting(consumer, MaterialIds.amethystBronze, TinkerFluids.moltenAmethystBronze, folder);
        materialMeltingCasting(consumer, MaterialIds.roseGold, TinkerFluids.moltenRoseGold, folder);
        materialMeltingCasting(consumer, MaterialIds.pigIron, TinkerFluids.moltenPigIron, folder);
        materialMeltingCasting(consumer, MaterialIds.cobalt, TinkerFluids.moltenCobalt, folder);

        materialComposite(consumer, MaterialIds.string, MaterialIds.roseGold, TinkerFluids.moltenRoseGold, 144, folder);

        materialMeltingCasting(consumer, MaterialIds.queensSlime, TinkerFluids.moltenQueensSlime, folder);
        materialMeltingCasting(consumer, MaterialIds.hepatizon, TinkerFluids.moltenHepatizon, folder);
        materialMeltingCasting(consumer, MaterialIds.manyullyn, TinkerFluids.moltenManyullyn, folder);
        materialMeltingComposite(consumer, MaterialIds.leather, MaterialIds.ancientHide, TinkerFluids.moltenDebris, 144, folder);

        compatMeltingCasting(consumer, MaterialIds.osmium, TinkerFluids.moltenOsmium, folder);
        compatMeltingCasting(consumer, MaterialIds.tungsten, TinkerFluids.moltenTungsten, folder);
        compatMeltingCasting(consumer, MaterialIds.platinum, TinkerFluids.moltenPlatinum, folder);
        compatMeltingCasting(consumer, MaterialIds.silver, TinkerFluids.moltenSilver, folder);
        compatMeltingCasting(consumer, MaterialIds.lead, TinkerFluids.moltenLead, folder);
        compatMeltingCasting(consumer, MaterialIds.aluminum, TinkerFluids.moltenAluminum, folder);
        materialComposite(withCondition(consumer, tagCondition("ingots/aluminum")), MaterialIds.rock, MaterialIds.whitestoneAluminum, TinkerFluids.moltenAluminum,144, folder, "whitestone_from_aluminum");
        materialComposite(withCondition(consumer, tagCondition("ingots/tin")), MaterialIds.rock, MaterialIds.whitestoneTin, TinkerFluids.moltenTin,144, folder, "whitestone_from_tin");
        materialComposite(withCondition(consumer, tagCondition("ingots/zinc")), MaterialIds.rock, MaterialIds.whitestoneZinc, TinkerFluids.moltenZinc, 144, folder, "whitestone_from_zinc");

        compatMeltingCasting(consumer, MaterialIds.steel, TinkerFluids.moltenSteel, folder);
        compatMeltingCasting(consumer, MaterialIds.constantan, TinkerFluids.moltenConstantan, "nickel", folder);
        compatMeltingCasting(consumer, MaterialIds.invar, TinkerFluids.moltenInvar,"nickel", folder);
        compatMeltingCasting(consumer, MaterialIds.electrum, TinkerFluids.moltenElectrum,"silver", folder);
        compatMeltingCasting(consumer, MaterialIds.bronze, TinkerFluids.moltenBronze,"tin", folder);
        materialMeltingComposite(withCondition(consumer, tagCondition("ingots/uranium")), MaterialIds.necroticBone, MaterialIds.necronium, TinkerFluids.moltenUranium, 144, folder);
        materialMeltingComposite(withCondition(consumer, new OrCondition(tagCondition("ingots/brass"), tagCondition("ingots/zinc"))),
                MaterialIds.slimewood, MaterialIds.platedSlimewood, TinkerFluids.moltenBrass, 144, folder);

        materialMeltingCasting(consumer, MaterialIds.gold, TinkerFluids.moltenGold, folder);

        folder = "smeltery/casting/seared/";
        ItemCastingRecipeBuilder.retexturedBasinRecipe(ItemOutput.fromItem(TinkerSmeltery.smelteryController))
                .setCast(TinkerTags.Items.SMELTERY_BRICKS, true)
                .setFluidAndTime(TinkerFluids.moltenCopper, 144 * 4)
                .save(consumer, prefix(TinkerSmeltery.smelteryController, folder));

        folder = "smeltery/melting/seared/";
        MeltingRecipeBuilder.melting(NoContainerIngredient.of(TinkerSmeltery.searedLantern), TinkerFluids.searedStone, FluidValues.BRICK * 2, 1.0f)
                .addByproduct(TinkerFluids.moltenGlass.result(FluidValues.GLASS_PANE))
                .addByproduct(TinkerFluids.moltenIron.result(16 * 3))
                .save(consumer, location(folder + "lantern"));

        MeltingRecipeBuilder.melting(Ingredient.of(TinkerSmeltery.smelteryController), TinkerFluids.moltenCopper, 144 * 4, 3.5f)
                .addByproduct(TinkerFluids.searedStone.result(FluidValues.BRICK * 4))
                .save(consumer, location("smeltery/melting/metal/copper/smeltery_controller"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerSmeltery.searedDrain, TinkerSmeltery.searedChute), TinkerFluids.moltenCopper, 144 * 2, 2.5f)
                .addByproduct(TinkerFluids.searedStone.result(FluidValues.BRICK * 4))
                .save(consumer, location("smeltery/melting/metal/copper/smeltery_io"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerSmeltery.searedDuct), TinkerFluids.moltenGold, 144 * 2, 2.5f)
                .addByproduct(TinkerFluids.searedStone.result(FluidValues.BRICK * 4))
                .save(consumer, location("smeltery/melting/metal/cobalt/seared_duct"));

        MeltingRecipeBuilder.melting(Ingredient.of(TinkerSmeltery.scorchedDuct), TinkerFluids.moltenGold, 144 * 2, 2.5f)
                .addByproduct(TinkerFluids.scorchedStone.result(FluidValues.BRICK * 4))
                .save(consumer, location("smeltery/melting/metal/cobalt/scorched_duct"));

        folder = "smeltery/casting/";
        ContainerFillingRecipeBuilder.tableRecipe(TinkerSmeltery.copperCan, 144)
                .save(consumer, location(folder + "filling/copper_can"));

        ContainerFillingRecipeBuilder.basinRecipe(TinkerSmeltery.searedTank.get(TankType.INGOT_TANK), 144)
                .save(consumer, location(folder + "filling/seared_ingot_tank"));
        ContainerFillingRecipeBuilder.basinRecipe(TinkerSmeltery.searedTank.get(TankType.INGOT_GAUGE), 144)
                .save(consumer, location(folder + "filling/seared_ingot_gauge"));

        ContainerFillingRecipeBuilder.basinRecipe(TinkerSmeltery.scorchedTank.get(TankType.INGOT_TANK), 144)
                .save(consumer, location(folder + "filling/scorched_ingot_tank"));
        ContainerFillingRecipeBuilder.basinRecipe(TinkerSmeltery.scorchedTank.get(TankType.INGOT_GAUGE), 144)
                .save(consumer, location(folder + "filling/scorched_ingot_gauge"));

        folder = "smeltery/casts/";
        this.castCreation(consumer, Tags.Items.INGOTS, TinkerSmeltery.ingotCast, folder);
        this.castCreation(consumer, Tags.Items.NUGGETS, TinkerSmeltery.nuggetCast, folder);
        this.castCreation(consumer, Tags.Items.GEMS, TinkerSmeltery.gemCast, folder);
        this.castCreation(consumer, Tags.Items.RODS, TinkerSmeltery.rodCast, folder);
        
        this.castCreation(withCondition(consumer, tagCondition("plates")), getItemTag(COMMON, "plates"), TinkerSmeltery.plateCast, folder);
        this.castCreation(withCondition(consumer, tagCondition("gears")),  getItemTag(COMMON, "gears"), TinkerSmeltery.gearCast, folder);
        this.castCreation(withCondition(consumer, tagCondition("coins")),  getItemTag(COMMON, "coins"), TinkerSmeltery.coinCast, folder);
        this.castCreation(withCondition(consumer, tagCondition("wires")),  getItemTag(COMMON, "wires"), TinkerSmeltery.wireCast, folder);

        this.castCreation(consumer, TinkerToolParts.repairKit, TinkerSmeltery.repairKitCast, folder);

        this.castCreation(consumer, TinkerToolParts.pickHead,     TinkerSmeltery.pickHeadCast,     folder);
        this.castCreation(consumer, TinkerToolParts.hammerHead,   TinkerSmeltery.hammerHeadCast,   folder);
        this.castCreation(consumer, TinkerToolParts.smallAxeHead, TinkerSmeltery.smallAxeHeadCast, folder);
        this.castCreation(consumer, TinkerToolParts.broadAxeHead, TinkerSmeltery.broadAxeHeadCast, folder);
        this.castCreation(consumer, TinkerToolParts.smallBlade,   TinkerSmeltery.smallBladeCast,   folder);
        this.castCreation(consumer, TinkerToolParts.broadBlade,   TinkerSmeltery.broadBladeCast,   folder);
        this.castCreation(consumer, TinkerToolParts.bowLimb,      TinkerSmeltery.bowLimbCast,      folder);
        this.castCreation(consumer, TinkerToolParts.bowGrip,      TinkerSmeltery.bowGripCast,      folder);

        this.castCreation(consumer, TinkerToolParts.toolBinding,  TinkerSmeltery.toolBindingCast,  folder);
        this.castCreation(consumer, TinkerToolParts.toughBinding, TinkerSmeltery.toughBindingCast, folder);
        this.castCreation(consumer, TinkerToolParts.adzeHead,     TinkerSmeltery.adzeHeadCast,     folder);
        this.castCreation(consumer, TinkerToolParts.largePlate,   TinkerSmeltery.largePlateCast,   folder);
        this.castCreation(consumer, TinkerToolParts.toolHandle,   TinkerSmeltery.toolHandleCast,   folder);
        this.castCreation(consumer, TinkerToolParts.toughHandle,  TinkerSmeltery.toughHandleCast,  folder);

        this.castCreation(consumer, CompoundIngredient.of(Ingredient.of(TinkerSmeltery.dummyPlating.get(ArmorItem.Type.HELMET), TinkerToolParts.plating.get(ArmorItem.Type.HELMET))),         TinkerSmeltery.helmetPlatingCast,     folder, id(TinkerToolParts.plating.get(ArmorItem.Type.HELMET)).getPath());
        this.castCreation(consumer, CompoundIngredient.of(Ingredient.of(TinkerSmeltery.dummyPlating.get(ArmorItem.Type.CHESTPLATE), TinkerToolParts.plating.get(ArmorItem.Type.CHESTPLATE))), TinkerSmeltery.chestplatePlatingCast, folder, id(TinkerToolParts.plating.get(ArmorItem.Type.CHESTPLATE)).getPath());
        this.castCreation(consumer, CompoundIngredient.of(Ingredient.of(TinkerSmeltery.dummyPlating.get(ArmorItem.Type.LEGGINGS), TinkerToolParts.plating.get(ArmorItem.Type.LEGGINGS))),     TinkerSmeltery.leggingsPlatingCast,   folder, id(TinkerToolParts.plating.get(ArmorItem.Type.LEGGINGS)).getPath());
        this.castCreation(consumer, CompoundIngredient.of(Ingredient.of(TinkerSmeltery.dummyPlating.get(ArmorItem.Type.BOOTS), TinkerToolParts.plating.get(ArmorItem.Type.BOOTS))),           TinkerSmeltery.bootsPlatingCast,      folder, id(TinkerToolParts.plating.get(ArmorItem.Type.BOOTS)).getPath());
        this.castCreation(consumer, TinkerToolParts.maille, TinkerSmeltery.mailleCast, folder);


        folder = "smeltery/casting/metal/";
        ItemCastingRecipeBuilder.tableRecipe(TinkerCommons.goldBars)
                .setFluidAndTime(TinkerFluids.moltenGold, 16 * 3)
                .save(consumer, location(folder + "gold/bars"));
        ItemCastingRecipeBuilder.tableRecipe(Items.GOLDEN_APPLE)
                .setFluidAndTime(TinkerFluids.moltenGold, 144 * 8)
                .setCast(Items.APPLE, true)
                .save(consumer, location(folder + "gold/apple"));
        ItemCastingRecipeBuilder.tableRecipe(Items.GLISTERING_MELON_SLICE)
                .setFluidAndTime(TinkerFluids.moltenGold, 16 * 8)
                .setCast(Items.MELON_SLICE, true)
                .save(consumer, location(folder + "gold/melon"));
        ItemCastingRecipeBuilder.tableRecipe(Items.GOLDEN_CARROT)
                .setFluidAndTime(TinkerFluids.moltenGold, 16 * 8)
                .setCast(Items.CARROT, true)
                .save(consumer, location(folder + "gold/carrot"));
        ItemCastingRecipeBuilder.tableRecipe(Items.CLOCK)
                .setFluidAndTime(TinkerFluids.moltenGold, 144 * 4)
                .setCast(Items.REDSTONE, true)
                .save(consumer, location(folder + "gold/clock"));
        
        ItemCastingRecipeBuilder.tableRecipe(Blocks.IRON_BARS)
                .setFluidAndTime(TinkerFluids.moltenIron, 16 * 3)
                .save(consumer, location(folder + "iron/bars"));
        ItemCastingRecipeBuilder.tableRecipe(Items.LANTERN)
                .setFluidAndTime(TinkerFluids.moltenIron, 16 * 8)
                .setCast(Blocks.TORCH, true)
                .save(consumer, location(folder + "iron/lantern"));
        ItemCastingRecipeBuilder.tableRecipe(Items.SOUL_LANTERN)
                .setFluidAndTime(TinkerFluids.moltenIron, 16 * 8)
                .setCast(Blocks.SOUL_TORCH, true)
                .save(consumer, location(folder + "iron/soul_lantern"));
        ItemCastingRecipeBuilder.tableRecipe(Items.COMPASS)
                .setFluidAndTime(TinkerFluids.moltenIron, 144 * 4)
                .setCast(Items.REDSTONE, true)
                .save(consumer, location(folder + "iron/compass"));

        this.metalCasting(consumer, TinkerFluids.moltenIron,      Blocks.IRON_BLOCK,      Items.IRON_INGOT,      Items.IRON_NUGGET, folder, "iron");
        this.metalCasting(consumer, TinkerFluids.moltenGold,      Blocks.GOLD_BLOCK,      Items.GOLD_INGOT,      Items.GOLD_NUGGET, folder, "gold");
        this.metalCasting(consumer, TinkerFluids.moltenCopper,    Blocks.COPPER_BLOCK,    Items.COPPER_INGOT,    null, folder, "copper");
        this.metalCasting(consumer, TinkerFluids.moltenNetherite, Blocks.NETHERITE_BLOCK, Items.NETHERITE_INGOT, null, folder, "netherite");
        this.ingotCasting(consumer, TinkerFluids.moltenDebris, Items.NETHERITE_SCRAP, folder + "netherite/scrap");
        this.tagCasting(consumer, TinkerFluids.moltenCopper,    16, TinkerSmeltery.nuggetCast, TinkerTags.Items.NUGGETS_COPPER.location().getPath(),          folder + "copper/nugget", false);
        this.tagCasting(consumer, TinkerFluids.moltenNetherite, 16, TinkerSmeltery.nuggetCast, TinkerTags.Items.NUGGETS_NETHERITE.location().getPath(),       folder + "netherite/nugget", false);
        this.tagCasting(consumer, TinkerFluids.moltenDebris,    16, TinkerSmeltery.nuggetCast, TinkerTags.Items.NUGGETS_NETHERITE_SCRAP.location().getPath(), folder + "netherite/debris_nugget", false);

        this.metalTagCasting(consumer, TinkerFluids.moltenCobalt, "cobalt", folder, true);

        this.metalTagCasting(consumer, TinkerFluids.moltenAmethystBronze, "amethyst_bronze", folder, true);
        this.metalTagCasting(consumer, TinkerFluids.moltenRoseGold, "rose_gold", folder, true);
        this.metalCasting(consumer, TinkerFluids.moltenSlimesteel, TinkerMaterials.slimesteel, folder, "slimesteel");
        this.metalCasting(consumer, TinkerFluids.moltenPigIron, TinkerMaterials.pigIron, folder, "pig_iron");

        this.metalTagCasting(consumer, TinkerFluids.moltenManyullyn, "manyullyn", folder, true);
        this.metalTagCasting(consumer, TinkerFluids.moltenHepatizon, "hepatizon", folder, true);
        this.metalCasting(consumer, TinkerFluids.moltenQueensSlime, TinkerMaterials.queensSlime, folder, "queens_slime");
        this.metalCasting(consumer, TinkerFluids.moltenSoulsteel, TinkerMaterials.soulsteel, folder, "soulsteel");

        this.metalCasting(consumer, TinkerFluids.moltenKnightslime, TinkerMaterials.knightslime, folder, "knightslime");

        for (int i = 10; i < SmelteryCompat.values().length; i++) {
            SmelteryCompat compat = SmelteryCompat.values()[i];
            String name = compat.getName();
            if(compat.getName().contains("gmc_")) name = name.substring(4);
            this.metalTagCasting(consumer, compat.getFluid(), name, folder, false);
        }
        
        folder = "smeltery/melting/metal/";
        metalMelting(consumer, TinkerFluids.moltenIron,   "iron",   true, true, folder, false, Enum.valueOf(Byproduct.class, "GMC_NICKEL"), Enum.valueOf(Byproduct.class, "GMC_COPPER"));
        metalMelting(consumer, TinkerFluids.moltenGold,   "gold",   true, true, folder, false, Enum.valueOf(Byproduct.class, "GMC_COPPER"));
        metalMelting(consumer, TinkerFluids.moltenCopper, "copper", true, true, folder, false, Enum.valueOf(Byproduct.class, "GMC_SMALL_GOLD"));
        metalMelting(consumer, TinkerFluids.moltenCobalt, "cobalt", true, true, folder, false, Enum.valueOf(Byproduct.class, "GMC_IRON"));
        
        MeltingRecipeBuilder.melting(Ingredient.of(Tags.Items.ORES_NETHERITE_SCRAP), TinkerFluids.moltenDebris, 144, 2.0f)
                .setOre(OreRateType.METAL, OreRateType.GEM, OreRateType.METAL)
                .addByproduct(TinkerFluids.moltenDiamond.result(FluidValues.GEM))
                .addByproduct(TinkerFluids.moltenGold.result(144 * 3))
                .save(consumer, location(folder + "molten_debris/ore"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerTags.Items.INGOTS_NETHERITE_SCRAP), TinkerFluids.moltenDebris, 144, 1.0f)
                .save(consumer, location(folder + "molten_debris/scrap"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerTags.Items.NUGGETS_NETHERITE_SCRAP), TinkerFluids.moltenDebris, 16, 1 / 3f)
                .save(consumer, location(folder + "molten_debris/debris_nugget"));

        metalMelting(consumer, TinkerFluids.moltenSlimesteel, "slimesteel", false, false, folder, false);
        metalMelting(consumer, TinkerFluids.moltenAmethystBronze, "amethyst_bronze", false, true, folder, false);
        metalMelting(consumer, TinkerFluids.moltenRoseGold, "rose_gold", false, true, folder, false);
        metalMelting(consumer, TinkerFluids.moltenPigIron, "pig_iron", false, false, folder, false);
        
        metalMelting(consumer, TinkerFluids.moltenManyullyn, "manyullyn", false, true, folder, false);
        metalMelting(consumer, TinkerFluids.moltenHepatizon, "hepatizon", false, true, folder, false);
        metalMelting(consumer, TinkerFluids.moltenQueensSlime, "queens_slime", false, false, folder, false);
        
        metalMelting(consumer, TinkerFluids.moltenNetherite, "netherite", false, true, folder, false);
        for (int i = 10; i < SmelteryCompat.values().length; i++) {
            SmelteryCompat compat = SmelteryCompat.values()[i];
            String name = compat.getName();
            if(compat.getName().contains("gmc_")) name = name.substring(4);
            this.metalMelting(consumer, compat.getFluid(), name, compat.isOre(), compat.hasDust(), folder, true, compat.getByproducts());
        }

        MeltingRecipeBuilder.melting(NoContainerIngredient.of(TinkerSmeltery.copperCan), TinkerFluids.moltenCopper, 144, 1.0f)
                .save(consumer, location(folder + "copper/can"));

        MeltingRecipeBuilder.melting(Ingredient.of(Items.ACTIVATOR_RAIL, Items.DETECTOR_RAIL, Blocks.STONECUTTER, Blocks.PISTON, Blocks.STICKY_PISTON), TinkerFluids.moltenIron, 144)
                .save(consumer, location(folder + "iron/ingot_1"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.HEAVY_WEIGHTED_PRESSURE_PLATE, Items.IRON_DOOR, Blocks.SMITHING_TABLE), TinkerFluids.moltenIron, 144 * 2)
                .save(consumer, location(folder + "iron/ingot_2"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.BUCKET), TinkerFluids.moltenIron, 144 * 3)
                .save(consumer, location(folder + "iron/bucket"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.COMPASS, Blocks.IRON_TRAPDOOR), TinkerFluids.moltenIron, 144 * 4)
                .save(consumer, location(folder + "iron/ingot_4"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.BLAST_FURNACE, Blocks.HOPPER, Items.MINECART), TinkerFluids.moltenIron, 144 * 5)
                .save(consumer, location(folder + "iron/ingot_5"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.CAULDRON), TinkerFluids.moltenIron, 144 * 7)
                .save(consumer, location(folder + "iron/cauldron"));
    
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.CHAIN), TinkerFluids.moltenIron, 144 + 16 * 2)
                .save(consumer, location(folder + "iron/chain"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.ANVIL, Blocks.CHIPPED_ANVIL, Blocks.DAMAGED_ANVIL), TinkerFluids.moltenIron, 144 * 4 + 1296 * 3)
                .save(consumer, location(folder + "iron/anvil"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.IRON_BARS, Blocks.RAIL), TinkerFluids.moltenIron, 16 * 3)
                .save(consumer, location(folder + "iron/nugget_3"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerCommons.ironPlatform), TinkerFluids.moltenIron, 16 * 10)
                .save(consumer, location(folder + "iron/platform"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.TRIPWIRE_HOOK), TinkerFluids.moltenIron, 16 * 4)
                .save(consumer, location(folder + "iron/tripwire"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.LANTERN, Blocks.SOUL_LANTERN), TinkerFluids.moltenIron, 16 * 8)
                .save(consumer, location(folder + "iron/lantern"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerModifiers.ironReinforcement), TinkerFluids.moltenIron, 144)
                .save(consumer, location(folder + "iron/reinforcement"));
   
        MeltingRecipeBuilder.melting(Ingredient.of(Items.IRON_HELMET), TinkerFluids.moltenIron, 144 * 5)
                .setDamagable(16)
                .save(consumer, location(folder + "iron/helmet"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.IRON_CHESTPLATE), TinkerFluids.moltenIron, 144 * 8)
                .setDamagable(16)
                .save(consumer, location(folder + "iron/chestplate"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.IRON_LEGGINGS), TinkerFluids.moltenIron, 144 * 7)
                .setDamagable(16)
                .save(consumer, location(folder + "iron/leggings"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.IRON_BOOTS), TinkerFluids.moltenIron, 144 * 4)
                .setDamagable(16)
                .save(consumer, location(folder + "iron/boots"));
   
        MeltingRecipeBuilder.melting(Ingredient.of(Items.IRON_AXE, Items.IRON_PICKAXE), TinkerFluids.moltenIron, 144 * 3)
                .setDamagable(16)
                .save(consumer, location(folder + "iron/axes"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.IRON_SWORD, Items.IRON_HOE, Items.SHEARS), TinkerFluids.moltenIron, 144 * 2)
                .setDamagable(16)
                .save(consumer, location(folder + "iron/weapon"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.IRON_SHOVEL, Items.FLINT_AND_STEEL, Items.SHIELD), TinkerFluids.moltenIron, 144)
                .setDamagable(16)
                .save(consumer, location(folder + "iron/small"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.CROSSBOW), TinkerFluids.moltenIron, 16 * 13)
                .setDamagable(16)
                .save(consumer, location(folder + "iron/crossbow"));
      
        MeltingRecipeBuilder.melting(Ingredient.of(Items.IRON_HORSE_ARMOR), TinkerFluids.moltenIron, 144 * 7)
                .save(consumer, location(folder + "iron/horse_armor"));


   
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerTags.Items.GOLD_CASTS), TinkerFluids.moltenGold, 144)
                .save(consumer, location(folder + "gold/cast"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.POWERED_RAIL), TinkerFluids.moltenGold, 144)
                .save(consumer, location(folder + "gold/powered_rail"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE), TinkerFluids.moltenGold, 144 * 2)
                .save(consumer, location(folder + "gold/plate"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.CLOCK), TinkerFluids.moltenGold, 144 * 4)
                .save(consumer, location(folder + "gold/clock"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.GOLDEN_APPLE), TinkerFluids.moltenGold, 144 * 8)
                .save(consumer, location(folder + "gold/apple"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.GLISTERING_MELON_SLICE, Items.GOLDEN_CARROT), TinkerFluids.moltenGold, 16 * 8)
                .save(consumer, location(folder + "gold/produce"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerModifiers.goldReinforcement), TinkerFluids.moltenGold, 144)
                .save(consumer, location(folder + "gold/reinforcement"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerCommons.goldBars), TinkerFluids.moltenGold, 16 * 3)
                .save(consumer, location(folder + "gold/nugget_3"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerCommons.goldPlatform), TinkerFluids.moltenGold, 16 * 10)
                .save(consumer, location(folder + "gold/platform"));
      
        MeltingRecipeBuilder.melting(Ingredient.of(Items.GOLDEN_HELMET), TinkerFluids.moltenGold, 144 * 5)
                .setDamagable(16)
                .save(consumer, location(folder + "gold/helmet"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.GOLDEN_CHESTPLATE), TinkerFluids.moltenGold, 144 * 8)
                .setDamagable(16)
                .save(consumer, location(folder + "gold/chestplate"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.GOLDEN_LEGGINGS), TinkerFluids.moltenGold, 144 * 7)
                .setDamagable(16)
                .save(consumer, location(folder + "gold/leggings"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.GOLDEN_BOOTS), TinkerFluids.moltenGold, 144 * 4)
                .setDamagable(16)
                .save(consumer, location(folder + "gold/boots"));
       
        MeltingRecipeBuilder.melting(Ingredient.of(Items.GOLDEN_AXE, Items.GOLDEN_PICKAXE), TinkerFluids.moltenGold, 144 * 3)
                .setDamagable(16)
                .save(consumer, location(folder + "gold/axes"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.GOLDEN_SWORD, Items.GOLDEN_HOE), TinkerFluids.moltenGold, 144 * 2)
                .setDamagable(16)
                .save(consumer, location(folder + "gold/weapon"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.GOLDEN_SHOVEL), TinkerFluids.moltenGold, 144)
                .setDamagable(16)
                .save(consumer, location(folder + "gold/shovel"));
       
        MeltingRecipeBuilder.melting(Ingredient.of(Items.GOLDEN_HORSE_ARMOR), TinkerFluids.moltenGold, 144 * 7)
                .save(consumer, location(folder + "gold/horse_armor"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.ENCHANTED_GOLDEN_APPLE), TinkerFluids.moltenGold, 1296 * 8)
                .save(consumer, location(folder + "gold/enchanted_apple"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.GILDED_BLACKSTONE), TinkerFluids.moltenGold, 16 * 6)
                .setOre(OreRateType.METAL)
                .save(consumer, location(folder + "gold/gilded_blackstone"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.BELL), TinkerFluids.moltenGold, 144 * 4)
                .save(consumer, location(folder + "gold/bell"));

        MeltingRecipeBuilder.melting(Ingredient.of(
                                Blocks.EXPOSED_COPPER, Blocks.WEATHERED_COPPER, Blocks.OXIDIZED_COPPER,
                                Blocks.WAXED_COPPER_BLOCK, Blocks.WAXED_EXPOSED_COPPER, Blocks.WAXED_WEATHERED_COPPER, Blocks.WAXED_OXIDIZED_COPPER),
                        TinkerFluids.moltenCopper, 1296)
                .save(consumer, location(folder + "copper/decorative_block"));
        MeltingRecipeBuilder.melting(Ingredient.of(
                                Blocks.CUT_COPPER, Blocks.EXPOSED_CUT_COPPER, Blocks.WEATHERED_CUT_COPPER, Blocks.OXIDIZED_CUT_COPPER,
                                Blocks.CUT_COPPER_STAIRS, Blocks.EXPOSED_CUT_COPPER_STAIRS, Blocks.WEATHERED_CUT_COPPER_STAIRS, Blocks.OXIDIZED_CUT_COPPER_STAIRS,
                                Blocks.WAXED_CUT_COPPER, Blocks.WAXED_EXPOSED_CUT_COPPER, Blocks.WAXED_WEATHERED_CUT_COPPER, Blocks.WAXED_OXIDIZED_CUT_COPPER,
                                Blocks.WAXED_CUT_COPPER_STAIRS, Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS, Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS, Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS),
                        TinkerFluids.moltenCopper, 16 * 20)
                .save(consumer, location(folder + "copper/cut_block"));
        MeltingRecipeBuilder.melting(Ingredient.of(
                                Blocks.CUT_COPPER_SLAB, Blocks.EXPOSED_CUT_COPPER_SLAB, Blocks.WEATHERED_CUT_COPPER_SLAB, Blocks.OXIDIZED_CUT_COPPER_SLAB,
                                Blocks.WAXED_CUT_COPPER_SLAB, Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB, Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB, Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB),
                        TinkerFluids.moltenCopper, 16 * 10)
                .save(consumer, location(folder + "copper/cut_slab"));
        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.LIGHTNING_ROD), TinkerFluids.moltenCopper, 144 * 3)
                .save(consumer, location(folder + "copper/lightning_rod"));
        MeltingRecipeBuilder.melting(Ingredient.of(TinkerTags.Items.COPPER_PLATFORMS), TinkerFluids.moltenCopper, 16 * 10)
                .save(consumer, location(folder + "copper/platform"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.BRUSH), TinkerFluids.moltenCopper, 144)
                .setDamagable(16)
                .save(consumer, location(folder + "copper/brush"));

        MeltingRecipeBuilder.melting(Ingredient.of(Items.SPYGLASS), TinkerFluids.moltenAmethyst, FluidValues.GEM)
                .addByproduct(TinkerFluids.moltenCopper.result(144 * 2))
                .save(consumer, location(folder + "amethyst/spyglass"));

        MeltingRecipeBuilder.melting(Ingredient.of(Blocks.LODESTONE), TinkerFluids.moltenNetherite, 144)
                .save(consumer, location(folder + "netherite/lodestone"));

        int[] netheriteSizes = {16, FluidValues.GEM_SHARD};
        MeltingRecipeBuilder.melting(Ingredient.of(Items.NETHERITE_HELMET), TinkerFluids.moltenNetherite, 144)
                .setDamagable(netheriteSizes)
                .addByproduct(TinkerFluids.moltenDiamond.result(FluidValues.GEM * 5))
                .save(consumer, location(folder + "netherite/helmet"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.NETHERITE_CHESTPLATE), TinkerFluids.moltenNetherite, 144)
                .setDamagable(netheriteSizes)
                .addByproduct(TinkerFluids.moltenDiamond.result(FluidValues.GEM * 8))
                .save(consumer, location(folder + "netherite/chestplate"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.NETHERITE_LEGGINGS), TinkerFluids.moltenNetherite, 144)
                .setDamagable(netheriteSizes)
                .addByproduct(TinkerFluids.moltenDiamond.result(FluidValues.GEM * 7))
                .save(consumer, location(folder + "netherite/leggings"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.NETHERITE_BOOTS), TinkerFluids.moltenNetherite, 144)
                .setDamagable(netheriteSizes)
                .addByproduct(TinkerFluids.moltenDiamond.result(FluidValues.GEM * 4))
                .save(consumer, location(folder + "netherite/boots"));

        MeltingRecipeBuilder.melting(Ingredient.of(Items.NETHERITE_AXE, Items.NETHERITE_PICKAXE), TinkerFluids.moltenNetherite, 144)
                .setDamagable(netheriteSizes)
                .addByproduct(TinkerFluids.moltenDiamond.result(FluidValues.GEM * 3))
                .save(consumer, location(folder + "netherite/axes"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.NETHERITE_SWORD, Items.NETHERITE_HOE), TinkerFluids.moltenNetherite, 144)
                .setDamagable(netheriteSizes)
                .addByproduct(TinkerFluids.moltenDiamond.result(FluidValues.GEM * 2))
                .save(consumer, location(folder + "netherite/weapon"));
        MeltingRecipeBuilder.melting(Ingredient.of(Items.NETHERITE_SHOVEL), TinkerFluids.moltenNetherite, 144)
                .setDamagable(netheriteSizes)
                .addByproduct(TinkerFluids.moltenDiamond.result(FluidValues.GEM))
                .save(consumer, location(folder + "netherite/shovel"));

        MeltingRecipeBuilder.melting(Ingredient.of(TinkerModifiers.silkyCloth), TinkerFluids.moltenRoseGold, 144)
                .save(consumer, location(folder + "rose_gold/silky_cloth"));

        MeltingRecipeBuilder.melting(Ingredient.of(TinkerModifiers.cobaltReinforcement), TinkerFluids.moltenCobalt, 144)
                .save(consumer, location(folder + "cobalt/reinforcement"));
        
        folder = "smeltery/alloys/";
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
                .addCondition(TrueCondition.INSTANCE)
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

        
        Function<String, ICondition> fluidTagLoaded = name -> new NotCondition(new TagEmptyCondition<>(Registries.FLUID, commonResource(name)));
        Function<String, TagKey<Fluid>> fluidTag = name -> FluidTags.create(commonResource(name));
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
        
        folder = "smeltery/entity_melting/";
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
        
        folder = "compat/";
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

        wrapped = withCondition(consumer, tagCondition("ingots/refined_glowstone"), tagCondition("ingots/osmium"));
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

        folder = "tools/modifiers/";
        ItemCastingRecipeBuilder.tableRecipe(TinkerModifiers.slimesteelReinforcement)
                .setFluidAndTime(TinkerFluids.moltenSlimesteel, 16 * 3)
                .setCast(TinkerCommons.obsidianPane, true)
                .save(consumer, prefix(TinkerModifiers.slimesteelReinforcement, folder));
        ItemCastingRecipeBuilder.tableRecipe(TinkerModifiers.ironReinforcement)
                .setFluidAndTime(TinkerFluids.moltenIron, 144)
                .setCast(TinkerTables.pattern, true)
                .save(consumer, prefix(TinkerModifiers.ironReinforcement, folder));
        ItemCastingRecipeBuilder.tableRecipe(TinkerModifiers.goldReinforcement)
                .setFluidAndTime(TinkerFluids.moltenGold, 144)
                .setCast(TinkerTables.pattern, true)
                .save(consumer, prefix(TinkerModifiers.goldReinforcement, folder));
        ItemCastingRecipeBuilder.tableRecipe(TinkerModifiers.cobaltReinforcement)
                .setFluidAndTime(TinkerFluids.moltenCobalt, 144)
                .setCast(TinkerTables.pattern, true)
                .save(consumer, prefix(TinkerModifiers.cobaltReinforcement, folder));
        ItemCastingRecipeBuilder.tableRecipe(TinkerModifiers.silkyCloth)
                .setCast(Items.COBWEB, true)
                .setFluidAndTime(TinkerFluids.moltenRoseGold, 144)
                .save(consumer, prefix(TinkerModifiers.silkyCloth, folder));
    }

    @Override
    public void materialMeltingCasting(Consumer<FinishedRecipe> consumer, MaterialVariantId material, FluidObject<?> fluid, String folder) {
        IMaterialRecipeHelper.super.materialMeltingCasting(consumer, material, fluid, 144, folder);
    }

    private void castCreation(Consumer<FinishedRecipe> consumer, ItemObject<?> part, CastItemObject cast, String folder) {
        castCreation(consumer, MaterialIngredient.of(part), cast, folder, id(part).getPath());
    }

    @Override
    public void castCreation(Consumer<FinishedRecipe> consumer, Ingredient input, CastItemObject cast, String folder, String name) {
        ItemCastingRecipeBuilder.tableRecipe(cast)
                .setFluidAndTime(TinkerFluids.moltenGold, 144)
                .setCast(input, true)
                .setSwitchSlots()
                .save(consumer, location(folder + "gold/" + name));
    }

    @Override
    public void metalMelting(Consumer<FinishedRecipe> consumer, Fluid fluid, String name, boolean hasOre, boolean hasDust, String folder, boolean isOptional, IByproduct... byproducts) {
        metalMelting(consumer, size -> FluidOutput.fromFluid(fluid, size), getTemperature(fluid), name, hasOre, hasDust, folder, isOptional, byproducts);
    }

    @Override
    public void metalMelting(Consumer<FinishedRecipe> consumer, FluidObject<?> fluid, String name, boolean hasOre, boolean hasDust, String folder, boolean isOptional, IByproduct... byproducts) {
        metalMelting(consumer, fluid::result, getTemperature(fluid), name, hasOre, hasDust, folder, isOptional, byproducts);
    }

    private void metalMelting(Consumer<FinishedRecipe> consumer, IntFunction<FluidOutput> fluid, int temperature, String name, boolean hasOre, boolean hasDust, String folder, boolean isOptional, IByproduct... byproducts) {
        String prefix = folder + "/" + name + "/";
        tagMelting(consumer, fluid.apply(1296), temperature, "storage_blocks/" + name, 3.0f, prefix + "block", isOptional);
        tagMelting(consumer, fluid.apply(144), temperature, "ingots/" + name, 1.0f, prefix + "ingot", isOptional);
        tagMelting(consumer, fluid.apply(16), temperature, "nuggets/" + name, 1 / 3f, prefix + "nugget", isOptional);
        if (hasOre) {
            oreMelting(consumer, fluid.apply(144),     temperature, "raw_materials/" + name,      null, 1.5f, prefix + "raw",       isOptional, OreRateType.METAL, 1.0f, byproducts);
            oreMelting(consumer, fluid.apply(144 * 9), temperature, "storage_blocks/raw_" + name, null, 6.0f, prefix + "raw_block", isOptional, OreRateType.METAL, 9.0f, byproducts);
            oreMelting(consumer, fluid.apply(144),     temperature, "ores/" + name, Tags.Items.ORE_RATES_SPARSE,   1.5f, prefix + "ore_sparse",   isOptional, OreRateType.METAL, 1.0f, byproducts);
            oreMelting(consumer, fluid.apply(144 * 2), temperature, "ores/" + name, Tags.Items.ORE_RATES_SINGULAR, 2.5f, prefix + "ore_singular", isOptional, OreRateType.METAL, 2.0f, byproducts);
            oreMelting(consumer, fluid.apply(144 * 6), temperature, "ores/" + name, Tags.Items.ORE_RATES_DENSE,    4.5f, prefix + "ore_dense",    isOptional, OreRateType.METAL, 6.0f, byproducts);

            tagMelting(consumer, fluid.apply(144),     temperature, "geore_shards/" + name, 1.0f, prefix + "geore/shard", true);
            tagMelting(consumer, fluid.apply(144 * 4), temperature, "geore_blocks/" + name, 2.0f, prefix + "geore/block", true);
          
            tagMelting(consumer, fluid.apply(144 * 4), temperature, "geore_clusters/" + name,    2.5f, prefix + "geore/cluster", true);
            tagMelting(consumer, fluid.apply(144),     temperature, "geore_small_buds/" + name,  1.0f, prefix + "geore/bud_small", true);
            tagMelting(consumer, fluid.apply(144 * 2), temperature, "geore_medium_buds/" + name, 1.5f, prefix + "geore/bud_medium", true);
            tagMelting(consumer, fluid.apply(144 * 3), temperature, "geore_large_buds/" + name,  2.0f, prefix + "geore/bud_large", true);

        }
        if (hasDust) {
            tagMelting(consumer, fluid.apply(144), temperature, "dusts/" + name, 0.75f, prefix + "dust", true);
        }
        tagMelting(consumer, fluid.apply(144),      temperature, "plates/" + name, 1.0f, prefix + "plates", true);
        tagMelting(consumer, fluid.apply(144 * 4),  temperature, "gears/" + name, 2.0f, prefix + "gear", true);
        tagMelting(consumer, fluid.apply(16 * 3), temperature, "coins/" + name, 2 / 3f, prefix + "coin", true);
        tagMelting(consumer, fluid.apply(144 / 2),  temperature, "rods/" + name, 1 / 5f, prefix + "rod", true);
        tagMelting(consumer, fluid.apply(144 / 2),  temperature, "wires/" + name, 1 / 5f, prefix + "wire", true);
        tagMelting(consumer, fluid.apply(144),      temperature, "sheetmetals/" + name, 1.0f, prefix + "sheetmetal", true);
    }

    @Override
    public void metalTagCasting(Consumer<FinishedRecipe> consumer, FluidObject<?> fluid, String name, String folder, boolean forceStandard) {
        tagCasting(consumer, fluid, 16, TinkerSmeltery.nuggetCast, "nuggets/" + name, folder + name + "/nugget", !forceStandard);
        tagCasting(consumer, fluid, 144, TinkerSmeltery.ingotCast, "ingots/" + name, folder + name + "/ingot", !forceStandard);
        tagCasting(consumer, fluid, 144, TinkerSmeltery.plateCast, "plates/" + name, folder + name + "/plate", true);
        tagCasting(consumer, fluid, 144 * 4, TinkerSmeltery.gearCast, "gears/" + name, folder + name + "/gear", true);
        tagCasting(consumer, fluid, 16 * 3, TinkerSmeltery.coinCast, "coins/" + name, folder + name + "/coin", true);
        tagCasting(consumer, fluid, 144 / 2, TinkerSmeltery.rodCast, "rods/" + name, folder + name + "/rod", true);
        tagCasting(consumer, fluid, 144 / 2, TinkerSmeltery.wireCast, "wires/" + name, folder + name + "/wire", true);
        // block
        TagKey<Item> block = getItemTag(COMMON, "storage_blocks/" + name);
        Consumer<FinishedRecipe> wrapped = forceStandard ? consumer : withCondition(consumer, tagCondition("storage_blocks/" + name));
        ItemCastingRecipeBuilder.basinRecipe(block)
                .setFluidAndTime(fluid, 1296)
                .save(wrapped, location(folder + name + "/block"));
    }

    @Override
    public void metalCasting(Consumer<FinishedRecipe> consumer, FluidObject<?> fluid, @Nullable ItemLike block, @Nullable ItemLike ingot, @Nullable ItemLike nugget, String folder, String metal) {
        String metalFolder = folder + metal + "/";
        if (block != null) {
            ItemCastingRecipeBuilder.basinRecipe(block)
                    .setFluidAndTime(fluid, 1296)
                    .save(consumer, location(metalFolder + "block"));
        }
        if (ingot != null) {
            ingotCasting(consumer, fluid, ingot, metalFolder + "ingot");
        }
        if (nugget != null) {
            nuggetCasting(consumer, fluid, nugget, metalFolder + "nugget");
        }
        
        tagCasting(consumer, fluid, 144, TinkerSmeltery.plateCast, "plates/" + metal, folder + metal + "/plate", true);
        tagCasting(consumer, fluid, 144 * 4, TinkerSmeltery.gearCast, "gears/" + metal, folder + metal + "/gear", true);
        tagCasting(consumer, fluid, 16 * 3, TinkerSmeltery.coinCast, "coins/" + metal, folder + metal + "/coin", true);
        tagCasting(consumer, fluid, 144 / 2, TinkerSmeltery.rodCast, "rods/" + metal, folder + metal + "/rod", true);
        tagCasting(consumer, fluid, 144 / 2, TinkerSmeltery.wireCast, "wires/" + metal, folder + metal + "/wire", true);
    }

    @Override
    public void nuggetCasting(Consumer<FinishedRecipe> consumer, FluidObject<?> fluid, ItemLike nugget, String location) {
        castingWithCast(consumer, fluid, 16, TinkerSmeltery.nuggetCast, nugget, location);
    }

    @Override
    public void ingotCasting(Consumer<FinishedRecipe> consumer, FluidObject<?> fluid, ItemLike ingot, String location) {
        ingotCasting(consumer, fluid, 144, ingot, location);
    }

    @Override
    public String getModId() {
        return TConstruct.MOD_ID;
    }
}
