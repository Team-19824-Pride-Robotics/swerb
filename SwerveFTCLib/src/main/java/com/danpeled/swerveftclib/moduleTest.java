package com.danpeled.swerveftclib;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.Range;


@Config
@TeleOp(name = "moduleTest")
public class moduleTest extends LinearOpMode {


    public static double sv;
    public static double ds = 0;


    DcMotorEx D1;
    ServoImplEx servo;
    AnalogInput sEncoder1;
    AnalogInput sEncoder2;
    AnalogInput sEncoder3;
    AnalogInput sEncoder4;



    public void runOpMode() {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        D1 = hardwareMap.get(DcMotorEx.class, "RF");
        servo = (ServoImplEx) hardwareMap.get(Servo.class, "sRF");
        servo.setPwmRange(new PwmControl.PwmRange(505, 2495));
        sEncoder1 = hardwareMap.get(AnalogInput.class, "eRF");
        sEncoder2 = hardwareMap.get(AnalogInput.class, "eLF");
        sEncoder3 = hardwareMap.get(AnalogInput.class, "eRB");
        sEncoder4 = hardwareMap.get(AnalogInput.class, "eLB");


        waitForStart();

        while (opModeIsActive()) {





            double pos1 = sEncoder1.getVoltage() / 3.3 * 360;
            double pos2 = sEncoder2.getVoltage() / 3.3 * 360;
            double pos3 = sEncoder3.getVoltage() / 3.3 * 360;
            double pos4 = sEncoder4.getVoltage() / 3.3 * 360;
            
            ds = gamepad1.right_stick_y;
            sv= (gamepad1.left_stick_x+1)/2;
            D1.setPower(ds);

            sv = Range.clip(sv, .01, .99);

            servo.setPosition(sv);

            telemetry.addData("Run time",getRuntime());
            telemetry.addData("pos1", pos1);
            telemetry.addData("pos2", pos2);
            telemetry.addData("pos3", pos3);
            telemetry.addData("pos4", pos4);
            telemetry.update();
        }

    }

    //when G2y changes states from what it previously was



}