package fi.fabianadrian.proxychat.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public final class ProxyChatConfig {

    private FormatSection formats = new FormatSection();

    public FormatSection formats() {
        return this.formats;
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
