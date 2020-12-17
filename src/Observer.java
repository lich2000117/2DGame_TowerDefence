/**
 * Abstract class using observer design pattern
 *
 * **/

public abstract class Observer {
    protected Player player;
    protected Level level;
    public abstract void updateInfo();
}
