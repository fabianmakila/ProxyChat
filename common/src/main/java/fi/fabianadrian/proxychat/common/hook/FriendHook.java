package fi.fabianadrian.proxychat.common.hook;

import java.util.UUID;

public interface FriendHook {

    boolean areFriends(UUID uuid1, UUID uuid2);

    static FriendHook empty() {
        return (uuid1, uuid2) -> false;
    }
}
