package com.thoriuslight.professionsmod.profession.skill;

import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.item.ItemStack;

public class SkillList{
	private ArrayList<Skill> skills;
	private HashMap<String, Skill> skillMapping;
	private int size = 0;
	public SkillList(){
		skills = new ArrayList<Skill>();
		skillMapping = new HashMap<String, Skill>();
	}
	
	public Skill register(final String name, ItemStack Icon, int x, int y, int requiredPoints, Skill... requirements) {
		x *= 32;
		y *= 32;
		Skill skill = new Skill(name, x, y, Icon, requiredPoints, size, requirements);
		++size;
		skills.add(skill);
		skillMapping.put(name, skill);
		return skill;
	}
	public ArrayList<Skill> getSkills(){
		return skills;
	}
	
	public Skill getSkill(String name) {
		return skillMapping.get(name);
	}
}
