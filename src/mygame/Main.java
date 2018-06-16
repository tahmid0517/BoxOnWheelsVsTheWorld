package mygame;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.font.BitmapText;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;
import util.Boxie;
import util.Input;
import util.Level;
public class Main extends SimpleApplication {
    private static Main instance;
    public int currentLevel;
    StartScreen startScreen;
    PrepScreen prepScreen;
    GameOverScreen gameOver;
    EndScreen endScreen;
    FirstLevel firstLevel;
    SecondLevel secondLevel;
    ThirdLevel thirdLevel;
    FourthLevel fourthLevel;
    FifthLevel fifthLevel;
    Boxie boxie;
    Input input;
    BitmapText numOfLives,jumpsAvailable;
    Level[] levels;
    public Main(){
        currentLevel = 1;
        startScreen = StartScreen.getInstance();
        firstLevel = new FirstLevel();
        prepScreen = new PrepScreen();
        gameOver = new GameOverScreen();
        endScreen = new EndScreen();
        secondLevel = new SecondLevel();
        thirdLevel = new ThirdLevel();
        fourthLevel = new FourthLevel();
        fifthLevel = new FifthLevel();
        levels = new Level[]{firstLevel,secondLevel,thirdLevel,fourthLevel,fifthLevel};
    }
    public static Main getInstance(){
        if(instance==null){
            instance = new Main();
        }
        return instance;
    }
    public static void main(String args[]){
        getInstance().setShowSettings(false);
        AppSettings settings = new AppSettings(true);
        settings.setResolution(1024,768);
        settings.setTitle("Boxie Vs The World");
        getInstance().setSettings(settings);
        getInstance().start();
    }
    @Override
    public void simpleInitApp() {
       stateManager.attach(startScreen);
       setDisplayFps(false);
       setDisplayStatView(false);
       initLights();
       boxie = new Boxie();
       input = Input.getInstance();
       numOfLives = new BitmapText(guiFont,false);
       numOfLives.setColor(ColorRGBA.White);
       numOfLives.setSize(25);
       numOfLives.setLocalTranslation(0,getHeight(),0);
       numOfLives.setText("LIVES: 3");
       jumpsAvailable = new BitmapText(guiFont,false);
       jumpsAvailable.setColor(ColorRGBA.White);
       jumpsAvailable.setSize(25);
       jumpsAvailable.setLocalTranslation(0,getHeight()-25,0);
    }
    public void startGame(){
        stateManager.detach(startScreen);
        startScreen.nifty.exit();
        stateManager.attach(prepScreen);
    }
    public void restartGame(){
        stateManager.detach(endScreen);
        endScreen.nifty.exit();
        stateManager.attach(prepScreen);
    }
    public void startLevel(){
        if(gameOver.onScreen){
            stateManager.detach(gameOver);
            gameOver.onScreen = false;
        }
        boxie.attachToRootNode();
        stateManager.detach(prepScreen);
        prepScreen.nifty.exit();
        levels[currentLevel-1].reset();
        stateManager.attach((AppState)levels[currentLevel-1]);
    }
    public void failLevel(){
        detachCurrentLevel();
        rootNode.detachAllChildren();
        guiNode.detachAllChildren();
        stateManager.attach(gameOver);
    }
    public void endLevel(){
        detachCurrentLevel();
        currentLevel++;
        rootNode.detachAllChildren();
        guiNode.detachAllChildren();
        if(currentLevel==6){
            stateManager.attach(endScreen);
        }
        else{
            stateManager.attach(prepScreen);
        }
        input.clearMap();
    }
    public void initLights(){
        DirectionalLight top = new DirectionalLight();
        top.setDirection(new Vector3f(0,-1,0));
        rootNode.addLight(top);
        DirectionalLight front = new DirectionalLight();
        front.setDirection(new Vector3f(-1,0,0));
        rootNode.addLight(front);
        DirectionalLight side1 = new DirectionalLight();
        side1.setDirection(new Vector3f(0,0,-1));
        rootNode.addLight(side1);
        DirectionalLight side2 = new DirectionalLight();
        side2.setDirection(new Vector3f(0,0,1));
        rootNode.addLight(side2);
    }
    public int getHeight(){
        return settings.getHeight();
    }
    public void turnOnNumOfLivesHUD(){
        guiNode.attachChild(numOfLives);
    }
    public void setNumOfLives(int n){
        numOfLives.setText("LIVES: "+n);
    }
    public void turnOnJumpsAvailableHUD(){
        guiNode.attachChild(jumpsAvailable);
    }
    public void setJumpsAvailable(int n){
        jumpsAvailable.setText("JUMPS AVAILABLE: "+n);
    }
    public void detachCurrentLevel(){
        stateManager.detach((AppState)levels[currentLevel-1]);
    }
}
    
   

