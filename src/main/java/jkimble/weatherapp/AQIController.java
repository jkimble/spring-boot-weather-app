package jkimble.weatherapp;

import jakarta.validation.Valid;

import org.springframework.boot.autoconfigure.batch.BatchProperties.Job;
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
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

@Controller
public class AQIController implements WebMvcConfigurer {
    @GetMapping("/aqi")
    public String currentAQIForm(CurrentAQI currentAQI, Model aqiModel) {
        aqiModel.addAttribute("pageTitle", "Current AQI: Air Quality Alerts");
        aqiModel.addAttribute("activePage", "active");
        return "CurrentAQIForm";
    }

    @PostMapping("/aqi")
    public String processCityForm(@Valid CurrentAQI currentAQI, BindingResult bindingResult, Model aqiModel) throws IOException {

        if (bindingResult.hasErrors()) {
            return "CurrentAQIForm";
        }

        aqiModel.addAttribute("city", currentAQI.getCity());
        String cityQuery = currentAQI.getCity();
        JsonNode json;
        JsonNode aqiData;

        try {
            json = new ObjectMapper().readTree(new URL("http://api.openweathermap.org/geo/1.0/direct?q=" + cityQuery + "&limit=2&appid=8fe44c03b38ef50e722f5d2c9c2bd80d&units=imperial"));
        } catch (IOException e) {
            return "CurrentAQIForm";
        }

        JsonNode mostRel = json.get(0);
        JsonNode lat = mostRel.get("lat");
        JsonNode lon = mostRel.get("lon");
        JsonNode name = mostRel.get("name");
        aqiModel.addAttribute("name", name.toString());

        try {
            aqiData = new ObjectMapper().readTree(new URL("http://api.openweathermap.org/data/2.5/air_pollution?lat=" + lat.toString() + "&lon=" + lon.toString() + "&appid=8fe44c03b38ef50e722f5d2c9c2bd80d&units=imperial"));
        } catch (IOException e) {
            return "CurrentAQIForm";
        }

        JsonNode mainList = aqiData.get("list");
        
        Iterator<Map.Entry<String, JsonNode>> fields = mainList.fields();
        System.out.println(mainList.toString());
        
        
        //System.out.println(aqiData.toString());
        /*Iterator<Map.Entry<String, JsonNode>> fields = aqiData.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            System.out.println(field.getKey() + "=" + field.getValue());
            JsonNode components = field.getValue();
            Iterator<Map.Entry<String, JsonNode>> innerComps = components.fields();
            while (innerComps.hasNext()) {
                Map.Entry<String, JsonNode> innerComp = innerComps.next();
                System.out.println(innerComp.getKey() + ":" + innerComp.getValue().asText());
            }
        }*/


        aqiModel.addAttribute("pageTitle", "Air Quality Index for " + aqiModel.getAttribute("name"));
        aqiModel.addAttribute("activePage", "active");
        return "AQIResults";
    }
}
