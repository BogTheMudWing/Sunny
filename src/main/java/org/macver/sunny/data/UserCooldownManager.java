package org.macver.sunny.data;

import net.dv8tion.jda.api.entities.User;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserCooldownManager {

    private final Map<User, Instant> userQueryTimeMap = new HashMap<>();
    private final Duration coolDown = Duration.ofMinutes(10);

    public void markInteraction(User user) {
        userQueryTimeMap.put(user, Instant.now());
        clearOldUsers();
    }

    public boolean isUserOnCoolDown(User user) {
        Instant instant = userQueryTimeMap.get(user);
        return (instant != null && Duration.between(userQueryTimeMap.get(user), Instant.now()).compareTo(coolDown) < 0);
    }

    public void clearOldUsers() {
        Set<User> users = Set.copyOf(userQueryTimeMap.keySet());
        for (User user : users) {
            if (Duration.between(userQueryTimeMap.get(user), Instant.now()).compareTo(coolDown) > 0) {
                userQueryTimeMap.remove(user);
            }
        }
    }

}
