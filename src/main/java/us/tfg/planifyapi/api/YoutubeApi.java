package us.tfg.planifyapi.api;

import org.json.JSONArray;
import org.json.JSONObject;

import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.HttpUrl;
import okhttp3.HttpUrl.Builder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class YoutubeApi {

    public static void callYoutube() {
        OkHttpClient client = new OkHttpClient();
        Dotenv dotenv = Dotenv.configure().load();
        String apiKey = dotenv.get("API_KEY_YOUTUBE");
        System.out.println("API_KEY_YOTUBE: " + apiKey);
        Builder urlBuilder = HttpUrl.parse("https://www.googleapis.com/youtube/v3/search").newBuilder();
        urlBuilder.addQueryParameter("key", apiKey);
        urlBuilder.addQueryParameter("part", "snippet");
        urlBuilder.addQueryParameter("q", "gatos");

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonResponse = new JSONObject(responseBody);
                JSONArray items = jsonResponse.getJSONArray("items");

                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    JSONObject snippet = item.getJSONObject("snippet");
                    String videoTitle = snippet.getString("title");
                    System.out.println("Video Title: " + videoTitle);
                }
            } else {
                System.out.println("Error en la solicitud: " + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
