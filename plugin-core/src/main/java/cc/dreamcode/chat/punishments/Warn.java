package cc.dreamcode.chat.punishments;

import eu.okaeri.configs.OkaeriConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Warn extends OkaeriConfig {
    @Setter
    private String reason;
    private final String warnedBy;
    @Setter
    private long date;

    public Warn(String reason, String warnedBy, long date) {
        this.reason = reason;
        this.warnedBy = warnedBy;
        this.date = date;
    }

}