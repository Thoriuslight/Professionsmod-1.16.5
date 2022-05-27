package com.thoriuslight.professionsmod.item;

import com.thoriuslight.professionsmod.init.ItemInit;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BowlItem extends Item{

	public BowlItem(Properties properties) {
		super(properties);
	}
	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		RayTraceResult raytraceresult = getPlayerPOVHitResult(worldIn, playerIn, RayTraceContext.FluidMode.SOURCE_ONLY);
		if (raytraceresult.getType() == RayTraceResult.Type.MISS) {
			return ActionResult.pass(itemstack);
		} else {
			if (raytraceresult.getType() == RayTraceResult.Type.BLOCK) {
				BlockPos blockpos = ((BlockRayTraceResult)raytraceresult).getBlockPos();
				if (!worldIn.mayInteract(playerIn, blockpos)) {
					return ActionResult.pass(itemstack);
				}
				if (worldIn.getFluidState(blockpos).is(FluidTags.WATER)) {
					worldIn.playSound(playerIn, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
					return ActionResult.success(this.turnBottleIntoItem(itemstack, playerIn, new ItemStack(ItemInit.WATER_BOWL.get())));
				}
			}
			return ActionResult.pass(itemstack);
		} 
	}

		   protected ItemStack turnBottleIntoItem(ItemStack p_185061_1_, PlayerEntity player, ItemStack stack) {
		      p_185061_1_.shrink(1);
		      if (p_185061_1_.isEmpty()) {
		         return stack;
		      } else {
		         if (!player.inventory.add(stack)) {
		            player.drop(stack, false);
		         }

		         return p_185061_1_;
		      }
		   }
}
