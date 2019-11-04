package glBase.loadModel;

import org.obj2openjl.v3.model.OpenGLModelData;

import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.List;

public class Geometry {
    private String filePath = null;

    private int indexCount = 0;

    private ShortBuffer indexBuff = null;       //索引缓冲
    private FloatBuffer verticeBuff = null;     //顶点缓冲
    private FloatBuffer normalBuff = null;      //法线缓冲
    private FloatBuffer textureBuff = null;     //贴图坐标缓冲
    private List<String> usemtl = null;

    private float maxX = 0.0f;
    private float maxY = 0.0f;
    private float maxZ = 0.0f;
    private float minX = 0.0f;
    private float minY = 0.0f;
    private float minZ = 0.0f;

    public Geometry(String filePath) {
        this.filePath = filePath;
    }

    public boolean loadModel(boolean bNormalReverse, boolean triangleReverse) {
        org.obj2openjl.v3.model.RawOpenGLModel openGLModel = null;
        try {
            openGLModel = new org.obj2openjl.v3.Obj2OpenJL().convert(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        OpenGLModelData openGLModelData = openGLModel.normalize().center().getDataForGLDrawElements();
        float[] vertices  = openGLModelData.getVertices();
        float[] normals = openGLModelData.getNormals();
        float[] coordinates = openGLModelData.getTextureCoordinates();
        short[] indices = openGLModelData.getIndices();
        usemtl = openGLModelData.getUsemtl();
        //this.normalization(vertices);

        indexCount = indices.length;

        if(triangleReverse) {
            for (int i = 0; i < indices.length / 3; i++) {
                short temp = indices[i * 3];
                indices[i * 3] = indices[i * 3 + 2];
                indices[i * 3 + 2] = temp;
            }
        }
        if(bNormalReverse) {
            for(int i=0;i<normals.length; i++) {
                normals[i] = -normals[i];
            }
        }

        indexBuff = ByteBuffer.allocateDirect(indexCount * 2).order(ByteOrder.nativeOrder()).asShortBuffer().put(indices);
        verticeBuff = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertices);
        normalBuff = ByteBuffer.allocateDirect(normals.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(normals);
        textureBuff = ByteBuffer.allocateDirect(coordinates.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(coordinates);

        this.calcEdge(vertices);

        return true;
    }

    private void calcEdge(float[] vertices) {
        for(int i=0;i < vertices.length / 3; i++) {
            float x = vertices[i*3];
            float y = vertices[i*3+1];
            float z = vertices[i*3+2];
            if(i == 0) {
                maxX = x;
                minX = x;
                maxY = y;
                minY = y;
                maxZ = z;
                minZ = z;
            }

            if(x > maxX)
                maxX = x;
            if(x < minX)
                minX = x;
            if(y > maxY)
                maxY = y;
            if(y < minY)
                minY = y;
            if(z > maxZ)
                maxZ = z;
            if(z < minZ)
                minZ = z;
        }
    }

    public int getIndexCount() {
        return indexCount;
    }
    public ShortBuffer getIndexBuff() {
        indexBuff.position(0);
        return indexBuff;
    }

    public FloatBuffer getVerticeBuff() {
        if(verticeBuff != null) {
            verticeBuff.position(0);
        }
        return verticeBuff;
    }

    public FloatBuffer getNormalBuff() {
        if(normalBuff != null) {
            normalBuff.position(0);
        }
        return normalBuff;
    }

    public FloatBuffer getTextureBuff() {
        if(textureBuff != null) {
            textureBuff.position(0);
        }
        return textureBuff;
    }

    public String getUsemtl() {
        if(usemtl != null && usemtl.size() > 0) {
            return usemtl.get(0);
        }
        return null;
    }



    public float getMaxX() {
        return maxX;
    }

    public float getMaxY() {
        return maxY;
    }

    public float getMaxZ() {
        return maxZ;
    }

    public float getMinX() {
        return minX;
    }

    public float getMinY() {
        return minY;
    }

    public float getMinZ() {
        return minZ;
    }
}
