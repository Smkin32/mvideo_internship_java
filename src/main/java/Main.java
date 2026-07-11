import java.io.*;

public class Main {
    static void main(String[] args) {
        if (args.length != 2){
            System.err.println("Invalid args");
            System.exit(1);
        }

        String inputFileName = args[0];
        String outputFileName = args[1];

        try (FileController fileController = new FileController(inputFileName, outputFileName); Logger logger = new Logger()) {
            ProductManager pm = new ProductManager();
            pm.attachLogger(logger);

            String query;
            while ((query = fileController.getQuery()) != null) {
                String[] params = query.split(";");
                try {
                    if (params.length == 2) {
                        pm.sell(params[0], Integer.parseInt(params[1]));
                    } else if (params.length == 3) {
                        pm.add(params[0], params[1], Integer.parseInt(params[2]));
                    } else {
                        throw new RuntimeException("Invalid query");
                    }
                }
                catch (RuntimeException e){
                    logger.logErr(e.getMessage());
                }
            }

            fileController.writeResult(pm.generateCSV());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
