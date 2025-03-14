package jgame.event;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import jgame.core.BitHandler;

public class MouseListener 
{
    private double xScroll;
    private double yScroll;

    private double xPos;
    private double yPos;
    private double xLast;
    private double yLast;

    // bit array - 1 = true, 0 = false
    private int mouseButtonPressed;

    private boolean isDragging;

    private static MouseListener mouseListener;

    private MouseListener()
    {
        this.xScroll = 0.0;
        this.yScroll = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.xLast = 0.0;
        this.xLast = 0.0;
        this.mouseButtonPressed = 0;
    }

    public static MouseListener get()
    {
        if(MouseListener.mouseListener == null)
        {
            MouseListener.mouseListener = new MouseListener();
        }

        return MouseListener.mouseListener;
    }

    public static void mousePosCallback(long window, double xPos, double yPos)
    {
        get().xLast = get().xPos;
        get().yLast = get().yPos;
        get().xPos = xPos;
        get().yPos = yPos;

        get().isDragging = get().mouseButtonPressed != 0;
    }

    public static void mouseBtnCallabck(long window, int button, int action, int mods)
    {
        if(action == GLFW_PRESS)
        {
            get().mouseButtonPressed = BitHandler.setBit(get().mouseButtonPressed, button);
        }
        else if(action == GLFW_RELEASE)
        {
            get().mouseButtonPressed = BitHandler.removeBit(get().mouseButtonPressed, button);
            get().isDragging = false;
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset)
    {
        get().xScroll = xOffset;
        get().yScroll = yOffset;
    }

    public static void endFrame()
    {
        get().xScroll = 0.0;
        get().yScroll = 0.0;
        get().xLast = get().xPos;
        get().yLast = get().yPos;
    }

    public static float getX()
    {
        return (float) get().xPos;
    }
    
    public static float getY()
    {
        return (float) get().yPos;
    }

    public static float getDX()
    {
        return (float) (get().xLast - get().xPos);
    }
    
    public static float getDY()
    {
        return (float) (get().yLast - get().yPos);
    }

    public static float getScrollX()
    {
        return (float) get().xScroll;
    }

    public static float getScrollY()
    {
        return (float) get().yScroll;
    }

    public static boolean isDragging()
    {
        return get().isDragging;
    }

    public static boolean isMouseBtnDown(int button)
    {
        return BitHandler.getBit(get().mouseButtonPressed, button) != 0;
    }
}
