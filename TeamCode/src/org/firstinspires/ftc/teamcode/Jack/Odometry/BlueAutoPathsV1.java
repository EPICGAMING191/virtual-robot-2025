package org.firstinspires.ftc.teamcode.Jack.Odometry;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;

public class BlueAutoPathsV1 {
    public Pose startPose = new Pose(57, 9.6, Math.toRadians(90));
    public double toShootPoseTValue = 1;
    public Pose shootPose = new Pose(60.4, 17.4, Math.toRadians(110));

    public Pose toFirstArtifactsFalsePoint = new Pose(6, 73, Math.toRadians(180));
    public Pose toFirstArtifactsPose = new Pose(36, 36, Math.toRadians(180));
    public PointRange artifacts1Range = new PointRange(toFirstArtifactsPose, 5, 5, PointRange.RangeOptions.BOTH);

    public Pose pickUpArtifacts = new Pose(18,36, Math.toRadians(180));
    public Pose pickup1FalsePoint = new Pose(-30, 36, Math.toRadians(180));
    public PointRange pickup1Range = new PointRange(pickUpArtifacts, 5, 5, PointRange.RangeOptions.BOTH);

    public Path outOfStart, toFirstArtifacts, pickup1;

    //==============================================================================================
    public void buildPaths() {
        outOfStart = new Path(new BezierLine(startPose, shootPose));
        outOfStart.setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading());
        toFirstArtifacts = new Path(new BezierLine(shootPose, toFirstArtifactsFalsePoint));
        toFirstArtifacts.setLinearHeadingInterpolation(shootPose.getHeading(), toFirstArtifactsFalsePoint.getHeading());
        pickup1 = new Path(new BezierLine(toFirstArtifactsPose, pickup1FalsePoint));
        pickup1.setConstantHeadingInterpolation(toFirstArtifactsPose.getHeading());
    }
}
