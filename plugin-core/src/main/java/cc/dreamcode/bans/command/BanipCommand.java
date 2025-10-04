package cc.dreamcode.bans.command;

import cc.dreamcode.bans.profile.Profile;
import cc.dreamcode.bans.profile.ProfileRepository;
import cc.dreamcode.bans.service.BanService;
import cc.dreamcode.command.CommandBase;
import cc.dreamcode.command.annotation.*;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

@Command(name = "banip")
@Permission("dream-bans.banip")
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class BanipCommand implements CommandBase {

    private final BanService banService;
    private final ProfileRepository profileRepository;
    private final cc.dreamcode.bans.config.MessageConfig messageConfig;

    @Executor(description = "Banuje gracza po IP.")
    public void banIpPlayer(CommandSender sender,
                            @Arg("target") OfflinePlayer target,
                            @OptArg("reason") String reason) {

        if (reason == null || reason.isEmpty()) {
            reason = this.messageConfig.defaultReason;
        }

        Profile profile = this.profileRepository.findOrCreate(target.getUniqueId(), target.getName());
        String ip = profile.getLastIp();

        if (ip == null || ip.isEmpty()) {
            this.messageConfig.noIpFound
                    .with("player", target.getName())
                    .send(sender);
            return;
        }

        this.banService.createIpBan(sender, target.getUniqueId(), target.getName(), ip, reason);
    }
}

