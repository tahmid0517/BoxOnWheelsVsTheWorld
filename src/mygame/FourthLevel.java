package mygame;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import util.Boxie;
import util.CheeseMap;
import util.CoinMap;
import util.Input;
import util.Level;
import util.MovingPlatform;
import util.Parabola;
import util.Platform;
import util.Object;
public class FourthLevel extends AbstractAppState implements Level {
    Boxie boxie;
    Camera cam;
    boolean initializedInput = false;
    private static final float rotateSpeed = 0.045f;
    private static final float moveScale = 7;
    private static final float jumpSpeed = 0.8f;
    private static final float targetSpeed = 0.6f;
    private static final float platformSpeed = 0.3f;
    private static final float movingPlatformBoundary = 44;
    float platformDirection;
    Platform firstPlatform,fourthPlatform;
    MovingPlatform secondPlatform, thirdPlatform;
    float currentJumpTraveled;
    CoinMap coinMap;
    CheeseMap cheeseMap;
    boolean jumpStarted;
    Parabola parabola;
    int jumpsUsed;
    int lives;
    @Override
    public void initialize(AppStateManager stateManager, Application app){
        boxie = Main.getInstance().boxie;
        boxie.attachToRootNode();
        boxie.reset();
        cam = Main.getInstance().getCamera();
        Vector3f sidePlatformDimensions = new Vector3f(18,1,6);
        firstPlatform = new Platform(1,sidePlatformDimensions,new Vector3f(34,0,0),true);
        firstPlatform.geom.rotate(0,1.570796327f,0);
        fourthPlatform = new Platform(1,new Vector3f(36,1,12),new Vector3f(-70,0,0),true);
        fourthPlatform.geom.rotate(0,1.570796327f,0);
        Vector3f movingPlatformDimensions = new Vector3f(6,1,6);
        secondPlatform = new MovingPlatform(0,movingPlatformDimensions,new Vector3f(5,0,0),true);
        thirdPlatform = new MovingPlatform(0,movingPlatformDimensions,new Vector3f(-30,0,0),true);
        platformDirection = 1;
        initInput();
        lives = 3;
        cheeseMap = new CheeseMap(3);
        float cheeseHeight = 2;
        cheeseMap.setLocations(new float[][]{{34,cheeseHeight,-8},{5,cheeseHeight,3},{-30,cheeseHeight,-3}});
        cheeseMap.centreAll(2.5f);
        cheeseMap.scaleAll(0.35f);
        cheeseMap.attachToRootNode();
        coinMap = new CoinMap(5);
        float coinHeight = 2;
        coinMap.setLocations(new float [][]{{5,coinHeight,-5},{-30,coinHeight,5},{-70,coinHeight,10},{-70,coinHeight,0},{-70,coinHeight,-10}});
        coinMap.scaleAll(0.4f);
        coinMap.attachToRootNode();
        secondPlatform.attach(new Object[]{cheeseMap.getCheese(1),coinMap.getCoin(0)});
        thirdPlatform.attach(new Object[]{cheeseMap.getCheese(2),coinMap.getCoin(1)});
        jumpsUsed = 0;
        jumpStarted = false;
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
        if(secondPlatform.getLocation().z>=movingPlatformBoundary || secondPlatform.getLocation().z<=(-movingPlatformBoundary)){
            platformDirection*=-1;
        }
        secondPlatform.move(0,0,platformDirection*platformSpeed);
        thirdPlatform.move(0,0,-platformDirection*platformSpeed);
        boxie.movingPlatformControl(new MovingPlatform[]{secondPlatform,thirdPlatform});
        cheeseMap.rotateAll(0.02f);
        coinMap.rotateAll(-0.02f);
        Platform[] platforms = {firstPlatform,secondPlatform,thirdPlatform,fourthPlatform};
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
        cam.setLocation(new Vector3f(70,70,0));
        cam.lookAt(new Vector3f(0,0,0),new Vector3f(0,0,0));
    }
    public void initInput(){
        if(!initializedInput){
            Input.getInstance().addDefaults(analogListener,actionListener);
            initializedInput = true;
        }
    }
    AnalogListener analogListener = new AnalogListener(){
        public void onAnalog(String name, float value, float tpf) {
            boxie.movementControl(name,1.5f,0.2f);
            if(name.equals(Input.STARTJUMP)){
                boxie.moveTarget(targetSpeed);
            }
            Main.getInstance().setNumOfLives(lives);
            update();
        }
    };
    ActionListener actionListener = new ActionListener(){
        public void onAction(String name, boolean isPressed, float tpf) {
            if(name.equals(Input.STARTJUMP) && isPressed && isCheeseAvailable()){
                boxie.startJump();
            }
            else{
                boxie.setJumpTargetNotVisible();
            }
            if(name.equals(Input.JUMP) && isCheeseAvailable()){
                jumpStarted = true;
                parabola = new Parabola(10,boxie.getJumpDistance());
                boxie.moveForward(jumpSpeed);
                boxie.goToHeight(parabola.getOutput(jumpSpeed));
                currentJumpTraveled+=jumpSpeed;
                jumpsUsed++;
            }
        } 
    };
    public void reset() {
        lives = 3;
        Main.getInstance().setNumOfLives(lives);
    }
    public void endJump() {
        jumpStarted = false;
        currentJumpTraveled = 0;
        boxie.goToHeight(0);
    }
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
}
