package glBase.material;

import org.obj2openjl.v3.io.LineReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import glBase.tools.TextureHelper;

import static android.opengl.GLES20.glDeleteTextures;

public class Material {
    public class MaterialItem {
        public String name = "";
        public float Ns;
        public float Ni;
        public float d;
        public float Tr;
        public float Tf;
        public int illum;
        public float[] Ka = new float[3];
        public float[] Kd = new float[3];
        public float[] Ks = new float[3];
        public float[] Ke = new float[3];
        public String map_Ka;
        public String map_Kd;

        public int diffuseTextureId = -1;

        public boolean loadDiffuseTexture() {
            String[] pathArray = map_Kd.split("\\\\|/");
            String path = "/sdcard/Download/" + pathArray[pathArray.length-1];
            diffuseTextureId = TextureHelper.loadTexture(path);
            return true;
        }

        public boolean loadDiffuseTexture(String parentPath) {
            if(map_Kd == null)
                return false;
            String[] pathArray = map_Kd.split("\\\\|/");
            String path = parentPath + pathArray[pathArray.length-1];
            diffuseTextureId = TextureHelper.loadTexture(path);
            return true;
        }

        public void releaseDiffuseTexture() {
            if(diffuseTextureId != 0) {
                final int textureObjectIds[] = new int[1];
                textureObjectIds[0] = diffuseTextureId;
                glDeleteTextures(1, textureObjectIds, 0);
            }
        }
    }

    //private int diffuseTextureId = 0;
    private String mtlPath;
    private List<MaterialItem> materialItemsList = new ArrayList<>();

    public Material() {

    }

    public Material(String mtlPath) {
        this.mtlPath = mtlPath;
    }


    public boolean loadMaterial() {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(mtlPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        MaterialItem materialItem = null;
        LineReader lineReader = new LineReader(inputStream);
        String line = lineReader.readLine();
        while(line != null)
        {
            line = line.trim();
            if(line.length() > 1) {
                String firstChar = line.substring(0, 1);
                if(!firstChar.equals("#")) {
                    String[] strArr = line.split(" ");
                    if(strArr.length == 2 && strArr[0].equals("newmtl")) {
                        materialItem = new MaterialItem();
                        materialItem.name = strArr[1];
                        materialItemsList.add(materialItem);
                    } else if(strArr.length == 2 && strArr[0].equals("map_Ka")) {
                        materialItem.map_Ka = strArr[1];
                    } else if(strArr.length == 2 && strArr[0].equals("map_Kd")) {
                        materialItem.map_Kd = strArr[1];
                    } else {

                    }
                }
            }
            line = lineReader.readLine();
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] path = mtlPath.split("\\\\|/");
        String parentPath = "";
        for(int i=0; i < path.length-1; i++) {
            parentPath += path[i];
            parentPath += "/";
        }
        for(int i=0; i<materialItemsList.size(); i++) {
            MaterialItem materialItem1 = materialItemsList.get(i);
            materialItem1.loadDiffuseTexture(parentPath);
        }

        return true;
    }

    public void releaseMaterial() {
        for(int i=0; i<materialItemsList.size(); i++) {
            MaterialItem materialItem1 = materialItemsList.get(i);
            materialItem1.releaseDiffuseTexture();
        }
    }


    public int getDiffuseTextureId(String materiaName) {
        for(int i = 0; i < materialItemsList.size(); i++) {
            MaterialItem materialItem = materialItemsList.get(i);
            if(materialItem.name.equals(materiaName)) {
                return materialItem.diffuseTextureId;
            }
        }
        return -1;
    }

}
