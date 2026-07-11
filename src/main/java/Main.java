import java.io.*;

public class Main {
    static void main(String[] args) {
        // Cчитываем аргументы
        if (args.length != 2){
            System.err.println("Invalid args");
            System.exit(1);
        }

        String inputFileName = args[0];
        String outputFileName = args[1];


        // try-with-resources для работы с файлами
        try (FileController fileController = new FileController(inputFileName, outputFileName); Logger logger = new Logger()) {
            ProductManager pm = new ProductManager();
            pm.attachLogger(logger); // Прикрепляем логгер (паттерн Observer)


            // Основная логика программы, считываем строку из входного файла и работаем с ней
            String query;
            while ((query = fileController.getQuery()) != null) {
                String[] params = query.split(";");
                try {
                    if (params.length == 2) { // ПРОДАЖА
                        pm.sell(params[0], Integer.parseInt(params[1]));
                    } else if (params.length == 3) { // ПОСТУПЛЕНИЕ
                        pm.add(params[0], params[1], Integer.parseInt(params[2]));
                    } else { // ОШИБКА
                        throw new RuntimeException("Invalid query");
                    }
                }
                catch (RuntimeException e){
                    logger.logErr(e.getMessage()); // Логируем ошибки в обход
                }
            }

            fileController.writeResult(pm.generateCSV()); // Генерируем финальный CSV и записываем в выходной файл
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
