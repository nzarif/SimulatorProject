
public class Tuple {
	private String key;
	private int value;
	private double processTime;
	private double timeStamp;
	private Task source;
	public Tuple(String key,int value,Task source,double timeStamp){
		this.key=key;
		this.source=source;
		this.value=value;
		this.timeStamp=timeStamp;
	}
	public Tuple(String key,int value,Task source,double timeStamp,double processTime){
		this.key=key;
		this.source=source;
		this.value=value;
		this.timeStamp=timeStamp;
		this.processTime=processTime;
	}
	public void setTime(int time){
		this.timeStamp=time;
	}
	public void setProcessTime(int time){
		this.processTime=time;
	}
	public void setValue(int value){
		this.value=value;
	}
	public void setSource(Task source){
		this.source=source;
	}
	public double getTimeStamp(){
		return this.timeStamp;
	}
	public double getProcessTime(){
		return this.processTime;
	}
	public int getValue(){
		return this.value;
	}
	public Task getSource(){
		return this.source;
	}
	public double addTime(double add){
		processTime+=add;
		return this.processTime;
	}
	public String getKey(){
		return this.key;
	}
	public  void setKey(String key){
		this.key=key;
	}
}
