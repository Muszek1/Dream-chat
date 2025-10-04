package cc.dreamcode.chat.punishments;

import eu.okaeri.configs.OkaeriConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
public class TempMute extends OkaeriConfig {
    @Setter
    private String reason;
    private final String mutedBy;
    @Setter
    private long date;
    private final long until;

    public TempMute(String reason, String mutedBy, long date, long until) {
        this.reason = reason;
        this.mutedBy = mutedBy;
        this.date = date;
        this.until = until;
    }

}
