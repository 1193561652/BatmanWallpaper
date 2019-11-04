package glBase;

import android.graphics.Point;
import android.opengl.Matrix;


import java.lang.reflect.Array;
import java.nio.FloatBuffer;
import java.util.Vector;

import glBase.loadModel.Geometry;
import glBase.material.Material;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glDrawElements;

public class DrawModel {
    public class Vector3 {
        public float x = 0.0f;
        public float y = 0.0f;
        public float z = 0.0f;
        public Vector3() {}
        public Vector3(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    protected Model model = null;
    protected ShaderProgram program = null;

    protected float[] modelMatrix = new float[16];
    protected Vector3 centre = new Vector3();
    protected Vector3 scale = new Vector3(1.0f, 1.0f, 1.0f);

    public DrawModel() {
        Matrix.setIdentityM(modelMatrix, 0);
        this.loadShaderParogram();
    }

//    public DrawModel(String objPath, String mtlPath, ModelManager modelManager) {
//        Matrix.setIdentityM(modelMatrix, 0);
//        this.loadShaderParogram();
//        this.loadModel(objPath, mtlPath, modelManager);
//    }

    protected void loadShaderParogram() {
        //program = new TestProgram(null, R.raw.simple_vertex, R.raw.simple_fragment);
    }

    protected void loadModel(String objPath, String mtlPath, ModelManager modelManager) {
        //model = modelManager.loadModel(objPath, mtlPath, false, false);
    }

    protected void setUnifrom(float[] perspMatrix, float[] viewMatrix, float[] modelMatrix, int textureId) {
        program.setUnifrom(perspMatrix, viewMatrix, modelMatrix, textureId);
    }

    protected void setAttribut(FloatBuffer vertexBuff, FloatBuffer normalBuff, FloatBuffer textureBuff) {
        program.setAttribut(vertexBuff, normalBuff, textureBuff);
    }

    public void drawModel(float[] perspMatrix, float[] viewMatrix) {
        program.useProgram();

        this.setUnifrom(perspMatrix, viewMatrix, modelMatrix, model.getTextureId());
        this.setAttribut(model.getVerticeBuff(), model.getNormalBuff(), model.getTextureBuff());

        glDrawElements(GL_TRIANGLES, model.getIndexCount(), GL_UNSIGNED_SHORT, model.getIndexBuff());
    }





    public float[] getCurrModleMatrix() {
        float[] modelMatrix = new float[16];
        System.arraycopy(this.modelMatrix, 0, modelMatrix, 0, modelMatrix.length);
        return modelMatrix;
    }

    public void setModeMatrix(float[] modelMatrix) {
        System.arraycopy(modelMatrix, 0, this.modelMatrix, 0, this.modelMatrix.length);
        return;
    }

    public void appendModeMatrix(float[] appendMatrix) {
        float[] tempMatrix = this.getCurrModleMatrix();
        Matrix.multiplyMM(this.modelMatrix, 0, appendMatrix, 0, tempMatrix, 0);
        return;
    }


    public void moveBy(float x, float y, float z) {
        float[] tempMatrix = new float[16];
        Matrix.setIdentityM(tempMatrix, 0);
        Matrix.translateM(tempMatrix, 0, x, y, z);
        this.appendModeMatrix(tempMatrix);
        return;
    }

    public void scalc(float xscale, float yscale, float zscale) {
        float[] tempMatrix = new float[16];
        Matrix.setIdentityM(tempMatrix, 0);
        Matrix.scaleM(tempMatrix, 0, xscale, yscale, zscale);
        this.appendModeMatrix(tempMatrix);
        return;
    }


}
