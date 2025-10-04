package cc.dreamcode.chat.punishments;

import eu.okaeri.configs.OkaeriConfig;

public class Mute extends OkaeriConfig {
    private String reason;
    private String mutedBy;
    private long date;

    public Mute() {}

    public Mute(String reason, String mutedBy, long date) {
        this.reason = reason;
        this.mutedBy = mutedBy;
        this.date = date;
    }

    public String getReason() { return reason; }
    public String getMutedBy() { return mutedBy; }
    public long getDate() { return date; }

    public void setReason(String reason) { this.reason = reason; }
    public void setMutedBy(String mutedBy) { this.mutedBy = mutedBy; }
    public void setDate(long date) { this.date = date; }
}
