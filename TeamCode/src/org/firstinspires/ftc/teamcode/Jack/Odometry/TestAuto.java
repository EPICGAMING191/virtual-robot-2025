package org.firstinspires.ftc.teamcode.Jack.Odometry;

import com.pedropathing.Drivetrain;
import com.pedropathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Autonomous(group = "Pedro")
public class TestAuto extends LinearOpMode {
    //VARIABLES-------------------------------------------------------------------------------------
    public ElapsedTime pathTimer = new ElapsedTime();
    public PathState pathState = PathState.START;
    public boolean readyForNextState = false;
    public boolean pathFollowedInState = false;
    public List<Double> tValues = Arrays.asList(0.01, 0.3, 0.99);
    //FOLLOWERS/PATHING-----------------------------------------------------------------------------
    public Follower follower;
    //HARDWARE--------------------------------------------------------------------------------------
    //public PinpointV1 pinpoint = new PinpointV1();

    //STATES----------------------------------------------------------------------------------------
    public enum PathState {
        START,
        TO_SHOOT_1,
        TO_FIRST_ARTIFACTS,
        TO_SHOOT_2
    }

    public enum ActionStates {
        START
    }

    //POSES-----------------------------------------------------------------------------------------
    //TODO: Update with real coordinates and make sure this works
    public Pose startPose = new Pose(57,9.6, Math.toRadians(90));
    public Pose shootPose = new Pose(60.4, 17.4, Math.toRadians(110));
    //PATHS-----------------------------------------------------------------------------------------
    public Path outOfStart, toFirstArtifacts;
    //==============================================================================================
    public void buildPaths(){
        outOfStart = new Path(new BezierLine(startPose, shootPose));
        outOfStart.setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading());
    }




    //----------------------------------------------------------------------------------------------
    public void autonomousPathUpdate(){
        telemetry.addLine("Path Update");
        switch (pathState) {
            case START:
                setPathState(PathState.TO_SHOOT_1);
                break;
            case TO_SHOOT_1:
                if (!follower.isBusy()) {
                    followPath(outOfStart);
                }
                break;
        }
        //TODO: Add list for values of each path's t value
        if(isReadyForNextPath() && pathTimer.seconds() > 0.5){
            if(!isAtEndOfPathStates()) {
                follower.breakFollowing();
                setPathState(getNextPathState());
            }
        }
    }






    @Override
    public void runOpMode() {
        follower = Constants.createFollower(hardwareMap);

        follower.setPose(startPose);
        buildPaths();
        telemetry.addLine("Waiting for start");
        while (opModeInInit()) {
            telemetry.update();
        }
        waitForStart();
        //START-------------------------------------------------------------------------------------
        while (opModeIsActive()){
            logTelemetry();
            follower.update();
            autonomousPathUpdate();
        }
    }

    //FUNCTIONS-------------------------------------------------------------------------------------
    public boolean isAtEndOfPathStates(){
        return PathState.valueOf(pathState.name()).ordinal() + 1 == PathState.values().length;
    }
    public PathState getNextPathState(){
        return PathState.values()[PathState.valueOf(pathState.name()).ordinal() + 1];
    }
    public void followPath(Path path){
        follower.followPath(path);
        pathFollowedInState = true;
    }

    public boolean isReadyForNextPath(){
        return  follower.isBusy() && follower.getCurrentTValue() > tValues.get(pathState.ordinal()) && readyForNextState;
    }


    public void setPathState(PathState state){
        pathState = state;
        pathFollowedInState = false;
        readyForNextState = false;
        pathTimer.reset();
    }

    public void logTelemetry(){
        telemetry.addData("Path State: ", pathState.name());
        telemetry.addData("Position: ", follower.getPose().toString());
        telemetry.addData("Heading: ", follower.getPose().getHeading());
        telemetry.addData("Current T-value: ", follower.getCurrentTValue());
        telemetry.update();
    }
}
