package mygame;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import util.Boxie;
import util.CheeseMap;
import util.CoinMap;
import util.Input;
import util.Level;
import util.Parabola;
import util.Platform;
public class ThirdLevel extends AbstractAppState implements Level{
    Platform firstPlatform,secondPlatform,thirdPlatform;
    Node rootNode;
    Camera cam;
    Boxie boxie;
    boolean initializedInput = false;
    private static final float rotateSpeed = 0.12f;
    private static final float moveScale = 3;
    private static final float jumpSpeed = 0.5f;
    private static final float targetSpeed = 0.4f;
    float currentJumpTraveled;
    CoinMap coinMap;
    CheeseMap cheeseMap;
    boolean jumpStarted;
    Parabola parabola;
    int jumpsUsed;
    int lives;
    @Override
    public void initialize(AppStateManager stateManager, Application app){
        rootNode = Main.getInstance().getRootNode();
        cam = Main.getInstance().getCamera();
        boxie = Main.getInstance().boxie;
        Vector3f frontPlatformDimensions = new Vector3f(6,1,6);
        firstPlatform = new Platform(0,frontPlatformDimensions,new Vector3f(11.5f,0,10.5f),true);
        secondPlatform = new Platform(0,frontPlatformDimensions,new Vector3f(11.5f,0,-10.5f),true);
        thirdPlatform = new Platform(1,new Vector3f(18,6,12),new Vector3f(-26,-5,0),true);
        thirdPlatform.geom.rotate(0,1.570796327f,0);
        boxie.reset();
        boxie.attachToRootNode();
        initInput();
        coinMap = new CoinMap(7);
        float coinHeight = 2;
        coinMap.setLocations(new float[][]{{11.5f,coinHeight,15.5f},{11.5f,coinHeight,5},{6.25f,coinHeight,-10.25f},{17,coinHeight,-10.25f},{-14.5f,coinHeight,0},{-33.5f,coinHeight,15},{-33.5f,coinHeight,-15}});
        coinMap.scaleAll(0.25f);
        coinMap.attachToRootNode();
        cheeseMap = new CheeseMap(3);
        float cheeseHeight = 2;
        cheeseMap.setLocations(new float[][]{{11.5f,cheeseHeight,10.25f},{11.5f,cheeseHeight,-10.25f},{-25,cheeseHeight,-8}});
        cheeseMap.scaleAll(0.25f);
        cheeseMap.centreAll(1.8f);
        cheeseMap.attachToRootNode();
        currentJumpTraveled = 0;
        jumpStarted = false;
        jumpsUsed = 0;
        lives = 3;
        Main.getInstance().turnOnNumOfLivesHUD();
        Main.getInstance().turnOnJumpsAvailableHUD();
    }
    @Override
    public void update(float f){
        update();
        try{Thread.sleep(10);}
        catch(InterruptedException e){}
    }
    public void update(){
        cam.setLocation(new Vector3f(30,30,0));
        cam.lookAt(new Vector3f(0,0,0),new Vector3f(0,0,0));
        coinMap.rotateAll(-0.05f);
        cheeseMap.rotateAll(0.03f);
        Platform[] platforms = {firstPlatform,secondPlatform,thirdPlatform};
        if(!boxie.isInAir(platforms) && !boxie.isTouching(platforms)){
            endLife();
        }
        else if(jumpStarted && boxie.isInAir(platforms)){
            boxie.goToHeight(parabola.getOutput(currentJumpTraveled));
            boxie.moveForward(jumpSpeed);
            currentJumpTraveled+=jumpSpeed;
        }
        else{
            endJump();
        }
        if(lives==0){
            Main.getInstance().failLevel();
        }
        coinMap.runCollector(boxie.getSpatial());
        cheeseMap.runCollector(boxie.getSpatial());
        Main.getInstance().setJumpsAvailable(getJumpsAvailable());
    }
    public void initInput(){
        if(!initializedInput){
            Input.getInstance().addDefaults(analogListener,actionListener);
            initializedInput = true;
        }
    }
    AnalogListener analogListener = new AnalogListener(){
        public void onAnalog(String name, float value, float tpf) {
            boxie.movementControl(name,2,0.2f);
            update();
            if(name.equals(Input.STARTJUMP) && !jumpStarted && isCheeseAvailable()){
                boxie.moveTarget(targetSpeed);
            }
            Main.getInstance().setNumOfLives(lives);
        } 
    };
    ActionListener actionListener = new ActionListener(){
        public void onAction(String name, boolean isPressed, float tpf) {
            if(name.equals(Input.STARTJUMP) && isPressed && isCheeseAvailable()){
                boxie.resetTarget();
                boxie.setJumpTargetVisible(); 
            }
            else{
                boxie.setJumpTargetNotVisible(); 
            }
            if(name.equals(Input.JUMP) && isCheeseAvailable()){
                jumpStarted = true;
                parabola = new Parabola(7,boxie.getJumpDistance());
                boxie.moveForward(jumpSpeed);
                boxie.goToHeight(parabola.getOutput(jumpSpeed));
                currentJumpTraveled+=jumpSpeed;
                jumpsUsed++;
            }
        }  
    };
    public int getJumpsAvailable(){
        return Math.max(0,cheeseMap.getCheeseCollected()-jumpsUsed);
    }
    public boolean isCheeseAvailable(){
        if(getJumpsAvailable()>0){
            return true;
        }
        return false;
    }
    public void endLife(){
        endJump();
        lives--;
        boxie.reset();
        Main.getInstance().setNumOfLives(lives);
    }
    public void reset() {
        lives = 3;
        Main.getInstance().setNumOfLives(lives);
        Main.getInstance().setJumpsAvailable(0);
    }
    public void endJump() {
        jumpStarted = false;
        currentJumpTraveled = 0;
        boxie.goToHeight(0);
    }
}
