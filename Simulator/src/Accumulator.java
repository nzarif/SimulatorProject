import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class Accumulator extends Task {
    static private int lastId = -1;
    private int id;
    private double averageLatency=0;
    private int countTuple=0;
    private Map<String, Map<Integer,Long>> counter= new HashMap<String, Map<Integer, Long>>();
    public Accumulator(BlockingQueue<Tuple>[] inputs, BlockingQueue<Tuple> output){
        super(inputs, output);
        this.id = lastId + 1;
        lastId ++;
    }
    @Override
    public void run() {
        int time=0;
        double previousTimeStamp=0;
        double sumLatency=0;
        while(true){
            time++;
            for(int i=0;i<inputs.length;i++){
                if(inputs[i].size()>0){
                    String word=inputs[i].peek().getKey();
                    int source=inputs[i].peek().getSource().getID();
                    long count=inputs[i].peek().getValue();
                    Map<Integer, Long> subCounts = counter.get(word);
                    if (subCounts == null) {
                        subCounts = new HashMap<Integer, Long>();
                        counter.put(word, subCounts);
                    }
                    subCounts.put(source, count);
                    int sum = 0;
                    for (Long val : subCounts.values()) {
                        sum += val;
                    }
                    this.inputRate=Math.abs(1/(inputs[i].peek().getTimeStamp()-previousTimeStamp));
                    previousTimeStamp=inputs[i].peek().getTimeStamp();
                    this.setThroughput();
                    emittedTuples++;
                    this.output.add(new Tuple(word,sum,this,inputs[i].peek().getTimeStamp()+time/this.getInputRate(),inputs[i].peek().getProcessTime()+time/this.getInputRate()+this.getExecTime()));
                    sumLatency=inputs[i].peek().getTimeStamp()+time/this.getInputRate()+this.getExecTime();
                    countTuple++;
                    averageLatency=sumLatency/countTuple;
                    //System.out.printf("This Is:"+inputs[i].peek().getTimeStamp()+time/this.getInputRate()+this.getExecTime()+"\n");
                    if(inputs[i].peek().getTimeStamp()+time/this.getInputRate()+this.getExecTime()>this.maxLatency){
                       // System.out.println("Here");
                        this.maxLatency=inputs[i].peek().getTimeStamp()+time/this.getInputRate()+this.getExecTime();
                    }
                    avgLatency=(avgLatency*(emittedTuples-1)+inputs[i].peek().getTimeStamp()+time/this.getInputRate())/emittedTuples;
                    inputs[i].remove();
                    //System.out.printf("%s:%d\n",word,sum);
                }
                for (String name: counter.keySet()){
                    String key = name.toString();
                    for(Integer number: counter.get(name).keySet()) {
                        String value = counter.get(name).get(number).toString();
                       // System.out.println(key + " " + value);
                    }
                  //  System.out.printf("throughput"+this.getThroughput()+"\n");
                  //  System.out.printf("latency"+this.getMaxLatency()+"\n");
                }
            }
            //System.out.println(this.maxLatency);
            //System.out.println(this.avgLatency);
            //System.out.println(this.getThroughput());
            //System.out.println(this.emittedTuples);
            //System.out.println("--------------------------------");

        }
    }

    @Override
    public int getID() {
        return id;
    }
    @Override
    public double getInputRate(){
        return this.inputRate;
    }
}
