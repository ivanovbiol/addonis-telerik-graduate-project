package com.company.addonis.controllers.rest;

import com.company.addonis.controllers.AuthenticationHelper;
import com.company.addonis.exceptions.EntityNotFoundException;
import com.company.addonis.exceptions.UnauthorizedOperationException;
import com.company.addonis.models.Addon;
import com.company.addonis.models.User;
import com.company.addonis.models.dtos.AddonDto;
import com.company.addonis.models.mappers.AddonMapper;
import com.company.addonis.services.contracts.AddonService;
import com.company.addonis.services.contracts.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.company.addonis.utils.ObjectFieldsSetter.setBinaryData;
import static com.company.addonis.utils.ObjectFieldsSetter.setRating;
import static com.company.addonis.utils.UserRoleValidator.*;

@RestController
@RequestMapping("/api/addons")
public class AddonController {

    private final AddonService addonService;
    private final AuthenticationHelper authenticationHelper;
    private final AddonMapper addonMapper;
    private final StatusService statusService;

    @Autowired
    public AddonController(AddonService addonService,
                           AuthenticationHelper authenticationHelper,
                           AddonMapper addonMapper,
                           StatusService statusService) {
        this.addonService = addonService;
        this.authenticationHelper = authenticationHelper;
        this.addonMapper = addonMapper;
        this.statusService = statusService;
    }

    @GetMapping
    public List<Addon> getAll() {
        return addonService.getAll();
    }

    @GetMapping("/{id}")
    public Addon getById(@RequestHeader HttpHeaders headers,
                         @Valid @PathVariable int id) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(headers);
            throwIfNotAdmin(loggedUser);
            return addonService.getById(id);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public void create(@RequestHeader HttpHeaders headers,
                       @Valid @RequestBody AddonDto addonDto) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(headers);
            throwIfNotUser(loggedUser);
            Addon addon = addonMapper.fromDto(addonDto, loggedUser);
            addonService.create(addon);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public void update(@RequestHeader HttpHeaders headers,
                       @Valid @PathVariable int id,
                       @Valid @RequestBody AddonDto addonDto) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(headers);
            throwIfNotAdmin(loggedUser);
            Addon addon = addonMapper.fromDto(addonDto, id);
            addonService.update(addon);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}/binarydata")
    public void uploadBinaryData(@RequestHeader HttpHeaders headers,
                                 @Valid @PathVariable int id,
                                 MultipartFile file,
                                 MultipartFile image) throws IOException {
        try {
            User loggedUser = authenticationHelper.tryGetUser(headers);
            Addon addon = addonService.getById(id);
            throwIfNotAdminAndModifiesOtherUsersAddons(loggedUser, addon);
            setBinaryData(addon, file, image);
            addonService.update(addon);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}/approve")
    public void approve(@RequestHeader HttpHeaders headers,
                        @Valid @PathVariable int id) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(headers);
            throwIfNotAdmin(loggedUser);
            Addon addon = addonService.getById(id);
            addon.setStatus(statusService.getById(2));
            addonService.update(addon);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}/rate")
    public void rate(@RequestHeader HttpHeaders headers,
                     @Valid @PathVariable int id, @Positive int score) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(headers);
            throwIfNotUser(loggedUser);
            Addon addon = addonService.getById(id);
            setRating(addon, score);
            addonService.update(addon);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}/feature")
    public void feature(@RequestHeader HttpHeaders headers,
                        @Valid @PathVariable int id, boolean featured) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(headers);
            throwIfNotAdmin(loggedUser);
            Addon addon = addonService.getById(id);
            addon.setFeatured(featured);
            addonService.update(addon);
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers,
                       @Valid @PathVariable int id) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(headers);
            Addon addon = addonService.getById(id);
            throwIfNotAdminAndModifiesOtherUsersAddons(loggedUser, addon);
            addonService.delete(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/filter")
    public List<Addon> filter(@RequestParam(required = false) Optional<String> addonName,
                              @RequestParam(required = false) Optional<String> ideName,
                              @RequestParam(required = false) Optional<String> featured,
                              @RequestParam(required = false) Optional<String> username,
                              @RequestParam(required = false) Optional<String> sortBy) {
        return addonService.filter(addonName, ideName, featured, username, sortBy);
    }

    @GetMapping("/popular")
    public List<Addon> getPopular(int count) {
        return addonService.getByColumn("downloadCount", count);
    }

    @GetMapping("/newest")
    public List<Addon> getNewest(int count) {
        return addonService.getByColumn("uploadDate", count);
    }
}
