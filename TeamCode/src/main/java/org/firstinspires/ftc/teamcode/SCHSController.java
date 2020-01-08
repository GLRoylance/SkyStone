package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.SCHSConstants.*;

import static android.os.SystemClock.sleep;
import static org.firstinspires.ftc.teamcode.SCHSConstants.*;

@Autonomous(name="SCHSController", group="SCHS")
//@Disabled
public class SCHSController extends OpMode {

    private SCHSDrive rileyChassis = null;
    private boolean isInitialized = false;
    private SCHSDetection rileyEnv = null;
    private boolean isRoundTwo = false;
    private SCHSArm rileyArm = null;
    private int skyPos = 0;

    private enum State {
        STATE_FD_INITIAL,
        STATE_FD_GO_TO_FOUNDATION,
        STATE_FD_POSITION_FOUNDATION,
        STATE_FD_GO_TO_SKYBRIDGE,

        STATE_STONES_INITIAL,
        STATE_STONES_FIRST_MOVE,
        STATE_STONES_GO_TO_SKYSTONE,
        STATE_STONES_PICK_STONE,
        STATE_STONES_RETREAT,
        STATE_STONES_ARM_DOWN,
        STATE_STONES_DELIVER,
        STATE_STONES_RETURN_TO_STONES,
        STATE_STONES_DROP_STONE,
        STATE_STONES_GO_TO_SECOND,
        STATE_STONES_RETREAT_SECOND,
        STATE_STONES_DELIVER_SECOND,
        STATE_STONES_BACK_FD,

        STATE_STONES_CLOSE_STONE,
        STATE_STONES_OPEN_STONE,
        STATE_STONES_RETRIEVE_STONE,
        STATE_STONES_SECOND_DROP,
        STATE_STONES_EXTEND_OUT,
        STATE_STONES_EXTEND_IN,

        STATE_FD_TEST,
        STATE_EXTEND_TEST,
        STATE_TEST_INITIAL,
        STATE_TEST_1_5,
        STATE_TEST_2_INITIAL,

        STATE_STOP,
    }

    private State currState; //current state machine
    private SCHSPathSeg[] currPath; // array holding current path
    private int currSeg; //index of leg of current path

    // time into round and time into current state
    private ElapsedTime runtime = new ElapsedTime();
    private ElapsedTime currStateTime = new ElapsedTime();

    @Override
    public void init() {
        rileyChassis = new SCHSDrive();
        rileyChassis.initialize(hardwareMap);
        rileyEnv = new SCHSDetection();
        rileyEnv.iniitialize(hardwareMap);
        rileyArm = new SCHSArm();
        rileyArm.initialize(hardwareMap);
        telemetry.addLine("Done Initializing");
    }

    //@Override
    public void init_loop() {
        //keep resetting encoders and show current values
        rileyChassis.resetEncoders();
        rileyArm.resetArmEncoders();
        telemetry.addData("ENC", String.format("L:R %s:%s", rileyChassis.getLeftPosition(), rileyChassis.getRightPosition()));
    }

    @Override
    public void start() {
        //Set up robot devices, initial state, and game timer
        rileyChassis.setDrivePower(0,0);
        rileyArm.setArmPower(0, LIFT);
        rileyArm.setArmPower(0, ARM);
        //rileyChassis.setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);
        runtime.reset();
        //newState(State.STATE_STONES_INITIAL);
        newState(State.STATE_TEST_INITIAL);
    }

    @Override
    public void loop() {
        // Send the current state info (state and time) back to first line of driver station telemetry.
        //telemetry.addData("0", String.format("%4.1f ", currStateTime.time()) + currState.toString());

        switch (currState) {
            case STATE_FD_INITIAL:
                if (rileyChassis.encodersAtZero()){
                    telemetry.addLine("SCHS: startseg(): move arm extend" + rileyArm.getExtendPos());
                    Log.d("SCHS: startseg():", "move arm extend" + rileyArm.getExtendPos());
                    //startPath(testPath);
                    telemetry.addLine("SCHS: startseg(): move arm extend" + rileyArm.getExtendPos());
                    Log.d("SCHS: startseg():", "move arm extend" + rileyArm.getExtendPos());
                    newState((State.STATE_FD_TEST));
                } else {
                    telemetry.addLine("SCHS: STATE_FD_INITIAL else");
                    Log.d("SCHS:", "STATE_FD_INITIAL else");
                }
                break;

            case STATE_FD_TEST:
                if (pathComplete(ARM, false))
                {
                    //startPath(testPath2);
                    telemetry.addLine("SCHS: startseg(): move arm extend" + rileyArm.getExtendPos());
                    Log.d("SCHS: startseg():", "move arm extend" + rileyArm.getExtendPos());
                    newState(State.STATE_STOP);      // Next State:
                }
                else
                {
                    telemetry.addLine("SCHS: STATE_FD_TEST else");
                    Log.d("SCHS:", "STATE_FD_TEST else");
                }
                break;

            case STATE_TEST_INITIAL:
                if(rileyChassis.encodersAtZero()) {
                    //rileyArm.closeServo();
                    rileyArm.closeServo(rileyArm.rightHook);
                    rileyArm.openServo(rileyArm.leftHook);
                    sleep(1000);
                    //rileyArm.moveServoNew(0.8, SERVO_OPEN);
                    //rileyArm.moveServo(SERVO_OPEN);
                    //rileyArm.moveServoNew(0.5, SERVO_CLOSE);
                    //rileyArm.moveServoNew(0, SERVO_OPEN);
                    newState(State.STATE_TEST_1_5);
                }
                break;

            case STATE_TEST_1_5:
                if (rileyChassis.encodersAtZero()) {
                    startPath(testPathRun);
                    newState(State.STATE_TEST_2_INITIAL);
                }


            case STATE_TEST_2_INITIAL:
                if (pathComplete(DRIVE, false))
                {
                    rileyArm.openServo(rileyArm.rightHook);
                    rileyArm.closeServo(rileyArm.leftHook);
                    sleep(1000);
                    //rileyArm.moveServoNew(0.002, SERVO_CLOSE);
                    //sleep(1000);
                    newState(State.STATE_STOP);

                }
                else
                {
                }
                break;

            case STATE_FD_GO_TO_FOUNDATION:
                if (pathComplete(ARM, false))
                {
                    //rileyChassis.setDrivePower(0, 0);
                    rileyArm.setArmPower(0, LIFT);
                    rileyArm.setArmPower(0, ARM);
                    newState(State.STATE_STOP);      // Next State:
                }
                else
                {
                    // Display Diagnostic data for this state.
                    /*telemetry.addData("1", String.format("%d of %d. L %5d:%5s - R %5d:%5d ",
                            currSeg, currPath.length,
                            rileyChassis.getLeftEncoderTarget(), rileyChassis.getLeftPosition(),
                            rileyChassis.getRightEncoderTarget(), rileyChassis.getRightPosition()));*/
                }
                break;

            case STATE_FD_POSITION_FOUNDATION:
                break;

            case STATE_FD_GO_TO_SKYBRIDGE:
                break;

            case STATE_STONES_INITIAL:
                Log.d("SCHS", "inside STATES_STONES_INITIAL");
                telemetry.addLine("inside STATES_STONES_INITIAL");
                if (rileyChassis.encodersAtZero()){
                    Log.d("SCHS", "inside STATES_STONES_INITIAL if case");
                    telemetry.addLine("STATES_STONES_INITIAL");
                    //startPath(startBotPath);
                    skyPos = rileyEnv.detectSkyPos(); //scan blocks
                    telemetry.addLine("skyPos:" + skyPos);
                    Log.d("SCHS: DETECT_SKYSTONE", "skyPos:" + skyPos);
                    //newState(State.STATE_STONES_DETECT_SKYSTONE);
                    newState(State.STATE_STONES_FIRST_MOVE);
                } else {
                    Log.d("SCHS", "inside STATES_STONES_INITIAL else case");
                    telemetry.addLine("inside initial else");
                }
                break;

                //case STATE_STONES_DETECT_SKYSTONE
                case STATE_STONES_FIRST_MOVE:
                if (rileyChassis.encodersAtZero()){
                    telemetry.addLine("STATES_STONES_FIRST");
                    Log.d("SCHS: DETECT_SKYSTONE", "inside STATES_STONES_FIRST");
                    startPath(startBotPath);
                    newState((State.STATE_STONES_GO_TO_SKYSTONE));
                } else {
                }
                break;

            case STATE_STONES_GO_TO_SKYSTONE:
                if (pathComplete(DRIVE, false)){
                    Log.d("SCHS", "inside STATES_STONES_GO_TO_SKYSTONE");
                    telemetry.addLine("STATES_STONES_GO_TO_SKYSTONE");
                    if (skyPos == LEFT_POS) {
                        Log.d("SCHS: GO_TO_SKYSTONE", "left pos");
                        telemetry.addLine("STATES_STONES_GO_TO_SKYSTONE, left pos");
                        startPath(goToLBPath);
                    } else if (skyPos == MID_POS) {
                        telemetry.addLine("STATES_STONES_GO_TO_SKYSTONE, mid pos");
                        Log.d("SCHS: GO_TO_SKYSTONE", "mid pos");
                        startPath(goToMBPath);
                    } else if (skyPos == RIGHT_POS) {
                        Log.d("SCHS: GO_TO_SKYSTONE", "right pos");
                        telemetry.addLine("STATES_STONES_GO_TO_SKYSTONE, right pos");
                        startPath(goToRBPath);
                    } else {
                        Log.d("SCHS", "inside STATES_STONES_GO_TO_SKYSTONE else");
                        telemetry.addData("AAAAAAAAHHHH! WRONG SKY POS! skypos:", skyPos);
                    }
                    newState((State.STATE_STONES_PICK_STONE));
                    //newState(State.STATE_STONES_RETREAT);
                } else {
                }
                break;

            case STATE_STONES_PICK_STONE:
                if (pathComplete(DRIVE, false)){
                    Log.d("SCHS", "inside STATES_STONES_PICK_STONE");
                    telemetry.addLine("inside STATES_STONES_PICK_STONE");
                    startPath(pickStoneArmPath);
                    newState(State.STATE_STONES_CLOSE_STONE);
                    //pick up blocks
                } else {
                }
                break;

            case STATE_STONES_CLOSE_STONE:
                if (pathComplete(LIFT, true)){
                    Log.d("SCHS", "inside STATES_STONES_CLOSE_STONE");
                    //rileyArm.openServo(rileyArm.grabServo);
                    //sleep(2000);
                    newState(State.STATE_STONES_RETRIEVE_STONE);
                } else {
                }
                break;

            case STATE_STONES_RETRIEVE_STONE:
                startPath(retreatStoneArmPath);
                Log.d("SCHS:", "inside STATES_STONES_RETRIEVE_STONE");
                newState(State.STATE_STONES_ARM_DOWN);
                /*if (!isRoundTwo){
                    Log.d("SCHS:", "STATES_STONES_RETRIEVE_STONE, round one");
                    telemetry.addLine("STATES_STONES_RETRIEVE_STONE, round one");
                    newState((State.STATE_STONES_RETREAT));
                } else {
                    Log.d("SCHS:", "STATES_STONES_RETRIEVE_STONE, round 2");
                    telemetry.addLine("STATES_STONES_RETRIEVE_STONE, round 2");
                    newState(State.STATE_STONES_RETREAT_SECOND);
                    //newState(State.STATE_STOP);
                }*/
                break;

            case STATE_STONES_ARM_DOWN:
                if (pathComplete(LIFT, true)) {
                    startPath(stoneDownPath);
                    if (!isRoundTwo){
                        Log.d("SCHS:", "STATES_STONES_RETRIEVE_STONE, round one");
                        telemetry.addLine("STATES_STONES_RETRIEVE_STONE, round one");
                        newState((State.STATE_STONES_RETREAT));
                    } else {
                        Log.d("SCHS:", "STATES_STONES_RETRIEVE_STONE, round 2");
                        telemetry.addLine("STATES_STONES_RETRIEVE_STONE, round 2");
                        newState(State.STATE_STONES_RETREAT_SECOND);
                    }
                } else {

                }
                break;

            case STATE_STONES_RETREAT:
                if (pathComplete(LIFT, false)){
                //if (pathComplete(DRIVE, false)) {
                    Log.d("SCHS","inside STATES_STONES_RETREAT");
                    telemetry.addLine("STATES_STONES_RETREAT");
                    if (skyPos == LEFT_POS) {
                        Log.d("SCHS: STONES_RETREAT", "left pos");
                        telemetry.addLine("STATES_STONES_RETREAT, left pos");
                        startPath(retreatLBPath);
                    } else if (skyPos == MID_POS) {
                        Log.d("SCHS: STONES_RETREAT", "mid pos");
                        telemetry.addLine("STATES_STONES_RETREAT, mid pos");
                        startPath(retreatMBPath);
                    } else if (skyPos == RIGHT_POS) {
                        Log.d("SCHS: STONES_RETREAT", "right pos");
                        telemetry.addLine("STATES_STONES_RETREAT, right pos");
                        startPath(retreatRBPath);
                    } else {
                        Log.d("SCHS", "inside STATES_STONES_RETREAT else case");
                        telemetry.addData("STATE_STONES_ RETREAT: AAAAAAAAHHHH! WRONG SKY POS! skypos:", skyPos);
                    }
                    newState((State.STATE_STONES_DELIVER));
                } else {
                }
                break;

            case STATE_STONES_DELIVER:
                if (pathComplete(DRIVE, false)){
                    telemetry.addLine("STATES_STONES_DELIVER");
                    startPath(deliverBlockPath);
                    newState((State.STATE_STONES_DROP_STONE));
                } else {
                }
                break;

            case STATE_STONES_DROP_STONE:
                if (pathComplete(DRIVE, false)){
                    Log.d("SCHS","inside STATES_STONES_DROP_STONE");
                    telemetry.addLine("STATES_STONES_DROP_STONE");
                    //drop blocks
                    if (!isRoundTwo){
                        rileyArm.openServo(rileyArm.grabServo);
                        sleep(2000);
                        Log.d("SCHS","inside STATES_STONES_DROP_STONE, round 1");
                        telemetry.addLine("STATES_STONES_DROP_STONE, round 1");
                        newState((State.STATE_STONES_RETURN_TO_STONES));
                    } else {
                        Log.d("SCHS","inside STATES_STONES_DROP_STONE, round 2");
                        telemetry.addLine("STATES_STONES_DROP_STONE, round 2");
                        startPath(extendOutPath);
                        newState(State.STATE_STONES_SECOND_DROP);
                    }
                } else {
                }
                break;

            case STATE_STONES_SECOND_DROP:
                if (pathComplete(ARM, false)){
                    rileyArm.openServo(rileyArm.grabServo);
                    sleep(2000);
                    newState(State.STATE_STONES_EXTEND_IN);
                } else {
                }
                break;

            case STATE_STONES_EXTEND_IN:
                startPath(extendInPath);
                newState(State.STATE_STONES_BACK_FD);
                break;

            case STATE_STONES_RETURN_TO_STONES:
                telemetry.addLine("STATES_STONES_RETURN_TO_STONES");
                isRoundTwo = true;
                startPath(backToBlocksPath);
                newState((State.STATE_STONES_GO_TO_SECOND));
                break;

            case STATE_STONES_GO_TO_SECOND:
                if (pathComplete(DRIVE, false)){
                    if (skyPos == LEFT_POS) {
                        Log.d("SCHS: GO_TO_2nd", "left pos");
                        telemetry.addLine("STATES_STONES_GO_TO_SECOND, left pos");
                        startPath(back2LBPath);
                    } else if (skyPos == MID_POS) {
                        Log.d("SCHS: GO_TO_2nd", "mid pos");
                        telemetry.addLine("STATES_STONES_GO_TO_SECOND, mid pos");
                        startPath(back2MBPath);
                    } else if (skyPos == RIGHT_POS) {
                        Log.d("SCHS: GO_TO_2nd", "right pos");
                        telemetry.addLine("STATES_STONES_GO_TO_SECOND, right pos");
                        startPath(back2RBPath);
                    } else {
                        Log.d("SCHS", "inside STATES_STONES_INITIAL");
                        telemetry.addData("STATE_STONES_ GO_TO_SECOND: AAAAAAAAHHHH! WRONG SKY POS! skypos:", skyPos);
                    }
                    newState((State.STATE_STONES_PICK_STONE));
                } else {
                }
                break;

            case STATE_STONES_RETREAT_SECOND:
                if (pathComplete(LIFT, false)){
                    telemetry.addLine("STATES_STONES_RETREAT_SECOND");
                    if (skyPos == LEFT_POS) {
                        Log.d("SCHS: RETREAT_SECOND", "left pos");
                        telemetry.addLine("STATES_STONES_RETREAT_SECOND, left pos");
                        startPath(retreat2LBPath);
                    } else if (skyPos == MID_POS) {
                        Log.d("SCHS: RETREAT_SECOND", "mid pos");
                        telemetry.addLine("STATES_STONES_RETREAT_SECOND, mid pos");
                        startPath(retreat2MBPath);
                    } else if (skyPos == RIGHT_POS) {
                        Log.d("SCHS: RETREAT_SECOND", "right pos");
                        telemetry.addLine("STATES_STONES_RETREAT_SECOND, right pos");
                        startPath(retreat2RBPath);
                    } else {
                        telemetry.addData("STATE_STONES_ RETREAT_SECOND: AAAAAAAAHHHH! WRONG SKY POS! skypos:", skyPos);
                    }
                    newState((State.STATE_STONES_DELIVER_SECOND));
                } else {
                }
                break;

            case STATE_STONES_DELIVER_SECOND:
                if (pathComplete(DRIVE, false)){
                    telemetry.addLine("STATES_STONES_DELIVER_SECOND");
                    startPath(deliver2BlockPath);
                    newState((State.STATE_STONES_DROP_STONE));
                } else {
                }
                break;

            case STATE_STONES_BACK_FD:
                if (pathComplete(ARM, false)){
                    Log.d("SCHS", "inside STATE_STONES_BACK_FD");
                    telemetry.addLine("STATES_STONES_BACK_FD");
                    startPath(backFromFDPath);
                    newState((State.STATE_STOP));
                } else {
                }
                break;

            case STATE_STOP:
                Log.d("SCHS", "inside STATES_STOP");
                telemetry.addLine("STATES_STOP");
                isRoundTwo = false;
                break;
        }
    }

    @Override
    public void stop(){
        rileyChassis.useConstantSpeed();
        rileyArm.useConstantSpeed(LIFT);
        rileyArm.useConstantSpeed(ARM);
        rileyChassis.setDrivePower(0,0);
        rileyArm.setArmPower(0, LIFT);
        rileyArm.setArmPower(0, ARM);
    }

    public void newState(State newState) {
        //reset state time, change to next state
        currStateTime.reset();
        currState = newState;
    }

    public void startPath(SCHSPathSeg[] path){
        currPath = path;    // Initialize path array
        currSeg = 0;
        rileyChassis.synchEncoders(); // Lock in the current position
        rileyArm.synchArmEncoder(LIFT);
        rileyArm.synchArmEncoder(ARM);
        //rileyArm.setArmMode(DcMotor.RunMode.RUN_TO_POSITION, LIFT);
        //rileyArm.setArmMode(DcMotor.RunMode.RUN_TO_POSITION, ARM);
        startSeg();             // Execute the current (first) Leg
        //rileyChassis.setDriveMode(DcMotor.RunMode.RUN_TO_POSITION); // Enable RunToPosition mode
    }

    public void startSeg() {
        int Left = 0;
        int Right = 0;
        int lift = 0;
        int extend = 0;

        if (currPath != null)
        {
            if(currPath[currSeg].isTwoSpeed == false && currPath[currSeg].armPart == 0){ //moving straight and turn in place
                // Load up the next motion based on the current segemnt.
                Left  = (int)(currPath[currSeg].leftDist * COUNTS_PER_INCH);
                Right = (int)(currPath[currSeg].rightDist * COUNTS_PER_INCH);
                rileyChassis.addEncoderTarget(Left, Right);
                rileyChassis.setDrivePower(currPath[currSeg].moveSpeed, currPath[currSeg].moveSpeed);
                rileyChassis.setDriveMode(DcMotor.RunMode.RUN_TO_POSITION); // Enable RunToPosition mode
                telemetry.addLine("SCHS: startseg(): move straight/turn");
                Log.d("SCHS: startseg():", "move straight/turn");
            } else if (currPath[currSeg].armPart == LIFT){ //move lift
                lift = (int)(currPath[currSeg].liftDist * LIFT_FACTOR);
                rileyArm.addEncoderTarget(lift, LIFT);
                rileyArm.setArmPower(currPath[currSeg].liftSpeed, LIFT);
                rileyArm.setArmMode(DcMotor.RunMode.RUN_TO_POSITION, LIFT);
                telemetry.addLine("SCHS: startseg(): move lift");
                Log.d("SCHS: startseg():", "move lift");
            } else if (currPath[currSeg].armPart == ARM) { //extend arm
                telemetry.addLine("SCHS: startseg(): move arm extend before");
                Log.d("SCHS: startseg():", "move arm extend before");
                extend = (int)(currPath[currSeg].extendDist * ARM_FACTOR);
                Log.d("SCHS: startseg():", "extend length = " + extend);
                rileyArm.addEncoderTarget(extend, ARM);
                rileyArm.setArmPower(currPath[currSeg].extendSpeed, ARM);
                rileyArm.setArmMode(DcMotor.RunMode.RUN_TO_POSITION, ARM);
                telemetry.addLine("SCHS: startseg(): move arm extend after" + rileyArm.getExtendPos());
                Log.d("SCHS: startseg():", "move arm extend after" + rileyArm.getExtendPos());
            } else {//arc turn
                Left  = (int)(currPath[currSeg].leftDist * COUNTS_PER_INCH);
                Right = (int)(currPath[currSeg].rightDist * COUNTS_PER_INCH);
                rileyChassis.addEncoderTarget(Left, Right);
                rileyChassis.setDrivePower(currPath[currSeg].leftSpeed, currPath[currSeg].rightSpeed);
                rileyChassis.setDriveMode(DcMotor.RunMode.RUN_TO_POSITION); // Enable RunToPosition mode
                telemetry.addLine("SCHS: startseg(): arc turn");
                Log.d("SCHS: startseg():", "arc turn");
            }
        }

            currSeg++;  // Move index to next segment of path
        }

     //checks if the current path is complete
     //As each segment completes, the next segment is started unless there are no more.
     //Returns true if the last leg has completed and the robot is stopped.

    private boolean pathComplete(int roboPart, boolean isBothArmLift) {
        // Wait for this Segement to end and then see what's next.
        if (moveComplete(roboPart, isBothArmLift)) {
            Log.d("SCHS: moveComplete", "complete for arm true");

            // Start next Segement if there is one.
            if (currSeg < currPath.length)
            {
                telemetry.addLine("SCHS: pathComplete(): move arm extend before startseg" + rileyArm.getExtendPos());
                Log.d("SCHS: pathComplete(): ", "move arm extend before startseg" + rileyArm.getExtendPos());
                startSeg();
                telemetry.addLine("SCHS: pathComplete(): move arm extend after startseg" + rileyArm.getExtendPos());
                Log.d("SCHS: pathComplete(): ", "move arm extend after startseg" + rileyArm.getExtendPos());

            }
            else  // Otherwise, stop and return done
            {
                currPath= null;
                currSeg= 0;
                rileyChassis.setDrivePower(0, 0);
                rileyChassis.useConstantSpeed();

                rileyArm.setArmPower(0, LIFT);
                rileyArm.setArmPower(0, ARM);
                rileyArm.useConstantSpeed(LIFT);
                rileyArm.useConstantSpeed(ARM);

                return true;
            }
        }
        Log.d("SCHS: moveComplete", "complete for arm false");
        telemetry.addLine("SCHS: startseg(): move arm extend false" + rileyArm.getExtendPos());
        Log.d("SCHS: startseg():", "move arm extend false" + rileyArm.getExtendPos());
        return false;
    }

    // Return true if motors have both reached the desired encoder target
    public boolean moveComplete(int roboPart, boolean isBothArmLift) {
        if (isBothArmLift) {
            if (roboPart == LIFT || roboPart == ARM) {
                Log.d("SCHS:", "inside moveComplete() lift/arm");
                Log.d("SCHS:", "moveComplete() arm extend pos=" + rileyArm.getExtendPos());
                Log.d("SCHS:", "moveComplete() arm encoder target=" + rileyArm.getArmEncoderTarget());

                return ((Math.abs(rileyArm.getLiftPos() - rileyArm.getLiftEncoderTarget()) < 10) &&
                        (Math.abs(rileyArm.getExtendPos() - rileyArm.getArmEncoderTarget()) < 10));
            }
        } else {
            if (roboPart == DRIVE){
                Log.d("SCHS: moveComplete", "inside moveComplete() for DRIVE");
                return ((Math.abs(rileyChassis.getLeftPosition() - rileyChassis.getLeftEncoderTarget()) < 10) &&
                        (Math.abs(rileyChassis.getRightPosition() - rileyChassis.getRightEncoderTarget()) < 10));
            } else if (roboPart == LIFT) {
                Log.d("SCHS:", "inside moveComplete() for LIFT");
                return (Math.abs(rileyArm.getLiftPos() - rileyArm.getLiftEncoderTarget()) < 10);
            } else if (roboPart == ARM) {
                Log.d("SCHS:", "inside moveComplete() for ARM");
                Log.d("SCHS:", "rileyArm getExtendPos = " +rileyArm.getExtendPos());
                Log.d("SCHS:", "rileyArm getArmEncoderTarget = " +rileyArm.getArmEncoderTarget());
                return (Math.abs(rileyArm.getExtendPos() - rileyArm.getArmEncoderTarget()) < 10);
            }
        }
        Log.d("SCHS:", "moveComplete() no case entered");
        return false;
    }
}

