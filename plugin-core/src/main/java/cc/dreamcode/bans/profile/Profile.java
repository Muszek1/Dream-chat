package cc.dreamcode.bans.profile;

import cc.dreamcode.bans.punishments.*;
import eu.okaeri.configs.annotation.CustomKey;
import eu.okaeri.persistence.document.Document;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Profile extends Document {

    @Setter
    @Getter
    @CustomKey("name")
    private String name;

    @Setter
    @Getter
    @CustomKey("banned")
    private boolean banned;

    @Setter
    @Getter
    @CustomKey("ban-reason")
    private String banReason;

    @Setter
    @Getter
    @CustomKey("banned-by")
    private String bannedBy;

    @Getter
    @Setter
    @CustomKey("ban-until")
    private long banUntil;

    @Setter
    @Getter
    @CustomKey("last-ip")
    private String lastIp;

    @Getter
    @CustomKey("muted")
    private boolean muted;

    @Getter
    @CustomKey("mute-reason")
    private String muteReason;

    @Getter
    @CustomKey("muted-by")
    private String mutedBy;

    @Setter
    @Getter
    @CustomKey("mute-until")
    private long muteUntil;

    // ==== HISTORIA KAR ====
    @CustomKey("bans")
    private java.util.List<Ban> bans = new java.util.ArrayList<>();
    public java.util.List<Ban> getBans() {
        if (this.bans == null) this.bans = new java.util.ArrayList<>();
        return this.bans;
    }

    @CustomKey("tempbans")
    private java.util.List<TempBan> tempBans = new java.util.ArrayList<>();
    public java.util.List<TempBan> getTempBans() {
        if (this.tempBans == null) this.tempBans = new java.util.ArrayList<>();
        return this.tempBans;
    }

    @CustomKey("kicks")
    private java.util.List<Kick> kicks = new java.util.ArrayList<>();
    public java.util.List<Kick> getKicks() {
        if (this.kicks == null) this.kicks = new java.util.ArrayList<>();
        return this.kicks;
    }

    @CustomKey("mutes")
    private java.util.List<Mute> mutes = new java.util.ArrayList<>();
    public java.util.List<Mute> getMutes() {
        if (this.mutes == null) this.mutes = new java.util.ArrayList<>();
        return this.mutes;
    }

    @CustomKey("tempmutes")
    private java.util.List<TempMute> tempMutes = new java.util.ArrayList<>();
    public java.util.List<TempMute> getTempMutes() {
        if (this.tempMutes == null) this.tempMutes = new java.util.ArrayList<>();
        return this.tempMutes;
    }

    @CustomKey("ip-bans")
    private java.util.List<IpBan> ipBansCheck = new java.util.ArrayList<>();

    public java.util.List<IpBan> getIpBansCheck() {
        if (this.ipBansCheck == null) {
            this.ipBansCheck = new java.util.ArrayList<>();
        }
        return this.ipBansCheck;
    }


    @CustomKey("warns")
    private List<Warn> warns = new ArrayList<>();

    public List<Warn> getWarns() {
        if (this.warns == null) {
            this.warns = new ArrayList<>();
        }
        return this.warns;
    }


    // ==== KONSTRUKTOR ====
    public Profile(UUID uuid) {}

    public UUID getUniqueId() {
        return this.getPath().toUUID();
    }


    public void ban(String reason, String admin) {
        this.banned = true;
        this.banReason = reason;
        this.bannedBy = admin;
    }

    public void unban() {
        this.banned = false;
        this.banReason = null;
        this.bannedBy = null;
        this.banUntil = 0;
    }

    public void mute(String reason, String admin) {
        this.muted = true;
        this.muteReason = reason;
        this.mutedBy = admin;
    }

    public void unmute() {
        this.muted = false;
        this.muteReason = null;
        this.mutedBy = null;
        this.muteUntil = 0;
    }

    public void banIp(String ip, String reason, String admin) {
        IpBan ipBan = new IpBan(ip, reason, admin, System.currentTimeMillis(), 0L);
        this.getIpBansCheck().add(ipBan);
    }

    public void unbanIp(String ip) {
        this.getIpBansCheck().removeIf(b -> b.getIp().equals(ip));
    }

    public boolean hasIpBan(String ip) {
        return this.getIpBansCheck().stream()
                .anyMatch(b -> b.getIp().equals(ip) &&
                        (b.getUntil() == 0L || b.getUntil() > System.currentTimeMillis()));
    }


}
