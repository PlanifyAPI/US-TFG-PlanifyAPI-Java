package us.tfg.planifyapi.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import us.tfg.planifyapi.api.YoutubeApi;

@Service
public class YoutubeService {
    
    public String fetchYoutubeData(String rateLimit) {
        Pattern pattern = Pattern.compile("(\\d+)/(\\d+)");
        Matcher matcher = pattern.matcher(rateLimit);
        int numCalls = 0;
        if (matcher.find()) {
            numCalls = Integer.parseInt(matcher.group(1));
        }
        YoutubeApi.callYoutube("Navidad", numCalls);
        return "Datos de YouTube obtenidos";
    }
}
