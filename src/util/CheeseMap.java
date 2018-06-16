package util;

import com.jme3.scene.Spatial;

public class CheeseMap {
    Cheese[] cheese;
    boolean[] cheeseCollected;
    public CheeseMap(int n){
        cheese = new Cheese[n];
        cheeseCollected = new boolean[n];
        for(int i = 0;i<n;i++){
            cheese[i] = new Cheese();
            cheeseCollected[i] = false;
        }
    }
    public void attachToRootNode(){
        for(int i = 0;i<cheese.length;i++){
            cheese[i].attachToRootNode();
        }
    }
    public void setLocations(float[][] coordinates){
        for(int i = 0;i<cheese.length;i++){
            cheese[i].setLocation(coordinates[i][0],coordinates[i][1],coordinates[i][2]);
        }
    }
    public void scaleAll(float f){
        for(int i = 0;i<cheese.length;i++){
            cheese[i].scale(f);
        }
    }
    public void rotateAll(float f){
        for(int i = 0;i<cheese.length;i++){
            cheese[i].rotate(f);
        }
    }
    public int isTouching(Spatial spatial){
        for(int i = 0;i<cheese.length;i++){
            if(cheese[i].isTouching(spatial)){
                return i;
            }
        }
        return -1;
    }
    public void detachFromRootNode(int i){
        cheese[i].detachFromRootNode();
    }
    public void centreAll(float z){
        for(int i = 0;i<cheese.length;i++){
            cheese[i].centreSpatial(z);
        }
    }
    public void collect(int i){
        detachFromRootNode(i);
        cheeseCollected[i] = true;
    }
    public void runCollector(Spatial spatial){
        int collected = isTouching(spatial);
        if(collected!=-1){
            collect(collected);
        }
    }
    public int getCheeseCollected(){
        int total = 0;
        for(int i = 0;i<cheese.length;i++){
            if(cheeseCollected[i]){
                total++;
            }
        }
        return total;
    }
    public Cheese getCheese(int i){
        return cheese[i];
    }
}
