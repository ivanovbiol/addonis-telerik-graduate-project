package com.company.addonis.utils;

import com.company.addonis.exceptions.UnauthorizedOperationException;
import com.company.addonis.models.Addon;
import com.company.addonis.models.User;

public class UserRoleValidator {

    public static final String ADMIN_AUTHORIZATION_ERROR_MESSAGE =
            "You are not authorized to execute this operation because you are not an admin!";
    public static final String USER_AUTHORIZATION_ERROR_MESSAGE =
            "You are not authorized to execute this operation because you are not a user!";
    public static final String YOU_CAN_MODIFY_ONLY_YOUR_ADDONS =
            "You can modify only your addons!";

    public static void throwIfNotUser(User authenticatedUser) {
        if (!authenticatedUser.isUser()) {
            throw new UnauthorizedOperationException(USER_AUTHORIZATION_ERROR_MESSAGE);
        }
    }

    public static void throwIfNotAdmin(User authenticatedUser) {
        if (authenticatedUser.isUser()) {
            throw new UnauthorizedOperationException(ADMIN_AUTHORIZATION_ERROR_MESSAGE);
        }
    }

    public static void throwIfNotAdminAndModifiesOtherUsersAddons(User loggedUser, Addon addon) {
        if (loggedUser.isUser()) {
            if (loggedUser.getId() != addon.getCreator().getId()) {
                throw new UnauthorizedOperationException(YOU_CAN_MODIFY_ONLY_YOUR_ADDONS);
            }
        }
    }
}
