package today.snowstorm.client.utils.render.shaders;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class ShaderManager {

    private int prg;

    private final Map<String, Integer> map = new HashMap<>();

    public ShaderManager(final String vtx, final String frg) {
        this.prg = glCreateProgram();

        glAttachShader(this.prg, createShader(vtx, GL_VERTEX_SHADER));
        glAttachShader(this.prg, createShader(frg, GL_FRAGMENT_SHADER));
        // Link the prg
        glLinkProgram(this.prg);
        final int status = glGetProgrami(this.prg, GL_LINK_STATUS);
        if (status == 0) {
            this.prg = -1;
            return;
        }

        this.setupUniforms();
    }

    private static int createShader(final String source, final int type) {
        final int shadez = glCreateShader(type);
        glShaderSource(shadez, source);
        glCompileShader(shadez);

        final int gaga = glGetShaderi(shadez, GL_COMPILE_STATUS);

        if (gaga == 0) {
            return -1;
        }

        return shadez;
    }

    public void use() {
        glUseProgram(this.prg);
        this.updateUniforms();
    }

    public int getProgram() {
        return prg;
    }

    public void setupUniforms() {}

    public void updateUniforms() {}

    public void setupUniform(final String uni) {
        this.map.put(uni, glGetUniformLocation(this.prg, uni));
    }

    public int getUniformLocation(final String uniform) {
        return this.map.get(uniform);
    }
}