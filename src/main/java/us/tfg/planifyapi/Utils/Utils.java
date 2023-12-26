package us.tfg.planifyapi.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Utils {

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

    public static String parseContent(String content, int numCallApi) {
        String timeString = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String contentParsed = String.format("\n# Llamada a la API de YouTube #%d\n**Hora:** %s\n**Contenido:** \n\t**Título:** %s\n", numCallApi, timeString, content); 
        return contentParsed;
    }

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
            e.printStackTrace();
        }
        return "";
    }

    public static void generateDailyReport(String filename) {
        String fileRoute = String.format("src/main/resources/report/%s-report.md", filename);
        String content = readFile(fileRoute);
        Pattern patternNumCall = Pattern.compile("#\\d+");
        String numCalls = String.valueOf(patternNumCall.matcher(content).results().count());
        String report = String.format("# Reporte del día %s\n\n## Número de llamadas a la API \n%s", filename, numCalls);
        System.out.println("Reporte generado: \n" + report);
    }
    
}