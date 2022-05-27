package com.thoriuslight.professionsmod.util.combat.jei.smith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thoriuslight.professionsmod.ProfessionsMod;
import com.thoriuslight.professionsmod.init.BlockInit;
import com.thoriuslight.professionsmod.init.SkillInit;
import com.thoriuslight.professionsmod.item.crafting.ISmithRecipe;
import com.thoriuslight.professionsmod.item.crafting.SmithRecipe;
import com.thoriuslight.professionsmod.profession.capabilities.CapabilityProfession;
import com.thoriuslight.professionsmod.profession.capabilities.IProfession;
import com.thoriuslight.professionsmod.profession.capabilities.ProfessionTypes.profession;
import com.thoriuslight.professionsmod.profession.skill.Skill;
import com.thoriuslight.professionsmod.util.combat.jei.ISkillRecipeCategory;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraft.util.text.ITextComponent;

public class SmithRecipeCategory implements ISkillRecipeCategory<ISmithRecipe>{
	public static final ResourceLocation Uid = new ResourceLocation(ProfessionsMod.MODID, "smith_crafting");
	private final String localizedName;
	private final IDrawableStatic background;
	private final IDrawable icon;
	private static final int craftOutputSlot = 0;
	private static final int craftInputSlot1 = 1;
	private final ICraftingGridHelper craftingGridHelper;
	public SmithRecipeCategory(IGuiHelper helper) {
		this.localizedName = I18n.get("container.professionsmod.smithcrafting");
		this.background = helper.drawableBuilder(new ResourceLocation("jei", "textures/gui/gui_vanilla.png"), 0, 60, 116, 54)
				.addPadding(0, 0, 32, 0)
				.build();
		this.icon = helper.createDrawableIngredient(new ItemStack(BlockInit.SMITHCRAFTINGTABLE_BLOCK.get().asItem()));
		craftingGridHelper = helper.createCraftingGridHelper(craftInputSlot1);
	}
	@Override
	public  ResourceLocation getUid() {
		return Uid;
	}
	@Override
	public Class<? extends ISmithRecipe> getRecipeClass() {
		return ISmithRecipe.class;
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
	public void setIngredients(ISmithRecipe recipe, IIngredients ingredients) {
		NonNullList<Ingredient> list = recipe.getIngredients();
		if(list != null) {
			List<List<ItemStack>> input = new ArrayList<>();	
			for(Ingredient ingredient : list) {
				input.add(Arrays.asList(ingredient.getItems()));
			}
			ingredients.setInputLists(VanillaTypes.ITEM, input);
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
		}
	}
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, ISmithRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(craftOutputSlot, false, 126, 18);
		for (int y = 0; y < 3; ++y) {
			for (int x = 0; x < 3; ++x) {
				int index = craftInputSlot1 + x + (y * 3);
				guiItemStacks.init(index, true, x * 18 + 32, y * 18);
			}
		}
		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
		List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);
		if(inputs.size() > 0) {
			if(recipe instanceof SmithRecipe) {
				SmithRecipe smithRecipe = (SmithRecipe) recipe;
				craftingGridHelper.setInputs(guiItemStacks, inputs, smithRecipe.getRecipeWidth(), smithRecipe.getRecipeHeight());
			}
			else {
				craftingGridHelper.setInputs(guiItemStacks, inputs, 3, 3);
				recipeLayout.setShapeless();
			}
			guiItemStacks.set(craftOutputSlot, outputs.get(0));
		}
	}
	@Override
	public void draw(ISmithRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
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
			int y = 18 + (int) vector.y();
			//Render widget
		   // RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		    //RenderSystem.enableBlend();
			minecraft.getTextureManager().bind(WIDGETS);
        	ISkillRecipeCategory.blit(matrixStack, 2, 15, 0, status, 0, 24, 24, 64, 64);
		    //Render icon
		    minecraft.getItemRenderer().renderAndDecorateItem((LivingEntity)null, skill.getIcon(), x, y);
			//int mainColor = 0xFF80FF20;
			//drawSkill(minecraft, matrixStack, skill.getDisplayName(), mainColor);
		}
	}
	@SuppressWarnings("unused")
	private void drawSkill(Minecraft minecraft, MatrixStack matrixStack, ITextComponent iTextComponent, int mainColor) {
		int shadowColor = 0xFF000000 | (mainColor & 0xFCFCFC) >> 2;
		int width = minecraft.font.width(iTextComponent);
		int x = background.getWidth() - 2 - width;
		int y = 27 + 32;

		minecraft.font.draw(matrixStack, iTextComponent, x + 1, y + 1, shadowColor);
		minecraft.font.draw(matrixStack, iTextComponent, x, y, mainColor);
	}

}
