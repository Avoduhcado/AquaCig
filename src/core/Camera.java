package core;

import java.awt.geom.Rectangle2D;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import core.entities.Entity;

public class Camera {
	
	private final int WIDTH = 800;
	private final int HEIGHT = 600;
	public static final int TARGET_FPS = 60;
	
	public Rectangle2D frame = new Rectangle2D.Double(0, 0, WIDTH, HEIGHT);
	
	public int projectionMatrixLocation = 0;
	public int orthoMatrixLocation = 0;
	public int viewMatrixLocation = 0;
	public int modelMatrixLocation = 0;
	public Matrix4f projectionMatrix = null;
	public Matrix4f orthoMatrix = null;
	public Matrix4f viewMatrix = null;
	public Vector3f cameraPos = null;
	public Vector3f cameraAngle = null;
	public Vector3f cameraScale = null;
	public FloatBuffer matrix44Buffer = null;
	
	public Entity focus;
	public float scale = 1f;
		
	private static Camera camera;
	
	public static void init() {
		camera = new Camera();
	}
	
	public static Camera get() {
		return camera;
	}
	
	public Camera() {
		try {
			PixelFormat pixelFormat = new PixelFormat();
			ContextAttribs contextAtrributes = new ContextAttribs(3, 2)
				.withForwardCompatible(true)
				.withProfileCore(true);
			
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			updateHeader();
			Display.create(pixelFormat, contextAtrributes);
			
			//Mouse.setGrabbed(true);
			
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_STENCIL_TEST);
			
			GL11.glViewport(0, 0, WIDTH, HEIGHT);
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		// Setup an XNA like background color
		GL11.glClearColor(0.4f, 0.6f, 0.9f, 0f);
		
		// Map the internal OpenGL coordinate system to the entire screen
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		
		setupMatrices();
	}
	
	/*public static ByteBuffer[] loadIcon(String ref) throws IOException {
        InputStream fis = ResourceLoader.getResourceAsStream(ref);
        try {
            PNGDecoder decoder = new PNGDecoder(fis);
            ByteBuffer bb = ByteBuffer.allocateDirect(decoder.getWidth()*decoder.getHeight()*4);
            decoder.decode(bb, decoder.getWidth()*4, PNGDecoder.Format.RGBA);
            bb.flip();
            ByteBuffer[] buffer = new ByteBuffer[1];
            buffer[0] = bb;
            return buffer;
        } finally {
            fis.close();
        }
    }*/
	
	public void updateHeader() {
		Display.setTitle("HELLO, WELCOME TO PIXEL HELL" + "  FPS: " + Theater.fps);
	}
	
	public void update() {
		Display.sync(TARGET_FPS);
		Display.update();
		
		cameraPos = new Vector3f((float) -focus.getBox().getCenterX() + ((WIDTH) / 2f),
				(float) -focus.getBox().getCenterY() + (HEIGHT / 2f), 0);
		frame = new Rectangle2D.Double(cameraPos.x, -cameraPos.y, WIDTH * scale, HEIGHT * scale);
		
		// Setup orthogonal matrix
		float tx = (float) -((frame.getWidth() + ((-WIDTH / 1.5f) * (scale - 1f))) / (frame.getWidth() - ((-WIDTH / 1.5f) * (scale - 1f))));
		float ty = (float) -((((-HEIGHT / 1f) * (scale - 1f)) + frame.getHeight()) / (((-HEIGHT / 1f) * (scale - 1f)) - frame.getHeight()));
		float tz = -((-1 + 1) / (-1 - 1));

		orthoMatrix = new Matrix4f();
		orthoMatrix.m00 = (float) (2.0f / (frame.getWidth() - ((-WIDTH / 2f) * (scale - 1f))));
		orthoMatrix.m11 = (float) (2.0f / (((-HEIGHT / 2f) * (scale - 1f)) - frame.getHeight()));
		orthoMatrix.m22 = -2.0f / (-1 - 1);
		orthoMatrix.m30 = tx;
		orthoMatrix.m31 = ty;
		orthoMatrix.m32 = tz;
		orthoMatrix.m33 = 1.0f;
		
		//viewMatrix = new Matrix4f();
		
		// Translate camera
		Matrix4f.translate(new Vector3f(cameraPos.x, cameraPos.y, 0), orthoMatrix, orthoMatrix);
		//Matrix4f.rotate((float)Math.toRadians(cameraAngle.z), new Vector3f(0,0,1), orthoMatrix, orthoMatrix);
		//Matrix4f.rotate((float)Math.toRadians(cameraAngle.y), new Vector3f(0,1,0), viewMatrix, viewMatrix);
		//Matrix4f.rotate((float)Math.toRadians(cameraAngle.x), new Vector3f(1,0,0), viewMatrix, viewMatrix);
		//Matrix4f.translate(new Vector3f((float) -focus.getBox().getWidth() / 2f, (float) -focus.getBox().getHeight() / 2f, 0), orthoMatrix, orthoMatrix);
		
		/*if(!Mouse.isGrabbed()) {
			if(Mouse.isButtonDown(0) && Mouse.isInsideWindow()) {
				Mouse.setGrabbed(true);
			}
		}*/
	}
	
	public void draw() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		Theater.get().getStage().draw();		
	}
	
	private void setupMatrices() {
		// Setup projection matrix
		projectionMatrix = new Matrix4f();
		float fieldOfView = 60f;
		float aspectRatio = (float)WIDTH / (float)HEIGHT;
		float near_plane = 0.1f;
		float far_plane = 100f;

		float y_scale = this.coTangent((float) Math.toRadians(fieldOfView / 2f));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = far_plane - near_plane;

		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((far_plane + near_plane) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * near_plane * far_plane) / frustum_length);
		projectionMatrix.m33 = 0;
		
		// Setup orthogonal matrix
		float tx = -((WIDTH + 0) / (WIDTH - 0));
		float ty = -((0 + HEIGHT) / (0 - HEIGHT));
		float tz = -((-1 + 1) / (-1 - 1));

		orthoMatrix = new Matrix4f();
		orthoMatrix.m00 = 2.0f / (WIDTH - 0);
		orthoMatrix.m11 = 2.0f / (0 - HEIGHT);
		orthoMatrix.m22 = -2.0f / (-1 - 1);
		orthoMatrix.m30 = tx;
		orthoMatrix.m31 = ty;
		orthoMatrix.m32 = tz;
		orthoMatrix.m33 = 1.0f;

		// Setup view matrix
		viewMatrix = new Matrix4f();

		// Create a FloatBuffer with the proper size to store our matrices later
		matrix44Buffer = BufferUtils.createFloatBuffer(16);

		cameraPos = new Vector3f(0, -2f, -8.5f);
		cameraAngle = new Vector3f(0, 0, 0);
		cameraScale = new Vector3f(0, 0, 0);
	}
	
	private float coTangent(float angle) {
		return (float)(1f / Math.tan(angle));
	}
	
	public void translate(float x, float y, float z) {
		cameraPos.z += z;
		cameraPos.y += y;
		cameraPos.x += x;
	}
	
	public void rotate(Vector3f angle) {
		cameraAngle.x += angle.x;
		cameraAngle.y += angle.y;
		cameraAngle.z += angle.z;
	}
	
	public void scale(Vector3f scale) {
		cameraScale.x += scale.x;
		cameraScale.y += scale.y;
		cameraScale.z += scale.z;
	}
	
}
