/*--------------------------------------------------------
*
*File: StandUp.java 
*Date: March 2, 2012
*
* +++++++++++++++++++++++++++++++++++++++++++++++++++++++
* Description:
*
*/
//import robotics.utilities.*;
import com.cyberbotics.webots.controller.*;

public class StandUp implements Behavior{
    private Player player;
    private boolean suppressed;
    private double[] accs;
    public StandUp(Player player){
        this.player = player;
        
    }
    public void suppress(){
            suppressed = true;
        
    }
    
    public boolean takeControl(){
       accs = player.getAcc();
       return accs[2] < 6.0;
    }
    
    @Override
    public void action(){ 
        suppressed = false;
        accs = player.getAcc();
        while(accs[2] < 5.0){
            if (accs[0] < - 4.0)
                player.standUpFromFront();
            if (accs[0] >  4.0)
                player.standUpFromBack();
            if (accs[1] < - 4.0)
                player.returnFromSide();
            if (accs[1] > 4.0)
                player.returnFromSide();
            player.runStep();
        }    
    }    
}

