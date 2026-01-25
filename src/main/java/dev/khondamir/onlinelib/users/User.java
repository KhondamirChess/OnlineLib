package dev.khondamir.onlinelib.users;

public record User(
        Long id,
        String login,
        UserRole role
) {
}
