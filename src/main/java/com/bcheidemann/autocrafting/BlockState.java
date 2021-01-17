package com.bcheidemann.autocrafting;

public class BlockState {
	private int blockPower;
	
	public BlockState(int power) {
		this.blockPower = power;
	}
	
	public boolean equals(BlockState blockState) {
		return (this.blockPower == blockState.blockPower);
	}
	
	public int getBlockPower() {
		return this.blockPower;
	}
}
