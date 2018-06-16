package mygame;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class PrepScreen extends AbstractAppState implements ScreenController {
    Nifty nifty;
    @Override
    public void initialize(AppStateManager stateManager, Application app){
        nifty = StartScreen.getInstance().display.getNifty();
        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");
        nifty.addScreen("prepScreen",new ScreenBuilder("Screen"){{
            controller(new PrepScreen());
            layer(new LayerBuilder("layer"){{
                childLayoutVertical();
                panel(new PanelBuilder("panel"){{
                    childLayoutAbsolute();
                    control(new LabelBuilder(){{
                        x("37%");
                        height("5%");
                        width("25%");
                        text("ONTO LEVEL "+Main.getInstance().currentLevel);
                    }});
                    text(new TextBuilder(){{
                        int level = Main.getInstance().currentLevel;
                        text(getText(level));
                        x("25%");
                        y(getTextPosY(level));
                        height("50%");
                        width("50%");
                        wrap(true);
                        font("Interface/Fonts/Default.fnt");
                    }});
                    control(new ButtonBuilder("button","ONWARD!"){{
                        x("43%");
                        y("90%");
                        height("5%");
                        width("15%");
                        interactOnClick("startLevel()");
                    }});
                }});
            }});
            layer(new LayerBuilder(){{
                childLayoutAbsolute();
                image(new ImageBuilder(){{
                    filename("Interface/boxie.png");
                    y("10%");
                }});
            }});
        }}.build(nifty));
        nifty.gotoScreen("prepScreen");
    }
    public String getText(int level){
        switch(level){
            case 1:
                return "Collect all the coins to move onto the next level. Avoid falling off the platform. Use the up and down keys to move forward and backward and the left and right keys to rotate.";
            case 2:
                return "Collect a cheese to be able to jump. Hold Z to aim your jump and X to jump. Each jump uses up one cheese.";
            case 3:
                return "Keep doing what you've been doing. More cheese and coins in this level.";
            case 4:
                return "Watch out for the moving platforms. They're harder to jump on.";
            case 5:
                return "Want to know what's worse than moving platforms? Platforms that move in a circle.";
        }
        return "";
    }
    public String getTextPosY(int level){
        switch(level){
            case 1:
                return "81%";
            case 4:
                return "87%";
        }
        return "84%";
    }
    public void startLevel(){
        Main.getInstance().startLevel();
    }
    public void onStartScreen() {}
    public void onEndScreen() {}
    public void bind(Nifty nifty, Screen screen) {}
}
