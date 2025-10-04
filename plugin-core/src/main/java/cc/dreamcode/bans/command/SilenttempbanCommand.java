package cc.dreamcode.bans.command;

import cc.dreamcode.bans.service.BanService;
import cc.dreamcode.command.CommandBase;
import cc.dreamcode.command.annotation.*;
import cc.dreamcode.utilities.ParseUtil;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.time.Duration;
import java.util.Optional;

@Command(name = "silenttempban")
@Permission("dream-bans.silenttempban")
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SilenttempbanCommand implements CommandBase {

    private final BanService banService;
    private final cc.dreamcode.bans.config.MessageConfig messageConfig;

    @Completion(arg = "target", value = "@allplayers")
    @Executor(description = "Cicho banuje gracza tymczasowo.")
    public void banPlayer(CommandSender sender,
                          @Arg("target") OfflinePlayer target,
                          @Arg("banExpire") String banExpire,
                          @OptArg("reason") String reason) {

        if (reason == null || reason.isEmpty()) {
            reason = this.messageConfig.defaultReason;
        }

        Optional<Duration> durationOpt = ParseUtil.parsePeriod(banExpire);
        if (durationOpt.isEmpty()) {
            this.messageConfig.invalidFormat.with("input", banExpire).send(sender);
            return;
        }
        long millis = durationOpt.get().toMillis();
        long expireAt = System.currentTimeMillis() + millis;
        String formatted = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new java.util.Date(expireAt));

        this.banService.createTempBan(sender, target.getUniqueId(), target.getName(), millis, reason);

        String finalReason = reason;
        Bukkit.getOnlinePlayers().forEach(online -> {
            if (online.hasPermission("dream-chat.silentinfo")) {
                this.messageConfig.silentTempBanNotify
                        .with("player", target.getName())
                        .with("issuer", sender.getName())
                        .with("banExpire", formatted)
                        .with("reason", finalReason)
                        .send(online);
            }
        });

        this.messageConfig.silentTempBanSuccess
                .with("player", target.getName())
                .with("issuer", sender.getName())
                .with("banExpire", formatted)
                .with("reason", reason)
                .send(sender);
    }
}

