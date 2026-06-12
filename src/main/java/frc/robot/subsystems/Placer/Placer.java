// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Placer;

import frc.robot.utils.Kraken;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.PlacerConstants;
// import frc.robot.subsystems.Elevator.Elevator.CurrentState;
// import frc.robot.subsystems.Elevator.Elevator.WantedState;

public class Placer extends SubsystemBase {

  private Kraken frontMotor;
  private Kraken rearMotor;
  private DigitalInput placerBeamBreak;
  public boolean previousPlacerCoralState;

  public enum WantedPlacerState{
    Collect_From_Hopper,
    Eject_Coral,
    Spin_Backward,
    Spin_Stop
  }

  public enum CurrentPlacerState{
    Collecting_From_Hopper,
    Ejecting_Coral_Forward,
    Spinning_Backward,
    Spinning_Stopped
  }

  WantedPlacerState wantedPlacerState = WantedPlacerState.Spin_Stop;
  CurrentPlacerState currentPlacerState = CurrentPlacerState.Spinning_Stopped;

  /** Creates a new Placer. */
  public Placer() {

    // instantiating hardware
    frontMotor = new Kraken(PlacerConstants.placerFrontMotorID);
    rearMotor = new Kraken(PlacerConstants.placerRearMotorID);
    placerBeamBreak = new DigitalInput(PlacerConstants.placerBeamBreakID);


    // configuring motors
    frontMotor.setBrakeMode();
    rearMotor.setBrakeMode();

    // sets up coral states to update on first scheduler tick
    previousPlacerCoralState = isCoralInPlacer();

    // sends inital coral state into smart dash
    SmartDashboard.putBoolean("Placer Beam Break State", previousPlacerCoralState);
  }

  public void setWantedPlacerState(WantedPlacerState wantedPlacerState){
    this.wantedPlacerState = wantedPlacerState;

    switch (wantedPlacerState) {
      case Collect_From_Hopper:
        currentPlacerState = CurrentPlacerState.Collecting_From_Hopper;
        break;
      case Eject_Coral:
        currentPlacerState = CurrentPlacerState.Ejecting_Coral_Forward;
        break;
      case Spin_Backward:
        currentPlacerState = CurrentPlacerState.Spinning_Backward;
        break;
      case Spin_Stop:
        currentPlacerState = CurrentPlacerState.Spinning_Stopped;
        break;
    }
  }



  public void applyWantedPlacerState(CurrentPlacerState currentPlacerState){
    this.currentPlacerState = currentPlacerState;

    switch (currentPlacerState) {
      case Collecting_From_Hopper:
        setRearMotor(PlacerConstants.placerBackMotorSpeed);
        break;
    case Ejecting_Coral_Forward:
        setRearMotor(PlacerConstants.placerBackMotorSpeed);
        setFrontMotor(PlacerConstants.placerFrontMotorSpeed);
        break;
    case Spinning_Backward:
        setFrontMotor(-PlacerConstants.placerBackMotorSpeed);
        break;
    case Spinning_Stopped:
        setRearMotor(0);
        setFrontMotor(0);
        break;
      default:
        break;
    }
  }

  public CurrentPlacerState getCurrentPlacerState(){
    return this.currentPlacerState;
  }
  


  @Override
  public void periodic() {

    applyWantedPlacerState(currentPlacerState);
    // This method will be called once per scheduler run

    // adds beam break state to smart dashboard
    // only if it changes states to reduce smartdash load
    boolean placerCoralState = isCoralInPlacer();
    if (placerCoralState != previousPlacerCoralState) {

      SmartDashboard.putBoolean("Placer Beam Break State", placerCoralState);
      previousPlacerCoralState = placerCoralState;
    }
  }

  /** Sets the speed of the front motor. <p> 
   *  Only used for moving forwards. <p>
   *  Use positive numbers for the speed variable only.
   */
  public void setFrontMotor(double speed) {
    frontMotor.setMotorSpeed(speed);
  }

  /** Sets the speed of the rear motor. <p> 
   *  Only used for moving forwards. <p>
   *  Use positive numbers for the speed variable only.
   */
  public void setRearMotor(double speed) {
    rearMotor.setMotorSpeed(speed);
  }

  public void setBothMotors(double speed) {
    frontMotor.setMotorSpeed(speed);
    rearMotor.setMotorSpeed(speed);
  }

  /** Gets the state of the beam break sensor. <p>
   *  This sensor is used in the scoring pipeline to determine when the coral is prepared
   *  to be scored and the elevator is safe to move. <p>
   *  Returns false when beam is obstructed and true when beam is unobstructed
   */
  public boolean isCoralInPlacer() {
    return !placerBeamBreak.get();
  }
}