package cc.dreamcode.chat.command;

import cc.dreamcode.chat.profile.Profile;
import cc.dreamcode.chat.profile.ProfileRepository;
import cc.dreamcode.chat.punishments.Warn;
import cc.dreamcode.command.CommandBase;
import cc.dreamcode.command.annotation.*;
import cc.dreamcode.notice.bukkit.BukkitNotice;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "warn")
@Permission("dream-chat.warn")
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class WarnCommand implements CommandBase {

    private final cc.dreamcode.chat.config.MessageConfig messageConfig;
    private final ProfileRepository profileRepository;

    @Completion(arg = "target", value = "@allplayers")
    @Executor(description = "Ostrzega gracza.")
    public BukkitNotice warnPlayer(CommandSender sender,
                                   @Arg("target") Player target,
                                   @OptArg("reason") String reason) {
        if (reason == null || reason.isEmpty()) {
            reason = this.messageConfig.defaultReason;
        }

        Profile profile = this.profileRepository.findOrCreate(target.getUniqueId(), target.getName());
        Warn warn = new Warn(reason, sender.getName(), System.currentTimeMillis());
        profile.getWarns().add(warn);
        this.profileRepository.save(profile);

        this.messageConfig.actionBarWarn
                .with("player", target.getName())
                .with("issuer", sender.getName())
                .with("reason", reason)
                .with("&", "§")
                .send(target);

        return this.messageConfig.warnNotify
                .with("player", target.getName())
                .with("issuer", sender.getName())
                .with("reason", reason);


    }
}