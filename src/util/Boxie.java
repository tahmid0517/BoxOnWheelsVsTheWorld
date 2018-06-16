package util;

import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import mygame.Main;

public class Boxie implements Object {
    static final float GROUNDLEVEL = 1.2999983f;
    static final float FULLCIRCLE = 6.283185307f;
    private Spatial spatial;
    private Spatial jumpTarget;
    private Node node;
    private float bearing;
    private float jumpDistance;
    private boolean jumpTargetVisible;
    static final float scale = 4;
    static final float rotateSpeed = 0.1f;
    public Boxie(){
        AssetManager assetManager = Main.getInstance().getAssetManager();
        spatial = assetManager.loadModel("Models/BoxOnWheels/BoxOnWheels.mesh.j3o");
        spatial.setMaterial(Main.getInstance().getAssetManager().loadMaterial("Materials/CharacterMaterial.j3m"));
        node = new Node();
        node.attachChild(spatial);
        spatial.scale(0.25f);
        spatial.setLocalTranslation(2,GROUNDLEVEL,1.3f);
        jumpTarget = assetManager.loadModel("Models/Target/Target.mesh.j3o");
        Material targetMat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        targetMat.setColor("Color",ColorRGBA.Red);
        jumpTarget.setMaterial(targetMat);
        jumpTarget.setLocalTranslation(4.75f,1,0);
        bearing = 0;
        jumpDistance = 0;
        jumpTargetVisible = false;
    }
    public void attachToRootNode(){
        Main.getInstance().getRootNode().attachChild(node);
    }
    public void detachFromRootNode(){
        Main.getInstance().getRootNode().detachChild(node);
    }
    public void move(float x,float y,float z){
        node.move(x,y,z);
    }
    public void rotate(float rotateSpeed){
        node.rotate(0,rotateSpeed,0);
        bearing+=rotateSpeed;
        if(bearing<0){
            bearing+=FULLCIRCLE;
        }
        else if(bearing>FULLCIRCLE){
            bearing-=FULLCIRCLE;
        }
    }
    public void setLocation(float x,float y,float z){
        node.setLocalTranslation(x, y, z);
    }
    public void setOriginalRotation(){
        node.setLocalRotation(new Quaternion(0,0,0,1));
        bearing = 0;
    }
    public void forward(float scale){
        move((float)Math.cos(bearing)/scale,0,(float)-Math.sin(bearing)/scale);
    }
    public void backward(float scale){
        move((float)-Math.cos(bearing)/scale,0,(float)Math.sin(bearing)/scale);
    }
    public void reset(){
        switch(Main.getInstance().currentLevel){
            case 1:
                setLocation(0,0,0);
                setOriginalRotation();
                break;
            case 2:
                setLocation(-5,0,-18);
                setOriginalRotation();
                break;
            case 3:
                setLocation(-24,0,10);
                setOriginalRotation();
                rotate(1.570796327f);
                break;
            case 4:
                setLocation(34,0,8);
                setOriginalRotation();
                break;
            case 5:
                setLocation(-5,0,0);
                setOriginalRotation();
                break;
        }
        
    }
    public Node getNode(){
        return node;
    }
    public Spatial getSpatial(){
        return spatial;
    }
    public boolean isTouching(Spatial model){
        CollisionResults results = new CollisionResults();
        CollisionResults results2 = new CollisionResults();
        spatial.collideWith(model.getWorldBound(), results);
        model.collideWith(spatial.getWorldBound(), results2);
        if(results.size()==0 && results2.size()==0){
            return false;
        }
        return true;
    }
    public boolean isTouching(Platform[] models){
        for(int i = 0;i<models.length;i++){
            if(isTouching(models[i].geom)){
                return true; 
            }
        }
        return false;
    }
    public boolean isInAir(Platform[] models){
        if(!isTouching(models) && getHeight()>0){
            return true;
        }
        return false;
    }
    public void moveForward(float f){
         move((float)Math.cos(bearing)*f,0,(float)-Math.sin(bearing)*f);
    }
    public void goToHeight(float h){
        Vector3f currentPos = node.getLocalTranslation();
        node.setLocalTranslation(currentPos.x,h,currentPos.z);
    }
    public void setJumpTargetVisible(){
        node.attachChild(jumpTarget);
        jumpTargetVisible = true;
    }
    public void setJumpTargetNotVisible(){
        node.detachChild(jumpTarget);
        jumpTargetVisible = false;
    }
    public boolean isJumpTargetVisible(){
        return jumpTargetVisible;
    }
    public void moveTarget(float f){
        jumpTarget.move(f,0,0);
        jumpDistance+=f;
    }
    public void resetTarget(){
        jumpTarget.setLocalTranslation(0,1,0);
        jumpDistance = 0;
    }
    public float getJumpDistance(){
        return jumpDistance;
    }
    public float getHeight(){
        return node.getLocalTranslation().y;
    }
    public void movementControl(String control){
        movementControl(control,scale,rotateSpeed);
    }
    public void movementControl(String control,float scale,float rotateSpeed){
        if(control.equals(Input.TURNRIGHT)){
            rotate(-rotateSpeed);
        }
        if(control.equals(Input.TURNLEFT)){
            rotate(rotateSpeed);
        }
        if(control.equals(Input.FORWARD)){
            forward(scale);
        }
        if(control.equals(Input.BACKWARD)){
            backward(scale);
        }
        try {Thread.sleep(10);} 
        catch (InterruptedException e) {}
    }
    public void targetControl(String control,float speed){
        if(control.equals(Input.STARTJUMP)){
            moveTarget(speed);
        }
    }
    public void movingPlatformControl(MovingPlatform[] platforms){
        for(int i = 0;i<platforms.length;i++){
            if(isTouching(platforms[i].geom)){
                platforms[i].attach(this);
                return;
            }
            platforms[i].detach(this);
        }
    }
    public void startJump(){
        resetTarget();
        setJumpTargetVisible();
    }
}
