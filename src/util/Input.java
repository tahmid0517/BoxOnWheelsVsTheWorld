package util;

import com.jme3.input.controls.InputListener;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import java.util.ArrayList;
import mygame.Main;

public class Input {
    public static final String TURNRIGHT = "TurnRight";
    public static final String TURNLEFT = "TurnLeft";
    public static final String FORWARD = "Forward";
    public static final String BACKWARD = "Backward";
    public static final String STARTJUMP = "StartJump";
    public static final String JUMP = "Jump";
    InputManager inputManager;
    ArrayList<String> map;
    private static Input instance;
    public static Input getInstance(){
        if(instance==null){
            instance = new Input();
        }
        return instance;
    }
    public Input(){
        inputManager = Main.getInstance().getInputManager();
        map = new ArrayList<String>();
    }
    public void addToMap(String mapping,int keyInput,InputListener listener){
        inputManager.addMapping(mapping,new KeyTrigger(keyInput));
        inputManager.addListener(listener,mapping);
        map.add(mapping);
    }
    public void clearMap(){
        for(int i = 0;i<map.size();i++){
            inputManager.deleteMapping(map.get(i));
        }
        map.clear();
    }
    public void addDefaults(AnalogListener analogListener,ActionListener actionListener){
        addToMap(TURNRIGHT,KeyInput.KEY_RIGHT,analogListener);
        addToMap(TURNLEFT,KeyInput.KEY_LEFT,analogListener);
        addToMap(FORWARD,KeyInput.KEY_UP,analogListener);
        addToMap(BACKWARD,KeyInput.KEY_DOWN,analogListener);
        addToMap(STARTJUMP,KeyInput.KEY_Z,actionListener);
        addToMap(STARTJUMP,KeyInput.KEY_Z,analogListener);
        addToMap(JUMP,KeyInput.KEY_X,actionListener);
    }
}
