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

public class AvoidCollision implements Behavior{
    private FieldPlayer player;
    private boolean suppressed;
    
    public AvoidCollision(FieldPlayer player){
        this.player = player;   
    }
    public void suppress(){
            suppressed = true;
        
    }
    
    public boolean takeControl(){
       return player.getPlayerDistance() < 0.10;

    }
    @Override
    public void action(){ 
        suppressed = false;
        player.sleepSteps(200);
        if(player.getPlayerDistance() < 0.10)
        player.turnBodyRel(0.8);
        else
        suppressed = true;
    }
	
}         

