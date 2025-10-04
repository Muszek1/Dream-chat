package cc.dreamcode.bans.command;

import cc.dreamcode.bans.service.BanService;
import cc.dreamcode.bans.profile.Profile;
import cc.dreamcode.bans.profile.ProfileRepository;
import cc.dreamcode.command.CommandBase;
import cc.dreamcode.command.annotation.*;
import cc.dreamcode.notice.bukkit.BukkitNotice;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

@Command(name = "unbanip")
@Permission("dream-bans.unbanip")
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class UnbanipCommand implements CommandBase {

    private final BanService banService;
    private final ProfileRepository profileRepository;
    private final cc.dreamcode.bans.config.MessageConfig messageConfig;

    @Executor(description = "Odbanowuje gracza po IP.")
    public BukkitNotice unbanIpPlayer(CommandSender sender,
                                      @Arg("target") OfflinePlayer target) {

        Profile profile = this.profileRepository.findOrCreate(target.getUniqueId(), target.getName());
        String ip = profile.getLastIp();

        if (ip == null || ip.isEmpty()) {
            return this.messageConfig.noIpFound
                    .with("player", target.getName());
        }

        this.banService.removeIpBan(sender, ip);

        return this.messageConfig.ipUnbanSuccess
                .with("player", target.getName())
                .with("ip", ip);
    }

}
