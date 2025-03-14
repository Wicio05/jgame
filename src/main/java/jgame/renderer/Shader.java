package jgame.renderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class Shader 
{
    private String vertSrc;
    private String fragSrc;

    private int shaderProgram;

    public Shader(String vertFilePath, String fragFilePath)
    {
        vertSrc = readFromFile(vertFilePath);
        fragSrc = readFromFile(fragFilePath);
    }

    /**
     * Compiles shaders and links shaders
     * 
    */
    public void compile()
    {
        int vertId;
        int fragId;

        // create shader
        vertId = glCreateShader(GL_VERTEX_SHADER);

        // load source code
        glShaderSource(vertId, vertSrc);

        // compile shader
        glCompileShader(vertId);

        int result = glGetShaderi(vertId, GL_COMPILE_STATUS);
        if(result == GL_FALSE)
        {
            int lenght = glGetShaderi(vertId, GL_INFO_LOG_LENGTH);
            System.err.println("ERROR: Vertex shader compilation failed");
            System.err.println(glGetShaderInfoLog(vertId, lenght));
            assert false: ""; 
        }

        // create shader
        fragId = glCreateShader(GL_FRAGMENT_SHADER);

        // load source code
        glShaderSource(fragId, fragSrc);

        // compile shader
        glCompileShader(fragId);

        result = glGetShaderi(fragId, GL_COMPILE_STATUS);
        if(result == GL_FALSE)
        {
            int lenght = glGetShaderi(fragId, GL_INFO_LOG_LENGTH);
            System.err.println("ERROR: Fragment shader compilation failed");
            System.err.println(glGetShaderInfoLog(fragId, lenght));
            assert false: ""; 
        }

        // link shaders
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertId);
        glAttachShader(shaderProgram, fragId);
        glLinkProgram(shaderProgram);

        // check for errors
        result = glGetProgrami(shaderProgram, GL_LINK_STATUS);

        if(result == GL_FALSE)
        {
            int lenght = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.err.println("ERROR: Shader program linking failed");
            System.err.println(glGetProgramInfoLog(shaderProgram, lenght));
            assert false: ""; 
        }
    }

    public void use()
    {
        // bind shader program
        glUseProgram(shaderProgram);
    }

    public void detach()
    {
        glUseProgram(0);
    }

    private String readFromFile(String filepath)
    {
        try
        {
            return new String(Files.readAllBytes(Paths.get(filepath)));
        }
        catch(IOException e)
        {
            e.printStackTrace();
            assert false: "ERROR: File not found. " + filepath;
        }

        return null;
    }
}
