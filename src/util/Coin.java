package util;
import com.jme3.collision.CollisionResults;
import com.jme3.scene.Spatial;
import java.util.Timer;
import java.util.TimerTask;
import mygame.Main;
public class Coin implements Object{
    Spatial spatial;
    public Coin(){
        spatial = Main.getInstance().getAssetManager().loadModel("Models/Coin001/Coin.001.mesh.j3o");
        spatial.setMaterial(Main.getInstance().getAssetManager().loadMaterial("Materials/CoinMaterial.j3m"));
    }
    public void attachToRootNode(){
        Main.getInstance().getRootNode().attachChild(spatial);
    }
    public void detachFromRootNode(){
        Main.getInstance().getRootNode().detachChild(spatial);
    }
    public void rotate(float f){
        spatial.rotate(0,f,0);
    }
    public void scale(float f){
        spatial.scale(f);
    }
    public void setLocation(float a,float b,float c){
        spatial.setLocalTranslation(a,b,c);
    }
    public boolean isTouching(Spatial model){
        CollisionResults results = new CollisionResults();
        spatial.collideWith(model.getWorldBound(), results);
        if(results.size()>0){
            return true;
        }
        return false;
    }
    public void move(float x,float y,float z){
        spatial.move(x,y,z);
    }
}
