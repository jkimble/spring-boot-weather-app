package jkimble.weatherapp;

import jakarta.validation.Valid;

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
import java.lang.reflect.Array;
import java.net.URL;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

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
        JsonNode json = new ObjectMapper().readTree(new URL("https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=8fe44c03b38ef50e722f5d2c9c2bd80d&units=imperial"));

        // get the weather data
    /*  JsonNode nameNode = json.get("name");
        JsonNode countryNode = json.get("sys").get("country");
        if (nameNode != null) {
            model.addAttribute("name", nameNode.asText());
        }
        if (countryNode != null) {
            model.addAttribute("country", countryNode.asText());
        }

        JsonNode weatherNode = json.get("weather");
        JsonNode weatherArray = weatherNode.get(0);
        JsonNode weatherType = weatherArray.get("main");
        JsonNode weatherDescription = weatherArray.get("description");
        JsonNode weatherIcon = weatherArray.get("icon");
        model.addAttribute("type", weatherType.asText());
        model.addAttribute("desc", weatherDescription.asText());
        model.addAttribute("icon", weatherIcon.asText());

        JsonNode mainNode = json.get("main");
        JsonNode tempNode = mainNode.get("temp");
        JsonNode tempFeel = mainNode.get("feels_like");
        JsonNode tempMin = mainNode.get("temp_min");
        JsonNode tempMax = mainNode.get("temp_max");
        JsonNode humidityNode = mainNode.get("humidity");
        model.addAttribute("temp", tempNode.asInt());


        JsonNode windNode = json.get("wind");
        JsonNode windSpeedNode = windNode.get("speed");
        JsonNode windDegNode = windNode.get("deg");
        JsonNode windGustNode = windNode.get("gust");
        model.addAttribute("wind", windSpeedNode.asText()); 

        Iterator<String> keys = (Iterator<String>) json.fieldNames();
        while (keys.hasNext()) {
            String key = keys.next();
            String value = json.get(key).asText();
            model.addAttribute(key, value);
        }
        */

        Iterator<Map.Entry<String, JsonNode>> fields = json.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            JsonNode inner = field.getValue();
            model.addAttribute(field.getKey(), field.getValue().asText());
            System.out.println(field.getKey() + ':' + field.getValue().asText());
        }
        
        return "results";
    }

    
}
