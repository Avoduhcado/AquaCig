package core;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import core.setups.Stage;
import core.keyboard.Keybinds;

public class Theater {

	private Stage stage;
	
	private float delta;
	private float deltaMax = 25f;
	private long currentTime;
	private long lastLoopTime;
	public static int fps = 0;
	private int currentfps = Camera.TARGET_FPS;
	
	public static boolean FLY_MODE = false;
	
	private static Theater theater;
	
	public static void init() {
		theater = new Theater();
	}
	
	public static Theater get() {
		return theater;
	}
	
	public Theater() {
		Camera.init();
		stage = new Stage();
	}
	
	public void play() {
		currentTime = getTime();
		
		while(!Display.isCloseRequested()) {
			getFps();
			Input.checkInput(stage);
			stage.update();
			Camera.get().update();
			Camera.get().draw();
			
			if(Keybinds.EXIT.clicked()) {
				Mouse.setGrabbed(false);
			}
		}
	}
	
	public Stage getStage() {
		return stage;
	}
	
	public void getFps() {
		delta = getTime() - currentTime;
		currentTime = getTime();
		lastLoopTime += delta;
		fps++;
		//System.out.println(delta);
		if(lastLoopTime >= 1000) {
			Camera.get().updateHeader();
			currentfps = fps;
			fps = 0;
			lastLoopTime = 0;
		}
	}
	
	public static long getTime() {
		return System.nanoTime() / 1000000;
	}
	
	public static float getDeltaSpeed(float speed) {
		return ((1000f / Theater.get().currentfps) * speed) / Theater.get().deltaMax;
		//return (Theater.get().delta * speed) / Theater.get().deltaMax;
	}
		
	public static void main(String[] args) {
		if(System.getProperty("os.name").startsWith("Windows")) {
			System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/native/windows");
		} else if(System.getProperty("os.name").startsWith("Mac")) {
			System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/native/macosx");
		} else if(System.getProperty("os.name").startsWith("Linux")) {
			System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/native/linux");
		} else {
			System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/native/solaris");
		}
		System.setProperty("resources", System.getProperty("user.dir") + "/resources");
		
		//SerialBox b = new SerialBox();
		//SerialBox.deserialize();
				
		Theater.init();
		Theater.get().play();
	}
	
}
