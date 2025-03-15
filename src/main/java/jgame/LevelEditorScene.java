package jgame;

import java.awt.event.KeyEvent;

import org.joml.Vector2f;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import jgame.event.KeyListener;
import jgame.renderer.Camera;
import jgame.renderer.IndexBuffer;
import jgame.renderer.Shader;
import jgame.renderer.Texture;
import jgame.renderer.VertexBuffer;

public class LevelEditorScene extends Scene
{
    private Shader shader;

    private Texture texture;

    private VertexBuffer vbo;
    
    private IndexBuffer ibo;

    public LevelEditorScene()
    {
        this.shader = new Shader("assets/shaders/defaultVert.glsl", "assets/shaders/defaultFrag.glsl");
        this.texture = new Texture("assets/images/testImage.png");
        this.vbo = new VertexBuffer();
        this.ibo = new IndexBuffer();
        this.camera = new Camera(new Vector2f());
    }

    @Override
    public void init()
    {
        shader.compile();
    }

    @Override
    public void update(float dt)
    {
        if(KeyListener.isKeyPressed(KeyEvent.VK_A))
        {
            camera.position.x -= 1.0f;
        }

        if(KeyListener.isKeyPressed(KeyEvent.VK_D))
        {
            camera.position.x += 1.0f;
        }

        if(KeyListener.isKeyPressed(KeyEvent.VK_W))
        {
            camera.position.y += 1.0f;
        }

        if(KeyListener.isKeyPressed(KeyEvent.VK_S))
        {
            camera.position.y -= 1.0f;
        }
    }

    @Override
    public void render()
    {
        shader.use();
        shader.uploadMat4f("uProjection", camera.getProjection());
        shader.uploadMat4f("uView", camera.getView());

        shader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        texture.bind();

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
