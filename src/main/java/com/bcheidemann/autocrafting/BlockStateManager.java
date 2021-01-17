package com.bcheidemann.autocrafting;

import java.util.HashMap;

import org.bukkit.Location;

public class BlockStateManager {
	// Credit for this code goes to u/KineticRX
	// https://www.reddit.com/r/javahelp/comments/1hbq60/nested_hashmaps_is_it_a_bad_idea_or_just_a_sloppy/?utm_source=amp&utm_medium=&utm_content=post_body
	
	private HashMap<Integer, HashMap<Integer, HashMap<Integer, BlockState>>> storedCoords = new HashMap<Integer, HashMap<Integer, HashMap<Integer, BlockState>>>();

	public void setBlockState(Location location, BlockState o) {
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();
		
	    // Create a map entry of this z pointing to array o
	    HashMap<Integer, BlockState> zTOo = new HashMap<Integer, BlockState>();
	    zTOo.put(z, o);

	    // Create a map entry of this y pointing to the z->o map
	    HashMap<Integer, HashMap<Integer, BlockState>> yTOz = new HashMap<Integer, HashMap<Integer, BlockState>>();
	    yTOz.put(y, zTOo);

	    // Place new coordinate into the "master" map.
	    if (!this.storedCoords.containsKey(x)) {
	        this.storedCoords.put(x, yTOz);
	    } else if (!this.storedCoords.get(x).containsKey(y)) {
	    	this.storedCoords.get(x).put(y, zTOo);
	    } else if (!this.storedCoords.get(x).get(y).containsKey(z)) {
	    	this.storedCoords.get(x).get(y).put(z, o);
	    } else {
	    	this.storedCoords.get(x).get(y).replace(z, o);
	    }
	}

	public BlockState getBlockState(Location location) {
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();
		BlockState retVals = null;
	    if (this.storedCoords.containsKey(x)) {
	        if (this.storedCoords.get(x).containsKey(y)) {
	            if (this.storedCoords.get(x).get(y).containsKey(z)) {
	                retVals = this.storedCoords.get(x).get(y).get(z);
	            }
	        }
	    }
	    return retVals;
	}
	
	public BlockState deleteBlockState(Location location) {
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();
		if (this.storedCoords.containsKey(x) 
				&& this.storedCoords.get(x).containsKey(y)
				&& this.storedCoords.get(x).get(y).containsKey(z)) {
			return this.storedCoords.get(x).get(y).remove(z);
		}
		return null;
	}
	
	public BlockUpdate updateBlockState(Location location, BlockState newBlockState) {
		BlockState blockState = this.getBlockState(location);
		if (blockState == null) {
			this.setBlockState(location, newBlockState);
			return new BlockUpdate(new BlockState(0), newBlockState);
		} else {
			if (blockState.equals(newBlockState)) {
				return new BlockUpdate();
			} else {
				int x = location.getBlockX();
				int y = location.getBlockY();
				int z = location.getBlockZ();
				this.storedCoords.get(x).get(y).replace(z, newBlockState);
				return new BlockUpdate(blockState, newBlockState);
			}
		}
	}
}
