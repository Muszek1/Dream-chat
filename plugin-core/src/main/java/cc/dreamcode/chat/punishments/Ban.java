package cc.dreamcode.chat.punishments;

import eu.okaeri.configs.OkaeriConfig;

public class Ban extends OkaeriConfig {
    private String reason;
    private String bannedBy;
    private long date;
    private long until; // 0 = permanent

    public Ban(String reason, String bannedBy, long date, long until) {
        this.reason = reason;
        this.bannedBy = bannedBy;
        this.date = date;
        this.until = until;
    }

    public String getReason() { return reason; }
    public String getBannedBy() { return bannedBy; }
    public long getDate() { return date; }
    public long getUntil() { return until; }

    public void setReason(String reason) { this.reason = reason; }
    public void setDate(long date) { this.date = date; }
}
