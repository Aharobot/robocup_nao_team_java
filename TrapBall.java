/*-------------------------------------------------------
 *File: TrapBall.java 
 *Date: April 2, 2012 
 *Author:Ruijiao Li 
 *
 * */
//import robotics.utilities.*;
import com.cyberbotics.webots.controller.*;

public class TrapBall implements Behavior{
    private FieldPlayer player;
    private boolean suppressed;
    public TrapBall(FieldPlayer player){
        this.player = player;
    }

    public void suppress(){
        suppressed = true;
    }

    public boolean takeControl(){
        return player.getBallDirection() < 3.0&& player.getBallDirection() != SimpleCam.UNKNOWN;

    }

    public void action(){
        suppressed = false;
        while(!suppressed){
                player.runStep();
               // double ballDir = player.getBallDirection();
                //double ballDist = player.getBallDistance();
                //double goalDir = player.getGoalDirection();
                if(player.getBallDistance() < 0.3){
                    if(player.getBallDistance() < -0.15){
                    
                        player.sideStepLeft();
                        player.headScan();
                    }    
                    else if(player.getBallDirection() > 0.15){
                        player.sideStepRight();
                        player.headScan();
                    }    
                    else if(player.getGoalDirection() < -0.35){
                        player.turnLeft40();
                        player.headScan();
                    }    
                    else if(player.getGoalDirection() > 0.35){
                        player.turnRight40();
                        player.headScan();
                    }    
                    else{
                        player.forwards();
                        player.headScan();
                        //suppressed = true;
                      }  
                }
                else{
                    double goDir = player.normalizeAngle(player.getBallDirection() - player.getGoalDirection());
                    if(goDir < player.getBallDirection() - 0.5)
                        goDir = player.getBallDirection() - 0.5;
                    else if(goDir > player.getBallDirection() + 0.5)
                        goDir = player.getBallDirection() + 0.5;
                    goDir = player.normalizeAngle(goDir);
                    player.turnBodyRel(goDir);
                    //if(player.getBallDistance() < 0.6)
                        player.forwards();
                   // else if(player.getBallDistance() > 0.6)
                        player.headScan();
                        //suppressed = true;
                        
                }
                if (player.getBallDirection() == SimpleCam.UNKNOWN){
                   suppressed = true;
                }
        }
    }
}

