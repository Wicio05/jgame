package jgame.renderer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera 
{
    private Matrix4f projection;
    private Matrix4f view;
    public Vector2f position;

    public Camera(Vector2f position)
    {
        this.projection = new Matrix4f();
        this.view = new Matrix4f();
        this.position = position;
        adjustProjection();
    }

    public void adjustProjection()
    {
        projection.identity();
        projection.ortho(0.0f, 32.0f * 40.0f, 0.0f, 32.0f * 21.0f, 0.0f, 100.0f);
    }

    public Matrix4f getView()
    {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        
        view.identity();
        view.lookAt(new Vector3f(position.x, position.y, 20.0f), cameraFront.add(position.x, position.y, 0.0f), cameraUp);

        return view;
    }

    public Matrix4f getProjection()
    {
        return projection;
    }
}
