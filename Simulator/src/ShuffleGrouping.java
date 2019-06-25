import java.util.concurrent.BlockingQueue;

public class ShuffleGrouping extends Grouping {
    public ShuffleGrouping(BlockingQueue<Tuple> input, BlockingQueue<Tuple>[] outputs){
        super(input,outputs);
    }

    @Override
    public void run() {
        int counter=0;
        while(true){
            if(input.size()>0){
                outputs[counter%outputs.length].add(input.peek());
                counter++;
            }
        }
    }

}
