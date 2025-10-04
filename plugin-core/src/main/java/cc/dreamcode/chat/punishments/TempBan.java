package cc.dreamcode.chat.punishments;

import eu.okaeri.configs.OkaeriConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
public class TempBan extends OkaeriConfig {
    @Setter
    private String reason;
    private final String bannedBy;
    @Setter
    private long date;
    private final long until;


    public TempBan(String reason, String bannedBy, long date, long until) {
        this.reason = reason;
        this.bannedBy = bannedBy;
        this.date = date;
        this.until = until;
    }

}
