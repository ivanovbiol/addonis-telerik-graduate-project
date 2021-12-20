package com.company.addonis.controllers.mvc;

import com.company.addonis.controllers.AuthenticationHelper;
import com.company.addonis.exceptions.AuthenticationFailureException;
import com.company.addonis.exceptions.DuplicateEntityException;
import com.company.addonis.models.Addon;
import com.company.addonis.models.User;
import com.company.addonis.models.dtos.UserDetailsUpdateDto;
import com.company.addonis.models.dtos.UserPasswordChangeDto;
import com.company.addonis.models.mappers.UserMapper;
import com.company.addonis.services.contracts.AddonService;
import com.company.addonis.services.contracts.EmailSenderService;
import com.company.addonis.services.contracts.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.company.addonis.controllers.mvc.AuthenticationController.PASSWORD_CONFIRMATION_ERROR;

@Controller
@RequestMapping("/user")
public class UserMvcController extends BaseAuthenticationController {

    public static final String OLD_PASSWORD_CONFIRMATION_ERROR = "Incorrect old password!";
    private final UserService userService;
    private final UserMapper userMapper;
    private final AddonService addonService;
    private final EmailSenderService emailSenderService;

    public UserMvcController(UserService userService,
                             UserMapper userMapper,
                             AddonService addonService,
                             AuthenticationHelper authenticationHelper, EmailSenderService emailSenderService) {
        super(authenticationHelper);
        this.userService = userService;
        this.userMapper = userMapper;
        this.addonService = addonService;
        this.emailSenderService = emailSenderService;
    }

    @GetMapping("{id}")
    public String getById(@Valid @PathVariable int id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return "users/user-profile";
    }

    @GetMapping("/updateDetails")
    public String update(HttpSession session, Model model) {
        User user = getCurrentUser(session);
        List<Addon> addons = addonService.getAllAddonsByUser(user.getId());
        model.addAttribute("userToUpdateDetails", new UserDetailsUpdateDto());
        model.addAttribute("addons", addons);
        model.addAttribute("user", user);
        return "users/edit-profile";
    }

    @PostMapping("/updateDetails")
    public String update(HttpSession session,
                         @ModelAttribute("userToUpdateDetails") UserDetailsUpdateDto userDetailsUpdateDto,
                         BindingResult errors) throws IOException {
        if (errors.hasErrors()) {
            return "users/edit-profile";
        }

        try {
            User user = getCurrentUser(session);
            User userToUpdate = userMapper.updateUserDetails(userDetailsUpdateDto, user.getId());
            userService.update(userToUpdate);
            return "redirect:/user/updateDetails";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("email", "email_error", e.getMessage());
            return "users/edit-profile";
        }
    }

    @GetMapping("/changePassword")
    public String changePassword(Model model, HttpSession session) {
        User user = getCurrentUser(session);
        model.addAttribute("userToUpdatePassword", new UserPasswordChangeDto());
        model.addAttribute("user", user);
        return "users/change-password";
    }

    @PostMapping("/changePassword")
    public String changePassword(HttpSession session,
                                 @ModelAttribute("userToUpdatePassword") UserPasswordChangeDto userPasswordChangeDto,
                                 BindingResult errors, Model model) {

        if (errors.hasErrors()) {
            return "users/change-password";
        }
        User user = getCurrentUser(session);
        model.addAttribute("user", user);
        if (!Objects.equals(user.getPassword(), userPasswordChangeDto.getOldPassword())) {
            errors.rejectValue("oldPassword", "password_error",
                    OLD_PASSWORD_CONFIRMATION_ERROR);
            return "users/change-password";

        }
        if (!Objects.equals(userPasswordChangeDto.getNewPassword(), userPasswordChangeDto.getConfirmNewPassword())) {
            errors.rejectValue("confirmNewPassword", "password_error",
                    PASSWORD_CONFIRMATION_ERROR);
            return "users/change-password";

        }
        user.setPassword(userPasswordChangeDto.getNewPassword());
        userService.update(user);
        return "redirect:/user/updateDetails";

    }

    @GetMapping("/invite")
    public String invite(HttpSession session, Model model) {
        try {
            User user = getCurrentUser(session);
            model.addAttribute("user", user);
            return "invite";
        } catch (AuthenticationFailureException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/inviteFriend")
    public String inviteFriend(Model model,
                               @RequestParam String friendEmail) {
        try {
            emailSenderService.inviteFriend(friendEmail);
            return "redirect:/";
        } catch (UnsupportedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    private User getCurrentUser(HttpSession session) {
        return getAuthenticationHelper().tryGetUser(session);
    }
}
