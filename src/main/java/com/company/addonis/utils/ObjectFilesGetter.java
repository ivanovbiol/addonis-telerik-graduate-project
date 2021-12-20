package com.company.addonis.utils;

import com.company.addonis.models.Addon;
import com.company.addonis.models.User;

import java.util.Base64;

public class ObjectFilesGetter {

    public static String getAddonImage(Addon addon) {
        if (addon.getImage() == null) {
            return null;
        }
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(addon.getImage());
    }

    public static String getUserImage(User user) {
        if (user.getPhoto() == null) {
            return null;
        }
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(user.getPhoto());
    }
}
