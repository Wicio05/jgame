package jgame;

public class LevelScene extends Scene
{
    public LevelScene()
    {
        System.out.println("Inside a level scene");
        Window.get().r = 1.0f;
        Window.get().g = 1.0f;
        Window.get().b = 1.0f;
    }

    @Override
    public void update(float dt)
    {

    }
}
