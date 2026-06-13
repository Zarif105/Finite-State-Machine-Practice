// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Elevator.Elevator;
import frc.robot.subsystems.Elevator.Elevator.CurrentElevatorState;
import frc.robot.subsystems.Elevator.Elevator.WantedElevatorState;
import frc.robot.subsystems.Placer.Placer;
import frc.robot.subsystems.Placer.Placer.CurrentPlacerState;
import frc.robot.subsystems.Placer.Placer.WantedPlacerState;

public class Superstructure extends SubsystemBase {
  /** Creates a new Superstructure. */

  Elevator elevator;
  Placer placer;


  public enum WantedSuperstructure{
    IDLE,
    COLLECT,
    MOVE_TO_POS,
    EJECT_CORAL,
    // SCORE_L1,
    // SCORE_L2,
    // SCORE_L3,
    // SCORE_L4,
    // TARGET_REEF
  }

  public enum CurrentSuperstructure{
    IDLE,
    COLLETING,
    MOVING_TO_POS,
    EJECTING_CORAL,
    // SCORING_L1,
    // SCORING_L2,
    // SCORING_L3,
    // SCORING_L4,
    // TARGETTING_REEF,
  }

  WantedSuperstructure wantedSuperstructure = WantedSuperstructure.IDLE;
  CurrentSuperstructure currentSuperstructure = CurrentSuperstructure.IDLE;
  private WantedElevatorState wantedElevatorState = WantedElevatorState.Home;


  public Superstructure(Elevator elevator, Placer placer) {
    this.elevator = elevator;
    this.placer = placer;
  }

  public void setWantedSuperstructure(WantedSuperstructure wantedSuperstructure){
    this.wantedSuperstructure = wantedSuperstructure;
  }

  public void setWantedSuperstructure(WantedSuperstructure wantedSuperstructure, WantedElevatorState wantedElevatorState){
    this.wantedSuperstructure = wantedSuperstructure;
    this.wantedElevatorState = wantedElevatorState;
  }


  public void updateCurrentSuperstucture(WantedSuperstructure wantedSuperstructure){
    this.wantedSuperstructure = wantedSuperstructure;
    switch (wantedSuperstructure) {
      case IDLE:
        currentSuperstructure = CurrentSuperstructure.IDLE;
      break;

      case COLLECT:
        currentSuperstructure = CurrentSuperstructure.COLLETING;
      break;

      case MOVE_TO_POS:
        currentSuperstructure = CurrentSuperstructure.MOVING_TO_POS;
      break;

      case EJECT_CORAL:
        currentSuperstructure = CurrentSuperstructure.EJECTING_CORAL;
      break;
    }
  }


  public void applySuperstructureState(CurrentSuperstructure currentSuperstructure){
    this.currentSuperstructure = currentSuperstructure;

    switch (currentSuperstructure) {
      case IDLE:
        goToIdle();
      break;

      case COLLETING:
        collect();
      break;

      case MOVING_TO_POS:
        requestElevatorPos();
      break;

      case EJECTING_CORAL:
        ejectCoral();
      break;
    }
  }


  public void goToIdle(){
    if(placer.getCurrentPlacerState() == CurrentPlacerState.Spinning_Stopped){
      elevator.setWantedElevatorState(WantedElevatorState.Home);
    }else{
      placer.setWantedPlacerState(WantedPlacerState.Spin_Stop);
    }
  }

  public void collect(){
    if((placer.getCurrentPlacerState() == CurrentPlacerState.Spinning_Stopped) 
        && (elevator.getCurrentElevatorState() == CurrentElevatorState.Home)
          && !placer.isCoralInPlacer()){
        placer.setWantedPlacerState(WantedPlacerState.Collect_From_Hopper);
    }else{
      placer.setWantedPlacerState(WantedPlacerState.Spin_Stop);
      elevator.setWantedElevatorState(WantedElevatorState.Home);
    }
  }

  public void requestElevatorPos(){
    if(wantedElevatorState == WantedElevatorState.L1Position){
      elevator.setWantedElevatorState(wantedElevatorState);
    }else{
      if(placer.isCoralInPlacer()){
        elevator.setWantedElevatorState(wantedElevatorState);
      }
    }
  }

  public void ejectCoral(){
    if(elevator.elevatorAtDesiredState() && placer.isCoralInPlacer()){
      placer.setWantedPlacerState(WantedPlacerState.Eject_Coral);
    }else{
      placer.setWantedPlacerState(WantedPlacerState.Spin_Stop);
    }
  }


  @Override
  public void periodic() {
    updateCurrentSuperstucture(wantedSuperstructure);
    applySuperstructureState(currentSuperstructure);

    // This method will be called once per scheduler run
  }
}
