package cc.dreamcode.chat.punishments;

import eu.okaeri.configs.OkaeriConfig;

public class Kick extends OkaeriConfig {
    private String reason;
    private String kickedBy;
    private long date;

    public Kick(String reason, String kickedBy, long date) {
        this.reason = reason;
        this.kickedBy = kickedBy;
        this.date = date;
    }

    public String getReason() { return reason; }
    public String getKickedBy() { return kickedBy; }
    public long getDate() { return date; }

    public void setReason(String reason) { this.reason = reason; }

    public void setDate(long date) { this.date = date; }
}
