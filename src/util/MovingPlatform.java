package util;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
public class MovingPlatform extends Platform {
    ArrayList<Object> objects;
    public MovingPlatform(int material,Vector3f dimensions,Vector3f location,boolean attachNow){
        super(material,dimensions,location,attachNow);
        objects = new ArrayList<Object>();
    }
    public void attach(Object object){
        if(!objects.contains(object)){
            objects.add(object);
        }
    }
    public void attach(Object[] objectArr){
        for(int i = 0;i<objectArr.length;i++){
            attach(objectArr[i]);
        }
    }
    public void detach(Object object){
        if(objects.contains(object)){
            objects.remove(object);
        }
    }
    public void move(float x,float y,float z){
        super.geom.move(x,y,z);
        for(int i = 0;i<objects.size();i++){
            objects.get(i).move(x, y, z);
        }
    }
    public void setLocation(float x,float y,float z){
        Vector3f currentLocation = super.geom.getWorldTranslation();
        float moveX = x - currentLocation.x;
        float moveY = y - currentLocation.y;
        float moveZ = z - currentLocation.z;
        for(int i = 0;i<objects.size();i++){
            objects.get(i).move(moveX,moveY,moveZ);
        }
        geom.setLocalTranslation(x,y,z);
    }
    public Vector3f getLocation(){
        return super.geom.getLocalTranslation();
    }
}
