package cc.dreamcode.chat.command;

import cc.dreamcode.chat.service.BanService;
import cc.dreamcode.chat.utils.TimeParser;
import cc.dreamcode.command.CommandBase;
import cc.dreamcode.command.annotation.*;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

@Command(name = "tempban")
@Permission("dream-chat.tempban")
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class TempbanCommand implements CommandBase {

    private final BanService banService;
    private final cc.dreamcode.chat.config.MessageConfig messageConfig;

    @Completion(arg = "target", value = "@allplayers")
    @Executor(description = "Banuje gracza tymczasowo.")
    public void tempBanPlayer(CommandSender sender,
                              @Arg("target") OfflinePlayer target,
                              @Arg("banExpire") String banExpire,
                              @OptArg("reason") String reason) {

        if (reason == null || reason.isEmpty()) {
            reason = this.messageConfig.defaultReason;
        }

        long millis = TimeParser.parseToMillis(banExpire);
        this.banService.createTempBan(sender, target.getUniqueId(), target.getName(), millis, reason);
    }
}
