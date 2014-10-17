package core.render;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import core.Camera;

public class RawTile {

	public VertexData[] vertices;

	public int shader;
	public int vaoId;
	public int vboId;
	public int vboiId;
	public ByteBuffer verticesByteBuffer;
	public int indicesCount;
	
	public RawTile(int width, int height) {
		setupShaders();
		setupVertices(width, height);
	}
	
	public void setupShaders() {
		// Load the vertex shader
		int vsId = this.loadShader(System.getProperty("resources") + "/shaders/orthoVertex.glsl", GL20.GL_VERTEX_SHADER);
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
		Camera.get().orthoMatrixLocation = GL20.glGetUniformLocation(shader, "projectionMatrix");
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
	
	public void setupVertices(int width, int height) {
		// We'll define our quad using 4 vertices of the custom 'TexturedVertex' class
		VertexData v0 = new VertexData(); {
			v0.setXYZ(0, 0, 0);
			v0.setRGB(1, 0, 0);
			v0.setST(0, 0);
		}
		VertexData v1 = new VertexData(); {
			v1.setXYZ(0, height, 0);
			v1.setRGB(0, 1, 0);
			v1.setST(0, 1f);
		}
		VertexData v2 = new VertexData(); {
			v2.setXYZ(width, height, 0);
			v2.setRGB(0, 0, 1);
			v2.setST(1f, 1f);
		}

		VertexData v3 = new VertexData(); {
			v3.setXYZ(width, 0, 0);
			v3.setRGB(1, 1, 1);
			v3.setST(1f, 0);
		}

		vertices = new VertexData[] {v0, v1, v2, v3};

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
	}
	
}
