package dev.electrolyte.gm_construct.datagen;

import com.gregtechceu.gtceu.common.data.GTMaterials;
import dev.electrolyte.gm_construct.GMConstruct;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class GMCFluidTagsProvider extends FluidTagsProvider {

    private static final TagKey<Fluid> MOLTEN_GOLD = ForgeRegistries.FLUIDS.tags().createTagKey(new ResourceLocation("forge", "molten_gold"));

    public GMCFluidTagsProvider(PackOutput output, CompletableFuture<Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, GMConstruct.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(Provider provider) {
        this.tag(MOLTEN_GOLD).add(GTMaterials.Gold.getFluid());
    }
}
