package cc.dreamcode.chat.punishments;

import eu.okaeri.configs.OkaeriConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Mute extends OkaeriConfig {
    @Setter
    private String reason;
    private final String mutedBy;
    @Setter
    private long date;

    public Mute(String reason, String mutedBy, long date) {
        this.reason = reason;
        this.mutedBy = mutedBy;
        this.date = date;
    }

}
