/*-------------------------------------------------------
 *File: TowardsBall.java 
 *Date: April 2, 2012 
 *Author:Ruijiao Li 
 *
 * */
import java.lang.Math;
//import robotics.utilities.*;
import com.cyberbotics.webots.controller.*;

public class TowardsBall implements Behavior{
    private FieldPlayer player;
    private boolean suppressed;
    public TowardsBall(FieldPlayer player){
        this.player = player;
    }

    public void suppress(){
        suppressed = true;
    }

    public boolean takeControl(){
        return player.getBallDistance() >= 0.6 && player.getBallDistance() < 2 && player.getBallDirection() != SimpleCam.UNKNOWN;
    }

    public void action(){
        suppressed = false;
        while(!suppressed){
            try{
                player.runStep();
                double ballDir = player.getBallDirection();
                double ballDist = player.getBallDistance();
                double goalDir = player.getGoalDirection();
                double coPlayerDir = player.getCoPlayerDirection();
                double coPlayerDist = player.getCoPlayerDistance();
                double opPlayerDir = player.getOpPlayerDirection();
                double opPlayerDist = player.getOpPlayerDistance();
                double disOpCop = 0;
                double disBallOp = 0;
                double disBallCop = 0;
                double angBallCop = player.normalizeAngle(Math.abs(ballDir - coPlayerDir));
                double angBallOp = player.normalizeAngle(Math.abs(ballDir - opPlayerDir));
                double angOpCop = player.normalizeAngle(Math.abs(opPlayerDir - coPlayerDir));
                disBallCop = Math.sqrt(Math.pow(ballDist, 2) + Math.pow(coPlayerDist, 2) - 2*ballDist*coPlayerDist*Math.cos(angBallCop));
                disBallOp = Math.sqrt(Math.pow(ballDist, 2) + Math.pow(opPlayerDist, 2) - 2*ballDist*opPlayerDist*Math.cos(angBallOp));
                disOpCop = Math.sqrt(Math.pow(coPlayerDist, 2) + Math.pow(opPlayerDist, 2) - 2*coPlayerDist*opPlayerDist*Math.cos(angOpCop));
                //Towards and possess the 
                double [] baccs = player.getAcc();
                if (baccs[2] < 5.0){
                    suppressed = true;
                }
                if(ballDist <= disBallCop)
                    player.turnBodyRel(ballDir);
                    player.forwards();
                if(coPlayerDir == SimpleCam.UNKNOWN || opPlayerDist == SimpleCam.UNKNOWN){
                    player.turnBodyRel(ballDir);
                    player.forwards50();
                }
                //cooperation
                if(ballDist > disBallCop ){
                    if(coPlayerDist < 0.5&& coPlayerDist > 0.2){
                    	 player.sleepSteps(200);
                         player.headScan();
                         player.turnBodyRel(ballDir);
                         player.forwards();
              
                    }
                    if(coPlayerDist < 0.2){
                      player.backwards();
                      player.headScan();
                    }
                    	
                     if(coPlayerDist > 0.5)
                         player.turnBodyRel(ballDir);
                         player.forwards();
                         player.headScan();
                }
                else{
                    player.sleepSteps(40);
                    player.runStep();
                    if(player.getBallDirection() == SimpleCam.UNKNOWN)
                      suppressed = true;
                }
          
                                      
            }
            catch(Exception e){;}

        }
    }
}


