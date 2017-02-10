package org.team751.subsystems;

import org.team751.commands.JoystickDrive;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Drivetrain extends Subsystem {
    public VictorSP leftDriveController1 = new VictorSP(0);
    public VictorSP leftDriveController2 = new VictorSP(1);
    public VictorSP leftDriveController3 = new VictorSP(2);
    
    public VictorSP rightDriveController1 = new VictorSP(3);
    public VictorSP rightDriveController2 = new VictorSP(4);
    public VictorSP rightDriveController3 = new VictorSP(5);
    
    
    public Encoder leftEncoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X); //not sure about the encoding type yet
    public Encoder rightEncoder = new Encoder(2, 3, false, Encoder.EncodingType.k4X);
    
    public boolean isDrivingAutonomously;
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new JoystickDrive());
    }
    
    //motors are assembled in opposite directions
    public void setLeftSpeed(double speed) {
    	leftDriveController1.set(-speed);
    	leftDriveController2.set(-speed);
    	leftDriveController3.set(-speed);
    }
    
    public void setRightSpeed(double speed) {
    	rightDriveController1.set(speed);
    	rightDriveController2.set(speed);
    	rightDriveController3.set(speed);
    }
}

