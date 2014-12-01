/*-------------------------------------------------------
 *File: FindBall.java 
 *Date: April 2, 2012 
 *Author:Ruijiao Li 
 *
 * */
//import robotics.utilities.*;
import com.cyberbotics.webots.controller.*;

public class FindBall implements Behavior{
    private FieldPlayer player;
    private boolean suppressed;
    private double accs[];
    public FindBall(FieldPlayer player){
        this.player = player;
        }

    public void suppress(){
        suppressed = true;
    }

    public boolean takeControl(){
        return player.getBallDirection() == SimpleCam.UNKNOWN; 

    }

    public void action(){
        suppressed = false;
        boolean flag = true;
        player.step(Player.SIMULATION_STEP);
        while(!suppressed){
                accs = player.getAcc();
                player.runStep();
                 while (player.getBallDirection() == SimpleCam.UNKNOWN) {
                     //System.out.println("searching the ball"); 
                    if(player.getBallDirection() != SimpleCam.UNKNOWN) break;
                        player.headScan();
                    if(player.getBallDirection() != SimpleCam.UNKNOWN) break;
                        player.backwards();
                    if(player.getBallDirection() != SimpleCam.UNKNOWN) break;
                        player.headScan();
                     if(player.getBallDirection() != SimpleCam.UNKNOWN) break;
                        player.turnLeft180();
                 }
                 if(player.getBallDirection() != SimpleCam.UNKNOWN) 
                     suppressed = true;           
        }
    }
}
