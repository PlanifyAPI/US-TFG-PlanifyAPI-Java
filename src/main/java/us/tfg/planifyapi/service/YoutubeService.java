package us.tfg.planifyapi.service;

import org.springframework.stereotype.Service;
import us.tfg.planifyapi.api.YoutubeApi;

@Service
public class YoutubeService {

    // Lógica para interactuar con la API de YouTube
    public String fetchYoutubeData() {
        // Implementa la lógica para obtener datos de la API de YouTube aquí
        YoutubeApi.callYoutube();
        return "Datos de YouTube obtenidos";
    }
}
