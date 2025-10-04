package cc.dreamcode.chat.punishments;

import eu.okaeri.configs.OkaeriConfig;

public class TempBan extends OkaeriConfig {
    private String reason;
    private String bannedBy;
    private long date;   // kiedy nadano bana
    private long until;  // czas zakończenia

    public TempBan() {}

    public TempBan(String reason, String bannedBy, long date, long until) {
        this.reason = reason;
        this.bannedBy = bannedBy;
        this.date = date;
        this.until = until;
    }

    // --- GETTERY ---
    public String getReason() { return reason; }
    public String getBannedBy() { return bannedBy; }
    public long getDate() { return date; }
    public long getUntil() { return until; }

    // --- SETTERY ---
    public void setReason(String reason) { this.reason = reason; }
    public void setBannedBy(String bannedBy) { this.bannedBy = bannedBy; }
    public void setDate(long date) { this.date = date; }
    public void setUntil(long until) { this.until = until; }
}
