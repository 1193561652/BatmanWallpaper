package glBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import glBase.loadModel.Geometry;
import glBase.material.Material;

public class ModelManager {
    protected Map<String, Geometry> geometryMap = new HashMap<String, Geometry>();
    protected Map<String, Material> materialMap = new HashMap<String, Material>();
    //protected Map<Integer, Material> materialMap = new HashMap<Integer, Material>();

    protected List<Model> modelList = new ArrayList<Model>();

    public Model loadModel(String objPath, String mtlPath, boolean bNormalReverse, boolean triangleReverse) {
        //Model result = new Model();
        Geometry geometry = null;
        Material material = null;
        if(geometryMap.containsKey(objPath)) {
            geometry = geometryMap.get(objPath);
        } else {
            geometry = new Geometry(objPath);
            geometry.loadModel(bNormalReverse, triangleReverse);
            geometryMap.put(objPath, geometry);
        }

        if(materialMap.containsKey(mtlPath)) {
            material = materialMap.get(mtlPath);
        } else {
            material = new Material(mtlPath);
            material.loadMaterial();
            materialMap.put(mtlPath, material);
        }

        Model model = new Model(geometry, material);
        modelList.add(model);

        return model;
    }

}
