package cc.dreamcode.bans.listener;

import cc.dreamcode.bans.config.MessageConfig;
import cc.dreamcode.bans.config.PluginConfig;
import cc.dreamcode.bans.profile.Profile;
import cc.dreamcode.bans.profile.ProfileRepository;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class BanListener implements Listener {

    private final ProfileRepository profileRepository;
    private final MessageConfig messageConfig;
    private final PluginConfig pluginConfig;

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        final String name = event.getName();
        final UUID uuid = event.getUniqueId();


        if (this.pluginConfig.blacklistPlayers.stream()
                .anyMatch(player -> player.equalsIgnoreCase(name))) {

            String kickMsg = this.messageConfig.blacklistKick
                    .replace("{reason}", "Blacklista z configu")
                    .replace("{blacklistedBy}", "Console")
                    .replace("&", "§");

            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, kickMsg);
            return;
        }

        final Profile profile = this.profileRepository.findOrCreate(uuid, name);
        long now = Instant.now().toEpochMilli();

        profile.setLastIp(event.getAddress().getHostAddress());
        this.profileRepository.save(profile);

        if (profile.getBanUntil() > 0 && profile.getBanUntil() <= now) {
            profile.setBanned(false);
            profile.setBanReason(null);
            profile.setBannedBy(null);
            profile.setBanUntil(0);
            this.profileRepository.save(profile);
            return;
        }

        String kickMsg;
        if (profile.isBanned() && profile.getBanUntil() == 0) {
            String reason = profile.getBanReason() == null ? "" : profile.getBanReason();
            String bannedBy = profile.getBannedBy() == null ? "" : profile.getBannedBy();

            kickMsg = String.valueOf(this.messageConfig.banKick
                    .replace("{reason}", reason)
                    .replace("{bannedBy}", bannedBy))
                    .replace("&", "§");;

            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, kickMsg);
        } else if (profile.isBanned() && profile.getBanUntil() > 0) {
            String reason = profile.getBanReason() == null ? "" : profile.getBanReason();
            String bannedBy = profile.getBannedBy() == null ? "" : profile.getBannedBy();
            long banUntil = profile.getBanUntil();

            kickMsg = String.valueOf(this.messageConfig.tempBanKick
                    .replace("{reason}", reason)
                    .replace("{bannedBy}", bannedBy)
                    .replace("{banExpire}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(banUntil))))
                    .replace("&", "§");

            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, kickMsg);
        }

        String ip = event.getAddress().getHostAddress();

        if (profile.hasIpBan(ip)) {
            String reason = profile.getBanReason() == null ? "" : profile.getBanReason();
            String bannedBy = profile.getBannedBy() == null ? "" : profile.getBannedBy();

             kickMsg = this.messageConfig.ipBanKick
                    .replace("{reason}", reason)
                    .replace("{bannedBy}", bannedBy)
                    .replace("&", "§");

            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, kickMsg);
        }


    }
}
