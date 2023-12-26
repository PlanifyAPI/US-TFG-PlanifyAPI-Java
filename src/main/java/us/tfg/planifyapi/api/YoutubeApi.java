package us.tfg.planifyapi.api;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.HttpUrl;
import okhttp3.HttpUrl.Builder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import us.tfg.planifyapi.Utils.Utils;

public class YoutubeApi {

    private static final String FILE_PATH = "src/main/resources/report";

    public static void callYoutube(String query, int numCallApi) {
       
        OkHttpClient client = new OkHttpClient();
        Dotenv dotenv = Dotenv.configure().load();
        String apiKey = dotenv.get("API_KEY_YOUTUBE");
        Builder urlBuilder = HttpUrl.parse("https://www.googleapis.com/youtube/v3/search").newBuilder();
        urlBuilder.addQueryParameter("key", apiKey);
        urlBuilder.addQueryParameter("part", "snippet");
        urlBuilder.addQueryParameter("q", query); 

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                LocalDate date = LocalDate.now();
                String filePath = String.format("%s/%s", FILE_PATH, date.toString() + ".md");
                Utils.createFile(filePath);
        
                String responseBody = response.body().string();
                JSONObject jsonResponse = new JSONObject(responseBody);
                JSONArray items = jsonResponse.getJSONArray("items");

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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
}
