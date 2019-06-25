import java.util.concurrent.BlockingQueue;


public class KeyGrouping extends Grouping {
	public KeyGrouping(BlockingQueue<Tuple> input, BlockingQueue<Tuple>[] outputs){
		super(input,outputs);
	}
	@Override
	public void run() {
		int a;
		while(true){
			synchronized (input){
			    if(input.size()>0){
				    a=Character.toLowerCase(input.peek().getKey().charAt(0))-'a';
				    synchronized (outputs){
				        int bus_ind = a / (26/outputs.length);
			    	    outputs[bus_ind].add(input.peek());
				    }
				    input.remove();
			}
			}
		}
	}
}
