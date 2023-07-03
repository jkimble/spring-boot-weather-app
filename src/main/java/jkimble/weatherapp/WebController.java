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
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

@Controller
public class WebController implements WebMvcConfigurer {
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/results").setViewName("results");
    }

    @GetMapping("/coord")
    public String showForm(CoordForm coordForm, Model model) {
        model.addAttribute("pageTitle", "Current Weather: Coordinate Search");
        model.addAttribute("activePage", "active");
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
        System.out.println(json.toString());

        JsonNode weatherNode = json.get("weather");
        JsonNode weatherArray = weatherNode.get(0);
        JsonNode weatherType = weatherArray.get("main");
        JsonNode weatherDescription = weatherArray.get("description");
        JsonNode weatherIcon = weatherArray.get("icon");
        JsonNode rain = json.get("rain");
        model.addAttribute("type", weatherType.asText());
        model.addAttribute("desc", weatherDescription.asText());
        model.addAttribute("icon", weatherIcon.asText());
        if (rain!= null) {
            model.addAttribute("rainfall", rain.get("1h").asText());
        }

        Iterator<Map.Entry<String, JsonNode>> fields = json.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            model.addAttribute(field.getKey(), field.getValue().asText());
            //System.out.println(field.getKey() + ':' + field.getValue().asText());
            JsonNode inner = field.getValue();
            Iterator<Map.Entry<String, JsonNode>> innerFields = inner.fields();
            while (innerFields.hasNext()) {
                Map.Entry<String, JsonNode> innerField = innerFields.next();
                //System.out.println(innerField.getKey() + ':' + innerField.getValue().asText());
                model.addAttribute(innerField.getKey(), innerField.getValue().asText());
            }
        }
        model.addAttribute("pageTitle", "Current weather for " + model.getAttribute("name"));
        model.addAttribute("activePage", "active");
        return "results";
    }

    
}
