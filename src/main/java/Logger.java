import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger implements AutoCloseable{
    private BufferedWriter loggerWriter;

    Logger() throws IOException {
        loggerWriter = new BufferedWriter(new FileWriter(new File(System.currentTimeMillis() + ".log")));
    }

    public void logAdd(String groupID, String productID, int quantity){
        String msg = "ADD :" + " Group-"+ groupID + " Product-"+ productID + " Quantity-" + String.valueOf(quantity);
        log(msg);
    }

    public void logSell(String groupID, String productID, int quantity){
        String msg = "SELL :" + " Group-"+ groupID + " Product-"+ productID + " Quantity-" + String.valueOf(quantity);
        log(msg);
    }

    public void logErr(String errMsg){
        String msg = "ERROR :" + errMsg + '\n';
        log(msg);
    }

    private void log(String msg) {
        try {
            loggerWriter.write(System.currentTimeMillis() + "-" + msg + '\n');
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() throws IOException {
        loggerWriter.close();
    }
}
