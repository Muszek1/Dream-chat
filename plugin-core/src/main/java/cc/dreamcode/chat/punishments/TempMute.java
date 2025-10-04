package cc.dreamcode.chat.punishments;

import eu.okaeri.configs.OkaeriConfig;

public class TempMute extends OkaeriConfig {
    private String reason;
    private String mutedBy;
    private long date;
    private long until;

    public TempMute(String reason, String mutedBy, long date, long until) {
        this.reason = reason;
        this.mutedBy = mutedBy;
        this.date = date;
        this.until = until;
    }

    public String getReason() { return reason; }
    public String getMutedBy() { return mutedBy; }
    public long getDate() { return date; }
    public long getUntil() { return until; }

    public void setReason(String reason) { this.reason = reason; }
    public void setMutedBy(String mutedBy) { this.mutedBy = mutedBy; }
    public void setDate(long date) { this.date = date; }
    public void setUntil(long until) { this.until = until; }
}
