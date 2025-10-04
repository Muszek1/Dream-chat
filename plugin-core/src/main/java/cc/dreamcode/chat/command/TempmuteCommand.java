package cc.dreamcode.chat.command;

import cc.dreamcode.chat.service.MuteService;
import cc.dreamcode.chat.utils.TimeParser;
import cc.dreamcode.command.CommandBase;
import cc.dreamcode.command.annotation.*;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

@Command(name = "tempmute")
@Permission("dream-chat.tempmute")
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class TempmuteCommand implements CommandBase {

    private final MuteService muteService;
    private final cc.dreamcode.chat.config.MessageConfig messageConfig;

    @Completion(arg = "target", value = "@allplayers")
    @Executor(description = "Tymczasowo wycisza gracza.")
    public void tempMutePlayer(CommandSender sender,
                               @Arg("target") OfflinePlayer target,
                               @Arg("muteExpire") String muteExpire,
                               @OptArg("reason") String reason) {

        if (reason == null || reason.isEmpty()) {
            reason = this.messageConfig.defaultReason;
        }

        long millis = TimeParser.parseToMillis(muteExpire);
        this.muteService.createTempMute(sender, target.getUniqueId(), target.getName(), millis, reason);
    }
}
