package com.bcheidemann.autocrafting;

import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BlockUpdateEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    
    private BlockUpdate blockUpdate;
    private Block block;

    public BlockUpdateEvent(BlockUpdate blockUpdate, Block block) {
    	this.blockUpdate = blockUpdate;
    	this.block = block;
    }
    
    public BlockUpdate getBlockUpdate() {
    	return this.blockUpdate;
    }
    
    public Block getBlock() {
    	return this.block;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
