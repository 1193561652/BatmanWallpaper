package com.example.administrator.wallpaper.glObject;

import android.opengl.Matrix;


import com.example.administrator.batmanwallpaper.R;
import com.example.administrator.wallpaper.TestProgram;

import glBase.DrawModel;
import glBase.ModelManager;

public class GeneralModel extends DrawModel {

    //protected boolean bTextureTurn = false;
    //protected float[] textureMatrix = new float[16];

    public GeneralModel(String objPath, String mtlPath, ModelManager modelManager) {
        Matrix.setIdentityM(modelMatrix, 0);
        //Matrix.setIdentityM(textureMatrix, 0);
        this.loadShaderParogram();
        this.loadModel(objPath, mtlPath, modelManager);
    }

    @Override
    protected void loadShaderParogram() {
        program = new TestProgram(null, R.raw.simple_vertex, R.raw.simple_fragment);
    }

    @Override
    protected void loadModel(String objPath, String mtlPath, ModelManager modelManager) {
        model = modelManager.loadModel(objPath, mtlPath, false, false);
    }

    @Override
    protected void setUnifrom(float[] perspMatrix, float[] viewMatrix, float[] modelMatrix, int textureId) {
        super.setUnifrom(perspMatrix, viewMatrix, modelMatrix, textureId);


    }

    @Override
    public void drawModel(float[] perspMatrix, float[] viewMatrix) {
        super.drawModel(perspMatrix, viewMatrix);
    }


    public void setTextureTurn(boolean bTextureTurn) {
        ((TestProgram)program).setTextureTurn(bTextureTurn);
    }

    public void setTextureMatrix(float[] textureMatrix) {
        ((TestProgram)program).setTextureMatrix(textureMatrix);
    }

    public void setLoopTexture(boolean bLoopTexture) {
        ((TestProgram)program).setLoopTexture(bLoopTexture);
    }



}
