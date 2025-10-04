package cc.dreamcode.chat.listener;

import cc.dreamcode.chat.config.MessageConfig;
import cc.dreamcode.chat.profile.Profile;
import cc.dreamcode.chat.profile.ProfileRepository;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class MultiAccountListener implements Listener {

    private final ProfileRepository profileRepository;
    private final MessageConfig messageConfig;

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        String ip = event.getAddress().getHostAddress();
        String joiningName = event.getName();

        Profile joiningProfile = profileRepository.findOrCreate(event.getUniqueId(), joiningName);
        joiningProfile.setLastIp(ip);
        profileRepository.save(joiningProfile);

        List<Profile> sameIpProfiles = profileRepository.findAll().stream()
                .filter(profile -> profile.getLastIp() != null)
                .filter(profile -> profile.getLastIp().equals(ip))
                .filter(profile -> !profile.getUniqueId().equals(event.getUniqueId()))
                .collect(Collectors.toList());

        if (sameIpProfiles.isEmpty()) {
            return;
        }

        Bukkit.getScheduler().runTask(
                Bukkit.getPluginManager().getPlugin("Dream-Chat"),
                () -> {
                    for (Player admin : Bukkit.getOnlinePlayers()) {
                        if (admin.hasPermission("dream-chat.alert.multikonto")) {
                            for (Profile profile : sameIpProfiles) {
                                if (profile.isBanned()) {
                                    messageConfig.multiAccountAlert
                                            .with("joining", joiningName)
                                            .with("banned", profile.getName())
                                            .send(admin);
                                } else {
                                    messageConfig.multiAccountAlert
                                            .with("joining", joiningName)
                                            .with("banned", profile.getName() + " (no ban)")
                                            .send(admin);
                                }
                            }
                        }
                    }
                }
        );
    }
}
