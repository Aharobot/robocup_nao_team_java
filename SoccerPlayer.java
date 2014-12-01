//---------------------------------------------------------------------------------------
//  File:         SoccerPlayer.java (to be used in a Webots java controllers)
//  Date:         April 30, 2008
//  Description:  This is a bootstrap for the Java controller example of Robotstadium
//                It selects and runs the correct player type: FieldPlayer or GoalKeeper
//  Project:      Robotstadium, the online robot soccer competition
//  Author:       Yvan Bourquin - www.cyberbotics.com
//  Changes:      November 4, 2008: Adapted to Webots 6
//                May 3, 2010: Changed how teamID and playerID are determined
//---------------------------------------------------------------------------------------
//import robotics.utilities.*;
public class SoccerPlayer {

  public static void main(String[] args) throws Exception {
    
    // get team and player id's from controllerArgs
    int playerID = Integer.parseInt(args[0]);
    int teamID   = Integer.parseInt(args[1]);
    FieldPlayer striker;
    FieldPlayer midFielder;
    FieldPlayer backPlayer;
    
    Behavior psp_1;
    Behavior sup_1;
    Behavior acn_1;
    Behavior aole_1;
    Behavior shoot_1;
    Behavior fbl_1;
    Behavior tbl_1;
    Behavior tsbl_1;
    Behavior ask_1;
    Behavior coop_1;
  
    Behavior psp_2;
    Behavior sup_2;
    Behavior acn_2;
    Behavior aole_2;
    Behavior shoot_2;
    Behavior fbl_2;
    Behavior tbl_2;
    Behavior tsbl_2;
   
    
    Behavior psp_3;
    Behavior sup_3;
    Behavior acn_3;
    Behavior aole_3;
    Behavior shoot_3;
    Behavior fbl_3;
    Behavior tbl_3;
    Behavior tsbl_3;
    Behavior beBack;
    Behavior coop_3;
 

    // choose GoalKepper/FieldPlayer role according to playerID
    if (playerID == 0)
      new GoalKeeper(playerID, teamID).run();
    if (playerID == 1){
    	 striker = new FieldPlayer(playerID, teamID);
    	 psp_1 = new PlayerStep(striker);
         sup_1 = new StandUp(striker);
    	 acn_1 = new AvoidCollision(striker);
    	 aole_1 = new AvoidOutLine(striker);
    	 shoot_1 = new Shooting(striker);
    	 fbl_1 = new FindBall(striker);
    	 tbl_1 = new TrapBall(striker);
    	 tsbl_1 = new TowardsBall(striker);
         ask_1 = new FieldPlayerAsk(striker);         
         coop_1 = new Cooperation(striker);
         //Behavior [] aArray = {fbl_1,tsbl_1,tbl_1, sup_1, psp_1/* /tbl_1*, shoot_1,aole_1, acn_1,*/};
         Behavior [] aArray = {psp_1,ask_1,fbl_1,tbl_1,tsbl_1, shoot_1,coop_1,acn_1,sup_1};
         Arbitrator arbya = new Arbitrator(aArray);
         striker.step(striker.SIMULATION_STEP);
         arbya.start();
    	}
    if (playerID == 2){
         midFielder = new FieldPlayer(playerID, teamID);
         psp_2 = new PlayerStep(midFielder);
         sup_2 = new StandUp(midFielder);
	     acn_2 = new AvoidCollision(midFielder);
	     aole_2 = new AvoidOutLine(midFielder);
	     shoot_2 = new Shooting(midFielder);
         fbl_2 = new FindBall(midFielder);
	     tbl_2 = new TrapBall(midFielder);
	     tsbl_2 = new TowardsBall(midFielder);
         Behavior [] bArray = {psp_2,fbl_2,tbl_2,tsbl_2,shoot_2,acn_2,sup_2};//, tbl_2, shoot_2, aole_2, acn_2, psp_2, sup_2};
         Arbitrator arbyb = new Arbitrator(bArray);
         midFielder.step(midFielder.SIMULATION_STEP);
         arbyb.start();
	    
    }
    
    if (playerID == 3){
    	 backPlayer = new FieldPlayer(playerID, teamID);
	     psp_3 = new PlayerStep(backPlayer);
	     sup_3 = new StandUp(backPlayer);
	     acn_3 = new AvoidCollision(backPlayer);
	     aole_3 = new AvoidOutLine(backPlayer);
	     shoot_3 = new Shooting(backPlayer);
	     fbl_3 = new FindBall(backPlayer);
	     tbl_3 = new TrapBall(backPlayer);
	     tsbl_3 = new TowardsBall(backPlayer);
         coop_3 = new Cooperation(backPlayer);
         beBack = new BeBackField(backPlayer);
         Behavior [] cArray = {psp_3,fbl_3,tbl_3,tsbl_3,shoot_3,beBack,coop_3, acn_3, sup_3};
         Arbitrator arbyc = new Arbitrator(cArray);
         backPlayer.step(backPlayer.SIMULATION_STEP);
         arbyc.start();
    }
	 
  }
}
