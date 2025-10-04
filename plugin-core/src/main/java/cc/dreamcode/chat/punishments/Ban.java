package cc.dreamcode.chat.punishments;

import eu.okaeri.configs.OkaeriConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Ban extends OkaeriConfig {
    @Setter
    private String reason;
    private final String bannedBy;
    @Setter
    private long date;
    private final long until;

    public Ban(String reason, String bannedBy, long date, long until) {
        this.reason = reason;
        this.bannedBy = bannedBy;
        this.date = date;
        this.until = until;
    }

}
