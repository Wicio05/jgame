package jgame;

import jgame.renderer.Camera;

public abstract class Scene 
{
    protected Camera camera;

    public Scene()
    {

    }

    public void init()
    {

    }

    public abstract void update(float dt);

    public abstract void render();
}
