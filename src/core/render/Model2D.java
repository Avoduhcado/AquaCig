package core.render;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import core.Camera;
import core.Theater;

public class Model2D extends Model {
	
	public Model2D(String[] textures) {
		super(textures);
		
		/*if(textures[currentTexture].contains("_")) {
			loadFrames(textures[currentTexture].substring(textures[currentTexture].indexOf("_") + 1, textures[currentTexture].indexOf(".")));
		}
		setupShaders();
		setupVertices();*/
	}
	
	@Override
	public void update() {
		if(maxDirection > 1 || maxFrame > 1) {
			vertices[0].setST((textures[currentTexture].getWidth() / maxFrame) * frame,
					(textures[currentTexture].getHeight() / maxDirection) * (direction - 1));
			vertices[1].setST((textures[currentTexture].getWidth() / maxFrame) * frame,
					(textures[currentTexture].getHeight() / maxDirection) * direction);
			vertices[2].setST((textures[currentTexture].getWidth() / maxFrame) * (frame + 1),
					(textures[currentTexture].getHeight() / maxDirection) * direction);
			vertices[3].setST((textures[currentTexture].getWidth() / maxFrame) * (frame + 1),
					(textures[currentTexture].getHeight() / maxDirection) * (direction - 1));
			
			verticesByteBuffer = BufferUtils.createByteBuffer(vertices.length * 
					VertexData.stride);				
			FloatBuffer verticesFloatBuffer = verticesByteBuffer.asFloatBuffer();
			for (int i = 0; i < vertices.length; i++) {
				// Add position, color and texture floats to the buffer
				verticesFloatBuffer.put(vertices[i].getElements());
			}
			verticesFloatBuffer.flip();
			
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesFloatBuffer, GL15.GL_STREAM_DRAW);
			GL20.glVertexAttribPointer(2, VertexData.textureElementCount, GL11.GL_FLOAT, 
					false, VertexData.stride, VertexData.textureByteOffset);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		}
		
		modelMatrix = new Matrix4f();
		
		Matrix4f.scale(modelScale, modelMatrix, modelMatrix);
		Matrix4f.translate(new Vector3f(modelPos.x, modelPos.y, 0), modelMatrix, modelMatrix);
		//Matrix4f.rotate((float)Math.toRadians(Camera.get().cameraAngle.z), new Vector3f(0, 0, 1), modelMatrix, modelMatrix);
		//Matrix4f.rotate((float)Math.toRadians(Camera.get().cameraAngle.y), new Vector3f(0, 1, 0), modelMatrix, modelMatrix);
		//Matrix4f.rotate((float)Math.toRadians(Camera.get().cameraAngle.x), new Vector3f(1, 0, 0), modelMatrix, modelMatrix);
		//Matrix4f.translate(new Vector3f(0, 0, 2f), modelMatrix, modelMatrix);
		
		// Upload matrices to the uniform variables
		GL20.glUseProgram(shader);
		
		Camera.get().orthoMatrix.store(Camera.get().matrix44Buffer); 
		Camera.get().matrix44Buffer.flip();
		GL20.glUniformMatrix4(Camera.get().orthoMatrixLocation, false, Camera.get().matrix44Buffer);
		modelMatrix.store(Camera.get().matrix44Buffer); 
		Camera.get().matrix44Buffer.flip();
		GL20.glUniformMatrix4(Camera.get().modelMatrixLocation, false, Camera.get().matrix44Buffer);
		
		GL20.glUseProgram(0);
	}
	
	@Override
	public void draw() {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		GL20.glUseProgram(shader);
		
		// Bind the texture
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		textures[currentTexture].bind();
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		
		// Bind to the VAO that has all the information about the vertices
		GL30.glBindVertexArray(vaoId);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		// Bind to the index VBO that has all the information about the order of the vertices
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);

		// Draw the vertices
		GL11.glDrawElements(GL11.GL_TRIANGLES, indicesCount, GL11.GL_UNSIGNED_BYTE, 0);
		
		// Put everything back to default (deselect)
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
		
		GL20.glUseProgram(0);
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	public void animate(boolean loop) {
		animStep += Theater.getDeltaSpeed(0.025f);
		if(animStep >= 0.16f) {
			animStep = 0f;
			frame++;
			if(frame >= maxFrame && loop) {
				frame = 0;
			} else if(frame >= maxFrame && !loop) {
				if(maxFrame > 2)
					frame = maxFrame - 2;
				else
					frame = maxFrame - 1;
			}
		}
	}
	
	@Override
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
	
	public void setupVertices() {
		VertexData v0 = new VertexData(); {
			v0.setXYZ(0f, 0f, 0);
			v0.setRGB(1, 0, 0);
			v0.setST(0, 0);
		}
		VertexData v1 = new VertexData(); {
			v1.setXYZ(0f, textures[currentTexture].getImageHeight() / maxDirection, 0);
			v1.setRGB(0, 1, 0);
			v1.setST(0, textures[currentTexture].getHeight() / maxDirection);
		}
		VertexData v2 = new VertexData(); {
			v2.setXYZ(textures[currentTexture].getImageWidth() / maxFrame, textures[currentTexture].getImageHeight() / maxDirection, 0);
			v2.setRGB(0, 0, 1);
			v2.setST(textures[currentTexture].getWidth() / maxFrame, textures[currentTexture].getHeight() / maxDirection);
		}

		VertexData v3 = new VertexData(); {
			v3.setXYZ(textures[currentTexture].getImageWidth() / maxFrame, 0f, 0);
			v3.setRGB(1, 1, 1);
			v3.setST(textures[currentTexture].getWidth() / maxFrame, 0);
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
		
		// Set the default quad rotation, scale and position values
		modelPos = new Vector3f(0, 0, 0);
		modelAngle = new Vector3f(0, 0, 0);
		modelScale = new Vector3f(1, 1, 1);

		modelMatrix = new Matrix4f();
	}
	
	public void scale(Vector3f scale) {
		/*modelScale.x = scale.x;
		modelScale.y = scale.y;
		modelScale.z = scale.z;*/
		
		vertices[0].setXYZ(0, 0, 0);
		vertices[1].setXYZ(0, (textures[currentTexture].getImageHeight() / maxDirection) * scale.y, 0);
		vertices[1].setST(0, (textures[currentTexture].getHeight() / maxDirection) * scale.y);
		vertices[2].setXYZ((textures[currentTexture].getImageWidth() / maxFrame) * scale.x,
				(textures[currentTexture].getImageHeight() / maxDirection) * scale.y, 0);
		vertices[2].setST((textures[currentTexture].getWidth() / maxFrame) * scale.x,
				(textures[currentTexture].getHeight() / maxDirection) * scale.y);
		vertices[3].setXYZ((textures[currentTexture].getImageWidth() / maxFrame) * scale.x, 0, 0);
		vertices[3].setST((textures[currentTexture].getWidth() / maxFrame) * scale.x, 0);
		
		verticesByteBuffer = BufferUtils.createByteBuffer(vertices.length * 
				VertexData.stride);				
		FloatBuffer verticesFloatBuffer = verticesByteBuffer.asFloatBuffer();
		for (int i = 0; i < vertices.length; i++) {
			// Add position, color and texture floats to the buffer
			verticesFloatBuffer.put(vertices[i].getElements());
		}
		verticesFloatBuffer.flip();
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesFloatBuffer, GL15.GL_STREAM_DRAW);
		GL20.glVertexAttribPointer(2, VertexData.textureElementCount, GL11.GL_FLOAT, 
				false, VertexData.stride, VertexData.textureByteOffset);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	@Override
	public float getWidth() {
		return (vertices[2].getXYZ()[0] - vertices[0].getXYZ()[0]) * modelScale.x;
	}
	
	@Override
	public float getHeight() {
		return (vertices[2].getXYZ()[1] - vertices[0].getXYZ()[1]) * modelScale.y;
	}
	
	public void setCurrentTexture(int currentTexture) {
		this.currentTexture = currentTexture;
		if(textureNames[currentTexture].contains("_")) {
			loadFrames(textureNames[currentTexture].substring(textureNames[currentTexture].indexOf("_") + 1,
					textureNames[currentTexture].indexOf(".")));
		}
	}

}
