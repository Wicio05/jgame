package jgame.renderer;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class VertexBuffer 
{
    private float[] vertexArray = 
    {
         50.5f, -50.5f,     0.0f, 0.5f, 0.5f, 0.5f, 1.0f,   1, 1,   // bottom right
        -50.5f,  50.5f,     0.0f, 1.0f, 0.0f, 0.0f, 1.0f,   0, 0,   // top left
         50.5f,  50.5f,     0.0f, 0.0f, 1.0f, 0.0f, 1.0f,   1, 0,   // top right
        -50.5f, -50.5f,     0.0f, 0.0f, 0.0f, 1.0f, 1.0f,   0, 1,   // bottom left
    };

    private int vaoId;
    private int vboId;

    public VertexBuffer()
    {
        // generate vao
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // create float buffer
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // generate vbo
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // add vertex attribute pointers
        int posSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSize = (posSize + colorSize + uvSize) * Float.BYTES;

        // specify the position in the array at location 0
        glVertexAttribPointer(0, posSize, GL_FLOAT, false, vertexSize, 0);
        glEnableVertexAttribArray(0);

        // specify the color in the array at location 1
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSize, posSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        // specify texture in the array at location 2
        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSize, (posSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    public void enableAttributes()
    {
        // enable attributes
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
    }

    public void disableAttributes()
    {
        // dispable attributes
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
    }

    public void bind()
    {
        // bind vao
        glBindVertexArray(vaoId);
    }

    public void unbind()
    {
        // unbind vao
        glBindVertexArray(0);
    }
}
