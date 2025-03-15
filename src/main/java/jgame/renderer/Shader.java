package jgame.renderer;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Matrix2f;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
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
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix2fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix3fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
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

    public void uploadMat2f(String varName, Matrix2f mat)
    {
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
        mat.get(buffer);

        use();
        glUniformMatrix2fv(varLocation, false, buffer);
    }
    
    public void uploadMat3f(String varName, Matrix3f mat)
    {
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(9);
        mat.get(buffer);

        use();
        glUniformMatrix3fv(varLocation, false, buffer);
    }

    public void uploadMat4f(String varName, Matrix4f mat)
    {
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        mat.get(buffer);

        use();
        glUniformMatrix4fv(varLocation, false, buffer);
    }

    public void uploadVec2f(String varName, Vector2f vec)
    {
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        glUniform2f(varLocation, vec.x, vec.y);
    }

    public void uploadVec3f(String varName, Vector3f vec)
    {
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }

    public void uploadVec4f(String varName, Vector4f vec)
    {
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadFloat(String varName, float val)
    {
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        glUniform1f(varLocation, val);
    }

    public void uploadInt(String varName, int val)
    {
        int varLocation = glGetUniformLocation(shaderProgram, varName);
        use();
        glUniform1i(varLocation, val);
    }

    public void uploadTexture(String varName, int slot)
    {
        uploadInt(varName, slot);
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
