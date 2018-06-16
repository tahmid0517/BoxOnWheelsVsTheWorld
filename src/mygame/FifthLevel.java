/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class FifthLevel extends AbstractAppState implements Level {
    Boxie boxie;
    Camera cam;
    boolean initializedInput = false;
    private static final float rotateSpeed = 0.045f;
    private static final float moveScale = 7;
    private static final float jumpSpeed = 1f;
    private static final float targetSpeed = 0.5f;
    private static final float platformSpeed = 0.0125f;
    private static final float movingPlatformBoundary = 44;
    private static final float platformRadius = 33;
    private static final float fullCircle = 6.283185307f;
    float currentJumpTraveled;
    CoinMap coinMap;
    CheeseMap cheeseMap;
    boolean jumpStarted;
    Parabola parabola;
    int jumpsUsed;
    int lives;
    Platform middlePlatform,endPlatform;
    MovingPlatform firstPlanetary,secondPlanetary,thirdPlanetary,fourthPlanetary;
    float platformDirection = -1;
    float platformBearing;
    boolean takingInput;
    String control;
    @Override
    public void initialize(AppStateManager stateManager, Application app){
        takingInput = false;
        control = "";
        boxie = Main.getInstance().boxie;
        boxie.attachToRootNode();
        boxie.reset();
        cam = Main.getInstance().getCamera();
        middlePlatform = new Platform(2,new Vector3f(10,1,10),new Vector3f(0,0,0),true);
        middlePlatform.geom.rotate(0,3.14159254f,0);
        endPlatform = new Platform(1,new Vector3f(18,1,12),new Vector3f(-90,0,0),true);
        endPlatform.geom.rotate(0,1.570796327f,0);
        Vector3f planetaryDimensions = new Vector3f(5,1,5);
        firstPlanetary = new MovingPlatform(0,planetaryDimensions,new Vector3f(platformRadius,0,0),true);
        secondPlanetary = new MovingPlatform(0,planetaryDimensions,new Vector3f(0,0,platformRadius),true);
        thirdPlanetary = new MovingPlatform(0,planetaryDimensions,new Vector3f(-platformRadius,0,0),true);
        fourthPlanetary = new MovingPlatform(0,planetaryDimensions,new Vector3f(0,0,-platformRadius),true);
        initInput();
        platformBearing = 0;
        cheeseMap = new CheeseMap(6);
        float cheeseHeight = 2;
        float cheeseRadius = platformRadius + 3;
        cheeseMap.setLocations(new float[][]{{7,cheeseHeight,5},{7,cheeseHeight,-5},{cheeseRadius,cheeseHeight,0},{0,cheeseHeight,cheeseRadius},{-cheeseRadius,cheeseHeight,0},{0,cheeseHeight,-cheeseRadius}});
        cheeseMap.scaleAll(0.25f);
        cheeseMap.centreAll(1.8f);
        cheeseMap.attachToRootNode();
        coinMap = new CoinMap(9);
        float coinHeight = 2.5f;
        float coinRadius = platformRadius - 4;
        coinMap.setLocations(new float[][]{{coinRadius,coinHeight,0},{0,cheeseHeight,coinRadius},{-coinRadius,cheeseHeight,0},{0,cheeseHeight,-coinRadius},{-90,cheeseHeight,0},{-90,cheeseHeight,6},{-90,cheeseHeight,-6},{-90,cheeseHeight,12},{-90,cheeseHeight,-12}});
        coinMap.scaleAll(0.4f);
        coinMap.attachToRootNode();
        firstPlanetary.attach(new Object[]{cheeseMap.getCheese(2),coinMap.getCoin(0)});
        secondPlanetary.attach(new Object[]{cheeseMap.getCheese(3),coinMap.getCoin(1)});
        thirdPlanetary.attach(new Object[]{cheeseMap.getCheese(4),coinMap.getCoin(2)});
        fourthPlanetary.attach(new Object[]{cheeseMap.getCheese(5),coinMap.getCoin(3)});
        jumpsUsed = 0;
        lives = 3;
        jumpStarted = false;
        currentJumpTraveled = 0;
        Main.getInstance().turnOnNumOfLivesHUD();
        Main.getInstance().turnOnJumpsAvailableHUD();
    }
    @Override
    public void update(float f){
        update();
        if(control!=""){
            boxie.movementControl(control,2,0.15f);
            boxie.targetControl(control,targetSpeed);
        }
        else{
            try{Thread.sleep(10);}
            catch(InterruptedException e){}
        }
        control = "";
    }
    public void update(){
        cam.setLocation(new Vector3f(70,50,0));
        cam.lookAt(new Vector3f(0,0,0), new Vector3f(0,0,0));
        float cos = (float)Math.cos(platformBearing)*platformRadius;
        float sin = (float)Math.sin(platformBearing)*platformRadius;
        firstPlanetary.setLocation(cos,0,sin);
        secondPlanetary.setLocation(-sin,0,cos);
        thirdPlanetary.setLocation(-cos,0,-sin);
        fourthPlanetary.setLocation(sin,0,-cos);
        platformBearing+=platformSpeed;
        if(platformBearing>=fullCircle){
            platformBearing = 0;
        }
        MovingPlatform[] movingPlatforms = {firstPlanetary,secondPlanetary,thirdPlanetary,fourthPlanetary};
        boxie.movingPlatformControl(movingPlatforms);
        cheeseMap.rotateAll(0.02f);
        coinMap.rotateAll(-0.03f);
        cheeseMap.runCollector(boxie.getSpatial());
        coinMap.runCollector(boxie.getSpatial());
        Platform[] platforms = {middlePlatform,endPlatform,firstPlanetary,secondPlanetary,thirdPlanetary,fourthPlanetary};
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
            control = name;
        }    
    };
    ActionListener actionListener = new ActionListener(){
        public void onAction(String name, boolean isPressed, float tpf) {
            if(name.equals(Input.STARTJUMP) && isPressed && isCheeseAvailable()){
                boxie.startJump();
            }
            else if(name.equals(Input.JUMP) && isPressed && isCheeseAvailable() && boxie.isJumpTargetVisible()){
                jumpStarted = true;
                parabola = new Parabola(15,boxie.getJumpDistance());
                boxie.moveForward(jumpSpeed);
                boxie.goToHeight(parabola.getOutput(jumpSpeed));
                currentJumpTraveled+=jumpSpeed;
                jumpsUsed++;
                boxie.setJumpTargetNotVisible();
                boxie.resetTarget();
            }
            else{
                boxie.resetTarget();
                boxie.setJumpTargetNotVisible();
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
    public void endLife() {
        boxie.reset();
        lives--;
        Main.getInstance().setNumOfLives(lives);
    }
    public int getJumpsAvailable() {
        return Math.max(0,cheeseMap.getCheeseCollected()-jumpsUsed);
    }
    public boolean isCheeseAvailable() {
        if(getJumpsAvailable()>0){
            return true;
        }
        return false;
    }
}
