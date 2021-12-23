package com.company.addonis.controllers.mvc;

import com.company.addonis.controllers.AuthenticationHelper;
import com.company.addonis.exceptions.EntityNotFoundException;
import com.company.addonis.models.Addon;
import com.company.addonis.models.IDE;
import com.company.addonis.models.dtos.ContactUsDto;
import com.company.addonis.services.contracts.AddonService;
import com.company.addonis.services.contracts.ContactsUsService;
import com.company.addonis.services.contracts.IdeService;
import com.company.addonis.services.contracts.UserService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class HomeMvcController extends BaseAuthenticationController {

    private final AddonService addonService;
    private final UserService userService;
    private final IdeService ideService;
    private final ContactsUsService contactsUsService;

    @Autowired
    public HomeMvcController(AuthenticationHelper authenticationHelper,
                             AddonService addonService,
                             UserService userService,
                             IdeService ideService,
                             ContactsUsService contactsUsService) {
        super(authenticationHelper);
        this.addonService = addonService;
        this.userService = userService;
        this.ideService = ideService;
        this.contactsUsService = contactsUsService;
    }

    @GetMapping("addon/{id}")
    public String getAddonById(Model model,
                               @PathVariable int id) {
        try {
            Addon addon = addonService.getById(id);
            model.addAttribute("addon", addon);
            return "addons/addon-details";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping
    public String showHomePage(Model model) {
        List<Addon> featuredAddons = addonService.filter(Optional.of("-1"),
                Optional.of("-1"), Optional.of("1"), Optional.of("-1"), Optional.of("-1"));
        List<Addon> newestAddons = addonService.getByColumn("uploadDate", 6);
        List<Addon> mostPopularAddons = addonService
                .getByColumn("totalScore / (totalVoters * 1.0)", 6);
        model.addAttribute("featuredAddons", featuredAddons);
        model.addAttribute("newestAddons", newestAddons);
        model.addAttribute("mostPopularAddons", mostPopularAddons);
        return "index";
    }

    @GetMapping("contact")
    public String showContactPage(Model model) {
        model.addAttribute("contactUs", new ContactUsDto());
        return "contact";
    }

    @GetMapping("addon/all")
    public String getAll(Model model,
                         @RequestParam(value = "ide", required = false) String ide,
                         @RequestParam(value = "searchedValue", required = false) String name,
                         @RequestParam(value = "sort", required = false) String sort) {
        List<Addon> addons;
        if (ide == null && name == null && sort == null) {
            addons = addonService.getByStatus("Approved");
        } else {
            addons = addonService.filter(name == null ? Optional.of("-1") : Optional.of(name),
                    ide == null ? Optional.of("-1") : Optional.of(ide)
                    , Optional.of("-1"), Optional.of("-1"), sort == null ? Optional.of("-1") : Optional.of(sort));
        }
        List<IDE> ides = ideService.getAll();
        model.addAttribute("addons", addons);
        model.addAttribute("ides", ides);
        return "addons/addons-all";
    }

    @GetMapping("addon/all/pending")
    public String getAllPending(Model model,
                                @RequestParam(value = "searchedValue", required = false) String name) {
        try {
            List<Addon> addons;
            if (name == null || name.isBlank()) {
                addons = addonService.getByStatus("Pending");
            } else {
                addons = addonService.getPendingAddonsByName(name);
            }
            model.addAttribute("addons", addons);
            return "addons/addons-approve";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/downloadAddon/{id}")
    public void download(@PathVariable int id, HttpServletResponse response) throws IOException {
        Addon addon = addonService.getById(id);
        byte[] content = addon.getBinaryContent();
        InputStream in = new ByteArrayInputStream(content);
        IOUtils.copy(in, response.getOutputStream());
        response.flushBuffer();
        addon.setDownloadCount(addon.getDownloadCount() + 1);
        addonService.update(addon);
    }

    @PostMapping("/contactUs")
    public String contactUs(@Valid @ModelAttribute("contactUs") ContactUsDto contactUsDto,
                            BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            return "contact";
        }

        try {
            contactsUsService.contactsUs(contactUsDto);
            return "redirect:/";
        } catch (MailException e) {
            model.addAttribute("error", errors);
            return "error";
        }
    }

    @ModelAttribute("populateAddonsCount")
    public int addonsCount() {
        return addonService.getAll().size();
    }

    @ModelAttribute("populateUsersCount")
    public int usersCount() {
        return userService.getAllUsers().size();
    }

    @ModelAttribute("populateUsersAndAdminsCount")
    public int usersAndAdminsCount() {
        return userService.getAll().size();
    }

    @ModelAttribute("populateDownloadCount")
    public long downloadCount() {
        return addonService.getTotalDownloads();
    }
}
