package org.smart.robot.constants;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.RegulatedMotor;

public class RobotConstants {
    private RobotConstants() {
    }

    public static final float ROBOT_WIDTH = 100;
    public static final float WHEEL_DIAMETER = 20;
    public static final boolean MOTORS_REVERSED = true;

    public static final RegulatedMotor leftMotor = Motor.B;
    public static final RegulatedMotor rightMotor = Motor.A;

    public static final SensorPort LEFT_BUMPER = SensorPort.S3;
    public static final SensorPort RIGHT_BUMPER = SensorPort.S2;

    public static final float LEFT_BAMPER_RANGE = ROBOT_WIDTH / 2;
    public static final float RIGHT_BAMPER_RANGE = -LEFT_BAMPER_RANGE;

    public static final float LEFT_BAMPER_ANGLE = 90;
    public static final float RIGHT_BAMPER_ANGLE = -90;
}
