package cc.dreamcode.chat.command;

import cc.dreamcode.chat.service.BanService;
import cc.dreamcode.chat.utils.TimeParser;
import cc.dreamcode.command.CommandBase;
import cc.dreamcode.command.annotation.*;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

@Command(name = "silenttempban")
@Permission("dream-chat.silenttempban")
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SilenttempbanCommand implements CommandBase {

    private final BanService banService;
    private final cc.dreamcode.chat.config.MessageConfig messageConfig;

    @Completion(arg = "target", value = "@allplayers")
    @Executor(description = "Cicho banuje gracza tymczasowo.")
    public void banPlayer(CommandSender sender,
                          @Arg("target") OfflinePlayer target,
                          @Arg("banExpire") String banExpire,
                          @OptArg("reason") String reason) {

        if (reason == null || reason.isEmpty()) {
            reason = this.messageConfig.defaultReason;
        }

        long millis = TimeParser.parseToMillis(banExpire);
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

