import java.util.ArrayList;

public class Machine {
	private double freeUtilization;
	ArrayList<Slot> slots=new ArrayList<Slot>();
	public enum Type{
		MIPS
		//TODO
	}
	private Type CPUType;
	public Machine(Type CPUType,int slotsNumber){
		this.CPUType=CPUType;
		this.freeUtilization=100*slotsNumber;
		for(int i=0;i<slotsNumber;i++){
			slots.add(new Slot(this));
		}
	}
	public ArrayList<Slot> getSlots(){
		return this.slots;
	}
	public Type getCPUType(){
		return this.CPUType;
	}
	public int getSlotsNumber(){
		return this.slots.size();
	}
	public double getFreeUtilization(){
		return this.freeUtilization;
	}
	public double reduceFreeUtilization(double reduction){
		this.freeUtilization-=reduction;
		return this.freeUtilization;
	}
}
