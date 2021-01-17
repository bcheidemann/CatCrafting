package com.bcheidemann.autocrafting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.block.data.Directional;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftInventoryCrafting;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_16_R2.IInventory;

public class AutoCraftingEventHandler implements Listener {
	
	private Server _server;
	private JavaPlugin _plugin;
	private Logger _logger;
	private CraftInventoryCrafting _craftingInventroy = null;
	
	public AutoCraftingEventHandler(Server server, JavaPlugin plugin, Logger logger) {
		_server = server;
		_plugin = plugin;
		_logger = logger;
	}
	
	private boolean isFacingCraftingBench(Dispenser dispenserBlockState, Directional directional) {
		BlockFace facing = directional.getFacing();
		Block blockFacing = dispenserBlockState.getLocation().add(facing.getDirection()).getBlock();
		return blockFacing.getType().equals(Material.CRAFTING_TABLE);
	}
	
	private boolean isAutoCrafter(Block block) {
		if (block.getType().equals(Material.DISPENSER)) {
			Dispenser dispenser = (Dispenser) block.getState();
			Directional dispenserData = (Directional) block.getBlockData();
			if (this.isFacingCraftingBench(dispenser, dispenserData)) {
				return true;
			}
		}
		return false;
	}
	
    @EventHandler
    public void onBlockUpdateEvent(BlockUpdateEvent event) {
		// Is the block becoming powered
    	if (event.getBlockUpdate().getOldBlockState().getBlockPower() == 0 && this.isAutoCrafter(event.getBlock())) {
			
			Dispenser dispenser = (Dispenser) event.getBlock().getState();
			Inventory dispenserInventory = dispenser.getInventory();
			ItemStack[] invMatrix = new ItemStack[] {
					dispenserInventory.getItem(0),
					dispenserInventory.getItem(1),
					dispenserInventory.getItem(2),
					
					dispenserInventory.getItem(3),
					dispenserInventory.getItem(4),
					dispenserInventory.getItem(5),
					
					dispenserInventory.getItem(6),
					dispenserInventory.getItem(7),
					dispenserInventory.getItem(8),
			};
			
			Player player = event.getBlock().getWorld().getPlayers().get(0);
			
			
			if (this._craftingInventroy == null) {
				
				CraftInventoryView civ = (CraftInventoryView) player.openWorkbench(player.getLocation(), true);
				this._craftingInventroy = (CraftInventoryCrafting) civ.getTopInventory();
				civ.close();
			}
	           
           
			this._craftingInventroy.setMatrix(invMatrix);
            
            Recipe r = this._craftingInventroy.getRecipe();
            
            if (r != null) {
            
	            ItemStack result = r.getResult();
	            
	            if (result != null) {
	            
		            Directional dispenserData = (Directional) dispenser.getBlockData();
		            BlockFace facing = dispenserData.getFacing();
		    		Block blockFacing = dispenser.getLocation().add(facing.getDirection()).getBlock();
		            
		            event.getBlock().getWorld().dropItem(blockFacing.getLocation().add(0.5, 0.5, 0.5), result).setPickupDelay(0);
		            
		            for (ItemStack item : invMatrix) {
		            	if (item != null) {
		            		item.setAmount(item.getAmount() - 1);
		            	}
		            }
		            
	            }
	            
            }
            
            this._craftingInventroy.clear();
    	}
    }
	
	@EventHandler
	public void onBlockDispense(BlockDispenseEvent event) {
		if (this.isAutoCrafter(event.getBlock())) {
			event.setCancelled(true);
		}
	}    
    
	public void old(BlockDispenseEvent event) {
		if (event.getBlock().getType().equals(Material.DISPENSER)) {
			Dispenser dispenser = (Dispenser) event.getBlock().getState();
			Directional dispenserData = (Directional) event.getBlock().getBlockData();
			if (this.isFacingCraftingBench(dispenser, dispenserData)) {
				Iterator<Recipe> recipeIterator = this._server.recipeIterator();
				
				while (recipeIterator.hasNext()) {
					boolean match = true;
					Recipe recipe = recipeIterator.next();
					if (recipe != null && recipe.getResult() != null) {
						Inventory dispenserInventory = dispenser.getInventory();
						if (recipe instanceof ShapelessRecipe) {
							/*ShapelessRecipe shapless = (ShapelessRecipe) recipe;
							for (ItemStack ingredient : shapless.getIngredientList()) {
								
							}*/
						}
						else if (recipe instanceof ShapedRecipe) {
							ShapedRecipe shaped = (ShapedRecipe) recipe;
							Map<Character, ItemStack> map = shaped.getIngredientMap();
							String[] shape = shaped.getShape();
							for (int i = 0; i < shape.length; i++) {
								String str = shape[i];
								for (int j = 0; i < str.length(); i++) {
									int dispenserIndex = i * 3 + j;
									ItemStack recipeStk = map.get(str.charAt(j));
									ItemStack dispenserStk = dispenserInventory.getItem(dispenserIndex);
									if (recipeStk != null) {
										if (dispenserStk != null) {
											if (!recipeStk.getType().equals(dispenserStk.getType())) {
												match = false;
												break;
											}
										} else {
											match = false;
											break;
										}
									}
									else {
										if (dispenserStk != null) {
											match = false;
											break;
										}
									}
								}
								if (!match) {
									break;
								}
							}
							if (match) {
								this._logger.info("MATCH:" + shaped.getResult().getType().toString());
							}
						}
					}
				}
				
				if (dispenser.getInventory().getStorageContents()[0] != null) {
					this._logger.info(dispenser.getInventory().getStorageContents()[0].getType().toString());
				}
				//event.setItem(new ItemStack(Material.DIAMOND));
			}
		}
	}
	
	@EventHandler
	public void onInventoryMoveItem(InventoryMoveItemEvent event) {
		if (event.getSource().getType().equals(InventoryType.HOPPER)) {
			Inventory destinantion = event.getDestination();
			if (destinantion.getType().equals(InventoryType.DISPENSER)) {
				Block dispenserBlock = destinantion.getLocation().getBlock();
				Dispenser dispenserBlockState = (Dispenser) dispenserBlock.getState();
				Directional dispenserData = (Directional) dispenserBlock.getBlockData();
				if (this.isFacingCraftingBench(dispenserBlockState, dispenserData)) {
					// if an empty slot exists in the dispenser then put the item in it
					for (int i = 0; i < 9; i++) {
						if (dispenserBlockState.getInventory().getItem(i) == null) {
							// put the item in the empty slot if available
							dispenserBlockState.getInventory().setItem(i, event.getItem());
							// Make sure that the item is not duplicated by replacing it with a new
							// item stack with amount 0.
							event.setItem(new ItemStack(event.getItem().getType(), 0));
							return;
						}
					}
				}
			}
		}
	}
	
}
