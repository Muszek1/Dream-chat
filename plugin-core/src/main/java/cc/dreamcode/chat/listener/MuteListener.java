package cc.dreamcode.chat.listener;

import cc.dreamcode.chat.config.MessageConfig;
import cc.dreamcode.chat.profile.Profile;
import cc.dreamcode.chat.profile.ProfileRepository;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class MuteListener implements Listener {

    private final ProfileRepository profileRepository;
    private final MessageConfig messageConfig;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.profileRepository.findOrCreate(player.getUniqueId(), player.getName());
        long now = Instant.now().toEpochMilli();

        if (profile.getMuteUntil() > 0 && profile.getMuteUntil() <= now) {
            profile.unmute();
            this.profileRepository.save(profile);
            return;
        }

        if (profile.isMuted()) {
            event.setCancelled(true);

            if (profile.getMuteUntil() > 0) {
                this.messageConfig.tempMuteChat
                        .with("reason", profile.getMuteReason() == null ? "" : profile.getMuteReason())
                        .with("mutedBy", profile.getMutedBy() == null ? "" : profile.getMutedBy())
                        .with("muteExpire", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                .format(new Date(profile.getMuteUntil())))
                        .send(player);

            } else {
                this.messageConfig.muteChat
                        .with("reason", profile.getMuteReason() == null ? "" : profile.getMuteReason())
                        .with("mutedBy", profile.getMutedBy() == null ? "" : profile.getMutedBy())
                        .send(player);
            }
        }
    }
}
