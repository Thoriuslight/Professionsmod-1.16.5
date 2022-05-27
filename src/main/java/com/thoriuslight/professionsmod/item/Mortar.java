package com.thoriuslight.professionsmod.item;

import java.util.Optional;

import com.thoriuslight.professionsmod.init.RecipeSerializerInit;
import com.thoriuslight.professionsmod.init.SkillInit;
import com.thoriuslight.professionsmod.item.crafting.IGrindingRecipe;
import com.thoriuslight.professionsmod.profession.capabilities.CapabilityProfession;
import com.thoriuslight.professionsmod.profession.capabilities.IProfession;
import com.thoriuslight.professionsmod.profession.capabilities.ProfessionTypes.profession;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class Mortar extends Item{
	private final int useDuration;
	private final int capacity;
	public Mortar(Properties properties, int useDuration, int capacity) {
		super(properties);
		this.useDuration = useDuration;
		this.capacity = capacity;
	}
	//----------------------------------------Processing-----------------------------------------------------------------------------------------
	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		IProfession iProfession = playerIn.getCapability(CapabilityProfession.PROFESSION, null).orElseThrow(IllegalStateException::new);
		if(iProfession.getProfession() == profession.ALCHEMIST) {
			if(handIn == Hand.OFF_HAND) {
				ItemStack mortar = playerIn.getItemInHand(Hand.OFF_HAND);
				if(Mortar.getState(mortar).isEmpty()) {
					ItemStack output = ItemStack.EMPTY;
					Optional<IGrindingRecipe> optional;
					if(!worldIn.isClientSide) {
						optional = worldIn.getServer().getRecipeManager().getRecipeFor(RecipeSerializerInit.GRINDING_TYPE, new Inventory(playerIn.getItemInHand(Hand.MAIN_HAND)), worldIn);
					} else {
						optional = worldIn.getRecipeManager().getRecipeFor(RecipeSerializerInit.GRINDING_TYPE, new Inventory(playerIn.getItemInHand(Hand.MAIN_HAND)), worldIn);
					}
					if (optional.isPresent()) {
						IGrindingRecipe igrindingrecipe = optional.get();
						if (igrindingrecipe.isUnlocked(iProfession.getSkillTree())) {
							output = igrindingrecipe.assemble(null);
							int num = igrindingrecipe.getIngredients().get(0).getItems()[0].getCount();
							int factor = 1;
							if(num == 1) {
								factor = capacity;
								if(iProfession.getSkillTalent(SkillInit.FAST_GRINDING.getId())) {
									++factor;
								}
								factor = Math.min(playerIn.getItemInHand(Hand.MAIN_HAND).getCount(), factor);
							}
							playerIn.getItemInHand(Hand.MAIN_HAND).shrink(num * factor);
							output.setCount(output.getCount() * factor);
							Mortar.setState(mortar, output);
							playerIn.startUsingItem(handIn);
							return  ActionResult.success(playerIn.getItemInHand(handIn));
						}
					}
				}
				else {
					playerIn.startUsingItem(handIn);
					return  ActionResult.consume(playerIn.getItemInHand(handIn));
				}
			}
			else if (playerIn.getItemInHand(Hand.OFF_HAND).isEmpty()) {
				ItemStack mortar = playerIn.getItemInHand(Hand.MAIN_HAND);
				playerIn.setItemInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
				playerIn.setItemInHand(Hand.OFF_HAND, mortar);
				return  ActionResult.consume(playerIn.getItemInHand(handIn));
			}
		}
		return  ActionResult.fail(playerIn.getItemInHand(handIn));
	}
	@Override
	public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		if (entityLiving instanceof PlayerEntity) {
			PlayerEntity playerentity = (PlayerEntity)entityLiving;
			if(!playerentity.addItem(Mortar.getState(stack))) {
				playerentity.drop(new ItemStack(Mortar.getState(stack).getItem()), false);
			}
			playerentity.getCapability(CapabilityProfession.PROFESSION, null).ifPresent(iProf -> {
				iProf.addSkill(2, playerentity);
			});
			Mortar.setState(stack, ItemStack.EMPTY);
			stack.hurtAndBreak(1, entityLiving, (p_220045_0_) -> {
				p_220045_0_.broadcastBreakEvent(EquipmentSlotType.OFFHAND);
				this.onBreak(p_220045_0_, stack);
			});
			if((stack.getMaxDamage() - stack.getDamageValue()) == 1) {
				playerentity.getCooldowns().addCooldown(this, 1);
			}
		}
		return stack;
	}
	protected void onBreak(LivingEntity entity, ItemStack stack) {}
	//----------------------------------------Properties-----------------------------------------------------------------------------------------
	@Override
	public int getUseDuration(ItemStack stack) {
		return useDuration;
	}
	@Override
	public UseAction getUseAnimation(ItemStack stack) {
		return UseAction.BOW;
	}
	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return true;
	}
	//----------------------------------------Data Management-----------------------------------------------------------------------------------------
	public static ItemStack getState(ItemStack stack) {
		CompoundNBT compoundnbt = stack.getTagElement("properties");
		return compoundnbt != null && compoundnbt.contains("result") ? ItemStack.of(compoundnbt.getCompound("result")) : ItemStack.EMPTY;
	}
	protected static void setState(ItemStack stack, ItemStack result) {
		stack.getOrCreateTagElement("properties").put("result", result.save((new CompoundNBT())));
	}
}
