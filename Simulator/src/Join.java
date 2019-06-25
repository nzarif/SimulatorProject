import java.util.concurrent.BlockingQueue;

public class Join extends Task {
    static private int lastId = -1;
    private int id;
    public BlockingQueue<Tuple> first;
    public BlockingQueue<Tuple> second;
    public Join(BlockingQueue<Tuple>[] inputs, BlockingQueue<Tuple> output){
        super(inputs,output);
        this.first=inputs[0];
        this.second=inputs[1];
        this.id = lastId + 1;
        lastId ++;
    }

    @Override
    public void run() {
        String st=new String();
        int time=0;
        double maxProcessTime=0;
        double maxTimeStamp=0;
        while(true){
            time++;
            if(first.size()>0 && second.size()>0){
                st=first.peek().getKey();
                st=st+second.peek().getKey();
                if(first.peek().getProcessTime()>second.peek().getProcessTime())
                    maxProcessTime=first.peek().getProcessTime();
                else
                    maxProcessTime=second.peek().getProcessTime();
                if(first.peek().getTimeStamp()>second.peek().getTimeStamp())
                    maxTimeStamp=first.peek().getTimeStamp();
                else
                    maxTimeStamp=second.peek().getTimeStamp();
                try {
                    output.put(new Tuple(st,1,this,maxTimeStamp+time/this.getInputRate(),maxProcessTime+this.getExecTime()));
                } catch (InterruptedException e) {
                    System.out.println("Could not join");
                }
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
