package com.thoriuslight.professionsmod.profession.capabilities;

import com.thoriuslight.professionsmod.profession.skill.SkillList;
import com.thoriuslight.professionsmod.init.SkillInit;

public class ProfessionTypes {
	public enum profession{
		NOTHING("", null),
		UNDEFINED("", null),
		SMITH("screen.smith.title", SkillInit.SMITH_SKILLS),
		ALCHEMIST("screen.alchemy.title", SkillInit.ALCHEMY_SKILLS);
		
		private final String name;
		private final SkillList Skills;
		private profession(String name, SkillList Skills) {
			this.name = name;
			this.Skills = Skills;
		}
		public String getName() {
			return name;
		}
		public SkillList getSkillList() {
			return Skills;
		}
	}
}
