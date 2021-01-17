package com.bcheidemann.autocrafting;

public class BlockUpdate {
	
	private BlockState oldBlockState;
	private BlockState newBlockState;
	
	public boolean wasUpdated;
	
	public BlockUpdate() {
		this.wasUpdated = false;
	}
	
	public BlockUpdate(BlockState oldBlockState, BlockState newBlockState) {
		this.wasUpdated = true;
		
		this.oldBlockState = oldBlockState;
		this.newBlockState = newBlockState;
	}
	
	public BlockState getOldBlockState() {
		return this.oldBlockState;
	}

	public BlockState getNewBlockState() {
		return this.newBlockState;
	}

}
