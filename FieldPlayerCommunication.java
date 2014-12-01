/**
 *This is a simple communication between robots. 
 *Robot analyse the context and ask for cooperating. 
 *This function can be improved by invoke Bayesian method.
 *
 *
 * */

public class FieldPlayerAsk implements Behavior{
    private FieldPlayer player;
    private Communication comm;
    private boolean suppressed;
    public FieldPlayerAsk(FieldPlayer player){
        this.player = player;
        comm = new Communication();
    }

    public void suppress(){
        suppressed = true;

    
    }

    public boolean takeControl(){
       return player.getBallDistance() > 0.6 && player.getOpPlayerDistance() < 0.6 && (player.getCoPlayerDistance() > 0.6 || player.getCoPlayerDirection() == SimpleCam.UNKNOWN) 
    
    }

    public void action(){
        suppressed = false;
        player.sendMessageToCop(comm.SENDCOP);
    }
}
