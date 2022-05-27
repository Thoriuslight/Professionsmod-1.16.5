package com.thoriuslight.professionsmod.event;

import com.thoriuslight.professionsmod.ProfessionsMod;
import com.thoriuslight.professionsmod.init.ItemInit.ModItemTier;
import com.thoriuslight.professionsmod.item.ToolCoreItem;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public @Mod.EventBusSubscriber(modid = ProfessionsMod.MODID, bus = Bus.FORGE)
class HurtEvent {
	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event){
		Entity entity = event.getSource().getEntity();
		if(entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)entity;
			Item item = player.getItemInHand(player.getUsedItemHand()).getItem();
			if(item instanceof ToolCoreItem) {
				if(((ToolCoreItem)item).getTier() == ModItemTier.SILVER) {
					if(event.getEntityLiving().getMobType() == CreatureAttribute.UNDEAD) {
						event.setAmount(event.getAmount() + 2.0f);
					}
				}
				else if(((ToolCoreItem)item).getTier() == ModItemTier.GOLD){
					if(event.getEntityLiving() instanceof EndermanEntity || event.getEntityLiving() instanceof EnderDragonEntity || event.getEntityLiving() instanceof EndermiteEntity) {
						event.setAmount(event.getAmount() + 3.0f);
					}
				}
			}
		}
	}
}
