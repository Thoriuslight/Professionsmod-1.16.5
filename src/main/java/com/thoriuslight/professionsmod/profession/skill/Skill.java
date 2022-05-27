package com.thoriuslight.professionsmod.profession.skill;

import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class Skill {
	private final int id;
	private final ITextComponent displayText;
	private final ItemStack Icon;
	private final int posX;
	private final int posY;
	private final int requiredPoints;
	private List<Skill> requiredSkill;
	private ITextComponent description;
	   
	public Skill(String registerName, int x, int y, ItemStack Icon, int requiredPoints, int id, Skill... requirements) {
		this.id = id;
		this.displayText = new TranslationTextComponent("skill.professionsmod." + registerName);
		this.posX = x;
		this.posY = y;
		this.Icon = Icon;
		this.requiredPoints = requiredPoints;
		this.requiredSkill = Arrays.asList(requirements);
	}
	public int getId() {
		return this.id;
	}
	   
	public ITextComponent getDisplayName() {
		return this.displayText;
	}
	   
	public int getPosX() {
		return posX;
	}
	public int getPosY() {
		return posY;
	}
	
	public ItemStack getIcon() {
		return Icon;
	}
	public int getRequiredPoints() {
		return this.requiredPoints;
	}
	public List<Skill> getRequirement() {
		return this.requiredSkill;
	}
	public boolean isMouseOver(int p_191816_1_, int p_191816_2_, int p_191816_3_, int p_191816_4_) {
        int i = p_191816_1_ + this.posX - 9;
        int j = i + 26;
        int k = p_191816_2_ + this.posY - 12;
        int l = k + 26;
        return p_191816_3_ >= i && p_191816_3_ <= j && p_191816_4_ >= k && p_191816_4_ <= l;
	}
	
	public ITextComponent getDescription() {
		return this.description;
	}
	public void setDescription(String string) {
		this.description = new StringTextComponent(string);
	}
}
