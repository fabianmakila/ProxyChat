package fi.fabianadrian.proxychat.common.hook.vanish;

import fi.fabianadrian.proxychat.common.user.User;

public interface VanishHook {
	static VanishHook empty() {
		return (user1, user2) -> true;
	}

	boolean canSee(User user1, User user2);
}
