package jgame;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;

import jgame.renderer.IndexBuffer;
import jgame.renderer.Shader;
import jgame.renderer.VertexBuffer;

public class LevelEditorScene extends Scene
{
    private Shader shader;

    private VertexBuffer vbo;
    
    private IndexBuffer ibo;

    public LevelEditorScene()
    {
        this.shader = new Shader("assets/shaders/defaultVert.glsl", "assets/shaders/defaultFrag.glsl");
        this.vbo = new VertexBuffer();
        this.ibo = new IndexBuffer();
    }

    @Override
    public void init()
    {
        shader.compile();
    }

    @Override
    public void update(float dt)
    {

    }

    @Override
    public void render()
    {
        shader.use();

        // bind vao
        vbo.bind();

        // enable atrributes
        vbo.enableAttributes();

        // draw
        glDrawElements(GL_TRIANGLES, ibo.getSize(), GL_UNSIGNED_INT, 0);

        // unbind everything
        vbo.disableAttributes();

        vbo.unbind();

        shader.detach();
    }
}
