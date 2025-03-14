package jgame;

import org.lwjgl.Version;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_MAXIMIZED;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

import jgame.event.KeyListener;
import jgame.event.MouseListener;
import jgame.util.Time;

public class Window 
{
    private static Window window = null;

    private long glfwWindow;
    
    private int width;
    private int height;
    private String title;

    public float r;
    public float g;
    public float b;
    public float a;

    private static Scene currentScene = null;

    private Window()
    {
        this.width = 1920;
        this.height = 1080;
        this.title = "jGame";
        this.r = this.g = this.b = this.a = 1.0f;
    }

    public static Window get()
    {
        if(Window.window == null)
        {
            Window.window = new Window();
        }

        return Window.window;
    }

    public static void changeScene(int newScene)
    {
        switch(newScene)
        {
            case 0:
            {
                currentScene = new LevelEditorScene();
                currentScene.init();
                break;
            }

            case 1:
            {
                currentScene = new LevelScene();
                currentScene.init();
                break;
            }

            default:
            {
                // Unknown scene
                assert false: "Unknown scene " + newScene;
                break;
            }
        }
    }

    private void init()
    {
        // Setup an error callback
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW
		if (!glfwInit())
        {
			throw new IllegalStateException("Unable to initialize GLFW");
        }

		// Configure GLFW
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

		// Create the window
		glfwWindow = glfwCreateWindow(this.width, this.height, title, NULL, NULL);

		if (glfwWindow == NULL)
        {    
			throw new RuntimeException("Failed to create the GLFW window");
        }

        // Set callbacks
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseBtnCallabck);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

		// Make the OpenGL context current
		glfwMakeContextCurrent(glfwWindow);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

        Window.changeScene(0);
    }

    private void loop()
    {
        float startTime = Time.getTime();
        float endTime = startTime;
        float dt = -1.0f;
        
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while (!glfwWindowShouldClose(glfwWindow)) 
        {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            // Set the clear color
		    glClearColor(r, g, b, a);

            if(dt >= 0)
            {
                currentScene.update(dt);
            }

            currentScene.render();

            // Swap the color buffers
			glfwSwapBuffers(glfwWindow);

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();

            // Delta time
            endTime = Time.getTime();
            dt = endTime - startTime;
            startTime = endTime;
		}
    }

    private void cleanup()
    {
        // Free the window callbacks and destroy the window
		glfwFreeCallbacks(glfwWindow);
		glfwDestroyWindow(glfwWindow);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
    }

    public void run()
    {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		init();
		loop();
        cleanup();
    }
}
