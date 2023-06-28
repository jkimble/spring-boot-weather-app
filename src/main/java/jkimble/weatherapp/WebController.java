package jkimble.weatherapp;

import jakarta.validation.Valid;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

@Controller
public class WebController implements WebMvcConfigurer {
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/results").setViewName("results");
    }

    @GetMapping("/coord")
    public String showForm(CoordForm coordForm, Model model) {
        return "form";
    }

    @PostMapping("/coord")
    public String processForm(@Valid CoordForm coordForm, BindingResult bindingResult, Model model) throws IOException {

        if (bindingResult.hasErrors()) {
            return "form";
        }

        model.addAttribute("lat", coordForm.getLat());
        model.addAttribute("lon", coordForm.getLon());

        double lat = coordForm.getLat();
        double lon = coordForm.getLon();
        JsonNode json = new ObjectMapper().readTree(new URL("https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=095052cd51fda4aea56dd97c5353a83e"));

        // get the weather data
        JsonNode nameNode = json.get("name");
        JsonNode countryNode = json.get("sys").get("country");
        model.addAttribute("name", nameNode.asText());
        model.addAttribute("country", countryNode.asText());

        JsonNode weatherNode = json.get("weather");
        JsonNode weatherArray = weatherNode.get(0);
        JsonNode weatherType = weatherArray.get("main");
        JsonNode weatherDescription = weatherArray.get("description");
        model.addAttribute("type", weatherType.asText());
        model.addAttribute("desc", weatherDescription.asText());
        

        JsonNode windNode = json.get("wind");
        JsonNode windSpeedNode = windNode.get("speed");
        model.addAttribute("wind", windSpeedNode.asText());

        System.out.println(json);
        
        return "results";
    }

    
}
