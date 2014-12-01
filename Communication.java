import java.nio.ByteBuffer;

public class Communication{
    public static final String SENDCOP = "come";
    public static final String BALLFIND = "find";
    public static final String DEFEND = "defend";
    private byte receivedMessage;
    public Communication(){
    }

    public void readBytes(ByteBuffer buffer){
        receivedMessage = buffer.get();
    }

    public byte getMessage(){
        return receivedMessage;
    }

    @Override public String toString(){
        StringBuilder rec = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");
        rec.append("receivedMessage" + receivedMessage);
        return rec.toString();
    }
    
}
