package core;

import java.awt.geom.Point2D;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import core.keyboard.Keybinds;
import core.setups.Stage;

public class Input {
	
	/** Global state of mouse press */
	public static boolean mousePressed;
	/** Global state of mouse hold */
	public static boolean mouseHeld;
	/** Location of mouse press */
	public static Point2D mouseClick;
	/** Location of mouse release */
	public static Point2D mouseRelease;
	
	/**
	 * Main processing of any and all input depending on current setup.
	 * @param setup The current setup of the game
	 */
	public static void checkInput(Stage stage) {
		// Refresh key bind presses
		Keybinds.update();
		
		// Enter debug mode
		if(Keybinds.DEBUG.clicked()) {
			Theater.FLY_MODE = !Theater.FLY_MODE;
			//Theater.get().debug = !Theater.get().debug;
		}
		
		// Refresh mouse clicks
		if(Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) {
			mousePress();
		} else {
			mouseRelease();
		}
		
		// Setup specific processing
		if(Keybinds.PAUSE.clicked()) {
			//Theater.get().pause();
		}
		
		//if(Mouse.isGrabbed()) {
			if(Mouse.isButtonDown(2)) {
				//Camera.get().rotate(new Vector3f(0, 0, -Mouse.getDY() + Mouse.getDX()));
				//Camera.get().rotate(new Vector3f(0, 0, Mouse.getDX()));
			} else {
				Mouse.getDY();
				Mouse.getDX();
			}
			if(Mouse.hasWheel()) {
				float wheel = -Theater.getDeltaSpeed(Mouse.getDWheel() / 1200f);
				if(Camera.get().scale + wheel >= 0.75f && Camera.get().scale + wheel <= 3f)
					Camera.get().scale += wheel;
				else if(Camera.get().scale + wheel >= 3f)
					Camera.get().scale = 3f;
				else
					Camera.get().scale = 0.75f;
				//System.out.println(Camera.get().offset);
			}
		//}
		
		if(Keybinds.D.press()) {
			stage.player.moveRight();
		}
		if(Keybinds.A.press()) {
			stage.player.moveLeft();
		}
		if(Keybinds.JUMP.press()) {
			stage.player.jump();
		} else if(stage.player.isAirborne()) {
			stage.player.jumpTimer = 0.4f;
		}
		if(Keybinds.S.press()) {
			stage.player.fastFall();
		}
		
		if(Keybinds.UP.press()) {
			Camera.get().rotate(new Vector3f(1f, 0, 0));
		}
		if(Keybinds.DOWN.press()) {
			Camera.get().rotate(new Vector3f(-1f, 0, 0));
		}
		if(Keybinds.LEFT.press()) {
			Camera.get().rotate(new Vector3f(0, 1f, 0));
		}
		if(Keybinds.RIGHT.press()) {
			Camera.get().rotate(new Vector3f(0, -1f, 0));
		}
		if(Keybinds.STRAFE_LEFT.press()) {
			Camera.get().rotate(new Vector3f(0, 0, -1f));
		}
		if(Keybinds.STRAFE_RIGHT.press()) {
			Camera.get().rotate(new Vector3f(0, 0, 1f));
		}
		
		if(Keybinds.CANCEL.clicked()) {
			Camera.get().cameraAngle = new Vector3f(0,0,0);
		}
	}
	
	/**
	 * Detect whether or not the mouse was pressed.
	 */
	public static void mousePress() {
		if(!mousePressed) {
			mousePressed = true;
			mouseHeld = true;
			//mouseClick = new Point2D.Double(Mouse.getX(), -(Mouse.getY() - Camera.get().HEIGHT));
		}
	}
	
	/**
	 * Detect whether or not the mouse was released.
	 */
	public static void mouseRelease() {
		if(mousePressed) {
			mousePressed = false;
			mouseHeld = false;
			//mouseRelease = new Point2D.Double(Mouse.getX(), -(Mouse.getY() - Camera.get().displayHeight));
		}
	}
	
	/**
	 * Clear the current mouse positions
	 */
	public static void clearMouse() {
		mouseClick = null;
		mouseRelease = null;
	}
	
	/**
	 * @return Location of mouse
	 */
	public static Point2D getCurrentMouse() {
		//return new Point2D.Double(Mouse.getX(), -(Mouse.getY() - Camera.get().displayHeight));
		return null;
	}
	
}
