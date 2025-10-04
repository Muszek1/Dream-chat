package cc.dreamcode.chat.command;

import cc.dreamcode.chat.config.MessageConfig;
import cc.dreamcode.chat.service.BanService;
import cc.dreamcode.command.CommandBase;
import cc.dreamcode.command.annotation.*;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

@Command(name = "ban")
@Permission("dream-chat.ban")
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class BanCommand implements CommandBase {

    private final BanService banService;
    private final MessageConfig messageConfig;

    @Completion(arg = "target", value = "@allplayers")
    @Executor(description = "Banuje gracza.")
    public void banPlayer(CommandSender sender,
                          @Arg("target") OfflinePlayer target,
                          @OptArg("reason") String reason) {

        if (target == null || target.getUniqueId() == null) {
            sender.sendMessage("§cNie znaleziono gracza.");
            return;
        }

        if (reason == null || reason.trim().isEmpty()) {
            reason = this.messageConfig.defaultReason;
        }

        this.banService.createBan(sender, target.getUniqueId(), target.getName(), reason);
    }
}
