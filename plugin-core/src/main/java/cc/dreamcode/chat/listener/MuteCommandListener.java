package cc.dreamcode.chat.listener;

import cc.dreamcode.chat.config.PluginConfig;
import cc.dreamcode.chat.config.MessageConfig;
import cc.dreamcode.chat.profile.Profile;
import cc.dreamcode.chat.profile.ProfileRepository;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class MuteCommandListener implements Listener {

    private final ProfileRepository profileRepository;
    private final PluginConfig pluginConfig;
    private final MessageConfig messageConfig;

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.profileRepository.findOrCreate(player.getUniqueId(), player.getName());

        if (!profile.isMuted()) {
            return;
        }

        String cmd = event.getMessage().split(" ")[0].toLowerCase();
        for (String blocked : this.pluginConfig.muteBlockedCommands) {
            if (cmd.equalsIgnoreCase(blocked.toLowerCase())) {
                event.setCancelled(true);
                this.messageConfig.muteCommandBlocked.send(player);
                return;
            }
        }
    }
}
