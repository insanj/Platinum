/*
 Created by Julian Weiss (insanj), updates frequent on Google+ (and sometimes Twitter)! 

 Please do not modify or decompile at any date, but feel free to distribute with credit.
 Designed and created entirely on Saturday, August 13th, 2011.
 Last edited on: 8/13/11

 Platinum version 1.0!
 Special thanks to: 
 		RugRats, for the idea and some motivation! :D
 		
 Works with the current CraftBukkit Build (#1000).
 All other information should be available at bukkit.org under Platinum.

 THIS VERSION CURRENT HAS TWO CLASSES:
			Platinum.java
			PlatinumListener.java

*/

package me.insanj.Platinum;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class PlatinumListener extends BlockListener{

	Platinum plugin;
	
	public PlatinumListener(Platinum instance){
		plugin = instance;
	}
	
	public void onBlockDamage(BlockDamageEvent event){
		
		ItemStack inhand = event.getPlayer().getItemInHand();
		
		if( inhand.getType().equals(Material.GOLD_PICKAXE) )
			if(event.getBlock().getType().name().contains("ORE") || event.getBlock().getType().name().contains("BLOCK"))
				if( timeDecider(event.getBlock()) != null)
					new Timer().schedule(new breakTask(event), timeDecider(event.getBlock()));
		
	}//end onBlockDamage()
	
	public void onBlockBreak(BlockBreakEvent event){
		
		ItemStack inhand = event.getPlayer().getItemInHand();
		
		if( inhand.getType().equals(Material.GOLD_PICKAXE) && pickaxeLoss() != null ){
			System.out.println("durability went from " + inhand.getDurability() );
			inhand.setDurability((short) (inhand.getDurability() - pickaxeLoss()));
			System.out.println("to " + inhand.getDurability() );
		}
			
	}//end onBlockBreak()
	
	public void onEntityDamage(EntityDamageEvent event){
		
		if(event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
			ItemStack[] armor = player.getInventory().getArmorContents();
			ArrayList<ItemStack> goldArmor = new ArrayList<ItemStack>();
			
			for(int i = 0; i < armor.length; i++)
				if(armor[i].getType().name().contains("GOLD"))
					goldArmor.add(armor[i]);
			
			if(goldArmor.size() > 0){
				for(ItemStack armorItem : goldArmor){
					System.out.println(armorItem.toString() + " went from " + armorItem.getDurability() );
					armorItem.setDurability( (short) ((armorItem.getDurability() + event.getDamage()) - event.getDamage() * armorModifier(armorItem.getType().name())));
					System.out.println("to " + armorItem.getDurability() );
				}
			}//end if
		}//end if
		
	}//end onEntityDamage()
	
	private Integer armorModifier(String name) {
		Scanner outdoors = new Scanner(plugin.config);
		while(outdoors.hasNextLine()){
			String line = outdoors.nextLine();
			if(line.contains(name + "_LOSS: ")){
				try{ return Integer.parseInt(line.substring(name.length() + 7)); }
				catch(Exception e){ plugin.log.severe("{Platinum} had trouble reading the " + name + "_LOSS line in the config: "); e.printStackTrace(); }
			}
		}//end while
		
		return null;
	}//end armorModifier()

	public Short pickaxeLoss(){
		Scanner outdoors = new Scanner(plugin.config);
		while(outdoors.hasNextLine()){
			String line = outdoors.nextLine();
			if(line.contains("GOLD_PICKAXE_LOSS: ")){
				try{ return Short.parseShort(line.substring(19)); }
				catch(Exception e){ plugin.log.severe("{Platinum} had trouble reading the GOLD_PICKAXE_LOSS line in the config: "); e.printStackTrace(); }
			}
		}//end while
		
		return null;
	}//end pickaxeLoss()
	
	public Integer timeDecider(Block block){
		
		Scanner outdoors = new Scanner(plugin.config);
		while(outdoors.hasNextLine()){
			String line = outdoors.nextLine();
			if(line.contains(block.getType().name())){
				line = line.substring(block.getType().name().length() + 2);
				try{ return Integer.parseInt(line) * 1000; }
				catch(Exception e){ plugin.log.severe("{Platinum} had trouble reading the " + block.getType().name() + " line in the config: "); e.printStackTrace(); }
			}
		}//end while
		
		return null;
	}//end timeDecider()
	
}//end PlatinumListener

class breakTask extends TimerTask{
	
	BlockDamageEvent event;
	breakTask(BlockDamageEvent taskEvent){
		event = taskEvent;
	}
	
	@Override
	public void run() {
		event.getBlock().setTypeId(0);
		event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(event.getBlock().getType(), getAmount(event.getBlock())));
	}

	private int getAmount(Block block) {
		if( block.getType().name().contains("LAPIS") ||
				block.getType().name().contains("REDSTONE") || 
				block.getType().name().contains("COAL") )
			return 4;
		
		return 1;
		
	}//end getAmount()
	
}//end class breakTask