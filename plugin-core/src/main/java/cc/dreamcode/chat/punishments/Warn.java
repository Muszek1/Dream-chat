package cc.dreamcode.chat.punishments;

import eu.okaeri.configs.OkaeriConfig;

public class Warn extends OkaeriConfig {
    private String reason;
    private String warnedBy;
    private long date;

    public Warn(String reason, String warnedBy, long date) {
        this.reason = reason;
        this.warnedBy = warnedBy;
        this.date = date;
    }

    public String getReason() { return reason; }
    public String getWarnedBy() { return warnedBy; }
    public long getDate() { return date; }

    public void setReason(String reason) { this.reason = reason; }
    public void setWarnedBy(String warnedBy) { this.warnedBy = warnedBy; }
    public void setDate(long date) { this.date = date; }
}