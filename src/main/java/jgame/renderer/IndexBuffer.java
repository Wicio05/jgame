package jgame.renderer;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

public class IndexBuffer 
{
    private int[] indices = 
    {
        2, 1, 0,
        0, 1, 3
    };

    private int iboId;

    public IndexBuffer()
    {
        // create int buffer
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(indices.length);
        indexBuffer.put(indices).flip();

        // generate ibo
        iboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
    }

    public int getSize()
    {
        return indices.length;
    }
}
