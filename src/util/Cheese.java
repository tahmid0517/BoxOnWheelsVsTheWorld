package util;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResults;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import mygame.Main;
public class Cheese implements Object {
    Spatial spatial;
    Node node;
    boolean visible;
    public Cheese(){
        AssetManager assetManager = Main.getInstance().getAssetManager();
        spatial = assetManager.loadModel("Models/Cheese/Cheese.001.mesh.j3o");
        spatial.setMaterial(assetManager.loadMaterial("Materials/CheeseMaterial.j3m"));
        node = new Node();
        node.attachChild(spatial);
        visible = false;
    }
    public void attachToRootNode(){
        Main.getInstance().getRootNode().attachChild(node);
        visible = true;
    }
    public void detachFromRootNode(){
        Main.getInstance().getRootNode().detachChild(node);
        visible = false;
    }
    public boolean isTouching(Spatial model){
        if(!visible){
            return false;
        }
        CollisionResults results = new CollisionResults();
        node.collideWith(model.getWorldBound(),results);
        if(results.size()>0){
            return true;
        }
        return false;
    }
    public void rotate(float f){
        node.rotate(0,f,0);
    }
    public void scale(float f){
        spatial.scale(f);
    }
    public void setLocation(float x,float y,float z){
        node.setLocalTranslation(x,y,z);
    }
    public void centreSpatial(float z){
        spatial.setLocalTranslation(0,0,z);
    }
    public void move(float x, float y, float z) {
        node.move(x,y,z);
    }
}
