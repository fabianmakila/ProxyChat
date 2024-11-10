package fi.fabianadrian.proxychat.common.user;

public final class MessageSettings {
	private PrivacySetting privacySetting = PrivacySetting.EVERYONE;
	private boolean spy = false;
	private boolean announcements = true;
	private boolean globalChat = true;

	public PrivacySetting privacySetting() {
		return this.privacySetting;
	}

	public void privacySetting(PrivacySetting setting) {
		this.privacySetting = setting;
	}

	public boolean spy() {
		return this.spy;
	}

	public void spy(boolean value) {
		this.spy = value;
	}

	public boolean announcements() {
		return announcements;
	}

	public void announcements(boolean visible) {
		this.announcements = visible;
	}

	public boolean globalChat() {
		return this.globalChat;
	}

	public void globalChat(boolean visible) {
		this.globalChat = visible;
	}

	public enum PrivacySetting {
		NOBODY, FRIENDS, EVERYONE
	}
}
