import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransferTable {
    private static Map<Object, Map<Object,Double>> transferTable=new HashMap<Object, Map<Object, Double>>();
    public TransferTable(ArrayList<Machine> machines, double data[][]){
        //first index of data:tasks
        //2nd index of data:machines
        for (int i=0;i<machines.size();i++){
            Map<Object,Double> task= new HashMap<Object, Double>();
            for (int j=0;j<machines.size();j++)
                if (i!=j)
                    task.put(machines.get(j).getClass(),data[i][j]);
                else
                    task.put(machines.get(j).getClass(),0.0);
            transferTable.put(machines.get(i).getClass(),task);
        }
    }
    public Map<Object, Map<Object,Double>> getTransferTable(){
        return this.transferTable;
    }
}
