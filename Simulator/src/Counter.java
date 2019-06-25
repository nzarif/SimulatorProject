import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

public class Counter extends Task {
	static public int lastId = -1;
	public int id;
	public int time;
	HashMap<String,Integer> counter=new HashMap<String,Integer>();

	public Counter(BlockingQueue<Tuple> input, BlockingQueue<Tuple> output){
		super(input,output);
		this.input=input;
		this.id = lastId + 1;
		lastId ++;
		time = 0;
	}
	@Override
	public void run() {
		double previousTimeStamp=0;
		while(true){
			time++;
			//System.out.println(input.size());
			if(input.size()>0){
				//System.out.println("here");
				if(counter.containsKey(input.peek().getKey())){
					counter.put(input.peek().getKey(),counter.get(input.peek().getKey())+1);
				}
				else{
					counter.put(input.peek().getKey(),1);
				}
				this.inputRate=1/(input.peek().getTimeStamp()-previousTimeStamp);
				previousTimeStamp=input.peek().getTimeStamp();
				this.setThroughput();
				emittedTuples++;
				this.output.add(new Tuple(input.peek().getKey(),counter.get(input.peek().getKey()),this,input.peek().getTimeStamp()+time/this.getInputRate(),input.peek().getProcessTime()+this.getExecTime()));
				if(input.peek().getTimeStamp()+time/this.getInputRate()>this.maxLatency){
					this.maxLatency=input.peek().getTimeStamp()+time/this.getInputRate();
				}
				this.avgLatency=(this.avgLatency*(emittedTuples-1)+input.peek().getTimeStamp()+time/this.getInputRate())/emittedTuples;
				if(counter.containsKey("hi") || counter.containsKey("this")){
					System.out.println(counter);
				}
				this.timeElapsed(input.peek());
				//System.out.println(this.maxLatency);
				//System.out.println(this.avgLatency);
				//System.out.println(this.getThroughput());
				//System.out.println(this.emittedTuples);
				//System.out.println("--------------------------------");
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
