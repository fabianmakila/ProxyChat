package fi.fabianadrian.proxychat.common.hook;

import fi.fabianadrian.proxychat.common.user.User;

public interface VanishPluginHook extends PluginHook {
	boolean canSee(User user1, User user2);
}
