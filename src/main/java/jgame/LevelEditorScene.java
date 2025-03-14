package jgame;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class LevelEditorScene extends Scene
{
    private Shader shader;

    private VertexBuffer vbo;
    
    private IndexBuffer ibo;

    public LevelEditorScene()
    {
        this.shader = new Shader();
        this.vbo = new VertexBuffer();
        this.ibo = new IndexBuffer();
    }

    @Override
    public void init()
    {
        shader.compile();
        shader.link();
    }

    @Override
    public void update(float dt)
    {

    }

    @Override
    public void render()
    {
        // bind shader program
        glUseProgram(shader.getProgId());

        // bind vao
        vbo.bind();

        // enable atrributes
        vbo.enableAttributes();

        // draw
        glDrawElements(GL_TRIANGLES, ibo.getSize(), GL_UNSIGNED_INT, 0);

        // unbind everything
        vbo.disableAttributes();

        vbo.unbind();

        glUseProgram(0);
    }
}
