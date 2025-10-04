package cc.dreamcode.chat.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.entity.Player;

public class CheckBanMenuListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;

        String title = e.getView().getTitle();
        if (title.startsWith("Historia kar:")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        String title = e.getView().getTitle();
        if (title.startsWith("Historia kar:")) {
            e.setCancelled(true);
        }
    }
}
