package fi.fabianadrian.proxychat.api.event;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.proxy.Player;

import java.util.Objects;

public final class PrivateMessageEvent implements ResultedEvent<ResultedEvent.GenericResult> {

    private final Player sender;
    private final Player recipient;
    private final String message;

    private ResultedEvent.GenericResult result = ResultedEvent.GenericResult.allowed();

    public PrivateMessageEvent(Player sender, Player recipient, String message) {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
    }

    public Player sender() {
        return this.sender;
    }

    public Player recipient() {
        return this.recipient;
    }

    public String message() {
        return this.message;
    }

    @Override
    public GenericResult getResult() {
        return this.result;
    }

    @Override
    public void setResult(ResultedEvent.GenericResult result) {
        this.result = Objects.requireNonNull(result);
    }
}
