package org.firstinspires.ftc.teamcode.Jack.Drive;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class AllInOneTuning extends OpMode {
    public GamepadV1 gamepad = new GamepadV1();
    public MecanumDriveOnly mecDrive = new MecanumDriveOnly();


    public enum modeSelected {
        SELECTED,
        NOT_SELECTED
    }

    public enum modes {
        DRIVE,
        SHOOT,
        INTAKE,
        CAMERA
    }

    public modeSelected isModeSelected = modeSelected.NOT_SELECTED;
    public modes mode = modes.DRIVE;

    public int selectedMode = 0;

    @Override
    public void init() {
        gamepad.init(gamepad1, 0.3);
        mecDrive.init(hardwareMap, gamepad);

    }

    @Override
    public void loop() {
        if(isModeSelected == modeSelected.NOT_SELECTED) {
            menuSelectUpdate();
        }
        else {
            tuningUpdate();
        }
    }

    public void menuSelectUpdate(){
        gamepad.update();
        telemetry.addData("Selected tuning mode: ", mode.name());
        if(gamepad.isGamepadReady()) {
            if (gamepad.dpad_up) {
                selectedMode -= 1;
                mode = modes.values()[Math.max(0, (selectedMode))];
                gamepad.resetTimer();
            } else if (gamepad.dpad_down) {
                selectedMode += 1;
                mode = modes.values()[Math.min((selectedMode), modes.values().length - 1)];
                gamepad.resetTimer();
            }
            if (gamepad.circle) {
                isModeSelected = modeSelected.SELECTED;
                gamepad.resetTimer();
            }
        }
    }

    public void tuningUpdate(){
        switch (mode){
            case DRIVE:
                mecDrive.drive();
                mecDrive.log(telemetry);
                break;
            case SHOOT:
                break;
        }
    }
}
