import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class ProductManagerTest {

    private ProductManager createManager() {
        return new ProductManager();
    }

    @Test
    void testAddNewProduct() {
        ProductManager pm = createManager();
        pm.add("G1", "P1", 10);
        String csv = pm.generateCSV();
        assertEquals("G1;P1;10\n", csv);
    }

    @Test
    void testAddExistingProduct() {
        ProductManager pm = createManager();
        pm.add("G1", "P1", 5);
        pm.add("G1", "P1", 7);
        String csv = pm.generateCSV();
        assertEquals("G1;P1;12\n", csv);
    }

    @Test
    void testAddDifferentProductsSameGroup() {
        ProductManager pm = createManager();
        pm.add("G1", "P1", 3);
        pm.add("G1", "P2", 4);
        String csv = pm.generateCSV();
        Set<String> expected = new HashSet<>(Arrays.asList("G1;P1;3", "G1;P2;4"));
        Set<String> actual = new HashSet<>(Arrays.asList(csv.split("\n")));
        assertEquals(expected, actual);
    }

    @Test
    void testSellEnoughFromOneProduct() {
        ProductManager pm = createManager();
        pm.add("G1", "P1", 10);
        pm.sell("G1", 5);
        String csv = pm.generateCSV();
        assertEquals("G1;P1;5\n", csv);
    }

    @Test
    void testSellFromMultipleProductsSorted() {
        ProductManager pm = createManager();
        pm.add("G1", "B", 5);
        pm.add("G1", "A", 3);
        pm.add("G1", "C", 2);
        pm.sell("G1", 6);
        Map<String, Integer> expected = new HashMap<>();
        expected.put("A", 0);
        expected.put("B", 2);
        expected.put("C", 2);
        String csv = pm.generateCSV();
        Map<String, Map<String, Integer>> parsed = parseCSV(csv);
        assertEquals(expected, parsed.get("G1"));
    }

    @Test
    void testSellWithDeficit() {
        ProductManager pm = createManager();
        pm.add("G1", "A", 5);
        pm.add("G1", "B", 3);
        pm.sell("G1", 10);
        Map<String, Integer> expected = new HashMap<>();
        expected.put("A", -2);
        expected.put("B", 0);
        String csv = pm.generateCSV();
        Map<String, Map<String, Integer>> parsed = parseCSV(csv);
        assertEquals(expected, parsed.get("G1"));
    }

    @Test
    void testSellWithDeficitOnlyOneProduct() {
        ProductManager pm = createManager();
        pm.add("G1", "X", 3);
        pm.sell("G1", 7);
        String csv = pm.generateCSV();
        assertEquals("G1;X;-4\n", csv);
    }

    @Test
    void testSellNonexistentGroupThrowsException() {
        ProductManager pm = createManager();
        assertThrows(RuntimeException.class, () -> pm.sell("G1", 5));
    }

    @Test
    void testGenerateCSVMultipleGroups() {
        ProductManager pm = createManager();
        pm.add("G1", "A", 1);
        pm.add("G2", "B", 2);
        String csv = pm.generateCSV();
        Set<String> lines = new HashSet<>(Arrays.asList(csv.split("\n")));
        Set<String> expected = new HashSet<>(Arrays.asList("G1;A;1", "G2;B;2"));
        assertEquals(expected, lines);
    }

    private Map<String, Map<String, Integer>> parseCSV(String csv) {
        Map<String, Map<String, Integer>> result = new HashMap<>();
        if (csv.isEmpty()) return result;
        String[] lines = csv.split("\n");
        for (String line : lines) {
            if (line.isEmpty()) continue;
            String[] parts = line.split(";");
            if (parts.length != 3) continue;
            String group = parts[0];
            String product = parts[1];
            int quantity = Integer.parseInt(parts[2]);
            result.computeIfAbsent(group, k -> new HashMap<>()).put(product, quantity);
        }
        return result;
    }
}
