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


public class AvoidOutLine implements Behavior{
    private FieldPlayer player;
    private boolean suppressed;
    private boolean nearline = false;
    
    public AvoidOutLine(FieldPlayer player){
        this.player = player;   
    }
    public void suppress(){
          //set suppressed to true to disable
            suppressed = true;
        
    }
    
    public boolean takeControl(){
       if(player.getLineDistance() < 0.1) {
           if((player.getGoalDirection() > 1.0 && player.getGoalDirection() < 1.6) || (player.getGoalDirection() > -1.6 && player.getGoalDirection() < -1.0))
               nearline = true;

           if((player.getOurGoalDirection() > 1.0 && player.getOurGoalDirection() < 1.6) || (player.getOurGoalDirection() > - 1.6 && player.getOurGoalDirection() < -1))
               nearline = true;
       } 
       else 
           nearline = false;
       return  nearline;

    }
    
    @Override
    public void action(){ 
        suppressed = false;
        player.turnLeft180();
        player.headScan();
        suppressed = true;
    }
         
}

