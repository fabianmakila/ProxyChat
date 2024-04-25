package fi.fabianadrian.proxychat.common.user;

public final class MessageSettings {
	private PrivacySetting privacySetting = PrivacySetting.EVERYONE;
	private boolean spy = false;

	public PrivacySetting privacySetting() {
		return this.privacySetting;
	}

	public void privacySetting(PrivacySetting value) {
		this.privacySetting = value;
	}

	public boolean spy() {
		return this.spy;
	}

	public void spy(boolean value) {
		this.spy = value;
	}

	public enum PrivacySetting {
		NOBODY, FRIENDS, EVERYONE
	}
}
