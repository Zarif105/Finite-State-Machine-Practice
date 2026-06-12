// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.mechanisms.swerve.LegacySwerveRequest.Idle;

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
    SCORE_L1,
    SCORE_L2,
    SCORE_L3,
    SCORE_L4,
    TARGET_REEF
  }

  public enum CurrentSuperstructure{
    IDLE,
    COLLETING,
    SCORING_L1,
    SCORING_L2,
    SCORING_L3,
    SCORING_L4,
    TARGETTING_REEF,
  }

  WantedSuperstructure wantedSuperstructure = WantedSuperstructure.IDLE;
  CurrentSuperstructure currentSuperstructure = CurrentSuperstructure.IDLE;



  public Superstructure(Elevator elevator, Placer placer) {
    this.elevator = elevator;
    this.placer = placer;
  }


  public void setWantedSuperstructure(WantedSuperstructure wantedSuperstructure){
    this.wantedSuperstructure = wantedSuperstructure;
    
    switch (wantedSuperstructure) {
      case IDLE:
          currentSuperstructure = CurrentSuperstructure.IDLE;
        break;

      case COLLECT:
        currentSuperstructure = CurrentSuperstructure.COLLETING;
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
    }
  }


  public void goToIdle(){
    if(placer.isCoralInPlacer() && placer.getCurrentPlacerState() == CurrentPlacerState.Spinning_Stopped){
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


  @Override
  public void periodic() {

    applySuperstructureState(currentSuperstructure);

    // This method will be called once per scheduler run
  }
}
