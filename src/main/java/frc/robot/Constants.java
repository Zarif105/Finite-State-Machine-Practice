// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }

   public static class ElevatorConstants {
    public static final int leftMotorID = 18;
    public static final int rightMotorID = 19;
    public static final int elevatorCANCoderID = 17;

    public static final int lowerLimitSwitchPort = 9;
    public static final int upperLimitSwitchPort = 8;

    // elevator motor PID
    public static final double ELEVATOR_P = 0.15;
    public static final double ELEVATOR_I = 0;
    public static final double ELEVATOR_D = 0;
    public static final double ELEVATOR_SFF = 0; // static feedforward
    public static final double ELEVATOR_VFF = 0; // velocity feedforward
    public static final double ELEVATOR_AFF = 0; // acceleration feedforward
    public static final double ELEVATOR_GFF = 0.296; // gravity feedforward 0.296
    public static final double ELEVATOR_MIN_OUTPUT = -1;
    public static final double ELEVATOR_MAX_OUTPUT = 1;
    
    // elevator setpoints (inches)
    public static final double homePosition = -2;
    public static final double L1Position = 19;
    public static final double L2Position = 31;
    public static final double L3Position = 44;
    public static final double L4Position = 67;
    public static final double positionTolerance = .1;
    public static final double softLimitMinPosition = 0;
    public static final double softLimitMaxPosition = 0;
    public static final double incrementMeasurement = 1.5;

    public static final double elevatorGearing = 14 / 60; // inches
    public static final double elevatorSprocketRadius = 1.037;
    public static final double elevatorSprocketCircumference = 2 * Math.PI * elevatorSprocketRadius; // inches
    public static final double elevatorEncoderToMechanismRatio = (1 / elevatorSprocketCircumference) / 3;
  }

  // Placer Constants
  public static class PlacerConstants {
    public static final int placerFrontMotorID = 21;
    public static final int placerRearMotorID = 22;
    public static final int placerBeamBreakID = 1;
    
    public static final double placerFrontMotorSpeed = 0.2; // .2
    public static final double placerBackMotorSpeed = 0.1; // .17 //.12
    public static final double placerAlgaeSpeed = 0.4;
    public static final double placerCollectAlgaeSpeed = 0.6;
  }

}
