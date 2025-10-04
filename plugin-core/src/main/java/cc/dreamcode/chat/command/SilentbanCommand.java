package cc.dreamcode.chat.command;

import cc.dreamcode.chat.service.BanService;
import cc.dreamcode.command.CommandBase;
import cc.dreamcode.command.annotation.*;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

@Command(name = "silentban")
@Permission("dream-chat.silentban")
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SilentbanCommand implements CommandBase {

    private final BanService banService;
    private final cc.dreamcode.chat.config.MessageConfig messageConfig;

    @Completion(arg = "target", value = "@allplayers")
    @Executor(description = "Cicho banuje gracza (bez broadcastu).")
    public void banPlayer(CommandSender sender,
                          @Arg("target") OfflinePlayer target,
                          @OptArg("reason") String reason) {
        if (reason == null || reason.isEmpty()) {
            reason = this.messageConfig.defaultReason;
        }

        this.banService.createBan(sender, target.getUniqueId(), target.getName(), reason);

        String finalReason = reason;
        Bukkit.getOnlinePlayers().forEach(online -> {
            if (online.hasPermission("dream-chat.silentinfo")) {
                this.messageConfig.silentBanNotify
                        .with("player", target.getName())
                        .with("issuer", sender.getName())
                        .with("reason", finalReason)
                        .send(online);
            }
        });

        this.messageConfig.silentBanSuccess
                .with("player", target.getName())
                .with("issuer", sender.getName())
                .with("reason", reason)
                .send(sender);
    }
}

