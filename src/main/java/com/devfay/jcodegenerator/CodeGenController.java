package com.devfay.jcodegenerator;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CodeGenController {

    @GetMapping("/generate")
    public String generateForm() {
        return "generateForm";
    }

    @PostMapping("/generate")
    public String generateClass(Model model,
                                @RequestParam("className") String className,
                                @RequestParam("attributes") List<String> attributes) {
        model.addAttribute("className", className);
        model.addAttribute("attributes", attributes);
        return "generatedClass";
    }
}
