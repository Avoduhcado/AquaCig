package core.entities;

import java.awt.geom.Rectangle2D;
import org.lwjgl.util.vector.Vector2f;

import core.Theater;
import core.render.Model2D;
import core.setups.Stage;
import core.utility.PropMap;

public class Actor extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1287998029455376001L;
	private boolean solid = true;
	private Vector2f velocity = null;
	private float dx, dy;
	private CharState state = CharState.IDLE;
	public float jumpTimer = 0f;
	public boolean dropping;
	
	public Actor(float x, float y, String[] ref) {
		super(x, y, ref[0]);
		model = new Model2D(ref);
		this.model.translate(x, y, 0);
		this.box = new Rectangle2D.Double(x, y, this.model.getWidth(), this.model.getHeight());
		
		velocity = new Vector2f(0, 0);
	}
	
	@Override
	public void update() {
		if(velocity.y + Theater.getDeltaSpeed(1f * Stage.gravity) <= 25f)
			velocity.y += Theater.getDeltaSpeed(1f * Stage.gravity);
		else
			velocity.y = 25f;

		if(velocity.x != 0) {
			if(velocity.x > 0) {
				velocity.x -= Theater.getDeltaSpeed(0.5f);
				if(velocity.x < 0)
					velocity.x = 0f;
			} else {
				velocity.x += Theater.getDeltaSpeed(0.5f);
				if(velocity.x > 0)
					velocity.x = 0f;
			}
		}
		
		checkCollision();
		if(velocity.x != 0 || velocity.y != 0)
			move();
		
		if(velocity.x != 0)
			model.setDirection(velocity.x < 0 ? 2 : 1);
		
		if(Math.abs(velocity.y) > 2f) {
		//if(velocity.y != 0) {
			if(velocity.y > 0)
				setState(CharState.FALLING);
			else
				setState(CharState.JUMPING);
		} else if(velocity.x != 0) {
			if(Math.abs(velocity.x) > 5f)
				setState(CharState.RUNNING);
			else
				setState(CharState.WALKING);
		} else {
			setState(CharState.IDLE);
		}
		
		model.animate(velocity.y == 0);
		super.update();
	}
	
	public void checkCollision() {
		Rectangle2D tempBox = new Rectangle2D.Double(getBox().getX(), getBox().getY(), getBox().getWidth(), getBox().getHeight());
		
		float xSteps = 0f;
		float maxX = Math.abs(Theater.getDeltaSpeed(velocity.x));
		dx = Theater.getDeltaSpeed(velocity.x);
		//float maxX = Math.abs(velocity.x);
		float ySteps = 0f;
		float maxY = Math.abs(Theater.getDeltaSpeed(velocity.y));
		dy = Theater.getDeltaSpeed(velocity.y);
		//float maxY = Math.abs(velocity.y);
		//boolean stepping = false;
		float tempY = 0f;
		
		while(xSteps < maxX || ySteps < maxY) {
			if(xSteps < maxX) {
				// Increment movement
				xSteps += maxX > 1f ? 1f : maxX;
				if(xSteps > maxX)
					xSteps = maxX;
				// Translate box with correct direction
				tempBox = new Rectangle2D.Double(getBox().getX() + (xSteps * (velocity.x / Math.abs(velocity.x))),
						getBox().getY() + tempY, getBox().getWidth(), getBox().getHeight());
				// Check collisions with entities
				for(core.tiled.Platform p : PropMap.getPlatformSectors(tempBox)) {
					if(p.intersects(tempBox, velocity)) {
						if(p.canStep(tempBox)) {
							dy = (float) -(box.getMaxY() - p.getBox().getY());
							velocity.y = 0;
							maxY = 0;
							tempY = dy;
						} else {
							dx = (velocity.x > 0 ? (float) (p.getBox().getX() - this.getBox().getMaxX()) :
								(float) (p.getBox().getMaxX() - this.getBox().getX()));
							velocity.x = 0;
							maxX = 0;
							break;
						}
					}
					/*if(p.intersects(tempBox)) {
						if(tempBox.getMaxY() - p.getY() > 4f || (p.getHeight() == 1 && box.getMaxY() < p.getMaxY())) {
							dx = (velocity.x > 0 ? (float) (p.getX() - this.getBox().getMaxX()) :
								(float) (p.getMaxX() - this.getBox().getX()));
							velocity.x = dx % 4;
							// Bounce physics
							//velocity.x = (xSteps - maxX) * (Math.abs(velocity.x) / velocity.x);
							maxX = 0;
							//if(stepping) {
							//	System.out.println("SDfsdfsdfsdf");
							//	tempY = 0f;
							//}
							break;
						} else {
							if(p.getHeight() != 1) {
								tempY = (float) -(tempBox.getMaxY() - p.getY());
							}
						}
					}*/
				}
			}
			
			if(ySteps < maxY) {
				ySteps += maxY > 1f ? 1f : maxY;
				if(ySteps > maxY)
					ySteps = maxY;
				tempBox = new Rectangle2D.Double(getBox().getX(), getBox().getY() + (ySteps * (Math.abs(velocity.y) / velocity.y)),
						getBox().getWidth(), getBox().getHeight());
				for(core.tiled.Platform p : PropMap.getPlatformSectors(tempBox)) {
					if(p.intersects(tempBox, velocity)) {
						if(p.isOneWay() && this.dropping) {
							dropping = false;
							break;
						} else {
							dy = (velocity.y > 0 ? (float) Math.floor(p.getBox().getY() - this.getBox().getMaxY()) :
								(float) (p.getBox().getMaxY() - this.getBox().getY()));
							if(velocity.y > 0 && dy == 0)
								jumpTimer = 0f;
							velocity.y = dy % 25;
							maxY = 0;
							break;
						}
					}
				}
				/*for(Rectangle2D p : PropMap.getMapSectors(tempBox)) {
					if(p.intersects(tempBox)) {
						if(p.getHeight() != 1 || (p.getHeight() == 1 && box.getMaxY() < p.getMaxY())) {
							//System.out.println("ySteps: " + ySteps + " maxY: " + maxY + " v.y: " + velocity.y + " dy: " + dy);
							dy = (velocity.y > 0 ? (float) (p.getY() - this.getBox().getMaxY()) :
								(float) (p.getMaxY() - this.getBox().getY()));
							if(velocity.y > 0 && dy == 0)
								jumpTimer = 0f;
							velocity.y = dy % 25;
							//velocity.y = (ySteps - maxY);
							//System.out.println(" v.y: " + velocity.y + " dy: " + dy + "\n");
							maxY = 0;
							break;
						}
					}
				}*/
			}
		}
		
		/*if(tempY != 0) {
			dy = tempY;
			velocity.y = dy % 25;
		}*/
	}
	
	public void move() {
		//System.out.println(model.modelPos.y + " " + Theater.getDeltaSpeed(velocity.y));
		model.modelPos.x += dx;
		model.modelPos.y += dy;
		
		updateBox();
	}
	
	public void moveLeft() {
		if(velocity.x - Theater.getDeltaSpeed(1f) >= -4f)
			velocity.x -= Theater.getDeltaSpeed(1f);
		else
			velocity.x = -4f;
	}
	
	public void moveRight() {
		if(velocity.x + Theater.getDeltaSpeed(1f) <= 4f)
			velocity.x += Theater.getDeltaSpeed(1f);
		else
			velocity.x = 4f;
	}
	
	public void jump() {
		if(jumpTimer + Theater.getDeltaSpeed(0.025f) < (0.4f / Stage.gravity) || Theater.FLY_MODE) {
			jumpTimer += Theater.getDeltaSpeed(0.025f);
			if(velocity.y - Theater.getDeltaSpeed(3f) >= -25f)
				velocity.y -= Theater.getDeltaSpeed(3f);
			else
				velocity.y = -25f;
		}
	}
	
	public void drop() {
		this.dropping = true;
	}
	
	public void fastFall() {
		if(velocity.y + 4f <= 5f)
			velocity.y += 4f;
		else
			velocity.y = 5f;
	}
	
	public void swapSprite(int sprite) {
		model.setCurrentTexture(sprite);
	}
	
	public void iterateState() {
		switch(state) {
		case IDLE:
			swapSprite(0);
			break;
		case WALKING:
			swapSprite(1);
			break;
		case RUNNING:
			swapSprite(2);
			break;
		case JUMPING:
			swapSprite(3);
			break;
		case FALLING:
			swapSprite(4);
			break;
		default:
			System.out.println("Undefined state " + state);
		}
	}

	public void setState(CharState state) {
		if(this.state != state) {
			this.state = state;
			iterateState();
		}
	}
	
	public boolean isAirborne() {
		return (this.state == CharState.FALLING || this.state == CharState.JUMPING);
	}
	
	public CharState getState() {
		return state;
	}
	
	public boolean isSolid() {
		return solid;
	}
	
	public void setSolid(boolean solid) {
		this.solid = solid;
	}

}
