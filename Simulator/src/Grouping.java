import java.util.concurrent.BlockingQueue;

public abstract class Grouping extends Thread {
	public abstract void run();
	BlockingQueue<Tuple> input;
	BlockingQueue<Tuple>[] outputs;
	public Grouping(BlockingQueue<Tuple> input, BlockingQueue<Tuple>[] outputs){
		this.input=input;
		this.outputs=outputs;
	}
}
