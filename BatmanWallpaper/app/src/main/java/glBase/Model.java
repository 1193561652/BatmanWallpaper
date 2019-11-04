package glBase;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import glBase.loadModel.Geometry;
import glBase.material.Material;

public class Model {
    protected Geometry geometry = null;
    protected Material material = null;
    protected String useMaterial = "";
    public Model() {

    }

    public Model(Geometry geometry, Material material) {
        this.geometry = geometry;
        this.material = material;
        useMaterial = geometry.getUsemtl();
    }

    public int getIndexCount() {
        return geometry.getIndexCount();
    }
    public ShortBuffer getIndexBuff() {
        return geometry.getIndexBuff();
    }

    public FloatBuffer getVerticeBuff() {
        return geometry.getVerticeBuff();
    }

    public FloatBuffer getNormalBuff() {
        return geometry.getNormalBuff();
    }

    public FloatBuffer getTextureBuff() {
        return geometry.getTextureBuff();
    }

    public int getTextureId() {
        //return material.getDiffuseTextureId(geometry.getUsemtl());
        int textureId = material.getDiffuseTextureId(useMaterial);
        if(textureId == -1)
            textureId = material.getDiffuseTextureId(geometry.getUsemtl());
        return textureId;
    }

    public void setUseMaterial(String useMaterial) {
        this.useMaterial = useMaterial;
    }

    public float getMaxX() {
        return geometry.getMaxX();
    }
    public float getMaxY() {
        return geometry.getMaxY();
    }
    public float getMaxZ() {
        return geometry.getMaxZ();
    }
    public float getMinX() {
        return geometry.getMinX();
    }
    public float getMinY() {
        return geometry.getMinY();
    }
    public float getMinZ() {
        return geometry.getMinZ();
    }
}
