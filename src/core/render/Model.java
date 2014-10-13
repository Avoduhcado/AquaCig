package core.render;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import core.Camera;

public class Model {

	protected VertexData[] vertices;
	
	public Vector3f modelPos = new Vector3f(0, 0, 0);
	public Vector3f modelAngle = new Vector3f(0, 0, 0);
	protected Vector3f modelScale = new Vector3f(1, 1, 1);
	protected Matrix4f modelMatrix = null;
	
	protected Texture[] textures;
	protected String[] textureNames;
	protected int currentTexture = 0;
	protected int shader;
	protected int vaoId;
	protected int vboId;
	protected int vboiId;
	protected ByteBuffer verticesByteBuffer;
	protected int indicesCount;
	
	protected int frame = 0;
	protected int maxFrame = 1;
	protected int direction = 1;
	protected int maxDirection = 1;
	protected float animStep = 0f;
	
	public Model(String[] textures) {
		this.textures = new Texture[textures.length];
		this.textureNames = textures;
		
		for(int x = 0; x<textures.length; x++) {
			/*try {
				this.textures[x] = TextureLoader.getTexture("PNG",
						ResourceLoader.getResourceAsStream(System.getProperty("resources") + "/textures/" + this.textureNames[x]));
			} catch (IOException e) {
				e.printStackTrace();
			}*/
			this.textures[x] = TextureList.loadTexture(this.textureNames[x]);
		}
		
		if(textures[currentTexture].contains("_")) {
			loadFrames(textures[currentTexture].substring(textures[currentTexture].indexOf("_") + 1, textures[currentTexture].indexOf(".")));
		}
		
		setupShaders();
		setupVertices();
	}
	
	public void update() {
		modelMatrix = new Matrix4f();
		
		Matrix4f.scale(modelScale, modelMatrix, modelMatrix);
		Matrix4f.translate(modelPos, modelMatrix, modelMatrix);
		Matrix4f.rotate((float)Math.toRadians(modelAngle.z), new Vector3f(0, 0, 1), modelMatrix, modelMatrix);
		Matrix4f.rotate((float)Math.toRadians(modelAngle.y), new Vector3f(0, 1, 0), modelMatrix, modelMatrix);
		Matrix4f.rotate((float)Math.toRadians(modelAngle.x), new Vector3f(1, 0, 0), modelMatrix, modelMatrix);
		
		// Upload matrices to the uniform variables
		GL20.glUseProgram(shader);
		
		Camera.get().projectionMatrix.store(Camera.get().matrix44Buffer); 
		Camera.get().matrix44Buffer.flip();
		GL20.glUniformMatrix4(Camera.get().projectionMatrixLocation, false, Camera.get().matrix44Buffer);
		Camera.get().viewMatrix.store(Camera.get().matrix44Buffer);
		Camera.get().matrix44Buffer.flip();
		GL20.glUniformMatrix4(Camera.get().viewMatrixLocation, false, Camera.get().matrix44Buffer);
		modelMatrix.store(Camera.get().matrix44Buffer); 
		Camera.get().matrix44Buffer.flip();
		GL20.glUniformMatrix4(Camera.get().modelMatrixLocation, false, Camera.get().matrix44Buffer);
		
		GL20.glUseProgram(0);
	}
	
	public void draw() {
		GL20.glUseProgram(shader);
		
		// Bind the texture
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		textures[currentTexture].bind();
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		
		// Bind to the VAO that has all the information about the vertices
		GL30.glBindVertexArray(vaoId);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		// Bind to the index VBO that has all the information about the order of the vertices
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
		
		for(int x = 1; x<textures.length; x++) {
			// Draw the vertices
			GL11.glDrawElements(GL11.GL_TRIANGLES, indicesCount / textures.length, GL11.GL_UNSIGNED_BYTE, 0);
			
			textures[currentTexture + 1].bind();
			GL11.glDrawElements(GL11.GL_TRIANGLES, indicesCount, GL11.GL_UNSIGNED_BYTE, 0);
		}
		
		// Put everything back to default (deselect)
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
		
		GL20.glUseProgram(0);
	}
	
	public void loadFrames(String texture) {
		String[] temp = texture.split("_");
		maxFrame = Integer.parseInt(temp[0]);
		if(temp.length > 1)
			maxDirection = Integer.parseInt(temp[1]);
	}
	
	public void setupShaders() {
		// Load the vertex shader
		int vsId = this.loadShader(System.getProperty("resources") + "/shaders/vertex.glsl", GL20.GL_VERTEX_SHADER);
		// Load the fragment shader
		int fsId = this.loadShader(System.getProperty("resources") + "/shaders/fragment.glsl", GL20.GL_FRAGMENT_SHADER);

		// Create a new shader program that links both shaders
		shader = GL20.glCreateProgram();
		GL20.glAttachShader(shader, vsId);
		GL20.glAttachShader(shader, fsId);

		// Position information will be attribute 0
		GL20.glBindAttribLocation(shader, 0, "in_Position");
		// Color information will be attribute 1
		GL20.glBindAttribLocation(shader, 1, "in_Color");
		// Textute information will be attribute 2
		GL20.glBindAttribLocation(shader, 2, "in_TextureCoord");

		GL20.glLinkProgram(shader);
		GL20.glValidateProgram(shader);

		// Get matrices uniform locations
		Camera.get().projectionMatrixLocation = GL20.glGetUniformLocation(shader, "projectionMatrix");
		Camera.get().viewMatrixLocation = GL20.glGetUniformLocation(shader, "viewMatrix");
		Camera.get().modelMatrixLocation = GL20.glGetUniformLocation(shader, "modelMatrix");
	}
	
	protected int loadShader(String filename, int type) {
		StringBuilder shaderSource = new StringBuilder();
		int shaderID = 0;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Could not read file.");
			e.printStackTrace();
			System.exit(-1);
		}
		
		shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
				
		return shaderID;
	}
	
	public void setupVertices() {
		// We'll define our quad using 4 vertices of the custom 'TexturedVertex' class
		VertexData v0 = new VertexData(); {
			v0.setXYZ(-1f, 1f, 0);
			v0.setRGB(1, 0, 0);
			v0.setST(0, 0);
		}
		VertexData v1 = new VertexData(); {
			v1.setXYZ(-1f, -1f, 0);
			v1.setRGB(0, 1, 0);
			v1.setST(0, this.textures[0].getHeight());
		}
		VertexData v2 = new VertexData(); {
			v2.setXYZ(0.5f, -1f, 0);
			v2.setRGB(0, 0, 1);
			v2.setST(this.textures[0].getWidth(), this.textures[0].getHeight());
		}

		VertexData v3 = new VertexData(); {
			v3.setXYZ(0.5f, 1f, 0);
			v3.setRGB(1, 1, 1);
			v3.setST(this.textures[0].getWidth(), 0);
		}

		// Back of the model
		VertexData v4 = new VertexData(); {
			v4.setXYZ(0.5f, 1f, -0.001f);
			v4.setRGB(1, 0, 0);
			v4.setST(0, 0);
		}
		VertexData v5 = new VertexData(); {
			v5.setXYZ(0.5f, -1f, -0.001f);
			v5.setRGB(0, 1, 0);
			v5.setST(0, this.textures[0].getHeight());
		}
		VertexData v6 = new VertexData(); {
			v6.setXYZ(-1f, -1f, -0.001f);
			v6.setRGB(0, 0, 1);
			v6.setST(this.textures[0].getWidth(), this.textures[0].getHeight());
		}

		VertexData v7 = new VertexData(); {
			v7.setXYZ(-1f, 1f, -0.001f);
			v7.setRGB(1, 1, 1);
			v7.setST(this.textures[0].getWidth(), 0);
		}

		vertices = new VertexData[] {v0, v1, v2, v3, v4, v5, v6, v7};

		// Put each 'Vertex' in one FloatBuffer
		verticesByteBuffer = BufferUtils.createByteBuffer(vertices.length * 
				VertexData.stride);				
		FloatBuffer verticesFloatBuffer = verticesByteBuffer.asFloatBuffer();
		for (int i = 0; i < vertices.length; i++) {
			// Add position, color and texture floats to the buffer
			verticesFloatBuffer.put(vertices[i].getElements());
		}
		verticesFloatBuffer.flip();

		byte[] indices = {
				0, 1, 2,
				2, 3, 0,
				4, 5, 6,
				6, 7, 4
		};
		indicesCount = indices.length;
		ByteBuffer indicesBuffer = BufferUtils.createByteBuffer(indicesCount);
		indicesBuffer.put(indices);
		indicesBuffer.flip();

		// Create a new Vertex Array Object in memory and select it (bind)
		vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoId);

		// Create a new Vertex Buffer Object in memory and select it (bind)
		vboId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesFloatBuffer, GL15.GL_STREAM_DRAW);

		// Put the position coordinates in attribute list 0
		GL20.glVertexAttribPointer(0, VertexData.positionElementCount, GL11.GL_FLOAT, 
				false, VertexData.stride, VertexData.positionByteOffset);
		// Put the color components in attribute list 1
		GL20.glVertexAttribPointer(1, VertexData.colorElementCount, GL11.GL_FLOAT, 
				false, VertexData.stride, VertexData.colorByteOffset);
		// Put the texture coordinates in attribute list 2
		GL20.glVertexAttribPointer(2, VertexData.textureElementCount, GL11.GL_FLOAT, 
				false, VertexData.stride, VertexData.textureByteOffset);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		// Deselect (bind to 0) the VAO
		GL30.glBindVertexArray(0);

		// Create a new VBO for the indices and select it (bind) - INDICES
		vboiId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, 
				GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

		// Set the default quad rotation, scale and position values
		modelPos = new Vector3f(0, 0, 0);
		modelAngle = new Vector3f(0, 0, 0);
		modelScale = new Vector3f(1, 1, 1);

		modelMatrix = new Matrix4f();
	}
	
	public void translate(float x, float y, float z) {
		modelPos.z += z;
		modelPos.y += y;
		modelPos.x += x;
	}
	
	public void rotate(Vector3f angle) {
		modelAngle.x += angle.x;
		modelAngle.y += angle.y;
		modelAngle.z += angle.z;
	}
	
	public float getWidth() {
		// TODO
		return 0;
	}
	
	public float getHeight() {
		// TODO
		return 0;
	}
	
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
}
