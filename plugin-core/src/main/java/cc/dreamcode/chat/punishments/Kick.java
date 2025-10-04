package cc.dreamcode.chat.punishments;

import eu.okaeri.configs.OkaeriConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Kick extends OkaeriConfig {
    @Setter
    private String reason;
    private final String kickedBy;
    @Setter
    private long date;

    public Kick(String reason, String kickedBy, long date) {
        this.reason = reason;
        this.kickedBy = kickedBy;
        this.date = date;
    }

}
