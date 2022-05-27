package com.thoriuslight.professionsmod.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.thoriuslight.professionsmod.init.ItemInit.ModItemTier;
import com.thoriuslight.professionsmod.state.properties.Tool;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class ToolCoreItem extends Item implements IForgeable, IHeatable, IMeltable{
	private final ModItemTier tier;
	protected final float attackDamage;
	protected final float attackSpeed;
	private final Tool ToolType;
	
	public ToolCoreItem(ModItemTier tier, float attackDamageIn, float attackSpeedIn, Properties properties, Tool tool) {
		super(properties);
		this.tier = tier;
	    this.attackDamage = attackDamageIn + tier.getAttackDamageBonus();
	    this.attackSpeed = attackSpeedIn;
	    this.ToolType = tool;
	}
	public int getEffectiveDurability(ItemStack stack) {
		if(this.getHardness(stack) > this.tier.getHardness()/2)
			return this.getMaxDamage(stack);
		else
			return this.getMaxDamage(stack)/2;
	}
	protected boolean hasHardness(ItemStack stack) {
		CompoundNBT compoundnbt = stack.getTagElement("properties");
		return compoundnbt != null && compoundnbt.contains("hardness", 99);
	}
	protected boolean hasWear(ItemStack stack) {
		CompoundNBT compoundnbt = stack.getTagElement("properties");
		return compoundnbt != null && compoundnbt.contains("wear", 99);
	}
	protected int getHardness(ItemStack stack) {
		CompoundNBT compoundnbt = stack.getTagElement("properties");
		return compoundnbt != null && compoundnbt.contains("hardness", 99) ? compoundnbt.getInt("hardness") : 0;
	}
	protected int getWear(ItemStack stack) {
		CompoundNBT compoundnbt = stack.getTagElement("properties");
		return compoundnbt != null && compoundnbt.contains("wear", 99) ? compoundnbt.getInt("wear") : 0;
	}
	protected void setHardness(ItemStack stack, int hardness) {
		stack.getOrCreateTagElement("properties").putInt("hardness", hardness);
	}
	protected void setWear(ItemStack stack, int wear) {
		stack.getOrCreateTagElement("properties").putInt("wear", wear);
	}
	
	public ModItemTier getTier() {
		return this.tier;
	}
	public boolean repair(ItemStack stack, int hardness) {
		int damage = this.getDamage(stack);
		int i = this.getWear(stack);
		int h = this.getHardness(stack);
		if(damage > i && i < (4*hardness)) {
			this.setWear(stack, ++i);
			damage -= Math.max(10-((h*h*h)/200), 1);
			this.setDamage(stack, Math.max(damage, i));
			return true;
		}
		return false;
	}
	@Override
	public int getEnchantmentValue() {
		return this.tier.getEnchantmentValue();
	}
	
	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
		return this.tier.getRepairIngredient().test(repair) || super.isValidRepairItem(toRepair, repair);
	}
	
	@Override
	public boolean hammer(ItemStack stack) {
		return false;
	}

	@Override
	public int resilience(ItemStack stack) {
		return -1;
	}

	@Override
	public ItemStack forged(ItemStack stack) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot, ItemStack stack) {
		Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
		if (equipmentSlot == EquipmentSlotType.MAINHAND) {
			multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", (double)this.attackDamage, AttributeModifier.Operation.ADDITION));
			multimap.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double)this.attackSpeed, AttributeModifier.Operation.ADDITION));
		}

		return multimap;
	}
	@Override
	public void heat(ItemStack stack) {
		this.setHardness(stack, 0);
	}
	@Override
	public int getNuggetAmount(ItemStack stack) {
		return (ToolType.getNuggetAmount() * (100 - (30*this.getDamage(stack))/this.getMaxDamage(stack) - this.getWear(stack)))/100;
	}
	@Override
	public ModItemTier getType(ItemStack stack) {
		return this.tier;
	}

}
