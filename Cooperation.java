/**
 *
 *
 * */
public class Cooperation implements Behavior{
    private FieldPlayer player;
    private boolean suppressed;
    private Communication comm;
    
    public Cooperation(FieldPlayer player){
        this.player = player;
    } 

    public void suppress(){
        suppressed = true;
    }

    public boolean takeControl(){
        return player.getIncomingMessage() == comm.SENDCOP;
    
    }

    public void action(){
        suppressed = false;
        if(player.getBallDirection() == SimpleCam.UNKNOWN){
            if(player.getOurGoalDirection() == SimpleCam.UNKNOWN){
                player.forwards();
            }
            else
                player.turnLeft180();
                player.forwards();
        }          
    }
}
