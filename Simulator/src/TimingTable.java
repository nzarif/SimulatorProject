import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TimingTable {
    private static Map<Object, Map<Object,Double>> timingTable=new HashMap<Object, Map<Object, Double>>();
    public TimingTable(ArrayList<Task> tasks, ArrayList<Machine> machines, double data[][]){
        //first index of data:tasks
        //2nd index of data:machines
        for (int i=0;i<tasks.size();i++){
            Map<Object,Double> task= new HashMap<Object, Double>();
            for (int j=0;j<machines.size();j++)
                task.put(machines.get(j).getClass(),data[i][j]);
            timingTable.put(tasks.get(i).getClass(),task);
        }
    }
    public Map<Object, Map<Object,Double>> getProfilingTable(){
        return this.timingTable;
    }
}
