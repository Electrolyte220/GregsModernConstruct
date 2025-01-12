package dev.electrolyte.gm_construct.mixin;

import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import slimeknights.tconstruct.common.json.ConfigEnabledCondition;

@Mixin(ConfigEnabledCondition.class)
public interface ConfigEnabledConditionInvoker {

    @Invoker(value = "add", remap = false) static ConfigEnabledCondition callAdd(String name, BooleanValue val) {
        throw new AssertionError();
    }
}
