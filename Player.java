//-----------------------------------------------------------------------------
//  File:         Player.java (to be used in a Webots java controllers)
//  Date:         Decmeber  2011
//  Description:  Base class for FieldPlayer and GoalKeeper
//  Project:      Robotstadium, the online robot soccer competition
//  Author:       Ruijiao Li 
//  Code Based on original provider :Yvan Bourquin - www.cyberbotics.com
        
//  
//-----------------------------------------------------------------------------

import com.cyberbotics.webots.controller.*;
public abstract class Player extends Robot {

  public enum Cam { TOP, BOTTOM };

  public static final int SIMULATION_STEP = 40;  // milliseconds
  public static final int CAMERA_STEP = 160;  // camera refresh rate in milliseconds
  public static final double CAMERA_OFFSET_ANGLE = 0.6981;  // 40 degrees between cameras axes

  protected RoboCupGameControlData gameControlData = new RoboCupGameControlData();
  protected int teamID;
  protected int playerID;
  protected double cameraAngle = 0.0;  // top camera
  private Motion standUpFromFrontMotion;
  private Motion standUpFromBackMotion;
  private Motion returnFromSideMotion;
  /**
  *setting  devices for robots
  */
  protected Servo headYaw, headPitch;
  protected SimpleCam camera;
  protected Servo cameraSelect;
  protected Accelerometer accelerometer;
  protected Gyro gyro;
  protected DistanceSensor topLeftUltrasound, topRightUltrasound, bottomLeftUltrasound, bottomRightUltrasound;
  protected LED chestLed, rightEyeLed, leftEyeLed, rightEarLed, leftEarLed, rightFootLed, leftFootLed;
  protected TouchSensor[] fsr;  // force sensitive resistors
  protected Emitter emitter, super_emitter;
  protected Receiver receiver;

  private double distance;
  private double[] acc;
  private String inMessage;
  /**
  * for debugging only ! This device does not exist on the real robot.
  */
  protected GPS gps;  

  public boolean isBlue() {
    return gameControlData.getTeam(RoboCupGameControlData.TEAM_BLUE).getTeamNumber() == teamID;
  }

  public boolean isRed() {
    return gameControlData.getTeam(RoboCupGameControlData.TEAM_RED).getTeamNumber() == teamID;
  }

  public Player(int playerID, int teamID) {
    this.playerID = playerID;
    this.teamID = teamID;

    // initialize accelerometer
    accelerometer = getAccelerometer("accelerometer");
    accelerometer.enable(SIMULATION_STEP);

    // initialize gyro
    gyro = getGyro("gyro");
    //gyro.enable(SIMULATION_STEP);   // uncomment only if needed !

    // get "HeadYaw" and "HeadPitch" motors and enable position feedback
    headYaw = getServo("HeadYaw");
    headYaw.enablePosition(SIMULATION_STEP);
    headPitch = getServo("HeadPitch");
    headPitch.enablePosition(SIMULATION_STEP);

    // get all LEDs
    chestLed = getLED("ChestBoard/Led");
    rightEyeLed = getLED("Face/Led/Right");
    leftEyeLed = getLED("Face/Led/Left");
    rightEarLed = getLED("Ears/Led/Right");
    leftEarLed = getLED("Ears/Led/Left");
    rightFootLed = getLED("RFoot/Led");
    leftFootLed = getLED("LFoot/Led");

    // make eyes shine blue
    rightEyeLed.set(0x2222ff);
    leftEyeLed.set(0x2222ff);

    // get camera
    camera = (SimpleCam)getCamera("camera");
    camera.enable(CAMERA_STEP);

    // get camera switch
    cameraSelect = getServo("CameraSelect");

    // foot sole touch sensors
    final String[] TOUCH_SENSOR_NAMES = {
      "RFsrFL", "RFsrFR", "RFsrBR", "RFsrBL",
      "LFsrFL", "LFsrFR", "LFsrBR", "LFsrBL"
    };
    fsr = new TouchSensor[TOUCH_SENSOR_NAMES.length];
    for (int i = 0; i < TOUCH_SENSOR_NAMES.length; i++) {
      fsr[i] = getTouchSensor(TOUCH_SENSOR_NAMES[i]);
      //fsr[i].enable(SIMULATION_STEP);  // uncomment only if needed !
    }

    // emitter/receiver devices that can be used for inter-robot communication
    // and for receiving RobotCupGameControleData
    emitter = getEmitter("emitter");
    receiver = getReceiver("receiver");
    receiver.enable(SIMULATION_STEP);

    // for sending 'move' request to Supervisor
    super_emitter = getEmitter("super_emitter");

    // useful to know the position of the robot
    // the real Nao does not have a GPS, this is for testing only
    // this info will be blurred during Robotstadium contest matches
    //gps = getGPS("gps");
    //gps.enable(SIMULATION_STEP);  // uncomment only if needed !

    // initialize ultrasound sensors
    topLeftUltrasound = getDistanceSensor("US/TopLeft");
    topRightUltrasound = getDistanceSensor("US/TopRight");
    bottomLeftUltrasound = getDistanceSensor("US/BottomLeft");
    bottomRightUltrasound = getDistanceSensor("US/BottomRight");
    topLeftUltrasound.enable(SIMULATION_STEP);  
    topRightUltrasound.enable(SIMULATION_STEP);  
    bottomLeftUltrasound.enable(SIMULATION_STEP);  
    bottomRightUltrasound.enable(SIMULATION_STEP);  

    // load stand up motion
    standUpFromFrontMotion = new Motion("StandUpFromFront.motion");
    standUpFromBackMotion = new Motion("StandUpFromBackRicky.motion");
    returnFromSideMotion = new Motion("ReturnFromSide.motion");
  
  }

  // overriden to create own type of Camera
  protected Camera createCamera(String name) {
    return new SimpleCam(name);
  }

  protected void selectCamera(Cam selection) {
    switch (selection) {
    case TOP:
      cameraSelect.setPosition(0.0);
      cameraAngle = 0.0;
      break;
    case BOTTOM:
      cameraSelect.setPosition(1.0);
      cameraAngle = CAMERA_OFFSET_ANGLE;  // 40 degrees down
      break;
    }
  }

  // play until the specified motion is over
  public void playMotion(Motion motion) {

    if (gameControlData.getState() != RoboCupGameControlData.STATE_PLAYING) {
      runStep();
      return;
    }
    // play to the end
    motion.play();
    do {
      runStep();
    }
    while (!motion.isOver());
  }

  // vertical acceleration should be approx earth acceleration (9.81 m/s^2). If a much lower value
  // is measured, this probably indicates that the robot is no longer in an upright position.
  // The x-axis is oriented towards the front, the y-axis towards the right hand, the z-axis looks
  // towards the head. 
  public double[] getAcc(){
       acc= accelerometer.getValues();
       return acc;
  }
  
  public void standUpFromBack(){
      playMotion(standUpFromBackMotion);
  }

  public void standUpFromFront(){
      playMotion(standUpFromFrontMotion);
  }

  public void returnFromSide(){
      playMotion(returnFromSideMotion);
  }
    
//get the distance between two players with respect to the body of Robots 
  public double getPlayerDistance(){
      double dl = topLeftUltrasound.getValue();
      double dr = topRightUltrasound.getValue();
      if(dl >= dr){
          distance = dl;
      }
      else{
          distance = dr;
      }
      return distance;
  }

  public double getCoPlayerDirection(){
      if(camera.getCoPlayerDirectionAngle() == SimpleCam.UNKNOWN)
          return SimpleCam.UNKNOWN;
      else
          return camera.getCoPlayerDirectionAngle() - headYaw.getPosition();
          
  }

  public double getCoPlayerDistance(){
      if(camera.getCoPlayerElevationAngle() == SimpleCam.UNKNOWN)
          return SimpleCam.UNKNOWN;
      double playerElev = camera.getCoPlayerElevationAngle() - headPitch.getPosition() - cameraAngle;
      return 0.57/Math.tan(-playerElev);
  }

  public double getOpPlayerDirection(){
      if(camera.getOurGoalDirectionAngle() == SimpleCam.UNKNOWN)
          return SimpleCam.UNKNOWN;
      else
          return camera.getOurGoalDirectionAngle() - headYaw.getPosition();
  }

  public double getOpPlayerDistance(){
      if(camera.getOpPlayerElevationAngle() == SimpleCam.UNKNOWN)
          return SimpleCam.UNKNOWN;
      double opPlayerElev = camera.getOpPlayerElevationAngle() - headPitch.getPosition() - cameraAngle;
      return 0.57/Math.tan(-opPlayerElev);
  }
 

  // move head from left to right and from right to left
  // until the ball is sighted or the scan motion is over
  public void headScan() {
    final int STEPS = 30;
    final double HEAD_YAW_MAX = 2.0;
    double yawAngle;
    headPitch.setPosition(0.0);  // horizontal head
    selectCamera(Cam.TOP);  // use top camera

    // left to right using TOP camera
    for (int i = 0; i < STEPS; i++) {
      yawAngle = ((double)i / (STEPS - 1) * 2.0 - 1.0) * HEAD_YAW_MAX;
      headYaw.setPosition(yawAngle);
      step(SIMULATION_STEP);
      camera.processImage();
      if (camera.getBallDirectionAngle() != SimpleCam.UNKNOWN)
        return;
    }

    selectCamera(Cam.BOTTOM);  // use bottom camera

    // right to left using BOTTOM camera
    for (int i = STEPS - 1; i >= 0; i--) {
      yawAngle = ((double)i / (STEPS - 1) * 2.0 - 1.0) * HEAD_YAW_MAX;
      headYaw.setPosition(yawAngle);
      step(SIMULATION_STEP);
      camera.processImage();
      if (camera.getBallDirectionAngle() != SimpleCam.UNKNOWN)
        return;
    }

    // ball was not found: restore head straight position
    headYaw.setPosition(0.0);
  }

  public double getBallDirection() {
    if (camera.getBallDirectionAngle() == SimpleCam.UNKNOWN)
      return SimpleCam.UNKNOWN;
    else
      return camera.getBallDirectionAngle() - headYaw.getPosition();
  }

  // compute floor distance between robot (feet) and ball
  // 0.51 -> approx robot camera base height with respect to ground in a standard posture of the robot
  // 0.043 -> ball radius
  public double getBallDistance() {
    if (camera.getBallElevationAngle() == SimpleCam.UNKNOWN)
      return SimpleCam.UNKNOWN;

    double ballElev = camera.getBallElevationAngle() - headPitch.getPosition() - cameraAngle;
    return (0.51 - 0.043) / Math.tan(-ballElev);
  }
   
  // turn head towards ball if ball position is known
  public void trackBall() {
    final double P = 0.7;
    double ballDirection = camera.getBallDirectionAngle();
    double ballElevation = camera.getBallElevationAngle();

    if (ballDirection == SimpleCam.UNKNOWN) return;

    // compute target head pitch
    double pitch = headPitch.getPosition() - ballElevation * P;

    if (pitch < -0.4 && cameraAngle > 0.0) { // need to switch to TOP camera ?
      //System.out.println("switched to TOP camera");
      selectCamera(Cam.TOP);
      pitch += CAMERA_OFFSET_ANGLE;  // move head down 40 degrees
      headPitch.setPosition(pitch);
      sleepSteps(8);  // allow some time to move head
      camera.processImage();
    }
    else if (pitch > 0.5 && cameraAngle == 0.0) { // need to switch to BOTTOM camera ?
      //System.out.println("switched to BOTTOM camera");
      selectCamera(Cam.BOTTOM);
      pitch -= CAMERA_OFFSET_ANGLE;  // move head up 40 degrees
      headPitch.setPosition(pitch);
      sleepSteps(8);  // allow some time to move head
      camera.processImage();
    }

    headPitch.setPosition(pitch);
    headYaw.setPosition(headYaw.getPosition() - ballDirection * P);
  }

  // update torso LED color according to game state
  public void updateGameControl() {

    // choose goal color according to team's color
    // and display team color on left foot LED
    if (isRed()) {
      camera.setGoalColor(SimpleCam.Goal.SKY_BLUE);
      leftFootLed.set(0xff2222);
      camera.setFootColor(SimpleCam.Foot.RED);
    }
    else if (isBlue()) {
      camera.setGoalColor(SimpleCam.Goal.YELLOW);
      leftFootLed.set(0x2222ff);
      camera.setFootColor(SimpleCam.Foot.BLUE);
    }

    switch (gameControlData.getState()) {
      case RoboCupGameControlData.STATE_INITIAL:
      case RoboCupGameControlData.STATE_FINISHED:
        chestLed.set(0x000000);  // off
        break;
      case RoboCupGameControlData.STATE_READY:
        chestLed.set(0x2222ff);  // blue
        break;
      case RoboCupGameControlData.STATE_SET:
        chestLed.set(0xffff22);  // yellow
        break;
      case RoboCupGameControlData.STATE_PLAYING:
        chestLed.set(0x22ff22);  // green
        break;
    }
  }

  public void readIncomingMessages() {
    while (receiver.getQueueLength() > 0) {
      byte[] data = receiver.getData();

      if (RoboCupGameControlData.hasValidHeader(data)) {
        gameControlData.update(data);
        try{
         inMessage = new String(data, "US-ASCII");
        }
        catch(java.io.UnsupportedEncodingException e){
          System.out.println(e);
      }
       
        //System.out.println(gameControlData);
        updateGameControl();
      }
      // else
      //   System.out.println("readIncomingMessages(): received unexpected message of " + data.length + " bytes");

      receiver.nextPacket();
    }
  }
  
  public String getIncomingMessage(){
      return inMessage;
  }
  public void sendMessageToCop(String message){
      try{
          emitter.send(message.getBytes("US-ASCII"));
      }
      catch(java.io.UnsupportedEncodingException e){
          System.out.println(e);
      }
  }



  // move the robot to a specified position (via a message sent to the Supervisor)
  // [tx ty tz]: the new robot position, alpha: the robot's heading direction
  // For debugging only: this is disabled during the contest rounds
  public void sendMoveRobotMessage(double tx, double ty, double tz, double alpha) {
    String request = "move robot " + playerID + " " + teamID + " " + tx + " " + ty + " " + tz + " " + alpha + "\0";
    try {
      super_emitter.send(request.getBytes("US-ASCII"));
    }
    catch (java.io.UnsupportedEncodingException e) {
      System.out.println(e);
    }
  }

  // move the ball to a specified position (via a message sent to the Supervisor)
  // [tx ty tz]: the new ball position
  // For debugging only: this is disabled during the contest rounds
  public void sendMoveBallMessage(double tx, double ty, double tz) {
    String request = "move ball " + tx + " " + ty + " " + tz + "\0";
    try {
      super_emitter.send(request.getBytes("US-ASCII"));
    }
    catch (java.io.UnsupportedEncodingException e) {
      System.out.println(e);
    }
  }
  
  // overidden method of the Robot baseclass
  // we need to read incoming messages at every step
  @Override
  public int step(int ms) {
    readIncomingMessages();
    return super.step(ms);
  }

  public void runStep()  {
    trackBall();
    step(SIMULATION_STEP);
    camera.processImage();
  }

  public void sleepSteps(int steps) {
    for (int i = 0; i < steps; i++)
      step(SIMULATION_STEP);
  }

   public abstract void run();
}
