package com.company.addonis.controllers.mvc;

import com.company.addonis.controllers.AuthenticationHelper;
import com.company.addonis.models.User;
import com.company.addonis.services.contracts.AddonService;
import com.company.addonis.services.contracts.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/admins")
public class AdminMvcController extends BaseAuthenticationController {

    private final UserService userService;
    private final AddonService addonService;

    public AdminMvcController(UserService userService,
                              AddonService addonService,
                              AuthenticationHelper authenticationHelper) {
        super(authenticationHelper);
        this.userService = userService;
        this.addonService = addonService;
    }

    @GetMapping("/users/all")
    public String showAll(Model model,
                          @RequestParam(value = "searchedValue", required = false) String search) {
        List<User> users;
        if (search != null) {
            users = userService.search(search);
        } else {
            users = userService.getAllUsers();
        }
        for (User user : users) {
            user.setImage(getUserPhoto(user));
            user.setUploadedAddons(addonService.getAllAddonsByUser(user.getId()).size());
        }
        int usersCount = userService.getAllUsers().size();
        model.addAttribute("users", users);
        model.addAttribute("usersCount", usersCount);

        return "admins/users";
    }


    @GetMapping("/user/{id}/block")
    public String blockUser(@PathVariable int id) {
        User user = userService.getById(id);
        user.setEnabled(false);
        userService.update(user);
        return "redirect:/admins/users/all";
    }

    @GetMapping("/user/{id}/unblock")
    public String unblockUser(@PathVariable int id) {
        User user = userService.getById(id);
        user.setEnabled(true);
        userService.update(user);
        return "redirect:/admins/users/all";
    }

    private String getUserPhoto(User user) {
        String userPhoto;
        if (user.getPhoto() == null) userPhoto = null;
        else userPhoto = "data:image/png;base64," + Base64.getEncoder().encodeToString(user.getPhoto());
        return userPhoto;
    }

}

