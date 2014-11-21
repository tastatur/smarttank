package org.smart.robot.boot;

import lejos.geom.Line;
import lejos.nxt.Button;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.pathfinding.Path;
import org.smart.robot.constants.RobotConstants;
import org.smart.robot.sys.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Setup the robot and start the behavior arbiter.
 */
public class Bootstrap {
    public static void main(String[] args) {
        registerSystemEvents();
        Robot robot = new BehaviorRobot();
        if(robot.setup()) {
            robot.start();
        }
    }

    private static void registerSystemEvents() {
        Button.ENTER.addButtonListener(new Shutdown());
    }
}
