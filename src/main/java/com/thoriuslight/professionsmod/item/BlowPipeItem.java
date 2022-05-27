package com.thoriuslight.professionsmod.item;

import com.thoriuslight.professionsmod.profession.capabilities.CapabilityProfession;
import com.thoriuslight.professionsmod.profession.capabilities.IProfession;
import com.thoriuslight.professionsmod.profession.capabilities.ProfessionTypes.profession;
import com.thoriuslight.professionsmod.tileentity.CrucibleTileEntity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BlowPipeItem extends Item{

	public BlowPipeItem(Properties properties) {
		super(properties);
	}
	
	@Override
	public UseAction getUseAnimation(ItemStack stack) {
		return UseAction.BOW;
	}
	@Override
	public int getUseDuration(ItemStack stack) {
		return 20;
	}
	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		IProfession iProfession = playerIn.getCapability(CapabilityProfession.PROFESSION, null).orElseThrow(IllegalStateException::new);
		if(iProfession.getProfession() == profession.SMITH) {
			playerIn.startUsingItem(handIn);
		}
		return ActionResult.fail(playerIn.getItemInHand(handIn));
	}
	@Override
	public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		RayTraceResult rayTraceBlock = entityLiving.pick(20.0D, 0.0F, false);
		if (rayTraceBlock.getType() == RayTraceResult.Type.BLOCK) {
			BlockPos blockpos = ((BlockRayTraceResult)rayTraceBlock).getBlockPos();
			TileEntity tileEntity = worldIn.getBlockEntity(blockpos);
			if(tileEntity instanceof CrucibleTileEntity) {
				((CrucibleTileEntity)tileEntity).addOxygen(400);
			}
		}
		return stack;
	}
}
