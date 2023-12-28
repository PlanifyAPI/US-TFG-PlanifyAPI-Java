package us.tfg.planifyapi.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Pattern;

import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.HttpUrl;
import okhttp3.HttpUrl.Builder;
import okhttp3.Request;

public class Utils {
    
    private static final String FILE_PATH_ERROR = "src/main/resources/report/error";
    
    /**
    * Creates a new file at the specified file path.
    * If the file already exists, no action is taken.
    *
    * @param filePath the path of the file to be created
    */
    public static void createFile(String filePath) {
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Archivo creado: " + file.getName());
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
    * Parses the given content and returns a formatted string with additional information.
    *
    * @param content The content to be parsed.
    * @param numCallApi The number of the API call.
    * @return The parsed and formatted string.
    */
    public static String parseContent(String content, int numCallApi) {
        String timeString = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String contentParsed = String.format("\n# Llamada a la API de YouTube #%d <br>\nHora: %s<br>\nContenido: <br>\nTítulo: %s<br>\n", numCallApi, timeString, content); 
        return contentParsed;
    }
    
    /**
    * Writes content to a file specified by the given file path.
    *
    * @param filePath the path of the file to write to
    * @param content  the content to write to the file
    */
    public static void writeFile(String filePath, String content) {
        File file = new File(filePath);
        try {
            FileWriter writer = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(content);
            bufferedWriter.close();
            System.out.println("Archivo escrito.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
    * Reads the content of a file and returns it as a string.
    *
    * @param filePath the path of the file to be read
    * @return the content of the file as a string
    */
    public static String readFile(String filePath) {
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            String content = "";
            while (scanner.hasNextLine()) {
                content += scanner.nextLine();
            }
            scanner.close();
            return content;
            
        } catch (Exception e) {
            errorHandler(e);
        }
        return "";
    }
    
    
    /**
    * Generates a daily report based on the content of a file.
    * The report includes the current date, the number of API calls, and saves it to a file.
    *
    * @param filePathRead The path of the file to read the content from.
    */
    public static void generateDailyReport(String filePathRead) {
        String filename = LocalDate.now().toString();
        String fileRoute = String.format("src/main/resources/report/%s-report.md", filename);
        String content = readFile(filePathRead);
        Pattern patternNumCall = Pattern.compile("#\\d+");
        String numCalls = String.valueOf(patternNumCall.matcher(content).results().count());
        String report = String.format("# Reporte del día %s\n ## Número de llamadas a la API \n%s", filename, numCalls);
        writeFile(fileRoute, report);
        System.out.println("Reporte generado: \n" + report);
    }
    
    /**
    * Builds a Request object with the specified parameters.
    *
    * @param nameApiKeyEnv the name of the API key environment variable
    * @param urlString the URL string
    * @param query the query parameter
    * @return a Request object
    */
    public static Request buildRequest(String nameApiKeyEnv, String urlString, String query) {
        Dotenv dotenv = Dotenv.configure().load();
        String apiKey = dotenv.get(nameApiKeyEnv);
        Builder urlBuilder = HttpUrl.parse(urlString).newBuilder();
        urlBuilder.addQueryParameter("key", apiKey);
        urlBuilder.addQueryParameter("part", "snippet");
        urlBuilder.addQueryParameter("q", query); 
        
        String url = urlBuilder.build().toString();
        
        Request request = new Request.Builder()
        .url(url)
        .build();
        
        return request;
    }
    
    public static void errorHandler(Exception e) {
        String trace = String.valueOf(e.getMessage());
        String timeError = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        Utils.createFile(String.format("%s/error%s.md", FILE_PATH_ERROR, timeError));
        Utils.writeFile(String.format("%s/error%s.md", FILE_PATH_ERROR, timeError), trace);
        System.out.println("ERROR: Archivo de error generado.");
    }
    
}