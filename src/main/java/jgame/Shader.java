package jgame;

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

public class Shader 
{
    private String vertSrc = "#version 410 core\n" + //
                "layout (location = 0) in vec3 aPos;\n" + //
                "layout (location = 1) in vec4 aColor;\n" + //
                "\n" + //
                "out vec4 fColor;\n" + //
                "\n" + //
                "void main()\n" + //
                "{\n" + //
                "    fColor = aColor;\n" + //
                "\n" + //
                "    gl_Position = vec4(aPos, 1.0);\n" + //
                "}";

    private String fragSrc = "#version 410 core\n" + //
                "\n" + //
                "in vec4 fColor;\n" + //
                "\n" + //
                "out vec4 color;\n" + //
                "\n" + //
                "void main()\n" + //
                "{\n" + //
                "    color = fColor;\n" + //
                "}";

    private int vertId;
    private int fragId;
    private int shaderProgram;

    public Shader()
    {

    }

    /**
     * Compiles shaders
    */
    public void compile()
    {
        // System.out.println(glGetString(GL_VERSION));
        // System.out.println(glGetString(GL_SHADING_LANGUAGE_VERSION));

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
    }

    /**
     * Links shaders
     * 
     */
    public void link()
    {
        // link shaders
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertId);
        glAttachShader(shaderProgram, fragId);
        glLinkProgram(shaderProgram);

        // check for errors
        int result = glGetProgrami(shaderProgram, GL_LINK_STATUS);

        if(result == GL_FALSE)
        {
            int lenght = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.err.println("ERROR: Shader program linking failed");
            System.err.println(glGetProgramInfoLog(shaderProgram, lenght));
            assert false: ""; 
        }
    }

    public int getProgId()
    {
        return shaderProgram;
    }
}
