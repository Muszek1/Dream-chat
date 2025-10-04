package cc.dreamcode.bans.gui;

import cc.dreamcode.bans.profile.Profile;
import cc.dreamcode.bans.punishments.*;
import cc.dreamcode.menu.bukkit.BukkitMenuBuilder;
import cc.dreamcode.menu.bukkit.base.BukkitMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class CheckBanMenu {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public void open(Profile profile, Player viewer) {
        // Tworzymy menu: 6 rzędów, tytuł. Używamy buildEmpty() zgodnie z Twoim builderem.
        BukkitMenu menu = new BukkitMenuBuilder(
                "Historia kar: " + safe(profile.getName()),
                6,
                null // brak wstępnie ustawionych itemów
        ).buildEmpty();

        // Podsumowanie
        menu.setItem(4, makeItem(Material.PAPER, "§ePodsumowanie kar",
                "§7Bany: §c" + profile.getBans().size(),
                "§7TempBany: §c" + profile.getTempBans().size(),
                "§7Mute: §c" + profile.getMutes().size(),
                "§7TempMute: §c" + profile.getTempMutes().size(), // poprawka: było getTempBans()
                "§7BanIP: §c" + profile.getIpBansCheck().size(),
                "§7Kicki: §c" + profile.getKicks().size()
        ));

        int slot = 9;

        for (Ban ban : profile.getBans()) {
            menu.setItem(slot++, makeItem(Material.BOOK, "§cBan",
                    "§7Powód: §f" + safe(ban.getReason()),
                    "§7Przez: §f" + safe(ban.getBannedBy()),
                    "§7Data: §f" + formatDate(ban.getDate()),
                    "§7Do: §f" + (ban.getUntil() > 0 ? formatDate(ban.getUntil()) : "§cPermanentny")
            ));
        }

        for (TempBan tempBan : profile.getTempBans()) {
            menu.setItem(slot++, makeItem(Material.ENCHANTED_BOOK, "§cTempBan",
                    "§7Powód: §f" + safe(tempBan.getReason()),
                    "§7Przez: §f" + safe(tempBan.getBannedBy()),
                    "§7Od: §f" + formatDate(tempBan.getDate()),
                    "§7Do: §f" + formatDate(tempBan.getUntil())
            ));
        }

        for (Mute mute : profile.getMutes()) {
            menu.setItem(slot++, makeItem(Material.FEATHER, "§dMute",
                    "§7Powód: §f" + safe(mute.getReason()),
                    "§7Przez: §f" + safe(mute.getMutedBy()),
                    "§7Data: §f" + formatDate(mute.getDate())
            ));
        }

        for (TempMute tempMute : profile.getTempMutes()) {
            menu.setItem(slot++, makeItem(Material.BOOK, "§dTempMute",
                    "§7Powód: §f" + safe(tempMute.getReason()),
                    "§7Przez: §f" + safe(tempMute.getMutedBy()),
                    "§7Od: §f" + formatDate(tempMute.getDate()),
                    "§7Do: §f" + formatDate(tempMute.getUntil())
            ));
        }

        for (IpBan ipBan : profile.getIpBansCheck()) {
            menu.setItem(slot++, makeItem(Material.BARRIER, "§4Ban IP",
                    "§7IP: §f" + safe(ipBan.getIp()),
                    "§7Powód: §f" + safe(ipBan.getReason()),
                    "§7Przez: §f" + safe(ipBan.getBannedBy()),
                    "§7Data: §f" + formatDate(ipBan.getDate()),
                    "§7Do: §f" + (ipBan.getUntil() > 0 ? formatDate(ipBan.getUntil()) : "§cPermanentny")
            ));
        }

        for (Kick kick : profile.getKicks()) {
            menu.setItem(slot++, makeItem(Material.IRON_BOOTS, "§bKick",
                    "§7Powód: §f" + safe(kick.getReason()),
                    "§7Przez: §f" + safe(kick.getKickedBy()),
                    "§7Data: §f" + formatDate(kick.getDate())
            ));
        }

        // otwórz
        menu.open(viewer);
    }

    private ItemStack makeItem(Material mat, String name, String... lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(lore));
            item.setItemMeta(meta);
        }
        return item;
    }

    private String formatDate(long ts) {
        if (ts <= 0) return "§cBrak";
        return dateFormat.format(new Date(ts));
    }

    private String safe(String s) {
        return (s == null || s.trim().isEmpty()) ? "—" : s;
    }
}
