package fi.fabianadrian.proxychat.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;

@ConfigSerializable
public final class AnnouncementsConfig {

    private final List<String> announcements = new ArrayList<>();

    private int interval = 30;

    private boolean random;

    private String prefix = "<gray>[<green>!</green>]</gray> ";

    public int interval() {
        return this.interval;
    }

    public boolean random() {
        return this.random;
    }

    public List<String> announcements() {
        return this.announcements;
    }

    public String prefix() {
        if (this.prefix == null) {
            return "";
        }
        return this.prefix;
    }
}
