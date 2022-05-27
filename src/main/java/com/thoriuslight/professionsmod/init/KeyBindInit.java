package com.thoriuslight.professionsmod.init;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBindInit {
	public static final KeyBinding PROMENU = new KeyBinding("key.promenu", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_P, "key.categories.promenu");
	public static void initKeyBinding() {
		ClientRegistry.registerKeyBinding(PROMENU);
	}
}
