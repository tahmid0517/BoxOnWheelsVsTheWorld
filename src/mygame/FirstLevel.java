package mygame;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import java.util.logging.Logger;
import util.Boxie;
import util.CoinMap;
import util.Input;
import util.Level;

public class FirstLevel extends AbstractAppState implements Level{
    public static final int respawnTime = 100;
    static boolean initializedInput = false;
    AssetManager assetManager; 
    Camera cam;
    Node rootNode;
    InputManager inputManager;
    Geometry planeGeom;
    boolean died = false;
    Boxie boxie;
    CoinMap coinMap;
    int lives = 3;
    private boolean[] coinsCollected;
    private static float movementScale = 2;
    private static float rotateSpeed = 0.2f;
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        assetManager = Main.getInstance().getAssetManager();
        cam = Main.getInstance().getCamera();
        rootNode = Main.getInstance().getRootNode();
        inputManager = Main.getInstance().getInputManager();
        boxie = Main.getInstance().boxie;
        boxie.attachToRootNode();
        planeGeom = new Geometry("Box",new Box(10,1,10));
        planeGeom.setMaterial(assetManager.loadMaterial("Materials/PlatformMaterial.j3m"));
        planeGeom.setLocalTranslation(0,0,0);
        cam.setLocation(new Vector3f(16.75f,20,0));
        rootNode.attachChild(planeGeom);
        initInput();
        coinMap = new CoinMap(4);
        float coinHeight = 2;
        coinMap.setLocations(new float[][]{{8.5f,coinHeight,8.5f},{8.5f,coinHeight,-8.5f},{-8.5f,coinHeight,8.5f},{-8.5f,coinHeight,-8.5f}});
        coinMap.scaleAll(0.25f);
        coinMap.attachToRootNode();
        Main.getInstance().turnOnNumOfLivesHUD();
        coinsCollected = new boolean[]{false,false,false,false};
    }
    public void initInput(){
        if(!initializedInput){
            Input input = Input.getInstance();
            input.addToMap("TurnRight",KeyInput.KEY_RIGHT,listener);
            input.addToMap("TurnLeft",KeyInput.KEY_LEFT,listener);
            input.addToMap("Forward",KeyInput.KEY_UP,listener);
            input.addToMap("Backward",KeyInput.KEY_DOWN,listener);
            initializedInput = true;
        }
    }
    @Override
    public void update(float f){
        cam.setLocation(new Vector3f(16.75f,20,0));
        cam.lookAt(new Vector3f(0,0,0),new Vector3f(0,0,0));
        coinMap.rotateAll(-0.025f);
        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
            Logger.getLogger(FirstLevel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
    private AnalogListener listener = new AnalogListener() {
        public void onAnalog(String name, float value, float tpf) {
           boxie.movementControl(name);
           int coinTouched = coinMap.isTouching(boxie.getSpatial());
           if(coinTouched!=-1){
               coinMap.detachFromRootNode(coinTouched);
               coinsCollected[coinTouched] = true;
           }
           CollisionResults results = new CollisionResults();
           boxie.getNode().collideWith(planeGeom.getModelBound(),results);
           CollisionResults results2 = new CollisionResults();
           planeGeom.collideWith(boxie.getSpatial().getWorldBound(),results2);
           if(results.size()==0 && results2.size()==0){
               boxie.reset();
               died = true;
               lives--;
               Main.getInstance().setNumOfLives(lives);
           }
           if(lives==0){
               Main.getInstance().failLevel();
           }
           if(getCoinsCollected()==4){
               Main.getInstance().endLevel();
           }
        }
    };
    public int getCoinsCollected(){
        int total = 0;
        for(int i = 0;i<4;i++){
            if(coinsCollected[i]){
                total++;
            }
        }
        return total;
    }
    public void reset(){
        lives = 3;
        Main.getInstance().setNumOfLives(lives);
        coinsCollected = new boolean[]{false,false,false,false};
    }
    public void endJump() {}
    public void endLife() {}
    public int getJumpsAvailable() {return 0;}
    public boolean isCheeseAvailable() {return false;}
}
