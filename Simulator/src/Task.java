import java.util.concurrent.BlockingQueue;

public abstract class Task extends Thread {
	private Executer executer;
	private double execTime;
	public double inputRate;
	public double maxLatency;
	public double avgLatency;
	public BlockingQueue<Tuple>[] inputs;
	public BlockingQueue<Tuple> input;
	public BlockingQueue<Tuple> output;
	public int emittedTuples=0;
	private double throughput;
	public Task(BlockingQueue<Tuple>[] inputs,BlockingQueue<Tuple> output){
		this.inputs=inputs;
		this.output=output;
	}
	public Task(BlockingQueue<Tuple> output){
		this.output=output;
	}
	public Task(BlockingQueue<Tuple> input, BlockingQueue<Tuple> output){
		this.output=output;
		this.input=input;
		this.maxLatency=0;
		this.avgLatency=0;
	}

	public void setExecuter(Executer executer) {
		this.executer = executer;
	}

	public Executer getExecuter(){
		return this.executer;
	}
	public void setExecTime(){
		Executer executer=this.executer;
		Machine.Type CPUType=executer.getCPUType();
		Machine machine=executer.getSlot().getMachine();
		this.execTime=Main.profilingTable.getProfilingTable().get(this.getClass()).get(machine.getClass())[2];
		//System.out.printf("time:%f\n",execTime);
		double freeUtilization=machine.getFreeUtilization();
		if(freeUtilization-machine.getSlotsNumber()*Main.profilingTable.getProfilingTable().get(this.getClass()).get(machine.getClass())[3]>=0)
			machine.reduceFreeUtilization(machine.getSlotsNumber()*Main.profilingTable.getProfilingTable().get(this.getClass()).get(machine.getClass())[3]);
	}
	public double getExecTime(){
		return this.execTime;
	}
	public abstract void run();
	public abstract int getID();
	public double timeElapsed(Tuple t){
		//t.addTime(execTime);
		//System.out.printf("time added:%f\n",t.getProcessTime());
		return t.getProcessTime();
	}
	public abstract double getInputRate();
	public void setInputRate(double inputRate){
		this.inputRate=inputRate;
		this.setThroughput();
	}
	public void setThroughput(){
		this.throughput=Main.tupleSize*this.inputRate;
	}
	public double getThroughput(){
		return this.throughput;
	}
	public double getMaxLatency(){
		return this.maxLatency;
	}
	public double getAvgLatency() {return this.avgLatency;}
}