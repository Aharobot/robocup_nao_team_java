//-----------------------------------------------------------------------------
//  File:         FieldPlayer.java (to be used in a Webots java controllers)
//  Date:         March, 2012
//  Description:  Field player "2", "3" or "4" for "red" or "blue" team
//  Author:       Ruijiao Li 
//  Code Based on original provider :Yvan Bourquin - www.cyberbotics.com
//-----------------------------------------------------------------------------

import com.cyberbotics.webots.controller.*;

public class FieldPlayer extends Player {

  private Motion backwardsMotion, forwardsMotion, forwards50Motion, turnRight40Motion, turnLeft40Motion;
  private Motion turnRight60Motion, turnLeft60Motion, turnLeft180Motion, sideStepRightMotion, sideStepLeftMotion;
  private Motion shootMotion, handWaveMotion;

  private double goalDir = 0.0; // interpolated goal direction (with respect to front direction of robot body)
  private double ourGoalDir = 0.0;
  private double lineDir = 0.0;
  private double lineDist = 0.0;
  private double goalDist = 0.0;
  private double ourGoalDist = 0.0;

  public FieldPlayer(int playerID, int teamID) {
    super(playerID, teamID);
    backwardsMotion     = new Motion("../../motions/Backwards.motion");
    forwardsMotion      = new Motion("../../motions/Forwards.motion");
    forwards50Motion    = new Motion("../../motions/Forwards50.motion");
    turnRight40Motion   = new Motion("../../motions/TurnRight40.motion");
    turnLeft40Motion    = new Motion("../../motions/TurnLeft40.motion");
    turnRight60Motion   = new Motion("../../motions/TurnRight60.motion");
    turnLeft60Motion    = new Motion("../../motions/TurnLeft60.motion");
    turnLeft180Motion   = new Motion("../../motions/TurnLeft180.motion");
    sideStepRightMotion = new Motion("../../motions/SideStepRight.motion");
    sideStepLeftMotion  = new Motion("../../motions/SideStepLeft.motion");
    shootMotion = new Motion("../../motions/Shoot.motion");
    handWaveMotion = new Motion("../../motions/HandWave.motion");

    // move arms along the body
    Servo leftShoulderPitch = getServo("LShoulderPitch");
    leftShoulderPitch.setPosition(1.5);
    Servo rightShoulderPitch = getServo("RShoulderPitch");
    rightShoulderPitch.setPosition(1.5);
  }
  // normalize angle between -PI and +PI
  public double normalizeAngle(double angle) {
    while (angle > Math.PI) angle -= 2.0 * Math.PI;
    while (angle < -Math.PI) angle += 2.0 * Math.PI;
    return angle;
  }

  // relative body turn
  public void turnBodyRel(double angle) {
    if (angle > 0.7)
      turnRight60();
    else if (angle < -0.7)
      turnLeft60();
    else if (angle > 0.3)
      turnRight40();
    else if (angle < -0.3)
      turnLeft40();
  }


  public void runStep() {
    super.runStep();
    double dir = camera.getGoalDirectionAngle();
    double ourDir = camera.getOurGoalDirectionAngle();
    double lineElev = 0;
    if (dir != SimpleCam.UNKNOWN)
      goalDir = dir - headYaw.getPosition();
    else 
        goalDir = SimpleCam.UNKNOWN;

    if(ourDir != SimpleCam.UNKNOWN)
        ourGoalDir = ourDir - headYaw.getPosition();
    else
        ourGoalDir = SimpleCam.UNKNOWN;

    if(camera.getLineDirectionAngle() == SimpleCam.UNKNOWN)
        lineDir = SimpleCam.UNKNOWN;
    else 
        lineDir =  camera.getLineDirectionAngle() - headYaw.getPosition();
     if(camera.getLineElevationAngle() == SimpleCam.UNKNOWN)
        lineDist = SimpleCam.UNKNOWN;
      else 
        lineElev =  camera.getLineElevationAngle() - headPitch.getPosition() - cameraAngle;
        lineDist = 0.51 / Math.tan(-lineElev);
     if(camera.getGoalElvationAngle()== SimpleCam.UNKNOWN)
        goalDist = SimpleCam.UNKNOWN;
     else
        goalDist = 0.51/ Math.tan(-dir);

  }

  public void turnRight60() {
      playMotion(turnRight60Motion); // 59.2 degrees
      goalDir = normalizeAngle(goalDir - 1.033);
      ourGoalDir = normalizeAngle(ourGoalDir - 1.033);
  }

  public void turnLeft60() {
      playMotion(turnLeft60Motion); // 59.2 degrees
      goalDir = normalizeAngle(goalDir + 1.033);
      ourGoalDir = normalizeAngle(ourGoalDir + 1.033);
  }

  public void turnRight40() {
      playMotion(turnRight40Motion); // 39.7 degrees
      goalDir = normalizeAngle(goalDir - 0.693);
      ourGoalDir = normalizeAngle(goalDir - 0.693);
  }

  public void turnLeft40() {
      playMotion(turnLeft40Motion); // 39.7 degrees
      goalDir = normalizeAngle(goalDir + 0.693);
      ourGoalDir = normalizeAngle(ourGoalDir + 0.693);
  }
  
  public void turnLeft180() {
      playMotion(turnLeft180Motion); // 163.6 degrees
      goalDir = normalizeAngle(goalDir + 2.855);
      ourGoalDir = normalizeAngle(ourGoalDir + 2.855);
  }

  public void backwards(){
      playMotion(backwardsMotion);
  }

  public void forwards(){
      playMotion(forwardsMotion);
  }

  public void forwards50(){
      playMotion(forwards50Motion);
  }
  
  public void sideStepLeft(){
      playMotion(sideStepLeftMotion);
  }

  public void sideStepRight(){
      playMotion(sideStepRightMotion);
  }

  public void shootGoal(){
      playMotion(shootMotion);
  }
  public void handWave(){
      playMotion(handWaveMotion);  

  }


  public double getGoalDirection(){
      return goalDir;
  }
  
  public double getOurGoalDirection(){
      return ourGoalDir;          
  }

  public double getLineDirection(){

      return lineDir;
  }

  public double getLineDistance(){
      return lineDist;
  }
 
  public void run() {
	// TODO Auto-generated method stub
	
  }
}
