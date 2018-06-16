package util;
public class Parabola {
    float a,middle,maxHeight;
    public Parabola(float maxHeight,float distance){
        middle = distance/2;
        this.maxHeight = maxHeight;
        a = -maxHeight/(middle*middle);
    }
    public float getOutput(float input){
        return a*((input-middle)*(input-middle)) + maxHeight;
    }
}
