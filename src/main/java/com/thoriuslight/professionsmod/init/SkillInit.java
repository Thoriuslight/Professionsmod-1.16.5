package com.thoriuslight.professionsmod.init;

import com.thoriuslight.professionsmod.profession.skill.Skill;
import com.thoriuslight.professionsmod.profession.skill.SkillList;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class SkillInit {
	public static final SkillList SMITH_SKILLS = new SkillList();
	public static final SkillList ALCHEMY_SKILLS = new SkillList();
	
	public static final Skill BASIC_SMITHING = SMITH_SKILLS.register("basic_smithing", new ItemStack(BlockInit.SMITHCRAFTINGTABLE_BLOCK.get().asItem()), 0, 1, 0);
	public static final Skill INSPECTION_0 = SMITH_SKILLS.register("inspection_0", new ItemStack(BlockInit.INSPECTIONTABLE_BLOCK.get().asItem()), 1, 1, 3, BASIC_SMITHING);
	public static final Skill WEAK_CHAIN_ARMOR = SMITH_SKILLS.register("weak_chain_armor", new ItemStack(ItemInit.COPPER_CHAIN.get()), -1, 1, 3, BASIC_SMITHING);
	public static final Skill FORGE = SMITH_SKILLS.register("forge", new ItemStack(BlockInit.FORGE_BLOCK.get().asItem()), 0, 0, 5, BASIC_SMITHING);
	public static final Skill TEMP_READING_I = SMITH_SKILLS.register("temp_reading_0", new ItemStack(Blocks.FURNACE.asItem()), -1, 0, 2, FORGE);
	public static final Skill TEMP_INSPECTION_I = SMITH_SKILLS.register("temp_inspection_0", new ItemStack(Items.BLAZE_POWDER), 1, 0, 1, FORGE, INSPECTION_0);
	public static final Skill FAST_HAMMERING = SMITH_SKILLS.register("fast_hammering", new ItemStack(ItemInit.STONEHAMMER_ITEM.get()), -2, 1, 2);
	public static final Skill CRUCIBLE = SMITH_SKILLS.register("crucible", new ItemStack(BlockInit.CRUCIBLE_BLOCK.get().asItem()), 0, -1, 5, FORGE);
	public static final Skill ARSENICAL_BRONZE = SMITH_SKILLS.register("arsenical_bronze", new ItemStack(ItemInit.ARSENICAL_BRONZE_PICKAXE.get().asItem()), -1, -1, 3, CRUCIBLE);
	public static final Skill RECYCLING = SMITH_SKILLS.register("recycling", new ItemStack(ItemInit.COPPER_INGOT.get().asItem()), 1, -1, 3, CRUCIBLE);
	public static final Skill PROFESSIONAL_TOOLS = SMITH_SKILLS.register("professional_tools", new ItemStack(ItemInit.COPPER_HAMMER.get().asItem()), -2, -1, 4, CRUCIBLE);
	
	public static final Skill BASIC_ALCHEMY = ALCHEMY_SKILLS.register("basic_alchemy", new ItemStack(ItemInit.DOUGH.get()), 0, 1, 0);
	public static final Skill HERBOLOGY = ALCHEMY_SKILLS.register("herbology", new ItemStack(Items.GREEN_DYE), -1, 1, 3, BASIC_ALCHEMY);
	public static final Skill ADVANCED_PROCESSING = ALCHEMY_SKILLS.register("advanced_processing", new ItemStack(ItemInit.COPPER_ORE_DUST.get()), 1, 1, 2, BASIC_ALCHEMY);
	public static final Skill OVENS = ALCHEMY_SKILLS.register("ovens", new ItemStack(Blocks.FURNACE.asItem()), 1, 0, 4, ADVANCED_PROCESSING);
	public static final Skill FAST_GRINDING = ALCHEMY_SKILLS.register("fast_grinding", new ItemStack(ItemInit.MORTARANDPESTLE.get()), -2, 1, 2, BASIC_ALCHEMY);
	public static final Skill EXTRACTOR = ALCHEMY_SKILLS.register("extractor", new ItemStack(BlockInit.EXTRACTOR_CONTROLLER_BLOCK.get().asItem()), 1, -1, 4, OVENS);
	
	public static void initSkills(){
		BASIC_SMITHING.setDescription("Basic metal tools");
		INSPECTION_0.setDescription("View the durability of tools");
		WEAK_CHAIN_ARMOR.setDescription("Basic metal armor");
		FORGE.setDescription("Heats metals");
		TEMP_READING_I.setDescription("Determine inaccurately the temperature of a heat source");
		TEMP_INSPECTION_I.setDescription("Read inaccurately the temperature of objects");
		FAST_HAMMERING.setDescription("Increases hammering speed");
		CRUCIBLE.setDescription("Melts metals more efficiently");
		ARSENICAL_BRONZE.setDescription("Strongest tier I material");
		RECYCLING.setDescription("Resmelt tools to reuse material");
		PROFESSIONAL_TOOLS.setDescription("Tools made out of metal perform better");
		
		BASIC_ALCHEMY.setDescription("Gives you basic ability to process items, can be used to create bread");
		HERBOLOGY.setDescription("Combining various herbs can create many useful medicines");
		ADVANCED_PROCESSING.setDescription("Unlocks some advanced grinding recipes");
		OVENS.setDescription("A fast and efficient way to cook items");
		FAST_GRINDING.setDescription("Increases grinding speed");
		EXTRACTOR.setDescription("Gathers byproducts of ovens");
	}
}
