package jkimble.weatherapp;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.fasterxml.jackson.core.JsonParser;
import org.apache.commons.io.*;

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
    public String processForm(@Valid CoordForm coordForm, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "form";
        }

        model.addAttribute("lat", coordForm.getLat());
        model.addAttribute("lon", coordForm.getLon());

        double lat = coordForm.getLat();
        double lon = coordForm.getLon();
        
        return "results";
    }
}
