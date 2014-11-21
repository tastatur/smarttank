package org.smart.robot.behaviors;

import lejos.geom.Line;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.objectdetection.Feature;
import lejos.robotics.objectdetection.FeatureDetector;
import lejos.robotics.objectdetection.FeatureListener;
import lejos.robotics.subsumption.Behavior;
import org.smart.robot.constants.RobotConstants;
import org.smart.robot.utils.*;

import java.util.List;

/**
 * Reaction on the pressed touch sensors (bumpers)
 */
public class BumpersListener implements FeatureListener, Behavior {
    private static final double BACKDISTANCE = -50;

    private DifferentialPilot pilot;
    private List<Line> obstacles;
    private boolean collisionDetected;
    private BackwardMovement movementMode;
    private PoseProvider pose;

    public BumpersListener(DifferentialPilot pilot, List<Line> obstacles, PoseProvider pose) {
        this.pilot = pilot;
        this.obstacles = obstacles;
        this.pose = pose;
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

        addObstacleToMap();
    }

    private void addObstacleToMap() {
        float trackWidth = RobotConstants.ROBOT_WIDTH / 2;
        float leftX = pose.getPose().getX() - trackWidth;
        float rightX = pose.getPose().getX() + trackWidth;
        float y = pose.getPose().getY();

        Line obstacle = new Line(leftX, y, rightX, y);
        obstacles.add(obstacle);
    }

    private boolean isBothBumpersPressed(Feature feature) {
        return feature.getRangeReadings().getNumReadings() == 2;
    }

    private boolean isLeftBumperPressed(Feature feature) {
        return feature.getRangeReading().getRange() == RobotConstants.LEFT_BAMPER_RANGE;
    }

    private boolean isRightBumperPressed(Feature feature) {
        return feature.getRangeReading().getRange() == RobotConstants.RIGHT_BAMPER_RANGE;
    }

    @Override
    public boolean takeControl() {
        return collisionDetected;
    }

    @Override
    public void action() {
        pilot.stop();
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
