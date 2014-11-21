package org.smart.robot.behaviors;

import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.subsumption.Behavior;

/**
 * Just move forward, if I have nothing more to do.
 * I also save my path, so I can always go back
 */
public class DumbForward implements Behavior {
    private static final double DEFAULT_FORWARD_SPEED = 50;
    private static final double MIN_DISTANCE = 10;
    private DifferentialPilot pilot;
    private Path myPath;
    private PoseProvider poseProvider;
    private boolean moveForward = true;
    private Pose previousPose = new Pose(0, 0, 0);

    public DumbForward(DifferentialPilot pilot, Path myPath, PoseProvider poseProvider) {
        this.pilot = pilot;
        this.myPath = myPath;
        this.poseProvider = poseProvider;
    }

    @Override
    public boolean takeControl() {
        return canMoveForward();
    }

    private boolean canMoveForward() {
        return !pilot.isMoving() && !pilot.isStalled();
    }

    @Override
    public void action() {
        moveForward = true;
        pilot.setTravelSpeed(DEFAULT_FORWARD_SPEED);
        pilot.forward();
        while (moveForward) {
            if(poseProvider.getPose().distanceTo(previousPose.getLocation()) > MIN_DISTANCE) {
                myPath.add(new Waypoint(poseProvider.getPose()));
                previousPose = poseProvider.getPose();
            }
            Thread.yield();
        }
        pilot.stop();
    }

    @Override
    public void suppress() {
        moveForward = false;
    }
}
