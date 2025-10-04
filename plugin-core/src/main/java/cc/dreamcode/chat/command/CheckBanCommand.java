package cc.dreamcode.chat.command;

import cc.dreamcode.chat.gui.CheckBanMenu;
import cc.dreamcode.chat.profile.Profile;
import cc.dreamcode.chat.profile.ProfileRepository;
import cc.dreamcode.command.CommandBase;
import cc.dreamcode.command.annotation.*;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "checkban")
@Permission("dream-chat.checkban")
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class CheckBanCommand implements CommandBase {

    private final ProfileRepository profileRepository;

    /**
     * /checkban <gracz>
     */
    @Completion(arg = "target", value = "@allplayers")
    @Executor(description = "Pokazuje historię banów gracza.")
    public void checkBan(CommandSender sender,
                         @Arg("target") OfflinePlayer target) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cTylko gracz może użyć tej komendy.");
            return;
        }

        Player player = (Player) sender;
        Profile profile = this.profileRepository.findOrCreate(target.getUniqueId(), target.getName());

        if (profile == null) {
            player.sendMessage("§cNie znaleziono profilu gracza.");
            return;
        }

        player.openInventory(new CheckBanMenu(profile).getInventory());
    }
}
