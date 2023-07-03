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
            return "form";
        }
        aqiModel.addAttribute("city", currentAQI.getCity());
        return "CityResults";
    }
}
