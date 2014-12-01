/*-------------------------------------------------------
 *File: PlayerStep.java 
 *Date: April 2, 2012 
 *Author:Ruijiao Li 
 *
 * */

import java.lang.Math;
import com.cyberbotics.webots.controller.*;

public class PlayerStep implements Behavior{
    private FieldPlayer player;
    private boolean suppressed;
    public PlayerStep(FieldPlayer player){
        this.player = player;
    }

    public void suppress(){
        suppressed = true;
    }

    public boolean takeControl(){
        return true;
    }

    public void action(){
       
        suppressed = false;
        player.step(player.SIMULATION_STEP);
            player.runStep();
            //player.headScan();
            suppressed = true;
    }
    
}

