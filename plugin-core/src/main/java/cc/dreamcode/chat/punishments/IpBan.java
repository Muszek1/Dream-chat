package cc.dreamcode.chat.punishments;

import lombok.Getter;
import lombok.Setter;

@Getter
public class IpBan {
    private final String ip;
    @Setter
    private String reason;
    private final String bannedBy;
    @Setter
    private long date;
    private final long until;

    public IpBan(String ip, String reason, String bannedBy, long date, long until) {
        this.ip = ip;
        this.reason = reason;
        this.bannedBy = bannedBy;
        this.date = date;
        this.until = until;
    }

}
