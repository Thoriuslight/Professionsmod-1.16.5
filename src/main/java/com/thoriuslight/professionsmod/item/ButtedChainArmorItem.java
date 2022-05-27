package com.thoriuslight.professionsmod.item;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class ButtedChainArmorItem extends ArmorItem implements IForgeable{
	public ButtedChainArmorItem(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builder) {
		super(materialIn, slot, builder);
	}

	@Override
	public boolean hammer(ItemStack stack) {
		int damage = this.getDamage(stack);
		int m = this.getMaterialAmount(stack);
		if(m > 0) {
			if(damage >= m) {
				int repairAmount = Math.min(m, 10);
				damage = damage - repairAmount;
				m = m - repairAmount;
				this.setDamage(stack, damage);
				this.setMaterialAmount(stack, m);
				return true;
			} else {
				this.setMaterialAmount(stack, damage);
			}
		}
		return false;
	}

	@Override
	public int resilience(ItemStack stack) {
		int m = this.getMaterialAmount(stack);
		m = Math.min(m, this.getDamage(stack));
		return m+9;
	}

	@Override
	public ItemStack forged(ItemStack stack) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean dropItem() {
		return false;
	}
	protected int getMaterialAmount(ItemStack stack) {
		CompoundNBT compoundnbt = stack.getTagElement("properties");
		return compoundnbt != null && compoundnbt.contains("material", 99) ? compoundnbt.getInt("material") : 0;
	}
	protected void setMaterialAmount(ItemStack stack, int material) {
		stack.getOrCreateTagElement("properties").putInt("material", Math.min(material, this.getDamage(stack)));
	}
	public void addMaterial(ItemStack stack) {
		int m = this.getMaterialAmount(stack);
		this.setMaterialAmount(stack, m + 40);
	}
	public void crafted(ItemStack stack) {
		this.setMaterialAmount(stack, this.getMaxDamage(stack));
	}
	public Item getRepairMaterial() {
		return this.getMaterial().getRepairIngredient().getItems()[0].getItem();
	}
}
