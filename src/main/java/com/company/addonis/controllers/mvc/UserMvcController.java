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

import static com.company.addonis.controllers.mvc.AuthenticationController.PASSWORD_CONFIRMATION_ERROR;

@Controller
@RequestMapping("/auth/user")
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

    @GetMapping("/{id}")
    public String getById(@Valid @PathVariable int id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return "users/user-profile";
    }

    @GetMapping("/update")
    public String update(HttpSession session, Model model) {
        User user = getAuthenticationHelper().tryGetUser(session);
        List<Addon> addons = addonService.getAddonsByUser(user.getId());
        model.addAttribute("userToUpdateDetails", new UserDetailsUpdateDto());
        model.addAttribute("addons", addons);
        model.addAttribute("user", user);
        return "users/user-edit-profile";
    }

    @PostMapping("/update")
    public String update(HttpSession session,
                         @Valid @ModelAttribute("userToUpdateDetails")
                                 UserDetailsUpdateDto userDetailsUpdateDto,
                         BindingResult errors, Model model) throws IOException {
        User user = getAuthenticationHelper().tryGetUser(session);
        if (errors.hasErrors()) {
            List<Addon> addons = addonService.getAddonsByUser(user.getId());
            model.addAttribute("user", user);
            model.addAttribute("addons", addons);
            return "users/user-edit-profile";
        }

        try {
            User userToUpdate = userMapper.updateUserDetails(userDetailsUpdateDto, user.getId());
            userService.update(userToUpdate);
            return "redirect:/auth/user/update";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("email", "email_error", e.getMessage());
            return "users/user-edit-profile";
        }
    }

    @GetMapping("/changePassword")
    public String changePassword(Model model, HttpSession session) {
        User user = getAuthenticationHelper().tryGetUser(session);
        model.addAttribute("userToUpdatePassword", new UserPasswordChangeDto());
        model.addAttribute("user", user);
        return "users/user-change-password";
    }

    @PostMapping("/changePassword")
    public String changePassword(HttpSession session,
                                 @Valid @ModelAttribute("userToUpdatePassword")
                                         UserPasswordChangeDto userPasswordChangeDto,
                                 BindingResult errors, Model model) {
        User user = getAuthenticationHelper().tryGetUser(session);
        model.addAttribute("user", user);
        if (errors.hasErrors()) {
            return "users/user-change-password";
        }
        if (!user.getPassword().equals(userPasswordChangeDto.getOldPassword())) {
            errors.rejectValue("oldPassword", "password_error",
                    OLD_PASSWORD_CONFIRMATION_ERROR);
            return "users/user-change-password";

        }
        if (!userPasswordChangeDto.getNewPassword().equals(userPasswordChangeDto.getConfirmNewPassword())) {
            errors.rejectValue("confirmNewPassword", "password_error",
                    PASSWORD_CONFIRMATION_ERROR);
            return "users/user-change-password";

        }
        user.setPassword(userPasswordChangeDto.getNewPassword());
        userService.update(user);
        return "redirect:/auth/user/update";
    }

    @GetMapping("/invite")
    public String invite(HttpSession session, Model model) {
        try {
            User user = getAuthenticationHelper().tryGetUser(session);
            model.addAttribute("user", user);
            return "invite";
        } catch (AuthenticationFailureException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/invite")
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

    @GetMapping("/all")
    public String showAll(Model model,
                          @RequestParam(value = "searchedValue", required = false) String search) {
        List<User> users;
        if (search != null) {
            users = userService.search(search);
        } else {
            users = userService.getAllUsers();
        }
        int usersCount = userService.getAllUsers().size();
        model.addAttribute("users", users);
        model.addAttribute("usersCount", usersCount);

        return "users/users-all";
    }


    @GetMapping("/{id}/block")
    public String blockUser(@PathVariable int id) {
        User user = userService.getById(id);
        user.setEnabled(false);
        userService.update(user);
        return "redirect:/auth/user/all";
    }

    @GetMapping("/{id}/unblock")
    public String unblockUser(@PathVariable int id) {
        User user = userService.getById(id);
        user.setEnabled(true);
        userService.update(user);
        return "redirect:/auth/user/all";
    }
}
