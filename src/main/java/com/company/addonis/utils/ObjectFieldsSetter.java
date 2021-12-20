package com.company.addonis.utils;

import com.company.addonis.exceptions.EntityNotFoundException;
import com.company.addonis.models.Addon;
import com.company.addonis.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.company.addonis.utils.ObjectFilesGetter.getAddonImage;
import static com.company.addonis.utils.ObjectFilesGetter.getUserImage;

public class ObjectFieldsSetter {

    public static void setBinaryData(Addon addon,
                                     MultipartFile file,
                                     MultipartFile image) throws IOException {
        if (image.isEmpty() || file.isEmpty()) {
            throw new EntityNotFoundException("Image or Content are empty");
        }

        addon.setImage(image.getBytes());
        addon.setBinaryContent(file.getBytes());
    }

    public static void setRating(Addon addon, int score) {
        addon.setTotalScore(addon.getTotalScore() + score);
        addon.setTotalVoters(addon.getTotalVoters() + 1);
    }

    public static void setAddonImageString(List<Addon> addons) {
        addons.forEach(addon -> addon.setImageString(getAddonImage(addon)));
    }

    public static void setAddonImageString(Addon addon) {
        addon.setImageString(getAddonImage(addon));
    }

    public static void setAddonTotalScore(Addon addon) {
        addon.setCalculatedScore(addon.getTotalScore() / (addon.getTotalVoters() * 1.0));
    }

    public static void setAddonTotalScore(List<Addon> addons) {
        addons.forEach(addon -> addon.setCalculatedScore(addon.getTotalScore() / (addon.getTotalVoters() * 1.0)));
    }

    public static void setAddonCreatorImageString(List<Addon> addons) {
        addons.forEach(addon -> setUserImageString(addon.getCreator()));
    }

    public static void setAddonCreatorImageString(Addon addon) {
        setUserImageString(addon.getCreator());
    }

    public static void setUserImageString(User user) {
        user.setImage(getUserImage(user));
    }
}
