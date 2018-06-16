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
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
public class GameOverScreen extends AbstractAppState implements ScreenController {
    boolean onScreen = false;
    Nifty nifty;
    @Override
    public void initialize(AppStateManager stateManager,Application app){
        onScreen = true;
        nifty = StartScreen.getInstance().display.getNifty();
        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");
        nifty.addScreen("GameOverScreen",new ScreenBuilder("GameOverScreen"){{
            controller(new GameOverScreen(){{
                layer(new LayerBuilder(){{
                    childLayoutVertical();
                    panel(new PanelBuilder("panel"){{
                        childLayoutAbsolute();
                        text(new TextBuilder("text"){{
                            text("Looks like you messed up and died. Don't worry. We all make mistakes. Click to try again.");
                            x("25%");
                            y("83%");
                            height("50%");
                            width("50%");
                            wrap(true);
                            font("Interface/Fonts/Default.fnt");
                        }});
                        control(new ButtonBuilder("button","LET'S TRY THIS AGAIN!"){{
                            height("5%");
                            width("15%");
                            x("43%");
                            y("90%");
                            interactOnClick("retry()");
                        }});
                    }});
                }});
                layer(new LayerBuilder(){{
                    childLayoutCenter();
                    image(new ImageBuilder() {{
                    filename("Interface/gameover.png");
                    height("50%");
                    width("50%");
                    }});
                }});
            }});
        }}.build(nifty));
        nifty.gotoScreen("GameOverScreen");
    }
    public void retry(){
        Main.getInstance().startLevel();
    }
    public void bind(Nifty nifty,Screen screen){}
    public void onStartScreen() {}
    public void onEndScreen() {}
}
