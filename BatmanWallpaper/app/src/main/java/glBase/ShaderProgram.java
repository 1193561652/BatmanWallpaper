package glBase;

import android.content.Context;

import java.nio.FloatBuffer;

import glBase.tools.ShaderHelper;
import glBase.tools.TextResourceReader;

import static android.opengl.GLES20.glUseProgram;

public class ShaderProgram {
    public static Context context = null;
    protected static final String U_COLOR = "u_Color";
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit"; //u_TextureUnit

    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    protected static final String U_TIME = "u_Time";
    protected static final String A_DIRECTIONVECTOR = "a_DirectionVector";
    protected static final String A_PARTICLESTARTIME = "a_ParticleStarTime";

    protected int program;
    protected ShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId) {
        if(context == null)
            context = ShaderProgram.context;
        program = ShaderHelper.buildProgram(TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId), TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId));
    }

    public void useProgram() {
        glUseProgram(program);
    }

    public void setUnifrom(float[] projectMatrix, float[] viewMatrix, float[] modelMatrix, int textureId) {}

    public void setAttribut(FloatBuffer vertexBuff, FloatBuffer normalBuff, FloatBuffer textureBuff) {}
}
