import java.util.concurrent.BlockingQueue;

public class RangeFilter extends Task {
    static private int lastId = -1;
    private int id;
    public RangeFilter(BlockingQueue<Tuple> input, BlockingQueue<Tuple> output){
        super(input,output);
        this.id = lastId + 1;
        lastId ++;
    }
    @Override
    public void run() {
        double time=0;
        String st=new String();
        while(true){
            time++;
            if(input.size()>0){
                st=input.peek().getKey();
                if(st.startsWith("a"))
                   // output.put(new Tuple(st,1,this, input.peek().getTimeStamp()+time/this.getInputRate(),input.peek().getProcessTime()+this.getExecTime()));
                input.remove();
            }
        }
    }

    @Override
    public int getID() {
        return this.id;
    }

    @Override
    public double getInputRate() {
        return this.inputRate;
    }
}
