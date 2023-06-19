package io.everyonecodes.project.movie_recommendations.communication.endpoint;

import io.everyonecodes.project.movie_recommendations.logic.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class UIController {

    private final UserService userService;

    public UIController(UserService userService) {this.userService = userService;}

    @GetMapping("/")
    public String viewGenres(Model model, Principal principal) {
        model.addAttribute("user_id", userService.getUserIdByUsername(principal.getName()));
        return "index";
    }
}
