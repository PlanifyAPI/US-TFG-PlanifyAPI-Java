package us.tfg.planifyapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import us.tfg.planifyapi.service.YoutubeService;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private YoutubeService youtubeService;
    
    @GetMapping("/youtube")
    public String getYoutubeData() {
        String rateLimit = "10000/1440min";
        return youtubeService.fetchYoutubeData(rateLimit);
    }

}
