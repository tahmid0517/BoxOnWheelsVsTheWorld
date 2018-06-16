/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
public class EndScreen extends AbstractAppState implements ScreenController {
    Nifty nifty;
    @Override
    public void initialize(AppStateManager stateManager, Application app){
        nifty = StartScreen.getInstance().display.getNifty();
        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");
        nifty.addScreen("endScreen",new ScreenBuilder("endScreen"){{
            controller(new EndScreen());
            layer(new LayerBuilder(){{
                childLayoutAbsolute();
                image(new ImageBuilder(){{
                    filename("Interface/endscreen.png");
                    x("12%");
                    y("20%");
                }});
            }});
            layer(new LayerBuilder(){{
                childLayoutAbsolute();
                control(new ButtonBuilder("button","RESTART"){{
                    height("5%");
                    width("15%");
                    x("43%");
                    y("90%");
                    interactOnClick("restartGame()");
                }});
            }});
        }}.build(nifty));
         nifty.gotoScreen("endScreen");
    }
    public void restartGame(){
        Main.getInstance().currentLevel = 1;
        Main.getInstance().boxie.reset();
        Main.getInstance().restartGame();
    }
    public void bind(Nifty nifty, Screen screen) {}
    public void onStartScreen() {}
    public void onEndScreen() {}     
}
