package org.example.session;

public class SessionManager {
    private static String currentUsername;

    public static void login(String username) {
        currentUsername = username;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static void logout() {
        currentUsername = null;
    }
}
