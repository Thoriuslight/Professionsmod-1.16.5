package com.thoriuslight.professionsmod.item;

import com.thoriuslight.professionsmod.block.CrucibleBlock;
import com.thoriuslight.professionsmod.block.StoneAnvilBlock;
import com.thoriuslight.professionsmod.init.BlockInit;
import com.thoriuslight.professionsmod.init.SkillInit;
import com.thoriuslight.professionsmod.profession.capabilities.CapabilityProfession;
import com.thoriuslight.professionsmod.profession.capabilities.IProfession;
import com.thoriuslight.professionsmod.profession.capabilities.ProfessionTypes.profession;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.TieredItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class HammerItem extends TieredItem{
	private final int strength;
	public HammerItem(Properties properties, IItemTier tier, int strength) {
		super(tier, properties);
		this.strength = strength;
	}
	
	@Override
	public ActionResultType useOn(ItemUseContext context) {
		if(context.getHand().equals(Hand.MAIN_HAND)) {
			World world = context.getLevel();
			PlayerEntity player = context.getPlayer();
			IProfession iProfession = player.getCapability(CapabilityProfession.PROFESSION, null).orElseThrow(IllegalStateException::new);
			if(iProfession.getProfession() == profession.SMITH) {
				BlockState blockstate = world.getBlockState(context.getClickedPos());
				if(blockstate.getBlock() == BlockInit.STONEANVIL_BLOCK.get()) {
					if(((StoneAnvilBlock)blockstate.getBlock()).hammer(world, context.getClickedPos(), player, this.strength)) {
						world.playSound((PlayerEntity)null, context.getClickedPos(), SoundEvents.ANVIL_HIT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
						int i = 20;
						if(iProfession.getSkillTalent(SkillInit.FAST_HAMMERING.getId()))
							i = 16;
						context.getPlayer().getCooldowns().addCooldown(this, i);
						context.getPlayer().swing(context.getHand());
					}
					return ActionResultType.SUCCESS;
				}
				else if(blockstate.getBlock() == BlockInit.CRUCIBLE_BLOCK.get()) {
					return ((CrucibleBlock) blockstate.getBlock()).setDrain(world, context.getClickedPos(), blockstate, context.getClickedFace());
				}
			}
		}
		return ActionResultType.PASS;
	}
}
