package util;

import com.jme3.scene.Spatial;
import mygame.Main;
public class CoinMap {
    Coin[] coins;
    boolean[] coinsCollected;
    public CoinMap(int n){
        coins = new Coin[n];
        coinsCollected = new boolean[n];
        for(int i = 0;i<n;i++){
            coins[i] = new Coin();
            coinsCollected[i] = false;
        }
    }
    public void attachToRootNode(){
        for(int i = 0;i<coins.length;i++){
            coins[i].attachToRootNode();
        }
    }
    public void setLocations(float[][] coordinates){
        for(int i = 0;i<coins.length;i++){
            coins[i].setLocation(coordinates[i][0],coordinates[i][1],coordinates[i][2]);
        }
    }
    public void scaleAll(float f){
        for(int i = 0;i<coins.length;i++){
            coins[i].scale(f);
        }
    }
    public void rotateAll(float f){
        for(int i = 0;i<coins.length;i++){
            coins[i].rotate(f);
        }
    }
    public int isTouching(Spatial spatial){
        for(int i = 0;i<coins.length;i++){
            if(coins[i].isTouching(spatial)){
                return i;
            }
        }
        return -1;
    }
    public void detachFromRootNode(int i){
        coins[i].detachFromRootNode();
    }
    public void collect(int i){
        detachFromRootNode(i);
        coinsCollected[i] = true;
    }
    public int getCoinsCollected(){
        int total = 0;
        for(int i = 0;i<coinsCollected.length;i++){
            if(coinsCollected[i]){
                total++;
            }
        
        }
    return total;
    }
    public void runCollector(Spatial spatial){
        int coinCollected = isTouching(spatial);
        if(coinCollected!=-1){
            collect(coinCollected);
        }
        if(getCoinsCollected()==coins.length){
            Main.getInstance().endLevel();
        }
    }
    public Coin getCoin(int i){
        return coins[i];
    }
}