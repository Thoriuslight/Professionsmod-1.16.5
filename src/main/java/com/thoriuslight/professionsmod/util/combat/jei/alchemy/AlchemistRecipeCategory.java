package com.thoriuslight.professionsmod.util.combat.jei.alchemy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thoriuslight.professionsmod.ProfessionsMod;
import com.thoriuslight.professionsmod.init.BlockInit;
import com.thoriuslight.professionsmod.init.SkillInit;
import com.thoriuslight.professionsmod.item.crafting.AlchemyRecipe;
import com.thoriuslight.professionsmod.item.crafting.IAlchemyRecipe;
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

public class AlchemistRecipeCategory implements ISkillRecipeCategory<IAlchemyRecipe>{
	public static final ResourceLocation Uid = new ResourceLocation(ProfessionsMod.MODID, "alchemy_crafting");
	private final String localizedName;
	private final IDrawableStatic background;
	private final IDrawable icon;
	private static final int craftOutputSlot = 0;
	private static final int craftInputSlot1 = 1;
	private final ICraftingGridHelper craftingGridHelper;
	public AlchemistRecipeCategory(IGuiHelper helper) {
		this.localizedName = I18n.get("container.professionsmod.alchemistcrafting");
		this.background = helper.drawableBuilder(new ResourceLocation("jei", "textures/gui/gui_vanilla.png"), 0, 60, 116, 54)
				.addPadding(0, 0, 32, 0).build();
		this.icon = helper.createDrawableIngredient(new ItemStack(BlockInit.ALCHEMISTCRAFTINGTABLE_BLOCK.get().asItem()));
		craftingGridHelper = helper.createCraftingGridHelper(craftInputSlot1);
	}
	@Override
	public ResourceLocation getUid() {
		return Uid;
	}

	@Override
	public Class<? extends IAlchemyRecipe> getRecipeClass() {
		return IAlchemyRecipe.class;
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
	public void setIngredients(IAlchemyRecipe recipe, IIngredients ingredients) {
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
	public void setRecipe(IRecipeLayout recipeLayout, IAlchemyRecipe recipe, IIngredients ingredients) {
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
		if(recipe instanceof AlchemyRecipe) {
			AlchemyRecipe alchemyRecipe = (AlchemyRecipe) recipe;
			craftingGridHelper.setInputs(guiItemStacks, inputs, alchemyRecipe.getRecipeWidth(), alchemyRecipe.getRecipeHeight());
		}
		else {
			craftingGridHelper.setInputs(guiItemStacks, inputs, 3, 3);
			recipeLayout.setShapeless();
		}
		guiItemStacks.set(craftOutputSlot, outputs.get(0));
	}
	@Override
	public void draw(IAlchemyRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		Minecraft minecraft = Minecraft.getInstance();
		ArrayList<Skill> skills = SkillInit.ALCHEMY_SKILLS.getSkills();
		int id = ISkillRecipeCategory.Log2(recipe.getRequirements());
		if(id < skills.size()) {
			//Init skills
			Skill skill = skills.get(id);
			IProfession iProfession = minecraft.player.getCapability(CapabilityProfession.PROFESSION, null).orElseThrow(IllegalStateException::new);
			int status = iProfession.getSkillTalent(id) && iProfession.getProfession() == profession.ALCHEMIST ? 24 : 0;
		    Vector4f vector = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
		    vector.transform(matrixStack.last().pose());
			int x = 5 + (int) vector.x();
			int y = 18 + (int) vector.y();
			//Render widget
			minecraft.getTextureManager().bind(WIDGETS);
        	ISkillRecipeCategory.blit(matrixStack, 2, 15, 0, status, 0, 24, 24, 64, 64);
		    //Render icon
		    minecraft.getItemRenderer().renderAndDecorateItem((LivingEntity)null, skill.getIcon(), x, y);
		}
	}
}
