import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ProductManager {
    HashMap<String, HashMap<String, Integer>> groups;


    ProductManager(){
        groups = new HashMap<>();
    }

    public void add(String groupID, String productID, int quantity){
        groups.putIfAbsent(groupID, new HashMap<>());
        groups.get(groupID).put(productID, groups.get(groupID).getOrDefault(productID, 0) + quantity);
    }

    public void sell(String groupID, int quantity) {
        int accumulator = quantity;
        Map<String, Integer> productsMap = groups.get(groupID);
        TreeSet<String> sortedProducts = new TreeSet<>(productsMap.keySet());

        int countNonZero = 0;
        for (String productID : sortedProducts) {
            if (productsMap.get(productID) > 0) {
                countNonZero++;
            }
        }
        while (accumulator > 0) {
            if (countNonZero == 0) {
                String highestRankProduct = sortedProducts.first();
                productsMap.put(highestRankProduct, productsMap.get(highestRankProduct) - accumulator);
                accumulator = 0;
            } else {
                for (String productID : sortedProducts) {
                    if (accumulator == 0){
                        break;
                    }
                    if (productsMap.get(productID) > 0) {
                        int toRemove = productsMap.get(productID);
                        if (toRemove > accumulator) {
                            toRemove = accumulator;
                        }
                        productsMap.put(productID, productsMap.get(productID) - toRemove);
                        accumulator -= toRemove;
                        countNonZero--;
                    }
                }
            }
        }
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
