/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
public class StartScreen extends AbstractAppState implements ScreenController {
    AssetManager assetManager; 
    InputManager inputManager;
    AudioRenderer audioRenderer;
    ViewPort guiViewPort;
    FlyByCamera flyCam;
    NiftyJmeDisplay display;
    Nifty nifty;
    private static StartScreen instance;
    public static StartScreen getInstance(){
        if(instance==null){
            instance = new StartScreen();
        }
        return instance;
    }
    @Override
    public void initialize (AppStateManager stateManager, Application app){
        assetManager = Main.getInstance().getAssetManager();
        inputManager = Main.getInstance().getInputManager();
        audioRenderer = Main.getInstance().getAudioRenderer();
        guiViewPort = Main.getInstance().getGuiViewPort();
        flyCam = Main.getInstance().getFlyByCamera();
        display = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        nifty = display.getNifty();
        guiViewPort.addProcessor(display);
        flyCam.setDragToRotate(true);
        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");
        nifty.addScreen("StartScreen",new ScreenBuilder("StartScreen"){{
            controller(StartScreen.getInstance());
            layer(new LayerBuilder("layer"){{
                childLayoutVertical();
                panel(new PanelBuilder("panel"){{
                    childLayoutAbsolute();
                    control(new ButtonBuilder("startButton","START GAME"){{
                        int height = 50;
                        int width = 100;
                        height("5%");
                        width("15%");
                        x("43%");
                        y("90%");
                        interactOnClick("startGame()");
                    }});
                }});
            }});
            layer(new LayerBuilder("layer"){{
                childLayoutCenter();
                image(new ImageBuilder(){{
                    filename("Interface/startpage.png");
                    height("75%");
                    width("75%");
                }});
            }});
        }}.build(nifty));
        nifty.gotoScreen("StartScreen");
    }
    @Override
    public void update(float f){
        
    }
    public void startGame(){
        Main.getInstance().startGame();
    }
    public void bind(Nifty nifty, Screen screen) {}
    public void onStartScreen() {}
    public void onEndScreen() {}
}
