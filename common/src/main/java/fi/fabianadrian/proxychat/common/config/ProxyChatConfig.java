package fi.fabianadrian.proxychat.common.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.util.List;

@ConfigSerializable
public final class ProxyChatConfig {

    private FormatSection formats = new FormatSection();
    @Comment("Placeholders: <name>")
    private List<String> welcomeMessage = List.of(
            "<rainbow>----------------------------------------</rainbow>",
            "        Welcome to our network, <name>!",
            "<rainbow>----------------------------------------</rainbow>"
    );

    public FormatSection formats() {
        return this.formats;
    }

    public List<String> welcomeMessage() {
        return this.welcomeMessage;
    }

    @ConfigSerializable
    public static final class FormatSection {
        private String msg = "<gold>[<red><sender> -> <receiver></red>]</gold> <message>";
        private String msgSpy = "<gold>[<aqua><sender> -> <receiver></aqua>]</gold> <message>";
        private String broadcast = "<white>[<green>Broadcast</green>]</white> <message>";

        public String msg() {
            return this.msg;
        }

        public String msgSpy() {
            return this.msgSpy;
        }

        public String broadcast() {
            return this.broadcast;
        }
    }
}
