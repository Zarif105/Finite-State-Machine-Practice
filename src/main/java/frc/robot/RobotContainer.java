// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.Superstructure;
import frc.robot.subsystems.Elevator.Elevator;
import frc.robot.subsystems.Elevator.Elevator.WantedElevatorState;
import frc.robot.subsystems.Placer.Placer;
// import frc.robot.subsystems.Placer.Placer.WantedPlacerState;
import frc.robot.subsystems.Superstructure.WantedSuperstructure;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

  Elevator elevator = new Elevator();
  Placer placer = new Placer();

  Superstructure superstructure = new Superstructure(elevator, placer);
  // The robot's subsystems and commands are defined here...


  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController operatorController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {

    
        operatorController.y().onTrue(Commands.runOnce(() -> superstructure.setWantedSuperstructure(WantedSuperstructure.IDLE)));

        operatorController.povDown().onTrue(Commands.runOnce(() -> superstructure.setWantedSuperstructure(WantedSuperstructure.MOVE_TO_POS, WantedElevatorState.L1Position)));
        
        operatorController.povRight().onTrue(Commands.runOnce(() -> superstructure.setWantedSuperstructure(WantedSuperstructure.MOVE_TO_POS, WantedElevatorState.L2Position)));
        operatorController.povLeft().onTrue(Commands.runOnce(() -> superstructure.setWantedSuperstructure(WantedSuperstructure.MOVE_TO_POS, WantedElevatorState.L3Position)));
        operatorController.povUp().onTrue(Commands.runOnce(() -> superstructure.setWantedSuperstructure(WantedSuperstructure.MOVE_TO_POS, WantedElevatorState.L4Position)));

        operatorController.a().whileTrue(Commands.runOnce(() -> superstructure.setWantedSuperstructure(WantedSuperstructure.COLLECT)));

        operatorController.rightTrigger().whileTrue(Commands.runOnce(() -> superstructure.setWantedSuperstructure(WantedSuperstructure.EJECT_CORAL)));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return null;
  }
}
