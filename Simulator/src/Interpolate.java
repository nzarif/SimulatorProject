import java.util.concurrent.BlockingQueue;

public class Interpolate extends Task {
    static private int lastId = -1;
    private int id;

    public Interpolate(BlockingQueue<Tuple> input, BlockingQueue<Tuple> output){
        super(input, output);
        this.id = lastId + 1;
        lastId ++;
    }
    @Override
    public void run() {

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
