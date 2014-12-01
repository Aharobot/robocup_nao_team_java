import com.cyberbotics.webots.controller.*;
import java.util.Random;
public class BeBackField implements Behavior{
    private FieldPlayer player;
    private boolean suppressed;
    private Random ram;

    public BeBackField(FieldPlayer player){
        this.player = player;
        ram = new Random();
    }

    public void suppress(){
        suppressed = true;
    }

    public boolean takeControl(){
        return (player.getBallDirection() == SimpleCam.UNKNOWN && player.getGoalDirection() ==  SimpleCam.UNKNOWN) || player.getBallDistance() > 1.8 || player.getOurGoalDirection() == SimpleCam.UNKNOWN;
    }

    public void action(){
        suppressed = false;
        double decission = 2*ram.nextDouble() - 1;
        if(decission < 0.6 && player.getLineDistance() < 1.0){
            player.forwards50();
            suppressed = true;
        }

        if(decission < 0.9 && decission > 0.7){
            player.turnLeft180();
            suppressed = true;
        }

        if(decission < 1 && decission > 0.9 && player.getGoalDirection() != SimpleCam.UNKNOWN){
            player.turnRight60();
            player.headScan();
            suppressed = true;

        }
     }

}
