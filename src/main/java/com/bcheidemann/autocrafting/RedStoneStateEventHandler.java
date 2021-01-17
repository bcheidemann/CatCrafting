package com.bcheidemann.autocrafting;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

public class RedStoneStateEventHandler implements Listener {

	private BlockStateManager _blockStateManager;
	
	public RedStoneStateEventHandler(BlockStateManager blockStateManager) {
		_blockStateManager = blockStateManager;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onBlockPhysics(BlockPhysicsEvent event) {
		
		Block block = event.getBlock();
		int newPower = block.getBlockPower();
		
		if (newPower == 0) {
			BlockState oldBlockState = this._blockStateManager.deleteBlockState(block.getLocation());
			if (oldBlockState != null && oldBlockState.getBlockPower() > 0) {
				BlockUpdate blockUpdate = new BlockUpdate(oldBlockState, new BlockState(newPower));
				BlockUpdateEvent blockUpdateEvent = new BlockUpdateEvent(blockUpdate, block);
				Bukkit.getServer().getPluginManager().callEvent(blockUpdateEvent);
			}
		} else {
			BlockUpdate blockUpdate = this._blockStateManager.updateBlockState(block.getLocation(), new BlockState(block.getBlockPower()));
			if (blockUpdate.wasUpdated) {
				BlockUpdateEvent blockUpdateEvent = new BlockUpdateEvent(blockUpdate, block);
				Bukkit.getServer().getPluginManager().callEvent(blockUpdateEvent);
			}
		}
	}
	
}
