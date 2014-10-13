package core.entities;

import org.lwjgl.util.vector.Vector3f;

public class Platform extends Entity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6457112459598616140L;
	private int leftHeight, rightHeight;
	private boolean oneWay;

	public Platform(float x, float y, String ref, float width, float height) {
		super(x, y, ref);
		this.model.scale(new Vector3f(width, height, 0));
		updateBox();
	}
	
	public boolean isSlope() {
		return (leftHeight != 0 || rightHeight != 0);
	}
	
	public int getSlope() {
		if(leftHeight == 32 && rightHeight == 0) {
			return 1;
		} else if(leftHeight == 32 && rightHeight == 16) {
			return 2;
		} else if(leftHeight == 16 && rightHeight == 0) {
			return 3;
		} else if(leftHeight == 0 && rightHeight == 32) {
			return 4;
		} else if(leftHeight == 16 && rightHeight == 32) {
			return 5;
		} else if(leftHeight == 0 && rightHeight == 16) {
			return 6;
		}
		
		return 0;
	}
	
	public void setSlope(int leftHeight, int rightHeight) {
		this.leftHeight = leftHeight;
		this.rightHeight = rightHeight;
	}

	public boolean isOneWay() {
		return oneWay;
	}
	
	public void setOneWay(boolean oneWay) {
		this.oneWay = oneWay;
	}
	
}
