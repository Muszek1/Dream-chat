package cc.dreamcode.chat.punishments;

public class IpBan {
    private String ip;
    private String reason;
    private String bannedBy;
    private long date;
    private long until;

    public IpBan(String ip, String reason, String bannedBy, long date, long until) {
        this.ip = ip;
        this.reason = reason;
        this.bannedBy = bannedBy;
        this.date = date;
        this.until = until;
    }

    public String getIp() { return ip; }
    public String getReason() { return reason; }
    public String getBannedBy() { return bannedBy; }
    public long getDate() { return date; }
    public long getUntil() { return until; }

    public void setIp(String ip) { this.ip = ip; }
    public void setReason(String reason) { this.reason = reason; }
    public void setDate(long date) { this.date = date; }
}
