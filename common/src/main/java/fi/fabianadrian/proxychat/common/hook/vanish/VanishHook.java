package fi.fabianadrian.proxychat.common.hook.vanish;

import fi.fabianadrian.proxychat.common.user.User;

public interface VanishHook {
    boolean canSee(User user1, User user2);

    static VanishHook empty() {
        return (user1, user2) -> false;
    }
}
