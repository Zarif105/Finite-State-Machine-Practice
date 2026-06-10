// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Elevator.Elevator;
import frc.robot.subsystems.Elevator.Elevator.WantedState;
import frc.robot.subsystems.Placer.Placer;
import frc.robot.subsystems.Placer.Placer.WantedPlacerState;

public class Superstructure extends SubsystemBase {
  /** Creates a new Superstructure. */

  Elevator elevator;
  Placer placer;


  public enum WantedSuperstructure{
    Spin_Placer_Backward,
    Collect_Coral_From_Hopper,
    Eject_Coral,
    L1Position,
    L2Position,
    L3Position,
    L4Position,
    Stop_Spinning_Placer,
    IDLE
  }

  public enum CurrentSuperstructure{
    Spinning_Backward,
    Collecting_From_Hopper,
    Ejecting_Coral,
    L1Position,
    L2Position,
    L3Position,
    L4Position,
    Stopping_Placer,
    IDLE
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

      case Eject_Coral:
        currentSuperstructure = CurrentSuperstructure.Ejecting_Coral;
        break;
        
      case L1Position:
        currentSuperstructure = CurrentSuperstructure.L1Position;
        break;

      case L2Position:
        currentSuperstructure = CurrentSuperstructure.L2Position;
        break;

      case L3Position:
        currentSuperstructure = CurrentSuperstructure.L3Position;
        break;

      case L4Position:
        currentSuperstructure = CurrentSuperstructure.L4Position;
        break;

      case Collect_Coral_From_Hopper:
        currentSuperstructure = CurrentSuperstructure.Collecting_From_Hopper;
        break;

      case Spin_Placer_Backward:
        currentSuperstructure = CurrentSuperstructure.Spinning_Backward;
        break;

      case Stop_Spinning_Placer:
        currentSuperstructure = CurrentSuperstructure.Stopping_Placer;
        break;
      
      case IDLE: 
        currentSuperstructure = CurrentSuperstructure.IDLE;
        break;
    }

  }


  public void applySuperstructureState(CurrentSuperstructure currentSuperstructure){
    this.currentSuperstructure = currentSuperstructure;

    switch (wantedSuperstructure) {

      case Eject_Coral:
        placer.setWantedPlacerState(WantedPlacerState.Eject_Coral);
        break;

      case L1Position:
        elevator.setWantedElevatorState(WantedState.L1Position);
        break;

      case L2Position:
        elevator.setWantedElevatorState(WantedState.L2Position);
        break;

      case L3Position:
        elevator.setWantedElevatorState(WantedState.L3Position);
        break;

      case L4Position:
        elevator.setWantedElevatorState(WantedState.L4Position);
        break;

      case Collect_Coral_From_Hopper:
        placer.setWantedPlacerState(WantedPlacerState.Collect_From_Hopper);
        break;
        
      case Spin_Placer_Backward:
        placer.setWantedPlacerState(WantedPlacerState.Spin_Backward);
        break;

      case Stop_Spinning_Placer:
        placer.setWantedPlacerState(WantedPlacerState.Spin_Stop);
        break;

      case IDLE:
                elevator.setWantedElevatorState(WantedState.Home); 
                placer.setWantedPlacerState(WantedPlacerState.Spin_Stop);
        break;
    }
  }



  @Override
  public void periodic() {

    applySuperstructureState(currentSuperstructure);

    // This method will be called once per scheduler run
  }
}
