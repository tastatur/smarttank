package org.smart.robot.boot;

public interface Robot {
    /**
     * Prepare it for the live
     */
    public boolean setup();
    /**
     * It is Alive!
     */
    public void start();
}
