package org.smart.robot.sys;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.robotics.subsumption.Behavior;

/**
 * Shutdown the robot, if the big orange button is pressed
 */
public class Shutdown implements ButtonListener {

    @Override
    public void buttonPressed(Button b) {
        System.exit(0);
    }

    @Override
    public void buttonReleased(Button b) {
    }
}
