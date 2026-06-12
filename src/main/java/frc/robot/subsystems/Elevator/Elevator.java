// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Elevator;

import static edu.wpi.first.units.Units.Second;
import static edu.wpi.first.units.Units.Volts;

import java.util.function.BooleanSupplier;

import com.ctre.phoenix6.SignalLogger;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.signals.MotorAlignmentValue;

import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Config;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Mechanism;
import frc.robot.Constants;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.utils.Kraken;

public class Elevator extends SubsystemBase {
  /** Creates a new Elevator. */
 
  private Kraken leftMotor, rightMotor;
  private CANcoder elevatorEncoder;
  private DigitalInput lowerLimitSwitch, upperLimitSwitch;
  private double desiredSetpoint; // desired setpoint of the encoder

  private SysIdRoutine appliedRoutine;


  public enum WantedElevatorState{
    Home,
    L1Position,
    L2Position,
    L3Position,
    L4Position,
  }

  public enum CurrentElevatorState{
    Home,
    L1Position,
    L2Position,
    L3Position,
    L4Position
  }

  WantedElevatorState wantedElevatorState = WantedElevatorState.Home;
  CurrentElevatorState currentElevatorState = CurrentElevatorState.Home;


  //https://v6.docs.ctr-electronics.com/en/stable/docs/api-reference/wpilib-integration/sysid-integration/plumbing-and-running-sysid.html
  private final SysIdRoutine elevatorSysIDRoutine = new SysIdRoutine(
    new Config(
      Volts.of(.5).per(Second),
      Volts.of(1),
      null,
      (state) -> SignalLogger.writeString("elevator sysID State", state.toString())),
    new Mechanism(
      (volts) -> leftMotor.setVolts(volts),
      null,
      this)
      );


  
  public Elevator() {
    appliedRoutine = elevatorSysIDRoutine;
    

    leftMotor = new Kraken(ElevatorConstants.leftMotorID);
    rightMotor = new Kraken(ElevatorConstants.rightMotorID);
    elevatorEncoder = new CANcoder(ElevatorConstants.elevatorCANCoderID);

    lowerLimitSwitch = new DigitalInput(ElevatorConstants.lowerLimitSwitchPort);
    upperLimitSwitch = new DigitalInput(ElevatorConstants.upperLimitSwitchPort);

    leftMotor.restoreFactoryDefaults();
    rightMotor.restoreFactoryDefaults();
    
    leftMotor.setInverted();
    
    rightMotor.follow(ElevatorConstants.leftMotorID, MotorAlignmentValue.Aligned);
    
    leftMotor.addEncoder(elevatorEncoder);
    
    leftMotor.setRotorToSensorRatio(ElevatorConstants.elevatorGearing);
    leftMotor.setSensorToMechanismRatio(ElevatorConstants.elevatorEncoderToMechanismRatio);
    
    
    leftMotor.setPIDValues(ElevatorConstants.ELEVATOR_P, ElevatorConstants.ELEVATOR_I,
    ElevatorConstants.ELEVATOR_D, ElevatorConstants.ELEVATOR_SFF,
    ElevatorConstants.ELEVATOR_VFF, ElevatorConstants.ELEVATOR_AFF, ElevatorConstants.ELEVATOR_GFF);
    
    leftMotor.setBrakeMode();
    rightMotor.setBrakeMode();
    
    leftMotor.setMotorCurrentLimits(40);
    leftMotor.setSoftLimits(ElevatorConstants.softLimitMinPosition, // prevent us from overdriving the motor
    ElevatorConstants.softLimitMaxPosition);
    
    desiredSetpoint = ElevatorConstants.homePosition;
    elevatorEncoder.setPosition(0);
    leftMotor.setDesiredEncoderPosition(desiredSetpoint);

    
    SignalLogger.start();
  }

  public void setWantedElevatorState(WantedElevatorState wantedElevatorState){
    this.wantedElevatorState = wantedElevatorState;

    switch (wantedElevatorState) {
      case Home:
        currentElevatorState = CurrentElevatorState.Home;
        break;
      case L1Position:
        currentElevatorState = CurrentElevatorState.L1Position;
        break;
      case L2Position:
        currentElevatorState = CurrentElevatorState.L2Position;
        break;
      case L3Position:
        currentElevatorState = CurrentElevatorState.L3Position;
        break;
      case L4Position:
        currentElevatorState = CurrentElevatorState.L4Position;
        break;

      default: currentElevatorState = CurrentElevatorState.Home;
        break;
    }
  }

  public void applyElevatorState(CurrentElevatorState currentElevatorState){
    this.currentElevatorState = currentElevatorState;
    
    switch (currentElevatorState) {
      case Home:
        setElevatorPosition(ElevatorConstants.homePosition);
        break;
      case L1Position:
        setElevatorPosition(ElevatorConstants.L1Position);
        break;
      case L2Position:
        setElevatorPosition(ElevatorConstants.L2Position);
        break;
      case L3Position:
        setElevatorPosition(ElevatorConstants.L3Position);
        break;
      case L4Position:
        setElevatorPosition(ElevatorConstants.L4Position);
        break;
      default: setElevatorPosition(0);
        break;
    }

  }

  public CurrentElevatorState getCurrentElevatorState(){
    return this.currentElevatorState;
  }

    public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
    return appliedRoutine.quasistatic(direction);
  }

  public Command sysIdDynamic(SysIdRoutine.Direction direction) {
    return appliedRoutine.dynamic(direction);
  }

  public void rightMotorSpin() {
    leftMotor.setMotorSpeed(.01);
  }

  /** Stop elevator (emergency feature). */
  public void stopElevator() {
    leftMotor.stopMotor();
  }

  public void setPercent(double percent) {
    leftMotor.setMotorSpeed(percent);
  }

  /** Stop elevator (emergency feature). */
  public void stopElevatorVolts() {
    leftMotor.setVolts(Voltage.ofBaseUnits(0, Volts));
  }

  /** Sets elevator position based off setpoint value. */
  public void setElevatorPosition(double setpoint) {
    desiredSetpoint = setpoint;
    leftMotor.setDesiredEncoderPosition(desiredSetpoint);
  }

  /** Adjust the position of the elevator by a set increment. */
  public void adjustPositionIncrementally(double increment) {
    desiredSetpoint += increment;
    leftMotor.setDesiredEncoderPosition(desiredSetpoint);
  }

  /** Scales the elevator encoder position by a factor of 2(pi)(r), converting rotations to inches. */
  public double getElevatorPositionInInches() {
    return leftMotor.getPosition();
  }

  public boolean isLowerLimitReached() {
    return lowerLimitSwitch.get();
  }

  public boolean isUpperLimitReached() {
    return !upperLimitSwitch.get();
  }

  public BooleanSupplier isAtPosition(double targetPosition) {

    // if ( Units.epsilonEquals(getElevatorPositionInInches(), position, Constants.ElevatorConstants.positionTolerance )) {
    double currentPosition = getElevatorPositionInInches();
    if ( (targetPosition - Constants.ElevatorConstants.positionTolerance >= currentPosition ) && 
         (targetPosition + Constants.ElevatorConstants.positionTolerance <= currentPosition)) {
      return () -> true;
    }
    
    return () -> false;
  }



  @Override
  public void periodic() {

    // This method will be called once per scheduler run
    applyElevatorState(currentElevatorState);
  }
}
