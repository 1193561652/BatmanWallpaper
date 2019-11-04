package com.example.administrator.wallpaper;

import android.content.Context;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import glBase.ShaderProgram;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;

public class TestProgram extends ShaderProgram {

    protected int u_ModelMatrix = 0;
    protected int a_Position = 0;
    protected int u_Color = 0;
    protected int a_Normal = 0;
    protected int a_TexturePosition = 0;

    protected int u_ProjectionMatrix = 0;
    protected int u_ViewMatrix = 0;

    protected int u_Texture = 0;

    protected int u_UseTexture = 0;
    protected int u_TextureTurn = 0;
    protected int u_TextureMatrix = 0;
    protected int u_loopTexture = 0;

    protected boolean bTextureTurn = false;
    protected boolean bLoopTexture = false;
    protected float[] textureMatrix = new float[16];

    public TestProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId) {
        super(context, vertexShaderResourceId, fragmentShaderResourceId);

        u_ModelMatrix = glGetUniformLocation(program, "u_ModelMatrix");
        a_Position = glGetAttribLocation(program, "a_Position");
        a_Normal = glGetAttribLocation(program, "a_Normal");
        //u_Color = glGetUniformLocation(program, "u_Color");
        u_ProjectionMatrix = glGetUniformLocation(program, "u_ProjectionMatrix");
        u_ViewMatrix = glGetUniformLocation(program, "u_ViewMatrix");

        a_TexturePosition = glGetAttribLocation(program, "a_TexturePosition");

        u_Texture = glGetUniformLocation(program, "u_Texture");
        u_UseTexture = glGetUniformLocation(program, "u_UseTexture");
        u_TextureTurn = glGetUniformLocation(program, "u_TextureTurn");
        u_TextureMatrix = glGetUniformLocation(program, "u_TextureMatrix");

        u_loopTexture = glGetUniformLocation(program, "u_loopTexture");

        Matrix.setIdentityM(textureMatrix, 0);
    }

    public void setUnifrom(float[] projectMatrix, float[] viewMatrix, float[] modelMatrix, int textureId) {
        //float[] matrix = new float[16];
        //Matrix.setIdentityM(matrix, 0);
        //Matrix.rotateM(matrix, 0, -30, 1, 0, 0);
        //Matrix.rotateM(matrix, 0, -90, 0, 1, 0);
        //Matrix.translateM(matrix, 0, 0, 0, -5.1f);
        //模型矩阵
        glUniformMatrix4fv(u_ModelMatrix, 1, false, modelMatrix, 0);
        glUniform4f(u_Color, 0.5f, 0.5f, 0.5f, 0.0f);

        //透视矩阵
        glUniformMatrix4fv(u_ProjectionMatrix, 1, false, projectMatrix, 0);
        //世界矩阵
        glUniformMatrix4fv(u_ViewMatrix, 1, false, viewMatrix, 0);

        if(textureId >= 0) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, textureId);
            glUniform1i(u_UseTexture, 1);
        } else {
            glUniform1i(u_UseTexture, 0);
        }

        glUniform1i(u_TextureTurn, bTextureTurn?1:0);
        glUniformMatrix4fv(u_TextureMatrix, 1, false, textureMatrix, 0);
        glUniform1i(u_loopTexture, bLoopTexture?1:0);
    }

    //private FloatBuffer floatBuffer = null;

    public void setAttribut(FloatBuffer vertexBuff, FloatBuffer normalBuff, FloatBuffer textureBuff) {
        int componentCount = 3;

        if(vertexBuff != null) {
            vertexBuff.position(0);
            glVertexAttribPointer(a_Position, componentCount, GL_FLOAT, false, 0, vertexBuff);
            glEnableVertexAttribArray(a_Position);
        }

        if(normalBuff != null) {
            normalBuff.position(0);
            glVertexAttribPointer(a_Normal, componentCount, GL_FLOAT, false, 0, normalBuff);
            glEnableVertexAttribArray(a_Normal);
        }

        if(textureBuff != null) {
            textureBuff.position(0);
            glVertexAttribPointer(a_TexturePosition, 2, GL_FLOAT, false, 0, textureBuff);
            glEnableVertexAttribArray(a_TexturePosition);
        }
    }

    public void setTextureTurn(boolean bTextureTurn) {
        this.bTextureTurn = bTextureTurn;
    }

    public void setTextureMatrix(float[] textureMatrix) {
        System.arraycopy(textureMatrix, 0, this.textureMatrix, 0, this.textureMatrix.length);
    }

    public void setLoopTexture(boolean bLoopTexture) {
        this.bLoopTexture = bLoopTexture;
    }

}
