public class BeStriker implements Behavior{
    private Player player;
    private boolean suppressed;
    public StandUp(Player player){
        this.player = player;
        
    }
    public void suppress(){
            suppressed = true;
        
    }
    
    public boolean takeControl(){
    }
    
    public void action(){ 
        suppressed = false;
