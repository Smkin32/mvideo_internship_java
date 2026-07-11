import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeSet;

public class ProductManager {
    // Hashmap для хранения данных о товарах, хранит группы как ключи к HashMap которые хранят товары как ключи к их количеству
    private HashMap<String, HashMap<String, Integer>> groups;

    private Logger logger;

    ProductManager(){
        // Инициализация главной Hashmap для хранения данных о товарах
        groups = new HashMap<>();
    }

    // Прикрепляем логгер
    public void attachLogger(Logger logger){
        this.logger = logger;
    }

    public void add(String groupID, String productID, int quantity){
        groups.putIfAbsent(groupID, new HashMap<>()); // Если группы еще не было создаем ее HashMap
        groups.get(groupID).put(productID, groups.get(groupID).getOrDefault(productID, 0) + quantity);
        tryLogAdd(groupID, productID, quantity);
    }

    public void sell(String groupID, int quantity) {
        int accumulator = quantity; // Кол-во товара для продажи
        Map<String, Integer> productsMap; // HashMap группы товаров

        try {
            productsMap = groups.get(groupID);
            if (productsMap == null || productsMap.isEmpty()){
                // Если у нас нет группы или товаров в этой группе, то выкидываем исключение
                throw new NoSuchElementException("No suitable products found");
            }
        }
        catch (NoSuchElementException e){
            tryLogErr(e.getMessage());
            throw new RuntimeException(e);
        }

        TreeSet<String> sortedProducts = new TreeSet<>(productsMap.keySet());

        for (String productId : sortedProducts){
            // Проходимся по ненулевым товарам и пытаемся продать необходимое количество
            if (productsMap.get(productId) > 0){
                int toRemove;
                // Если можем продать весь товар одного ранга
                if (productsMap.get(productId) > accumulator){
                    // Продаем сколько можем
                    toRemove = accumulator;
                    productsMap.put(productId, productsMap.get(productId) - toRemove);
                    accumulator = 0;
                    tryLogSell(groupID, productId, toRemove);
                }
                // Если товаров одного типа меньше чем нужно продать
                else {
                    // Продаем весь этот товар и идем дальше
                    toRemove = productsMap.get(productId);
                    productsMap.put(productId, 0);
                    accumulator -= toRemove;
                    tryLogSell(groupID, productId, toRemove);
                }

                // Если распродали сколько надо выходим
                if (accumulator == 0){
                    break;
                }
            }
        }

        // Если весь товар нулевой, но необходимое количество не было продано
        // (т.к в прошлом цикле мы гарантированно или продали сколько надо или продали все что было)
        if (accumulator > 0){
            // То берем товар высший по рангу и записываем отрицательное кол-во (которое осталось продать)
            String highestRankProduct = sortedProducts.getFirst();
            productsMap.put(highestRankProduct, -accumulator);
            tryLogSell(groupID, highestRankProduct, accumulator);
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

    private void tryLogAdd(String groupId, String productId, int quantity){
        if (logger != null) {
            logger.logAdd(groupId, productId, quantity);
        }
    }

    private void tryLogSell(String groupId, String productId, int quantity){
        if (logger != null){
            logger.logSell(groupId, productId, quantity);
        }
    }

    private void tryLogErr(String errMsg){
        if (logger != null){
            logger.logErr(errMsg);
        }
    }
}
