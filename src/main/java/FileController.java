import java.io.*;


// Класс для работы с файлами, закрывает их при закрытии себя
public class FileController implements AutoCloseable {
    private BufferedReader inputFileReader;
    private BufferedWriter outputFileWriter;

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
