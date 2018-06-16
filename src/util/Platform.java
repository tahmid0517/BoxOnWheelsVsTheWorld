package util;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import mygame.Main;

public class Platform {
    public Geometry geom;
    public Platform(int material,Vector3f dimensions,Vector3f location,boolean attachNow){
        geom = new Geometry("Box",new Box(dimensions.x,dimensions.y,dimensions.z));
        String matString = "";
        if(material==0){
            matString = "Materials/PlatformMaterial.j3m";
        }
        else if(material==1){
            matString = "Materials/PlatformMaterial2.j3m";
        }
        else if(material==2){
            matString = "Materials/PlatformMaterialSunArt.j3m";
        }
        geom.setMaterial(Main.getInstance().getAssetManager().loadMaterial(matString));
        geom.setLocalTranslation(location);
        if(attachNow){
            attachToRootNode();
        }
    }
    public void attachToRootNode(){
        Main.getInstance().getRootNode().attachChild(geom);
    }
}
