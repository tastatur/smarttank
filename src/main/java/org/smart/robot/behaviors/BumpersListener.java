package org.smart.robot.behaviors;

import lejos.geom.Line;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.objectdetection.Feature;
import lejos.robotics.objectdetection.FeatureDetector;
import lejos.robotics.objectdetection.FeatureListener;
import lejos.robotics.subsumption.Behavior;
import org.smart.robot.utils.*;

import java.util.List;

/**
 * Reaction on the pressed touch sensors (bumpers)
 */
public class BumpersListener implements FeatureListener, Behavior {
    private static final double BACKDISTANCE = -10;

    private DifferentialPilot pilot;
    private List<Line> obstacles;
    private boolean collisionDetected;
    private BackwardMovement movementMode;
    private float rightBumperRange, leftBumperRange;

    public BumpersListener(DifferentialPilot pilot, List<Line> obstacles, float rightBumperRange, float leftBumperRange) {
        this.pilot = pilot;
        this.obstacles = obstacles;
        this.rightBumperRange = rightBumperRange;
        this.leftBumperRange = leftBumperRange;
    }

    @Override
    public void featureDetected(Feature feature, FeatureDetector detector) {
        this.collisionDetected = true;
        if (isBothBumpersPressed(feature)) {
            movementMode = BackwardMovement.TURN_RANDOM;
        } else if (isLeftBumperPressed(feature)) {
            movementMode = BackwardMovement.TURN_RIGHT;
        } else if (isRightBumperPressed(feature)) {
            movementMode = BackwardMovement.TURN_LEFT;
        }
    }

    private boolean isBothBumpersPressed(Feature feature) {
        return feature.getRangeReadings().getNumReadings() == 2;
    }

    private boolean isLeftBumperPressed(Feature feature) {
        return feature.getRangeReading().getRange() == leftBumperRange;
    }

    private boolean isRightBumperPressed(Feature feature) {
        return feature.getRangeReading().getRange() == rightBumperRange;
    }

    @Override
    public boolean takeControl() {
        return collisionDetected;
    }

    @Override
    public void action() {
        pilot.travel(BACKDISTANCE);
        pilot.rotate(calculateRotationAngle());
        this.collisionDetected = false;
    }

    private double calculateRotationAngle() {
        switch (movementMode) {
            case TURN_RANDOM:
                return MathUtils.getRandomDouble(-90, 90);
            case TURN_LEFT:
                return MathUtils.getRandomDouble(10, 90);
            case TURN_RIGHT:
                return MathUtils.getRandomDouble(-90, -10);
            default:
                return 0;
        }
    }

    @Override
    public void suppress() {
        pilot.stop();
    }
}
