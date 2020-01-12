package org.firstinspires.ftc.teamcode;

public class SCHSConstants {

    // power constants between 1 and -1
    static final double POWER_FULL = 1;
    static final double POWER_HALF = 0.5;
    static final double POWER_TURN_SPEED = 0.5;

    //Robot Constants
    static final double ROBOT_WIDTH = 14; //inches
    static final double ROBOT_MAX_SPEED = 4; //feet/sec
    static final int LIFT = 1;
    static final int ARM = 2;
    static final int DRIVE = 0;

    //Servo Constants
    static final boolean SERVO_OPEN = true;
    static final boolean SERVO_CLOSE = false;
    static final double INCREMENT = 0.01;
    static final int CYCLE_MS = 50;


    // robot parameters
    // abstract to a class (eg, Robot) where static parameters describe the robot
    // the wheel diameters
    private final double mWheelDiameterLeft = 0.090;
    private final double mWheelDiameterRight = 0.090;
    // half the distance between the wheels
    private final double distWheel = 0.305 / 2;

    // derived robot parameters
    // Distance per tick
    //   leaving the units vague at this point
    // Currently using direct drive with a CoreHex motor
    // The CoreHex motor has 4 ticks per revolution and is geared down by 72
    //   those attributes should be in the DcMotor class
    // The HD Hex Motor has 56 ticks per revolution
    //    the 20:1 is geared 20 to 1
    //    the 40:1 is geared 40 to 1
    // The HD Hex Motor is also used with the Ultraplanetary gearbox
    //    the 3:1 cartridge is actually 84:29 (2.9...)
    //    the 4:1 cartridge is actually 76:21 (3.6...)
    //    the 5:1 cartridge is actually 68:13 (5.2...)
    static final double HEX_HD_RATIO_3_1 = 84.0/29.0;
    static final double HEX_HD_RATIO_4_1 = 76.0/21.0;
    static final double HEX_HD_RATIO_5_1 = 68.0/13.0;
    static final double COUNTS_PER_REV = 56.0 * HEX_HD_RATIO_4_1 * HEX_HD_RATIO_5_1;

    // The DcMotor class can allow some help
    //   MotorConfigurationType .getMotorType()
    //     MotorConfigurationType#getUnspecifiedMotorType()
    //       do not know where the enum is
    //   java.lang.String .getDeviceName() (not the config name)
    //   HardwareDevice.Manufacturer .getManufacturer()
    //     https://ftctechnh.github.io/ftc_app/doc/javadoc/index.html?com/qualcomm/robotcore/hardware/HardwareMap.html
    //       possibly uninteresting
    //   DcMotorEx has .getVelocity(AngleUnit unit), so it presumably knows the ticks per revolution
    //     however, there is not a .getCurrentPostion(AngleUnit unit)
    private final double distpertickLeft = mWheelDiameterLeft * Math.PI / (4 * 72);
    private final double distpertickRight = mWheelDiameterRight * Math.PI / (4 * 72);

    //245 counts ~ 90 degree turn

    //static final double COUNTS_PER_INCH = (288)/(3.54331*(Math.PI)); //for core hex motor
    static final double COUNTS_PER_INCH = (COUNTS_PER_REV)/(3.54331*(Math.PI)*2);
    static final double ARM_FACTOR = 80;
    static final double LIFT_FACTOR = 139.77;
    static final double TURN_VALUE_90 = 532/(COUNTS_PER_INCH); //BEFORE: 254, left 495, right -569 turn right, avg532
    static final double TURN_VALUE_MID_BLOCK = 700/COUNTS_PER_INCH; //left -635, right 768 turn left, avg701.5
    static final double TURN_VALUE_LEFT_BLOCK = 805/COUNTS_PER_INCH; //left -708, right 901 turn left, avg804.5

    //Detection Constants
    static final int LEFT_POS = 1;
    static final int MID_POS = 2;
    static final int RIGHT_POS = 3;

    //go to foundation
    static final SCHSPathSeg[] goToFDPath = {
            new SCHSPathSeg( 6, 6, 0.9),  // Forward 3.5 in
            new SCHSPathSeg( -TURN_VALUE_90, TURN_VALUE_90, 0.9),  // Left 90
            new SCHSPathSeg( 24, 24, 0.9),  // Forward 2 ft
            new SCHSPathSeg( TURN_VALUE_90, -TURN_VALUE_90, 0.9),  // right 90
            new SCHSPathSeg( 17, 17, 0.9),  // Forward 15

            //new SCHSPathSeg(((31*(Math.PI))/2)+2, ((17*(Math.PI))/2)+4, 0.75, 0.4), //arcturn forward radius 2 quarter circle
            //new SCHSPathSeg( 0, 0, 0),  // stop
            new SCHSPathSeg((-((31*(Math.PI))/2)-8), -(((17*(Math.PI))/2)-4), 0.8, 0.6), //arcturn backward radius 2 quarter circle
            //new SCHSPathSeg( 0, 0, 0),  // stop
            //new SCHSPathSeg( 28.5, 28.5, POWER_FULL),  // forward 2 feet
    };

    static final SCHSPathSeg[] pickStoneArmPath = {
            new SCHSPathSeg( LIFT, 4, 0.5, "yes"),  //lift 4 in
            new SCHSPathSeg( ARM, 12.0, 0.5, "yes"), //extend 4 in, change to 9.5
            new SCHSPathSeg(LIFT, -5.5,0.5,"yes") //lift down 5
    };

    static final SCHSPathSeg[] retreatStoneArmPath = { //
            new SCHSPathSeg(LIFT, 5, 0.5,"yes"), //lift 5 up
            new SCHSPathSeg(ARM, -4, 0.5, "yes"), //extend 2 back
            //new SCHSPathSeg(LIFT, -4, 0.5, "yes") //lift down 4, lowest height
    };

    static final SCHSPathSeg[] stoneDownPath = {
            new SCHSPathSeg(LIFT, -2, 0.5, "yes") //lift down 4, change to 4.5, lowest height
    };

    static final SCHSPathSeg[] extendOutPath = {
            new SCHSPathSeg(ARM, 6, 1,"yes"), //extend out 6
    };

    static final SCHSPathSeg[] extendInPath = {
            new SCHSPathSeg(ARM, -6, 1,"yes"), //extend in 6
    };

    static final SCHSPathSeg[] testPath2 = {
            //new SCHSPathSeg( LIFT, -18, 0.5, 1),
            //new SCHSPathSeg( ARM, -1.25, 0.5, 1)
    };

    static final SCHSPathSeg[] testPathExtend = {
            new SCHSPathSeg( LIFT, 18, 0.5, 1),  //lift 2 in
            //new SCHSPathSeg( ARM, 4, 0.25, 1),  //extend 2 in
    };

    static final SCHSPathSeg[] testPathRun = {
            //new SCHSPathSeg(-18, -18, POWER_FULL)
            new SCHSPathSeg(-72, -72, 0.9)
            //new SCHSPathSeg(48,48,0.84, 0.9)
    };

    static final SCHSPathSeg[] startBotPath = {
            new SCHSPathSeg( 15, 15, 0.5),  // Forward 15 in
    };

    static final SCHSPathSeg[] goToLBPath = {
            new SCHSPathSeg( -TURN_VALUE_90, TURN_VALUE_90, POWER_FULL), //left 90
            new SCHSPathSeg( 8, 8, POWER_FULL), //forward 8
            new SCHSPathSeg( TURN_VALUE_90, -TURN_VALUE_90, POWER_FULL), //right 90
            new SCHSPathSeg( 9, 9, POWER_FULL), //forward 9
    };

    static final SCHSPathSeg[] retreatLBPath= {
            new SCHSPathSeg( TURN_VALUE_90, -TURN_VALUE_90, 0.75),  // right 90
            new SCHSPathSeg( 20, 20, 0.9), //forward 20
    };

    static final SCHSPathSeg[] goToMBPath = {
            new SCHSPathSeg( 9, 9, POWER_FULL), //forward 9
    };

    static final SCHSPathSeg[] retreatMBPath = {
            new SCHSPathSeg( TURN_VALUE_90, -TURN_VALUE_90, 0.75),  // right 90
            new SCHSPathSeg( 12, 12, 0.9), //forward 12
    };

    static final SCHSPathSeg[] goToRBPath = {
            new SCHSPathSeg( TURN_VALUE_90, -TURN_VALUE_90, 0.5), //right 90
            new SCHSPathSeg( 8.25, 8.25, 0.5), //forward 8, change to 10, change to 6.5
            new SCHSPathSeg( -TURN_VALUE_90, TURN_VALUE_90, 0.5), //left 90
            new SCHSPathSeg( 4.5, 4.5, 0.5), //forward 9, change to 7, change to 4.5
    };

    static final SCHSPathSeg[] retreatRBPath = {
            new SCHSPathSeg( TURN_VALUE_90, -TURN_VALUE_90, 0.5),  // right 90
            new SCHSPathSeg( 4, 4, 0.5), //forward 4
    };

    static final SCHSPathSeg[] deliverBlockPath = {
            //new SCHSPathSeg( 25, 25, POWER_FULL), //forward 74.75, commenting for testing
            new SCHSPathSeg( 48, 48, POWER_FULL),
            new SCHSPathSeg( 26.75,26.75, POWER_FULL),
            //new SCHSPathSeg( 11, 11, POWER_FULL), //test forward 13, change to 11
            //new SCHSPathSeg( -TURN_VALUE_90, TURN_VALUE_90, 0.5), //left 90
            //new SCHSPathSeg( 6, 6, 0.5), //forward 6
    };

    static final SCHSPathSeg[] backToBlocksPath = {
            //new SCHSPathSeg( -6, -6, 0.5), //backward 6
            //new SCHSPathSeg( TURN_VALUE_90, -TURN_VALUE_90, 0.5), //right 90
            new SCHSPathSeg( -48, -48, POWER_FULL),
            new SCHSPathSeg( -50.75, -50.75, POWER_FULL),
            //new SCHSPathSeg( -25, -25, POWER_FULL),
            //new SCHSPathSeg( -23.75, -23.75, POWER_FULL), //backward 98.75, commented for testing
            //new SCHSPathSeg( -37, -37, POWER_FULL), //backward
    };

    static final SCHSPathSeg[] back2LBPath = {
            new SCHSPathSeg( -12, -12, 0.5), //backward 12
            new SCHSPathSeg( -TURN_VALUE_LEFT_BLOCK, TURN_VALUE_LEFT_BLOCK, 0.5), //left 124
            //new SCHSPathSeg( -1.5*TURN_VALUE_90, 1.5*TURN_VALUE_90, POWER_FULL), //left 135 test for home
    };

    static final SCHSPathSeg[] retreat2LBPath = {
            new SCHSPathSeg( TURN_VALUE_LEFT_BLOCK, -TURN_VALUE_LEFT_BLOCK, POWER_FULL), //right 124
            //new SCHSPathSeg( 1.5*TURN_VALUE_90, -1.5*TURN_VALUE_90, POWER_FULL), //right 135 test for home
            new SCHSPathSeg( 12, 12, POWER_FULL), //forward 12
    };

    static final SCHSPathSeg[] back2MBPath = {
            new SCHSPathSeg( -12, -12, POWER_FULL), //backward 12
            new SCHSPathSeg( -TURN_VALUE_MID_BLOCK, TURN_VALUE_MID_BLOCK, POWER_FULL), //left 90
    };

    static final SCHSPathSeg[] retreat2MBPath = {
            new SCHSPathSeg( TURN_VALUE_MID_BLOCK, -TURN_VALUE_MID_BLOCK, POWER_FULL), //right 90
            new SCHSPathSeg( 12, 12, POWER_FULL), //forward 12
    };

    static final SCHSPathSeg[] back2RBPath = {
            new SCHSPathSeg( -4, -4, 0.5), //backward 4
            new SCHSPathSeg( -TURN_VALUE_90, TURN_VALUE_90, 0.5), //left 90
    };

    static final SCHSPathSeg[] retreat2RBPath = {
            new SCHSPathSeg( TURN_VALUE_90, -TURN_VALUE_90, 0.5), //right 90
            new SCHSPathSeg( 4, 4, 0.5), //forward 4
    };

    static final SCHSPathSeg[] deliver2BlockPath = {
            new SCHSPathSeg( 98.75, 98.75, 0.5), //forward 98.75, commented for testing
            //new SCHSPathSeg( 37, 37, POWER_FULL), //test forward 36
            new SCHSPathSeg( -TURN_VALUE_90, TURN_VALUE_90, 0.5), //left 90
            new SCHSPathSeg( 6, 6, 0.5), //forward 6
    };

    static final SCHSPathSeg[] backFromFDPath = {
            new SCHSPathSeg( -6, -6, 0.5), //backward 6
    };

    //Tensor Flow Object detection
    static final String VUFORIA_KEY = "AUnX7nP/////AAABmZjfOTd2skx4p/r+LBA29VQAFar5mbPnEfGtcl78mMIqK+EtsUOR33zwyiDCmj1oYMUx0P4eWZGi6EMhZgTM66/5llx5azKwGGxGmTJUGotbAekyZgxYR7SWDme6xMYGR68jZcR9rkvJxfB1ZKFytPXWeRpwzSAQJ0VACF/hdguUyfA6SSkF2dnc/iH76TkSV3hA4zz0v3wjHfQmmNBvrtgPklvfOTX2f+G5tBfBq75PEx52LaX+tOPTtBajR9MFwVT26kcqFz2GJCEBgjO3PX1St0xNJBqbbudKvZ+B/6xWuVhwHVqwOgy/RsuHLBFskh4n9Ec1xnuB9uCnQXrrliEtcR1TbnmIEYTX6FZtxF5H";
    static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    static final String LABEL_FIRST_ELEMENT = "Stone";
    static final String LABEL_SECOND_ELEMENT = "Skystone";
    static final long SCAN_BALLS_TIME = 3000;

    static final double POS_3_LEFT_MIN = 318;
    static final double POS_3_LEFT_MAX = 418;
    static final double POS_3_RIGHT_MIN = 570;
    static final double POS_3_RIGHT_MAX = 670;

    static final double POS_2_LEFT_MIN = 135;
    static final double POS_2_LEFT_MAX = 235;
    static final double POS_2_RIGHT_MIN = 380;
    static final double POS_2_RIGHT_MAX = 480;

    static final double POS_1_LEFT_MIN = -50;
    static final double POS_1_LEFT_MAX = 50;
    static final double POS_1_RIGHT_MIN = 125;
    static final double POS_1_RIGHT_MAX = 225;

    //core hex motor constants
    static final double CHMOTOR_COUNTS_PER_REVOLUTION = 288;
    static final double REAR_WHEEL_BASE_= 12; //inches
    static final double TRACTION_WHEEL_DIAMETER = 90 * 0.0393701; //90mm converted to inches

    //formula for inches to counts (encoder value): 288 counts/revolution
    //1 revolution = 90pi mm = 3.54331pi inches
    //total counts = 288*rev
    //x inches / 3.54331pi = # rev
    //encoder value = (288*x)/(3.54331pi) = 310.466 at x = 12 inches

    //formula for degrees (encoder value): (a/360)*(3.54331pi) = y inches
    //encoder value = (288*y)/(12pi) = 310.466 at y inches for a degrees
    //at a = 90 degrees, encoder value = 243.839

}
