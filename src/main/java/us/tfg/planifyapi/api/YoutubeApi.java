package us.tfg.planifyapi.api;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import us.tfg.planifyapi.Utils.Utils;

public class YoutubeApi {
    
    private static final String FILE_PATH = "src/main/resources/report";
    
    // La cuota de youtube se renueva todos los días a las 9:00 am
    private static final LocalDateTime TIME_RENEW_QUOTA = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(9, 0, 0));
    
    /**
    * Calls the YouTube API to search for videos based on the given query.
    * 
    * @param query         the search query
    * @param numCallsApi   the number of API calls to make
    */
    public static void callYoutube(String query, int numCallsApi) {
        Request request = Utils.buildRequest("API_KEY_YOUTUBE", "https://www.googleapis.com/youtube/v3/search", query);
        OkHttpClient client = new OkHttpClient();
        
        for (int numCallApi=0; numCallApi <= numCallsApi; numCallApi++) {
            try {
                Response response = client.newCall(request).execute();
                String filePath = String.format("%s/%s", FILE_PATH, LocalDate.now().toString() + ".md");
                if (response.isSuccessful()) {
                    Utils.createFile(filePath);
                    
                    String responseBody = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    JSONArray items = jsonResponse.getJSONArray("items");
                    
                    // El response trae 5 videos, recorremos todos
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        JSONObject snippet = item.getJSONObject("snippet");
                        String videoTitle = snippet.getString("title");
                        String parsedContent = Utils.parseContent(videoTitle, numCallApi);
                        System.out.println(parsedContent);
                        Utils.writeFile(filePath, parsedContent);
                    }
                } else {
                    System.out.println("Error en la solicitud: " + response.code());
                    JSONObject jsonResponse = new JSONObject(response.body().string());
                    JSONObject error = jsonResponse.getJSONObject("error");
                    String message = error.getString("message");
                    boolean isQuotaError = message.contains("quota");
                    if (isQuotaError) {
                        System.out.println("Se ha alcanzado el límite de llamadas a la API de YouTube");
                        LocalDateTime timeNow = LocalDateTime.now();
                        System.out.println("Hora actual: " + timeNow);
                        System.out.println("Hora de renovación de la cuota: " + TIME_RENEW_QUOTA);
                        Long durationRenew = Duration.between(timeNow, TIME_RENEW_QUOTA).toMillis();
                        System.out.println("Esperando " + durationRenew + " milisegundos hasta renovar la cuota");
                        Utils.generateDailyReport(filePath);
                        Thread.sleep(durationRenew);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
}
