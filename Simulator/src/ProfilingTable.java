import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfilingTable {
    private static Map<Object, Map<Object,double[]>> profilingTable=new HashMap<Object, Map<Object, double[]>>();
    public ProfilingTable(ArrayList<Task> tasks, ArrayList<Machine> machines, double[][][] data){
        //first index of data:tasks
        //2nd index of data:machines
        for (int i=0;i<tasks.size();i++){
            Map<Object,double[]> task= new HashMap<Object, double[]>();
            for (int j=0;j<machines.size();j++) {
                task.put(machines.get(j).getClass(), data[j][i]);
            }
            profilingTable.put(tasks.get(i).getClass(),task);
        }
    }
    public Map<Object, Map<Object,double[]>> getProfilingTable(){
        return this.profilingTable;
    }
}
