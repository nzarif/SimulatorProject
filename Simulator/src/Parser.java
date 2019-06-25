import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;


public class Parser extends Task{
	static public int lastId = -1;
	public int id;
	int time=0;
	public BlockingQueue<Tuple> queue;
	public Parser(BlockingQueue<Tuple> output){
		super(output);
		queue=output;
		this.id = lastId + 1;
		lastId ++;
	}
	
	public void run(){
		File file = new File("input.txt");
		BufferedReader br = null;
		double maxLatency=0;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String st;
		  try {
			while ((st = br.readLine()) != null){
				//System.out.println(st);
				//System.out.println(queue.size());
			         try {
			         	emittedTuples++;
						queue.put(new Tuple(st,1,this,time/this.getInputRate(),this.getExecTime()));
						if(time/this.getInputRate()>maxLatency){
							maxLatency=time/this.getInputRate();
						}
						this.avgLatency=(this.avgLatency*(emittedTuples-1)+time/this.getInputRate())/emittedTuples;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				//this.timeElapsed(queue.peek());
				time++;
			    this.maxLatency=time/this.inputRate;
				//System.out.println(this.maxLatency);
				//System.out.println(this.avgLatency);
				//System.out.println(this.getThroughput());
				//System.out.println(this.time);
				//System.out.println(this.emittedTuples);
				//System.out.println("--------------------------------");
			  }
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public int getID(){
		return this.id;
	}

	@Override
	public double getInputRate() {
		return this.inputRate; //bytes per second
	}
}
