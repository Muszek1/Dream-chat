package cc.dreamcode.chat.command;

import cc.dreamcode.chat.config.MessageConfig;
import cc.dreamcode.chat.profile.Profile;
import cc.dreamcode.chat.profile.ProfileRepository;
import cc.dreamcode.chat.punishments.Kick;
import cc.dreamcode.command.CommandBase;
import cc.dreamcode.command.annotation.*;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "kick")
@Permission("dream-chat.kick")
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class KickCommand implements CommandBase {

    private final MessageConfig messageConfig;
    private final ProfileRepository profileRepository;

    @Completion(arg = "target", value = "@allplayers")
    @Executor(description = "Wyrzuca gracza z broadcastem i zapisuje w historii.")
    public void kickPlayer(CommandSender sender,
                           @Arg("target") Player target,
                           @OptArg("reason") String reason) {
        if (target == null) {
            this.messageConfig.playerNotFound.send(sender);
            return;
        }

        if (reason == null || reason.isEmpty()) {
            reason = this.messageConfig.defaultReason;
        }

        String finalReason = reason;

        Profile profile = this.profileRepository.findOrCreate(target.getUniqueId(), target.getName());
        Kick kick = new Kick(finalReason, sender.getName(), System.currentTimeMillis());
        profile.getKicks().add(kick);
        this.profileRepository.save(profile);

        String kickMsg = this.messageConfig.kickFormat
                .replace("{reason}", finalReason)
                .replace("{issuer}", sender.getName())
                .replace("&", "§");
        target.kickPlayer(kickMsg);

        Bukkit.getOnlinePlayers().forEach(online ->
                this.messageConfig.kickBroadcast
                        .with("player", target.getName())
                        .with("issuer", sender.getName())
                        .with("reason", finalReason)
                        .send(online)
        );

        this.messageConfig.kickSuccess
                .with("player", target.getName())
                .with("issuer", sender.getName())
                .with("reason", finalReason)
                .send(sender);
    }
}
