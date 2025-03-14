package jgame;

import java.awt.event.KeyEvent;

import jgame.event.KeyListener;

public class LevelEditorScene extends Scene
{
    private boolean changingScene = false;
    private float timeToChangeScene = 2.0f;

    public LevelEditorScene()
    {
        System.out.println("Inside level editor scene");
    }

    @Override
    public void update(float dt)
    {
        System.out.println("" + (1.0f / dt) + "FPS");

        if(!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE))
        {
            changingScene = true;
        }

        if(changingScene && timeToChangeScene > 0.0f)
        {
            timeToChangeScene -= dt;
            Window.get().r -= 5.0f * dt;
            Window.get().g -= 5.0f * dt;
            Window.get().b -= 5.0f * dt;
        }
        else if(changingScene)
        {
            Window.changeScene(1);
        }
    }
}
