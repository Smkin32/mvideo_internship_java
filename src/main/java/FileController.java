import java.io.*;

public class FileController implements AutoCloseable {
    BufferedReader inputFileReader;
    BufferedWriter outputFileWriter;

    FileController(String inputFileName, String outputFileName) throws IOException {
        inputFileReader = new BufferedReader(new FileReader(new File(inputFileName)));
        outputFileWriter = new BufferedWriter(new FileWriter(new File(outputFileName)));
    }

    public String getQuery() throws IOException {
        return inputFileReader.readLine();
    }

    public void writeResult(String CSV) throws IOException {
        outputFileWriter.write(CSV);
    }

    public void close() throws IOException {
        inputFileReader.close();
        outputFileWriter.close();
    }
}
