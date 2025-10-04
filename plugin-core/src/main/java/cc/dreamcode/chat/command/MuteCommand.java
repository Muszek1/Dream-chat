package cc.dreamcode.chat.command;

import cc.dreamcode.chat.service.MuteService;
import cc.dreamcode.command.CommandBase;
import cc.dreamcode.command.annotation.*;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "mute")
@Permission("dream-chat.mute")
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class MuteCommand implements CommandBase {

    private final MuteService muteService;
    private final cc.dreamcode.chat.config.MessageConfig messageConfig;

    @Completion(arg = "target", value = "@allplayers")
    @Executor(description = "Wycisza gracza.")
    public void mutePlayer(CommandSender sender,
                           @Arg("target") Player target,
                           @OptArg("reason") String reason) {
        if (reason == null || reason.isEmpty()) {
            reason = this.messageConfig.defaultReason;
        }
        this.muteService.createMute(sender, target.getUniqueId(), target.getName(), reason);
    }
}

