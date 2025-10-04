package cc.dreamcode.chat.gui;

import cc.dreamcode.chat.profile.Profile;
import cc.dreamcode.chat.punishments.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class CheckBanMenu {

    private final Inventory inventory;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public CheckBanMenu(Profile profile) {
        this.inventory = Bukkit.createInventory(null, 54, "Historia kar: " + profile.getName());

        ItemStack summary = new ItemStack(Material.PAPER);
        ItemMeta sm = summary.getItemMeta();
        if (sm != null) {
            sm.setDisplayName("§ePodsumowanie kar");
            sm.setLore(Arrays.asList(
                    "§7Bany: §c" + profile.getBans().size(),
                    "§7TempBany: §c" + profile.getTempBans().size(),
                    "§7Mute: §c" + profile.getMutes().size(),
                    "§7TempMute: §c" + profile.getTempMutes().size(),
                    "§7BanIP: §c" + profile.getIpBansCheck().size(),
                    "§7Kicki: §c" + profile.getKicks().size()
            ));
            summary.setItemMeta(sm);
        }
        inventory.setItem(4, summary);

        int slot = 9;

        for (Ban ban : profile.getBans()) {
            inventory.setItem(slot++, makeItem(Material.BOOK, "§cBan",
                    "§7Powód: §f" + nullSafe(ban.getReason()),
                    "§7Przez: §f" + nullSafe(ban.getBannedBy()),
                    "§7Data: §f" + formatDate(ban.getDate()),
                    "§7Do: §f" + (ban.getUntil() > 0 ? formatDate(ban.getUntil()) : "§cPermanentny")
            ));
        }

        for (TempBan tempBan : profile.getTempBans()) {
            inventory.setItem(slot++, makeItem(Material.ENCHANTED_BOOK, "§cTempBan",
                    "§7Powód: §f" + nullSafe(tempBan.getReason()),
                    "§7Przez: §f" + nullSafe(tempBan.getBannedBy()),
                    "§7Od: §f" + formatDate(tempBan.getDate()),
                    "§7Do: §f" + formatDate(tempBan.getUntil())
            ));
        }

        for (Mute mute : profile.getMutes()) {
            inventory.setItem(slot++, makeItem(Material.FEATHER, "§dMute",
                    "§7Powód: §f" + nullSafe(mute.getReason()),
                    "§7Przez: §f" + nullSafe(mute.getMutedBy()),
                    "§7Data: §f" + formatDate(mute.getDate())
            ));
        }

        for (TempMute tempMute : profile.getTempMutes()) {
            inventory.setItem(slot++, makeItem(Material.BOOK, "§dTempMute",
                    "§7Powód: §f" + nullSafe(tempMute.getReason()),
                    "§7Przez: §f" + nullSafe(tempMute.getMutedBy()),
                    "§7Od: §f" + formatDate(tempMute.getDate()),
                    "§7Do: §f" + formatDate(tempMute.getUntil())
            ));
        }

        for (IpBan ipBan : profile.getIpBansCheck()) {
            inventory.setItem(slot++, makeItem(Material.BARRIER, "§4Ban IP",
                    "§7IP: §f" + nullSafe(ipBan.getIp()),
                    "§7Powód: §f" + nullSafe(ipBan.getReason()),
                    "§7Przez: §f" + nullSafe(ipBan.getBannedBy()),
                    "§7Data: §f" + formatDate(ipBan.getDate()),
                    "§7Do: §f" + (ipBan.getUntil() > 0 ? formatDate(ipBan.getUntil()) : "§cPermanentny")
            ));
        }

        for (Kick kick : profile.getKicks()) {
            inventory.setItem(slot++, makeItem(Material.IRON_BOOTS, "§bKick",
                    "§7Powód: §f" + nullSafe(kick.getReason()),
                    "§7Przez: §f" + nullSafe(kick.getKickedBy()),
                    "§7Data: §f" + formatDate(kick.getDate())
            ));
        }
    }

    private ItemStack makeItem(Material mat, String name, String... lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta m = item.getItemMeta();
        if (m != null) {
            m.setDisplayName(name);
            m.setLore(Arrays.asList(lore));
            item.setItemMeta(m);
        }
        return item;
    }

    private String formatDate(long timestamp) {
        if (timestamp <= 0) return "§cBrak";
        return dateFormat.format(new Date(timestamp));
    }

    private String nullSafe(String s) {
        return (s == null || s.trim().isEmpty()) ? "—" : s;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
