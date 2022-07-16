package fi.fabianadrian.proxychat.api.event;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.proxy.Player;
import fi.fabianadrian.proxychat.api.channel.Channel;

import java.util.Objects;

public final class ChannelMessageEvent implements ResultedEvent<ResultedEvent.GenericResult> {

    private final Channel channel;
    private final Player sender;
    private final String message;

    private GenericResult result = GenericResult.allowed();

    public ChannelMessageEvent(Channel channel, Player sender, String message) {
        this.channel = channel;
        this.sender = sender;
        this.message = message;
    }

    public Channel channel() {
        return this.channel;
    }

    public Player sender() {
        return sender;
    }

    public String message() {
        return message;
    }

    @Override
    public GenericResult getResult() {
        return result;
    }

    @Override
    public void setResult(GenericResult result) {
        this.result = Objects.requireNonNull(result);
    }
}
