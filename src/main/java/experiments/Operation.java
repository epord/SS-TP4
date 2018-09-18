package experiments;

import java.util.ArrayList;
import java.util.List;

public enum Operation {
    MIN, MAX, MEAN, STD, MEDIAN, STD_LOW, STD_HIGH;
    public static List<Operation> getAll(){
        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(MIN);
        operations.add(MAX);
        operations.add(MEAN);
        operations.add(STD);
        operations.add(MEDIAN);
        operations.add(STD_HIGH);
        operations.add(STD_LOW);
        operations.add(MEDIAN);
        return operations;
    }
}
