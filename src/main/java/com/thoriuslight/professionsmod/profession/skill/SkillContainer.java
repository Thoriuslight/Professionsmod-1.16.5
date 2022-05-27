package com.thoriuslight.professionsmod.profession.skill;

public class SkillContainer {
	private int skills;
	public SkillContainer(){
		skills = 0;
	}
	public SkillContainer(int x){
		skills = x;
	}
	public int getSkillList(){
		return skills;
	}
	public void setSkillList(int skills){
		this.skills = skills;
	}
	public boolean getSkill(int id) {
		assert id < 32 : "Id is out of bounds";
		return 0 < (skills & (1 << id));	
	}
	public void setSkill(int id, boolean state) {
		assert id < 32 : "Id is out of bounds";
		skills &= ~(1 << id);
		skills |= 1 << id;
	}
}
