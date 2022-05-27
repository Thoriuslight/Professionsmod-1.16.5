package com.thoriuslight.professionsmod.util.combat.jei.smith;

import java.util.ArrayList;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thoriuslight.professionsmod.ProfessionsMod;
import com.thoriuslight.professionsmod.init.BlockInit;
import com.thoriuslight.professionsmod.init.SkillInit;
import com.thoriuslight.professionsmod.item.crafting.ICrushingRecipe;
import com.thoriuslight.professionsmod.profession.capabilities.CapabilityProfession;
import com.thoriuslight.professionsmod.profession.capabilities.IProfession;
import com.thoriuslight.professionsmod.profession.capabilities.ProfessionTypes.profession;
import com.thoriuslight.professionsmod.profession.skill.Skill;
import com.thoriuslight.professionsmod.util.combat.jei.ISkillRecipeCategory;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector4f;

public class CrushingRecipeCategory implements ISkillRecipeCategory<ICrushingRecipe>{
	public static final ResourceLocation Uid = new ResourceLocation(ProfessionsMod.MODID, "crushing_crafting");
	private final String localizedName;
	private final IDrawableStatic background;
	private final IDrawable icon;
	private static final int craftOutputSlot = 0;
	private static final int craftInputSlot1 = 1;
	public CrushingRecipeCategory(IGuiHelper helper) {
		this.localizedName = I18n.get("container.professionsmod.crushingcrafting");
		this.background = helper.drawableBuilder(new ResourceLocation("jei", "textures/gui/gui_vanilla.png"), 0, 220, 82, 34)
				.addPadding(0, 0, 32, 0).build();
		this.icon = helper.createDrawableIngredient(new ItemStack(BlockInit.STONEANVIL_BLOCK.get().asItem()));
	}
	@Override
	public ResourceLocation getUid() {
		return Uid;
	}

	@Override
	public Class<? extends ICrushingRecipe> getRecipeClass() {
		return ICrushingRecipe.class;
	}

	@Override
	public String getTitle() {
		return localizedName;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setIngredients(ICrushingRecipe recipe, IIngredients ingredients) {
		ingredients.setInputIngredients(recipe.getIngredients());
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, ICrushingRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(craftInputSlot1, true, 32, 8);
		guiItemStacks.init(craftOutputSlot, false, 92, 8);
		guiItemStacks.set(ingredients);
	}
	@Override
	public void draw(ICrushingRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		Minecraft minecraft = Minecraft.getInstance();
		ArrayList<Skill> skills = SkillInit.SMITH_SKILLS.getSkills();
		int id = ISkillRecipeCategory.Log2(recipe.getRequirements());
		if(id < skills.size()) {
			//Init skills
			Skill skill = skills.get(id);
			IProfession iProfession = minecraft.player.getCapability(CapabilityProfession.PROFESSION, null).orElseThrow(IllegalStateException::new);
			int status = iProfession.getSkillTalent(id) && iProfession.getProfession() == profession.SMITH ? 24 : 0;
		    Vector4f vector = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
		    vector.transform(matrixStack.last().pose());
			int x = 5 + (int) vector.x();
			int y = 8 + (int) vector.y();
			//Render widget
			minecraft.getTextureManager().bind(WIDGETS);
        	ISkillRecipeCategory.blit(matrixStack, 2, 5, 0, status, 0, 24, 24, 64, 64);
		    //Render icon
		    minecraft.getItemRenderer().renderAndDecorateItem((LivingEntity)null, skill.getIcon(), x, y);
		}
	}
}
