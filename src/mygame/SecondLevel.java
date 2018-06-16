package mygame;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import util.Boxie;
import util.CheeseMap;
import util.CoinMap;
import util.Input;
import util.Level;
import util.Parabola;

public class SecondLevel extends AbstractAppState implements Level {
    AssetManager assetManager; 
    Camera cam;
    Node rootNode;
    InputManager inputManager;
    Boxie boxie;
    Geometry firstPlatform,secondPlatform;
    boolean initializedInput = false;
    CoinMap coinMap;
    boolean[] coinsCollected;
    CheeseMap cheeseMap;
    BitmapText numOfLives;
    int lives;
    boolean jumpTargetIsVisible = false;
    boolean cheeseCollected;
    boolean cheeseUsed;
    boolean jumpStarted;
    Parabola parabola;
    float currentJumpTraveled;
    private static final float jumpSpeed = 0.2f;
    private static final float targetSpeed = 0.2f;
    private static final float movementScale = 2;
    private static final float rotateSpeed = 0.2f;
    @Override
    public void initialize(AppStateManager stateManager, Application app){
        assetManager = Main.getInstance().getAssetManager();
        cam = Main.getInstance().getCamera();
        rootNode = Main.getInstance().getRootNode();
        inputManager = Main.getInstance().getInputManager();
        boxie = Main.getInstance().boxie;
        boxie.attachToRootNode();
        boxie.reset();
        cam.setLocation(new Vector3f(30,30,0));
        cam.lookAt(new Vector3f(0,0,0),new Vector3f(0,0,0));
        firstPlatform = new Geometry("Box",new Box(16,1,6.5f));
        firstPlatform.setMaterial(assetManager.loadMaterial("Materials/PlatformMaterial2.j3m"));
        firstPlatform.setLocalTranslation(1.1f,0,10);
        rootNode.attachChild(firstPlatform);
        secondPlatform = new Geometry("Box",new Box(18,1,6));
        secondPlatform.setMaterial(assetManager.loadMaterial("Materials/PlatformMaterial2.j3m"));
        secondPlatform.setLocalTranslation(-20,0,-18.5f);
        rootNode.attachChild(secondPlatform);
        initInput();
        coinMap = new CoinMap(4);
        float coinHeight = 2;
        coinMap.setLocations(new float[][]{{2,coinHeight,15},{2,coinHeight,4.5f},{-13,coinHeight,15},{-13,coinHeight,4.5f}});
        coinMap.scaleAll(0.4f);
        coinMap.attachToRootNode();
        coinsCollected = new boolean[]{false,false,false,false};
        cheeseMap = new CheeseMap(1);
        cheeseMap.setLocations(new float[][]{{-31.25f,2,-19}});
        cheeseMap.scaleAll(0.25f);
        cheeseMap.centreAll(1.8f);
        cheeseMap.attachToRootNode();
        lives = 3;
        Main.getInstance().turnOnNumOfLivesHUD();
        cheeseCollected = false;
        cheeseUsed = false;
        jumpStarted = false;
        currentJumpTraveled = 0;
        Main.getInstance().turnOnJumpsAvailableHUD();
        Main.getInstance().setJumpsAvailable(0);
    }
    @Override
    public void update(float f){
        cam.setLocation(new Vector3f(30,30,0));
        cam.lookAt(new Vector3f(0,0,0),new Vector3f(0,0,0));
        coinMap.rotateAll(-0.02f);
        cheeseMap.rotateAll(-0.02f);
        if(jumpStarted && !boxie.isTouching(firstPlatform) && !boxie.isTouching(secondPlatform) && boxie.getHeight()>=0){
            boxie.goToHeight(parabola.getOutput(currentJumpTraveled));
            boxie.moveForward(jumpSpeed);
            currentJumpTraveled+=jumpSpeed;
        }
        else if(!boxie.isTouching(firstPlatform) && !boxie.isTouching(secondPlatform)){
            endJump();
            endLife();
        }
        else{
            endJump();
        }
    }
    public void initInput(){
        if(!initializedInput){
            Input input = Input.getInstance();
            input.addToMap("TurnRight",KeyInput.KEY_RIGHT, listener);
            input.addToMap("TurnLeft",KeyInput.KEY_LEFT,listener);
            input.addToMap("Forward",KeyInput.KEY_UP,listener);
            input.addToMap("Backward",KeyInput.KEY_DOWN,listener);
            input.addToMap("StartJump",KeyInput.KEY_Z,actionListener);
            input.addToMap("StartJump",KeyInput.KEY_Z,listener);
            input.addToMap("Jump",KeyInput.KEY_X,actionListener);
            initializedInput = true;
        }
    }
    AnalogListener listener = new AnalogListener() {
        public void onAnalog(String name, float value, float tpf) {
            boxie.movementControl(name);
            if(name.equals("StartJump") && !jumpStarted){
                boxie.moveTarget(targetSpeed);
            }
            int coinTouched = coinMap.isTouching(boxie.getSpatial());
            if(coinTouched!=-1){
                coinMap.detachFromRootNode(coinTouched);
                coinsCollected[coinTouched] = true;
            }
            if(getCoinsCollected()==4){
                Main.getInstance().endLevel();
            }
            if(cheeseMap.isTouching(boxie.getSpatial())==0){
                cheeseMap.detachFromRootNode(0);
                cheeseCollected = true;
                Main.getInstance().setJumpsAvailable(1);
            }
            if(!boxie.isTouching(firstPlatform) && !boxie.isTouching(secondPlatform) && !jumpStarted){
                endLife();
            }
        }
    };
    
    ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean isPressed, float tpf) {
            if(name.equals("StartJump") && isPressed && isCheeseAvailable()){
                boxie.resetTarget();
                boxie.setJumpTargetVisible(); 
            }
            else{
                boxie.setJumpTargetNotVisible(); 
            }
            if(name.equals("Jump") && isPressed && isCheeseAvailable()){
                jumpStarted = true;
                parabola = new Parabola(7,boxie.getJumpDistance());
                boxie.moveForward(jumpSpeed);
                boxie.goToHeight(parabola.getOutput(jumpSpeed));
                currentJumpTraveled+=jumpSpeed;
                cheeseUsed = true;
                Main.getInstance().setJumpsAvailable(0);
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
    public boolean isCheeseAvailable(){
        if(cheeseCollected && !cheeseUsed){
            return true;
        }
        return false;
    }
    public void endJump(){
        jumpStarted = false;
        currentJumpTraveled = 0;
        boxie.goToHeight(0);
    }
    public void endLife(){
        boxie.reset();
        lives--;
        Main.getInstance().setNumOfLives(lives);
        if(lives==0){
            Main.getInstance().failLevel();
        }
    }
    public void reset(){
        lives = 3;
        Main.getInstance().setNumOfLives(3);
        cheeseCollected = false;
        cheeseUsed = false;
    }
    public int getJumpsAvailable() {return 0;}
}
