package cc.dreamcode.bans.punishments;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IpBan {
    private final String ip;
    private String reason;
    private final String bannedBy;
    private long date;
    private final long until;

}
