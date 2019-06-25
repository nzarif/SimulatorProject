
public class Executer {
	private Slot slot;
	public Executer(Slot slot){
		this.slot=slot;
	}
	public Machine.Type getCPUType(){
		return this.slot.getMachine().getCPUType();
	}
	public Slot getSlot(){
		return this.slot;
	}
	public void setSlot(Slot slot){
		this.slot=slot;
	}
}
