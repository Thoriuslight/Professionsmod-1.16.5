package com.thoriuslight.professionsmod.client.gui;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.thoriuslight.professionsmod.ProfessionsMod;
import com.thoriuslight.professionsmod.init.ItemInit;
import com.thoriuslight.professionsmod.init.SkillInit;
import com.thoriuslight.professionsmod.network.ModPacketHandler;
import com.thoriuslight.professionsmod.network.PacketSelectCast;
import com.thoriuslight.professionsmod.profession.capabilities.IProfession;
import com.thoriuslight.professionsmod.state.properties.Tool;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;

public class CastingBasinScreen extends Screen{
	private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(ProfessionsMod.MODID, "textures/gui/casting_basin.png");
	private int xSize, ySize;
	private class Buttons {
		public final ItemStack icon;
		public final Tool type;
		Buttons(Item item, Tool tool){
			this.icon = new ItemStack(item);
			this.type = tool;
		}
	}
	private final List<Buttons> Items = Lists.newArrayList();
	private BlockPos pos;
	public CastingBasinScreen(BlockPos pos, IProfession prof) {
		super(new TranslationTextComponent("castingbasin.edit"));
		Items.add(new Buttons(ItemInit.COPPER_PICKAXEHEAD.get(), Tool.PICKAXE));
		Items.add(new Buttons(ItemInit.COPPER_SHOVELHEAD.get(), Tool.SHOVEL));
		Items.add(new Buttons(ItemInit.COPPER_AXEHEAD.get(), Tool.AXE));
		Items.add(new Buttons(ItemInit.COPPER_KNIFEHEAD.get(), Tool.KNIFE));
		Items.add(new Buttons(ItemInit.COPPER_HOEHEAD.get(), Tool.HOE));
		if(prof.getSkillTalent(SkillInit.PROFESSIONAL_TOOLS.getId())) {
			Items.add(new Buttons(ItemInit.COPPER_HAMMERHEAD.get(), Tool.HAMMER));
			Items.add(new Buttons(ItemInit.COPPER_MORTARANDPESTLE.get(), Tool.MORTAR));
		}
		Items.add(new Buttons(ItemInit.COPPER_INGOT.get(), Tool.INGOT));
		Items.add(new Buttons(ItemInit.COPPER_NUGGET.get(), Tool.NUGGET));
		Items.add(new Buttons(ItemInit.COPPER_RINGS.get(), Tool.RINGS));
		this.xSize = 176;
		this.ySize = 84;
		this.pos = pos;
	}
	
	@Override
	public boolean mouseClicked(double mouse_x, double mouse_y, int p_mouseClicked_5_) {
		int i = (this.width - this.xSize) / 2 + 52;
	    int j = (this.height - this.ySize) / 2 + 15;
	    int x = (((int) mouse_x) - i)/16;
	    int y = (((int) mouse_y) - j)/16;
	    if(x >= 0 && x < 5 && y >= 0 && y < (Items.size()/4)) {
			if(minecraft.player!=null) {
				ModPacketHandler.INSTANCE.sendToServer(new PacketSelectCast(Items.get(x + y * 4).type, pos));
			}
	        this.minecraft.setScreen((Screen)null);
            return true;
	    }
	    return super.mouseClicked(mouse_x, mouse_y, p_mouseClicked_5_);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float p_render_3_) {
		this.renderBackground(matrixStack);
	    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
	    this.minecraft.getTextureManager().bind(GUI_TEXTURE);
	      int i = (this.width - this.xSize) / 2;
	      int j = (this.height - this.ySize) / 2;
	    this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
	    for(int k = 0; k < Items.size(); ++k) {
		    int j2 = j + 15 + (k / 4) * 16;
	    	this.minecraft.getTextureManager().bind(GUI_TEXTURE);
	    	int j1 = 166;
	    	int k1 = i + 52 + (k % 4) * 16;
	    	if (mouseX >= k1 && mouseY >= j2 && mouseX < k1 + 16 && mouseY < j2 + 16) {
	    		j1 += 36;
	    	}
		    this.blit(matrixStack, k1, j2, 0, j1, 16, 18);
	        this.minecraft.getItemRenderer().renderAndDecorateItem(Items.get(k).icon, k1, j2);
	    }
		super.render(matrixStack, mouseX, mouseY, p_render_3_);
	}
	

}
