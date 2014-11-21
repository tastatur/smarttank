package org.smart.robot.boot;

import lejos.geom.Line;
import lejos.nxt.TouchSensor;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.objectdetection.FeatureDetector;
import lejos.robotics.objectdetection.FusorDetector;
import lejos.robotics.objectdetection.TouchFeatureDetector;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import org.smart.robot.behaviors.BumpersListener;
import org.smart.robot.behaviors.DumbForward;
import org.smart.robot.constants.RobotConstants;

import java.util.ArrayList;
import java.util.List;

public class BehaviorRobot implements Robot {
    private DifferentialPilot mainPilot;
    private PoseProvider poseProvider;
    private TouchSensor leftBumper, rightBumper;
    private Path myPath;
    private List<Line> obstacles;
    private Behavior[] behaviors;

    @Override
    public boolean setup() {
        try {
            setupSensorsAndMotors();
            setupBehaviours();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void setupBehaviours() {
        Behavior dumbForward = new DumbForward(mainPilot, myPath, poseProvider);
        BumpersListener bumper = getBumpersListener();
        behaviors = new Behavior[2];
        behaviors[0] = dumbForward;
        behaviors[1] = bumper;
    }

    private BumpersListener getBumpersListener() {
        BumpersListener bumper = new BumpersListener(mainPilot, obstacles, poseProvider);
        FeatureDetector leftBumperDetector = new TouchFeatureDetector(leftBumper, RobotConstants.LEFT_BAMPER_RANGE, 0);
        FeatureDetector rightBumperDetector = new TouchFeatureDetector(rightBumper, RobotConstants.RIGHT_BAMPER_RANGE, 0);

        FusorDetector bumpDetector = new FusorDetector();
        bumpDetector.addDetector(leftBumperDetector);
        bumpDetector.addDetector(rightBumperDetector);
        bumpDetector.addListener(bumper);
        bumpDetector.enableDetection(true);
        return bumper;
    }

    private void setupSensorsAndMotors() {
        mainPilot = new DifferentialPilot(RobotConstants.WHEEL_DIAMETER,
                RobotConstants.ROBOT_WIDTH, RobotConstants.leftMotor, RobotConstants.rightMotor,
                RobotConstants.MOTORS_REVERSED);
        myPath = new Path();
        obstacles = new ArrayList<>();
        poseProvider = new OdometryPoseProvider(mainPilot);
        leftBumper = new TouchSensor(RobotConstants.LEFT_BUMPER);
        rightBumper = new TouchSensor(RobotConstants.RIGHT_BUMPER);
    }

    @Override
    public void start() {
        Arbitrator arbitrator = new Arbitrator(behaviors, false);
        arbitrator.start();
    }
}
