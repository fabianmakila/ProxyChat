package fi.fabianadrian.proxychat.common.config;

import fi.fabianadrian.proxychat.common.channel.Channel;
import space.arim.dazzleconf.annote.ConfDefault;
import space.arim.dazzleconf.annote.SubSection;

import java.util.ArrayList;
import java.util.List;

public interface ChannelsConfig {
    static List<Channel> defaultChannels() {
        List<Channel> channelsList = new ArrayList<>();
        channelsList.add(Channel.of(
            "example",
            "[Example] <sender>: <receiver>",
            List.of(
                "examplealias1",
                "examplealias2"
            )
        ));
        return channelsList;
    }

    @ConfDefault.DefaultObject("defaultChannels")
    List<@SubSection Channel> channels();
}
