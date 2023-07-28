package de.rbredstone.captcha.main;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class main extends JavaPlugin implements Listener {

	public HashMap<Player, BukkitTask> captcha = new HashMap<>();

	BukkitTask count;

	public void onEnable() {
		init();
	}

	public void onDisable() {

	}

	public void init() {
		getServer().getPluginManager().registerEvents(this, this);

	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		openInv(p);
		startCount(p);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		
		if(captcha.containsKey(p)) {
			stopCount(p);
		}
	}

	@EventHandler
	public void onCaptcha(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		
		if(captcha.containsKey(p)) {
		e.setCancelled(true);
		
		}
	}

	@EventHandler
	public void onCaptcha2(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();

		if (captcha.containsKey(p)) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§aHier klicken!")) {
				stopCount(p);
				captcha.remove(p);
				p.closeInventory();
				p.sendMessage("§aCaptcha erfolgreich!");

			}
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§cHier §c§nnicht§r §cklicken!")) {
				stopCount(p);
				p.kickPlayer("§cCaptcha failed.");

			} else {
				e.setCancelled(true);
			}
		}
	}

	public void openInv(Player p) {

		Bukkit.getScheduler().runTaskLater(this, new Runnable() {

			@Override
			public void run() {
				Inventory inv = Bukkit.createInventory(null, 27,
						"§c§l§nCaptcha§r §8-> §cKlicke auf die §agrüne Glasscheibe§7!");

				ItemStack it = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
				ItemStack it2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);

				ItemMeta im = it2.getItemMeta();
				ItemMeta im2 = it2.getItemMeta();

				im.setDisplayName("§cHier §c§nnicht§r §cklicken!");
				im2.setDisplayName("§aHier klicken!");

				it.setItemMeta(im);
				it2.setItemMeta(im2);

				inv.setItem(0, it);
				inv.setItem(1, it);
				inv.setItem(2, it);
				inv.setItem(3, it);
				inv.setItem(4, it);
				inv.setItem(5, it);
				inv.setItem(6, it);
				inv.setItem(7, it);
				inv.setItem(8, it);
				inv.setItem(9, it);
				inv.setItem(10, it);
				inv.setItem(11, it);
				inv.setItem(12, it);
				inv.setItem(13, it);
				inv.setItem(14, it);
				inv.setItem(15, it);
				inv.setItem(16, it);
				inv.setItem(17, it);
				inv.setItem(18, it);
				inv.setItem(19, it);
				inv.setItem(20, it);
				inv.setItem(21, it);
				inv.setItem(22, it);
				inv.setItem(23, it);
				inv.setItem(24, it);
				inv.setItem(25, it);
				inv.setItem(26, it);
				
				Random rnd = new Random();
				
				int slot = rnd.nextInt(27);
				
				inv.setItem(slot, it2);

				p.openInventory(inv);

			}
		}, 20 * 1);
	}

	public void startCount(Player p) {
		
		count = captcha.put(p, Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
			
			int captchatime = 15;
			
			@Override
			public void run() {
				
				if(captchatime == 0) {
					p.kickPlayer("§cCaptcha failed");
					
				}
				captchatime--;
				
			}
		}, 0, 20L));
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if (captcha.containsKey(p)) {
			openInv(p);
			p.sendMessage("§cUm auf dem Server spielen zu können, mache bitte das Captcha.");
		}
	}

	public void stopCount(Player p) {
		captcha.get(p).cancel();
		captcha.remove(p);
	}

}
