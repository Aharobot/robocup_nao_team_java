public class BeMideFild implements Behavior{
    private Player player;
    private boolean suppressed;
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
