import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeSet;

public class ProductManager {
     private HashMap<String, HashMap<String, Integer>> groups;

     private Logger logger;

    ProductManager(){
        groups = new HashMap<>();
    }

    public void attachLogger(Logger logger){
        this.logger = logger;
    }

    public void add(String groupID, String productID, int quantity){
        groups.putIfAbsent(groupID, new HashMap<>());
        groups.get(groupID).put(productID, groups.get(groupID).getOrDefault(productID, 0) + quantity);
        logger.logAdd(groupID, productID, quantity);
    }

    public void sell(String groupID, int quantity) {
        int accumulator = quantity;
        Map<String, Integer> productsMap;

        try {
            productsMap = groups.get(groupID);
            if (productsMap == null || productsMap.isEmpty()){
                throw new NoSuchElementException("No suitable products found");
            }
        }
        catch (NoSuchElementException e){
            logger.logErr(e.getMessage());
            throw new RuntimeException(e);
        }

        TreeSet<String> sortedProducts = new TreeSet<>(productsMap.keySet());

        for (String productId : sortedProducts){
            if (productsMap.get(productId) > 0){
                int toRemove;
                if (productsMap.get(productId) > accumulator){
                    toRemove = accumulator;
                    productsMap.put(productId, productsMap.get(productId) - toRemove);
                    accumulator = 0;
                    logger.logSell(groupID, productId, toRemove);
                }
                else {
                    toRemove = productsMap.get(productId);
                    productsMap.put(productId, 0);
                    accumulator -= toRemove;
                    logger.logSell(groupID, productId, toRemove);
                }

                if (accumulator == 0){
                    break;
                }
            }
        }

        if (accumulator > 0){
            String highestRankProduct = sortedProducts.getFirst();
            productsMap.put(highestRankProduct, -accumulator);
            logger.logSell(groupID, highestRankProduct, accumulator);
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
