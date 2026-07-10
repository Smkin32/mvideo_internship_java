import java.io.*;

public class Main {
    static void main(String[] args) throws IOException {
        if (args.length != 2){
            System.err.println("Invalid args");
            System.exit(1);
        }

        String inputFileName = args[0];
        String outputFileName = args[1];

        ProductManager pm = new ProductManager();

        File inputFile = new File(inputFileName);
        BufferedReader inputFileReader = new BufferedReader(new FileReader(inputFile));
        String query;
        while ((query = inputFileReader.readLine()) != null){
            String[] params = query.split(" ");
            if (params.length == 2){
                pm.sell(params[0], Integer.parseInt(params[1]));
            } else if (params.length == 3) {
                pm.add(params[0], params[1], Integer.parseInt(params[2]));
            }
            else {

            }
        }

        inputFileReader.close();

        BufferedWriter outputFileWriter = new BufferedWriter(new FileWriter(outputFileName));
        outputFileWriter.write(pm.generateCSV());
        outputFileWriter.close();
    }
}
