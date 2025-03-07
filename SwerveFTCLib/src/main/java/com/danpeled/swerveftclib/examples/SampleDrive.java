package com.danpeled.swerveftclib.examples;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.button.Button;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.geometry.Translation2d;
import com.danpeled.swerveftclib.Swerve.SwerveDrive;
import com.danpeled.swerveftclib.Swerve.SwerveDriveCoefficients;
import com.danpeled.swerveftclib.Swerve.modules.AxonSwerveModule;
import com.danpeled.swerveftclib.Swerve.modules.SwerveModuleConfiguration;
import com.danpeled.swerveftclib.util.BaseDrive;
import com.danpeled.swerveftclib.util.Location;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

/**
 * SampleDrive class represents a sample teleoperated mode for controlling a swerve drive robot.
 * It extends the CommandOpMode class and implements the necessary methods for initializing and running the robot.
 *
 * @see CommandOpMode
 */
@TeleOp
public class SampleDrive extends CommandOpMode {
    // Constants for motor and servo control
    private static final double TICKS_PER_REVOLUTION = 28;  // example value
    private static final double WHEEL_CIRCUMFERENCE = Math.PI * 0.05765; // Wheel circumference in meters (example)
    SwerveDrive swerveDrive;
    GamepadEx driver;
    ExampleSwerveSubsystem swerveSubsystem;

    ExampleSwerveSubsystem swerveSubsystem;

    /**
     * Initializes the teleoperated mode by setting up the swerve drive subsystem,
     * configuring the default command, and mapping the gamepad buttons to commands.
     */
    @Override
    public void initialize() {

        driver = new GamepadEx(gamepad1);

        swerveDrive = new SwerveDrive(this, new SwerveDriveCoefficients(
                WHEEL_CIRCUMFERENCE, TICKS_PER_REVOLUTION,
                new PIDFCoefficients(1, 0, 0, 0),
                new PIDFCoefficients(1, 0, 0, 0),
                new Translation2d(5, 5),
                new Translation2d(-5, 5),
                new Translation2d(5, -5),
                new Translation2d(-5, -5),
                "imu 1")

        );

        swerveDrive.init(AxonSwerveModule.class, new SwerveModuleConfiguration[]{
                SwerveModuleConfiguration.create("RL", "sLF", "eLF"),
                SwerveModuleConfiguration.create("RF", "sRF", "eRF"),
                SwerveModuleConfiguration.create("LB", "sLB", "eLB"),
                SwerveModuleConfiguration.create("RB", "sRB", "eRB")
        });

        swerveSubsystem = new ExampleSwerveSubsystem(swerveDrive);
        // Set the default command for the swerve drive to SetPowerOriented
        swerveSubsystem.setDefaultCommand(
                new SwerveCommands.SetPowerOriented(
                        swerveSubsystem,
                        driver::getLeftX,
                        driver::getLeftY,
                        driver::getRightX,
                        true));

        // Map the A button on the gamepad to the GoTo command
        Button exampleButton = new GamepadButton(driver, GamepadKeys.Button.A);

        // Configure default settings for the GoTo command
        BaseDrive.GotoSettings defaultGoToSettings = new BaseDrive.GotoSettings.Builder()
                .setPower(1)
                .setSlowdownMode(false)
                .setTolerance(0.5)
                .build();

        // Assign the GoTo command to the A button
        exampleButton.whenPressed(new SwerveCommands.GoTo(swerveSubsystem, new Location(0, 0), defaultGoToSettings));
        // Register the swerve drive subsystem
        register(swerveSubsystem);

    }

    @Override
    public void run() {
        swerveSubsystem.getDefaultCommand().schedule();
    }
}
