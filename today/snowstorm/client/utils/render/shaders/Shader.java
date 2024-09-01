package today.snowstorm.client.utils.render.shaders;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private final int progId;
    private final int timeUniform;
    private final int mouseUniform;
    private final int resolutionUniform;

    public Shader(String fraLoc) throws IOException {
        int prog = glCreateProgram();
        glAttachShader(prog, createShader(Shader.class.getResourceAsStream("/assets/minecraft/Snowstorm/shader/shader.vsh"), GL_VERTEX_SHADER));
        glAttachShader(prog, createShader(Shader.class.getResourceAsStream(fraLoc), GL_FRAGMENT_SHADER));

        glLinkProgram(prog);

        int linked = glGetProgrami(prog, GL_LINK_STATUS);

        if (linked == 0) {
            System.err.println(glGetProgramInfoLog(prog, glGetProgrami(prog, GL_INFO_LOG_LENGTH)));

            throw new IllegalStateException("Shader failed to link");
        }

        this.progId = prog;

        glUseProgram(prog);

        this.timeUniform = glGetUniformLocation(prog, "time");
        this.mouseUniform = glGetUniformLocation(prog, "mouse");
        this.resolutionUniform = glGetUniformLocation(prog, "resolution");

        glUseProgram(0);
    }

    public void useShader(int width, int height, float mouseX, float mouseY, float time) {
        glUseProgram(this.progId);

        glUniform2f(this.resolutionUniform, width, height);
        glUniform2f(this.mouseUniform, mouseX / width, 1.0f - mouseY / height);
        glUniform1f(this.timeUniform, time);
    }

    private int createShader(InputStream inputStream, int shaderType) throws IOException {
        int shader = glCreateShader(shaderType);

        glShaderSource(shader, readStreamToString(inputStream));

        glCompileShader(shader);

        int compiled = glGetShaderi(shader, GL_COMPILE_STATUS);

        if (compiled == 0) {
            System.err.println(glGetShaderInfoLog(shader, GL_INFO_LOG_LENGTH));

            throw new IllegalStateException("Failed to compile shader");
        }

        return shader;
    }

    private String readStreamToString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        byte[] buffer = new byte[512];

        int read;

        while ((read = inputStream.read(buffer, 0, buffer.length)) != -1) {
            out.write(buffer, 0, read);
        }

        return new String(out.toByteArray(), StandardCharsets.UTF_8);
    }

}