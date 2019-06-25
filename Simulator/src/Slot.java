public class Slot {
	static private int lastId = -1;
	private int id;
	private Machine machine;
	public Machine getMachine(){
		return this.machine;
	}
	public Slot(Machine machine){
		this.machine=machine;
		this.id = lastId + 1;
		lastId ++;
	}
	public Machine.Type getCPUType(){
		return this.machine.getCPUType();
	}
}
