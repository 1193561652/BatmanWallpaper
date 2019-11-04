package com.example.administrator.wallpaper.glObject;

import android.opengl.Matrix;

import com.example.administrator.wallpaper.GLRander;

import java.util.concurrent.Future;

import glBase.ModelManager;

public class BackgroupModel extends GeneralModel {
    private float[] baseSetMatrix = new float[16];

    public BackgroupModel(String objPath, String mtlPath, ModelManager modelManager) {
        super(objPath, mtlPath, modelManager);
        Matrix.setIdentityM(baseSetMatrix, 0);
    }

    public void setMaxAndCurrentAndBack() {
        float maxX = model.getMaxX();
        float minX = model.getMinX();
        float maxY = model.getMaxY();
        float minY = model.getMinY();
        float maxZ = model.getMaxZ();

        Matrix.translateM(baseSetMatrix, 0, -(maxX+minX), -(maxY+minY), 1.0f - maxZ);
        if(maxX - minX > maxY - minY) {
            Matrix.scaleM(baseSetMatrix, 0, 2.0f / (maxY - minY), 2.0f / (maxY - minY), 1.0f);
        } else {
            Matrix.scaleM(baseSetMatrix, 0, 2.0f / (maxX - minX), 2.0f / (maxX - minX), 1.0f);
        }
        System.arraycopy(baseSetMatrix, 0, modelMatrix, 0, modelMatrix.length);
    }

    public void rollTo(float offset, float whRate) {
        float maxX = model.getMaxX();
        float minX = model.getMinX();
        float maxY = model.getMaxY();
        float minY = model.getMinY();

        float[] tmp = new float[16];
        Matrix.setIdentityM(tmp, 0);
        if(whRate <= 1.0f) {
            if(maxX - minX > maxY - minY) {
                float showW = 2.0f * whRate;
                float fullW = (maxX - minX) * 2.0f / (maxY - minY);
                float xOffset = (fullW - showW) * offset;
                float xCurrOffset = xOffset + showW / 2.0f;

                float xTrans = fullW/2.0f - xCurrOffset;    //((fullW/2.0f - xOffset) - 1.0f);

                Matrix.translateM(tmp,0, xTrans, 0f, 0f);
                Matrix.multiplyMM(modelMatrix, 0, tmp, 0, baseSetMatrix, 0);
            }
        } else {

        }
    }


    public void setBKImage(int state) {
        switch (state) {
            case GLRander.WS_WORKING:
                model.setUseMaterial("01___Default1");
                break;
            case GLRander.WS_SAFE:
                model.setUseMaterial("01___Default");
                break;
            case GLRander.WS_ERROR:
                model.setUseMaterial("01___Default");
                break;
            default:
                model.setUseMaterial("01___Default");
                break;
        }
    }

}
