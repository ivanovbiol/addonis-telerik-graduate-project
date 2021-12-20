package com.company.addonis.controllers.mvc;

import com.company.addonis.controllers.AuthenticationHelper;
import com.company.addonis.exceptions.AuthenticationFailureException;
import com.company.addonis.exceptions.DuplicateEntityException;
import com.company.addonis.exceptions.EntityNotFoundException;
import com.company.addonis.exceptions.UnauthorizedOperationException;
import com.company.addonis.models.ConfirmationToken;
import com.company.addonis.models.User;
import com.company.addonis.models.dtos.LoginDto;
import com.company.addonis.models.dtos.RegisterDto;
import com.company.addonis.models.dtos.UserDto;
import com.company.addonis.models.mappers.UserMapper;
import com.company.addonis.services.contracts.ConfirmationTokenService;
import com.company.addonis.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthenticationController extends BaseAuthenticationController {

    public static final String PASSWORD_CONFIRMATION_ERROR = "Password confirmation should match password.";

    private final UserMapper userMapper;
    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;

    @Autowired
    public AuthenticationController(UserMapper userMapper,
                                    UserService userService,
                                    AuthenticationHelper authenticationHelper,
                                    ConfirmationTokenService confirmationTokenService) {
        super(authenticationHelper);
        this.userMapper = userMapper;
        this.userService = userService;
        this.confirmationTokenService = confirmationTokenService;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("login", new LoginDto());
        return "login";
    }


    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") LoginDto login,
                              BindingResult bindingResult,
                              HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {
            super.getAuthenticationHelper().verifyAuthentication(login.getUsername(), login.getPassword());
            session.setAttribute("currentUser", login.getUsername());
            return "redirect:/";
        } catch (AuthenticationFailureException e) {
            bindingResult.rejectValue("username", "auth_error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("register", new RegisterDto());
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register")
                                         RegisterDto registerDto,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        if (!registerDto.getPassword().equals(registerDto.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "password_error",
                    PASSWORD_CONFIRMATION_ERROR);
            return "register";
        }
        try {
            UserDto userDto = userMapper.fromDtoToUser(registerDto);
            User user = userMapper.fromDto(userDto);
            userService.create(user);
            return "index";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("username", "username_error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.removeAttribute("currentUser");
        return "redirect:/";
    }

    @GetMapping("/confirm/{confirmationToken}")
    public String confirmUserAccount(@PathVariable String confirmationToken,
                                     Model model) {
        try {
            ConfirmationToken token =
                    confirmationTokenService.getByField(confirmationToken);
            User user = userService.getByField("email", token.getUser().getEmail());
            user.setEnabled(true);
            userService.update(user);
            return "confirm";
        } catch (EntityNotFoundException | UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}
