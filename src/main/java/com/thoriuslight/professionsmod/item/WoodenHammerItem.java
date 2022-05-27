package com.thoriuslight.professionsmod.item;

import com.thoriuslight.professionsmod.block.OvenBrickBlock;
import com.thoriuslight.professionsmod.block.OvenControllerBlock;
import com.thoriuslight.professionsmod.init.BlockInit;
import com.thoriuslight.professionsmod.init.SkillInit;
import com.thoriuslight.professionsmod.profession.capabilities.CapabilityProfession;
import com.thoriuslight.professionsmod.profession.capabilities.IProfession;
import com.thoriuslight.professionsmod.profession.capabilities.ProfessionTypes.profession;
import com.thoriuslight.professionsmod.tileentity.OvenControllerTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WoodenHammerItem extends Item{

	public WoodenHammerItem(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		World world = context.getLevel();
		PlayerEntity player = context.getPlayer();
		IProfession iProfession = player.getCapability(CapabilityProfession.PROFESSION, null).orElseThrow(IllegalStateException::new);
		if(iProfession.getProfession() == profession.ALCHEMIST) {
			if(iProfession.getSkillTalent(SkillInit.OVENS.getId())) {
				BlockPos pos = context.getClickedPos();
				Block reference = BlockInit.INSULATION_BRICK_BLOCK.get();
				int direction = 0;
				if(world.getBlockState(pos).getBlock() == reference) {
					boolean flag = true;
					Direction dir = context.getClickedFace();
					switch(dir) {
					case NORTH:
						pos = pos.offset(-1, -1, 0);
						direction = 2;
						break;
					case SOUTH:
						pos = pos.offset(-1, -1, -2);
						break;
					case WEST:
						pos = pos.offset(0, -1, -1);
						direction = 3;
						break;
					case EAST:
						pos = pos.offset(-2, -1, -1);
						direction = 1;
						break;
					default:
						flag = false;
						break;
					}
					if(flag) {
						OUTERLOOP:
						for(int x = 0; x < 3; ++x) {
							for(int y = 0; y < 3; ++y) {
								for(int z = 0; z < 3; ++z) {
									if(world.getBlockState(pos.offset(x, y, z)).getBlock() != reference) {
										flag = false;
										break OUTERLOOP;
									}
								}
							}
						}
					}
					if(flag) {
						if(!world.isClientSide) {
							BlockState state = BlockInit.OVEN_BRICK_BLOCK.get().defaultBlockState();
							for(int x = 0; x < 3; ++x) {
								for(int y = 0; y < 3; ++y) {
									for(int z = 0; z < 3; ++z) {
										BlockPos relPos = pos.offset(x, y, z);
										if(x==1 && y==1 && z==1) {
											world.setBlockAndUpdate(relPos, ((OvenControllerBlock)BlockInit.OVEN_CONTROLLER_BLOCK.get()).getDirectionalState(dir));
											TileEntity tile = world.getBlockEntity(relPos);
											if(tile instanceof OvenControllerTileEntity) {
												((OvenControllerTileEntity)tile).setDirection(direction);
											}
										}
										else {
											world.setBlockAndUpdate(relPos, state);
											OvenBrickBlock.setController(relPos, world, state, x + 3*y + 9*z);
										}
									}
								}
							}
						}
						return ActionResultType.SUCCESS;
					}
				}
			}
		}
		return ActionResultType.PASS;
	}
}
