import java.util.HashMap;

public class ProductManager {
    HashMap<String, HashMap<String, Integer>> groups;


    ProductManager(){};

    public void add(String groupID, String productID, int quantity){
        groups.putIfAbsent(groupID, new HashMap<>());
        groups.get(groupID).put(productID, groups.get(groupID).getOrDefault(productID, 0) + quantity);
    }

    public void sell(String groupID, int quantity){

    }

    public String generateCSV(){
        String csv;
        StringBuilder sb = new StringBuilder();
        for(String group : groups.keySet()){
            for(String product : groups.get(group).keySet()){
                sb.append(group);
                sb.append(';');
                sb.append(product);
                sb.append(';');
                sb.append(groups.get(group).get(product).toString());
                sb.append('\n');
            }
        }
        csv = sb.toString();
        return csv;
    }
}
