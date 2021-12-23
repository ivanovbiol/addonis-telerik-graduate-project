package com.company.addonis.controllers.mvc;

import com.company.addonis.controllers.AuthenticationHelper;
import com.company.addonis.exceptions.AuthenticationFailureException;
import com.company.addonis.exceptions.DuplicateEntityException;
import com.company.addonis.exceptions.EntityNotFoundException;
import com.company.addonis.exceptions.UnauthorizedOperationException;
import com.company.addonis.models.Addon;
import com.company.addonis.models.IDE;
import com.company.addonis.models.Tag;
import com.company.addonis.models.User;
import com.company.addonis.models.dtos.AddonDto;
import com.company.addonis.models.mappers.AddonMapper;
import com.company.addonis.services.contracts.AddonService;
import com.company.addonis.services.contracts.IdeService;
import com.company.addonis.services.contracts.StatusService;
import com.company.addonis.services.contracts.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static com.company.addonis.utils.ObjectFieldsSetter.setBinaryData;
import static com.company.addonis.utils.ObjectFieldsSetter.setRating;

@Controller
@RequestMapping("/auth/addon")
public class AddonMvcController extends BaseAuthenticationController {

    private final AddonService addonService;
    private final AddonMapper addonMapper;
    private final IdeService ideService;
    private final TagService tagService;
    private final StatusService statusService;

    @Autowired
    public AddonMvcController(AddonService addonService,
                              AddonMapper addonMapper,
                              IdeService ideService,
                              AuthenticationHelper authenticationHelper,
                              TagService tagService,
                              StatusService statusService) {
        super(authenticationHelper);
        this.addonService = addonService;
        this.addonMapper = addonMapper;
        this.ideService = ideService;
        this.tagService = tagService;
        this.statusService = statusService;
    }

    @GetMapping("/create")
    public String create(HttpSession session, Model model) {
        model.addAttribute("addon", new AddonDto());
        return "addons/addon-create";
    }

    @PostMapping("/create")
    public String createAddon(HttpSession session,
                              Model model,
                              @Valid @ModelAttribute("addon") AddonDto addonDto,
                              BindingResult errors,
                              @RequestParam("image") MultipartFile image,
                              @RequestParam("binaryContent") MultipartFile binaryContent) {
        if (errors.hasErrors()) {
            return "addons/addon-create";
        }

        try {
            User loggedUser = getAuthenticationHelper().tryGetUser(session);
            Addon addon = addonMapper.fromDto(addonDto, loggedUser);
            setBinaryData(addon, binaryContent, image);
            addonService.create(addon);
            return "redirect:/";
        } catch (AuthenticationFailureException | EntityNotFoundException |
                DuplicateEntityException | IOException |
                UnauthorizedOperationException | IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/{id}/update")
    public String update(@PathVariable int id,
                         HttpSession session,
                         Model model) {
        try {
            Addon addon = addonService.getById(id);
            AddonDto addonDto = addonMapper.toDto(addon);
            model.addAttribute("addon", addonDto);
            return "addons/addon-update";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/{id}/update")
    public String updateAddon(@PathVariable int id,
                              @Valid @ModelAttribute("addon") AddonDto addonDto,
                              BindingResult errors,
                              Model model, HttpSession session,
                              @RequestParam("image") MultipartFile image,
                              @RequestParam("binaryContent") MultipartFile binaryContent) {
        if (errors.hasErrors()) {
            return "addons/addon-update";
        }
        try {
            Addon addon = addonMapper.fromDto(addonDto, id);
            setBinaryData(addon, binaryContent, image);
            addonService.update(addon);
            return "redirect:/";
        } catch (AuthenticationFailureException | EntityNotFoundException |
                DuplicateEntityException | UnauthorizedOperationException | IOException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteAddon(@PathVariable int id,
                              HttpSession session,
                              Model model) {
        try {
            addonService.delete(id);
            return "redirect:/";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/approve")
    public String approve(Model model) {
        try {
            List<Addon> notApprovedAddons = addonService.getByStatus("Pending");
            model.addAttribute("addons", notApprovedAddons);
            return "addons/addons-approve";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/{id}/approve")
    public String approveAddon(@PathVariable int id, Model model) {
        try {
            Addon addon = addonService.getById(id);
            addon.setStatus(statusService.getById(2));
            addonService.update(addon);
            return "redirect:/auth/addon/approve";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/{id}/rate")
    public String rateAddon(@PathVariable int id,
                            Model model, @RequestParam("rate") int rate) {
        try {
            Addon addon = addonService.getById(id);
            setRating(addon, rate);
            addonService.update(addon);
            return "redirect:/addon/{id}";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @ModelAttribute("populatedIdes")
    public List<IDE> populateIdes() {
        return ideService.getAll();
    }

    @ModelAttribute("populatedTags")
    public List<Tag> populatedTags() {
        return tagService.getAll();
    }
}
