/*--------------------------------------------------------
*
*File: AvoidCollision.java 
*Date: March 2, 2012
*
* +++++++++++++++++++++++++++++++++++++++++++++++++++++++
* Description:
*
*/
//import robotics.utilities.*;
import java.lang.Math;
import com.cyberbotics.webots.controller.*;


public class Shooting implements Behavior{
    private FieldPlayer player;
    private boolean suppressed;
    private double ballDist;
    private double ballDir;
    private double goalDir;
    public Shooting(FieldPlayer player){
        this.player = player;   
    }
    public void suppress(){
          //set suppressed to true to disable
            suppressed = true;
            
    }
    
    public boolean takeControl(){
        boolean noDefender = false;
        ballDist = player.getBallDistance();
        ballDir = player.getBallDirection();
        goalDir = player.getGoalDirection();
        //if(player.getOpPlayerDirection() > 0.10|| player.getOpPlayerDirection() < -0.10 || player.getOpPlayerDirection() == SimpleCam.UNKNOWN )
          //  noDefender = true;
        return ballDist < 0.1 && Math.abs(ballDir) < 0.15 && Math.abs(goalDir) < 0.35 && noDefender;
      
    }
    
    @Override
    public void action(){ 
        suppressed = false;
        player.shootGoal();
        player.runStep();
        suppressed = true;
    }         

}

