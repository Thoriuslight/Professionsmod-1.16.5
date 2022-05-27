package com.thoriuslight.professionsmod.profession.capabilities;

import com.thoriuslight.professionsmod.profession.capabilities.ProfessionTypes.profession;
import net.minecraft.entity.player.PlayerEntity;

public interface IProfession {
	public profession getProfession();
	public void setProfession(profession Prof);
	public int getSkill();
	public void addSkill(int x, PlayerEntity player);
	public void setSkill(int x);
	public boolean getSkillTalent(int id);
	public boolean setSkillTalent(int id, boolean state, PlayerEntity player);
	public int getSkillTree();
	public void setSkillTree(int x);
	public int getSkillPoints();
}
