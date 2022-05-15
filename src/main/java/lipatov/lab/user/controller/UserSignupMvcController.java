package lipatov.lab.user.controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import lipatov.lab.util.validation.ValidationException;
import org.springframework.validation.BindingResult;
import org.springframework.stereotype.Controller;
import lipatov.lab.user.model.UserSignupDto;
import lipatov.lab.user.service.UserService;
import org.springframework.ui.Model;
import lipatov.lab.user.model.User;
import javax.validation.Valid;

@Controller
@RequestMapping(UserSignupMvcController.SIGNUP_URL)
public class UserSignupMvcController {
    public static final String SIGNUP_URL = "/signup";
    private final UserService userService;

    public UserSignupMvcController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showSignupForm(Model model) {
        model.addAttribute("userDto", new UserSignupDto());
        return "signup";
    }

    @PostMapping
    public String signup(@ModelAttribute("userDto") @Valid UserSignupDto userSignupDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "signup";
        }
        try {
            final User user = userService.createUser(
                    userSignupDto.getLogin(), userSignupDto.getPassword(), userSignupDto.getPasswordConfirm());
            return "redirect:/login?created=" + user.getLogin();
        } catch (ValidationException e) {
            model.addAttribute("errors", e.getMessage());
            return "signup";
        }
    }
}