package util;
import com.jme3.app.state.AbstractAppState;
/**
 *
 * @author owner
 */
public interface Level  {
    public abstract void reset();
    public abstract void endJump();
    public abstract void endLife();
    public abstract int getJumpsAvailable();
    public abstract boolean isCheeseAvailable();
}
