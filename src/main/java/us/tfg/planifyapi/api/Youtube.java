package us.tfg.planifyapi.api;

import okhttp3.HttpUrl;
import okhttp3.HttpUrl.Builder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;



public class Youtube {
    
    
    public static void main(String[] args) {
        
        OkHttpClient client = new OkHttpClient();
        
        
        String apiKey = System.getenv("API_KEY_YOTUBE");
        Builder urlBuilder = HttpUrl.parse("https://www.googleapis.com/youtube/v3/search").newBuilder();
        urlBuilder.addQueryParameter("apiKey", apiKey);
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
