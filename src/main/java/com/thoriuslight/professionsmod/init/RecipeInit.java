package com.thoriuslight.professionsmod.init;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.thoriuslight.professionsmod.ProfessionsMod;
import com.thoriuslight.professionsmod.item.crafting.DoughRecipe;
import com.thoriuslight.professionsmod.item.crafting.SmithChainArmorRecipe;

import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = ProfessionsMod.MODID, bus = Bus.MOD)
public class RecipeInit {
    public static final IRecipeSerializer<DoughRecipe> DOUGH_CRAFTING = null;
    public static final IRecipeSerializer<SmithChainArmorRecipe> CHAINMAIL_CRAFTING = null;
    
    @SubscribeEvent
    public static void registerRecipes(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
        //event.getRegistry().register(new SpecialRecipeSerializer<>(DoughRecipe::new).setRegistryName(ProfessionsMod.MODID, "dough_crafting"));
        event.getRegistry().register(new SpecialRecipeSerializer<>(SmithChainArmorRecipe::new).setRegistryName(ProfessionsMod.MODID, "chainmail_crafting"));
    }
}
