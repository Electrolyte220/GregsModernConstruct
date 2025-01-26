package dev.electrolyte.gm_construct.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.mantle.client.SafeClientAccess;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.mantle.fluid.tooltip.FluidTooltipHandler;
import slimeknights.mantle.item.BlockTooltipItem;
import slimeknights.tconstruct.smeltery.item.TankItem;

import java.util.List;

import static slimeknights.tconstruct.smeltery.item.TankItem.getFluidTank;

@Mixin(TankItem.class)
public abstract class TankItemMixin extends BlockTooltipItem {

    @Shadow @Final private static String KEY_FLUID;
    @Shadow @Final private static String KEY_MB;
    @Shadow @Final private static String KEY_INGOTS;
    @Shadow @Final private static String KEY_MIXED;

    public TankItemMixin(Block blockIn, Properties builder) {
        super(blockIn, builder);
    }

    @Inject(method = "appendHoverText", at = @At("HEAD"), cancellable = true)
    private void gmc$appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn, CallbackInfo ci) {
        if (stack.hasTag()) {
            FluidTank tank = getFluidTank(stack);
            if (tank.getFluidAmount() > 0) {
                tooltip.add(Component.translatable(KEY_FLUID, tank.getFluid().getDisplayName()).withStyle(ChatFormatting.GRAY));
                int amount = tank.getFluidAmount();
                TooltipKey key = SafeClientAccess.getTooltipKey();
                if (tank.getCapacity() % 144 != 0 || key == TooltipKey.SHIFT) {
                    tooltip.add(Component.translatable(KEY_MB, amount).withStyle(ChatFormatting.GRAY));
                } else {
                    int ingots = amount / 144;
                    int mb = amount % 144;
                    if (mb == 0) {
                        tooltip.add(Component.translatable(KEY_INGOTS, ingots).withStyle(ChatFormatting.GRAY));
                    } else {
                        tooltip.add(Component.translatable(KEY_MIXED, ingots, mb).withStyle(ChatFormatting.GRAY));
                    }
                    if (key != TooltipKey.UNKNOWN) {
                        tooltip.add(FluidTooltipHandler.HOLD_SHIFT);
                    }
                }
            }
        }
        else {
            super.appendHoverText(stack, worldIn, tooltip, flagIn);
        }
        ci.cancel();
    }
}
