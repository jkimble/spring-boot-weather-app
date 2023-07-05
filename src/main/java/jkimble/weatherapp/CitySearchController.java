package jkimble.weatherapp;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

@Controller
public class CitySearchController implements WebMvcConfigurer {
    @GetMapping("/city")
    public String showCityForm(CityCurrent cityCurrent, Model citymodel) {
        citymodel.addAttribute("pageTitle", "Current Weather: City Search");
        citymodel.addAttribute("activePage", "active");
        return "CurrentCityForm";
    }

    @PostMapping("/city")
    public String processCityForm(@Valid CityCurrent cityCurrent, BindingResult bindingResult, Model citymodel) throws IOException {
        if (bindingResult.hasErrors()) {
            citymodel.addAttribute("pageTitle", "Current Weather: City Search");
            citymodel.addAttribute("activePage", "active");
            return "CurrentCityForm";
        }

        citymodel.addAttribute("city", cityCurrent.getCity());
        String cityString = cityCurrent.getCity();
        JsonNode json;

        try {
            json = new ObjectMapper().readTree(new URL("https://api.openweathermap.org/data/2.5/weather?q=" + cityString + "&appid=8fe44c03b38ef50e722f5d2c9c2bd80d&units=imperial"));
        } catch (IOException e) {
            citymodel.addAttribute("pageTitle", "Current Weather: City Search");
            citymodel.addAttribute("activePage", "active");
            return "CurrentCityForm";
        }

        JsonNode weatherNode = json.get("weather");
        JsonNode weatherArray = weatherNode.get(0);
        JsonNode weatherType = weatherArray.get("main");
        JsonNode weatherDescription = weatherArray.get("description");
        JsonNode weatherIcon = weatherArray.get("icon");
        JsonNode rain = json.get("rain");
        citymodel.addAttribute("type", weatherType.asText());
        citymodel.addAttribute("desc", weatherDescription.asText());
        citymodel.addAttribute("icon", weatherIcon.asText());
        if (rain!= null) {
            citymodel.addAttribute("rainfall", rain.get("1h").asText());
        }

        Iterator<Map.Entry<String, JsonNode>> fields = json.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            citymodel.addAttribute(field.getKey(), field.getValue().asText());
            //System.out.println(field.getKey() + ':' + field.getValue().asText());
            JsonNode inner = field.getValue();
            Iterator<Map.Entry<String, JsonNode>> innerFields = inner.fields();
            while (innerFields.hasNext()) {
                Map.Entry<String, JsonNode> innerField = innerFields.next();
                //System.out.println(innerField.getKey() + ':' + innerField.getValue().asText());
                citymodel.addAttribute(innerField.getKey(), innerField.getValue().asText());
            }
        }
        citymodel.addAttribute("pageTitle", "Current weather for " + citymodel.getAttribute("name"));
        citymodel.addAttribute("activePage", "active");
        return "CityResults";
    }
}
