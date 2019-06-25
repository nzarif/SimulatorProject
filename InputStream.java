import java.util.concurrent.BlockingQueue;

public class InputStream {
    private BlockingQueue<Tuple> input1;
    private BlockingQueue<Tuple> input2;
    private BlockingQueue<Tuple> input3;
    int size=1;
    public InputStream(BlockingQueue<Tuple> input1){
        this.input1=input1;
    }
    public InputStream(BlockingQueue<Tuple> input1,BlockingQueue<Tuple> input2){
        this.input1=input1;
        this.input2=input2;
        this.size=2;
    }
    public InputStream(BlockingQueue<Tuple> input1,BlockingQueue<Tuple> input2,BlockingQueue<Tuple> input3){
        this.input1=input1;
        this.input2=input2;
        this.input3=input3;
        this.size=3;
    }

    public int getSize() {
        return this.size;
    }

    public BlockingQueue<Tuple> get(int i) {
        if (i==1)
            return input1;
        else if(i==2)
            return input2;
        else
            return input3;

    }
}
