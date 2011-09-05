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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

//TODO: Add support for mcMMO and Permissions.
//		Decrease durability loss (more uses) with each gold pickaxe hit.
//		Make gold armor last longer.
//		Set up a configuration file, that can control all gold items.

//TODO: Permissions nodes:
//				Platinum.pickaxe
//				Platinum.armor

//TODO: Maybe add iConomy and Platinum.pay?

public class Platinum extends JavaPlugin {

	PlatinumListener blockListener = new PlatinumListener(this);
	public final Logger log = Logger.getLogger("Minecraft");
	public static String version = "1.0";
	
	public String path = "plugins/Platinum";
	public String config = "plugins/Platinum/config.txt";
	
	@Override
	public void onEnable(){
		
		if(!new File(config).exists()){
			try{writeFile();}
			catch(Exception e){log.severe("{Platinum} had major issues generating the configuration folder/file: "); e.printStackTrace();}
		}
		
		this.getServer().getPluginManager().registerEvent(Event.Type.BLOCK_DAMAGE, blockListener, Event.Priority.Normal, this);
		this.getServer().getPluginManager().registerEvent(Event.Type.BLOCK_BREAK, blockListener, Event.Priority.Normal, this);
		this.getServer().getPluginManager().registerEvent(Event.Type.ENTITY_DAMAGE, blockListener, Event.Priority.Normal, this);
		log.info("{Platinum} version " + version + " (by insanj) has successfully started!");
	}
	
	@Override
	public void onDisable() {
		log.info("{Platinum} version " + version + " (by insanj) has been disabled.");
	}
	
	public void writeFile() throws IOException{
		
		new File(path).mkdir();
		FileWriter output = new FileWriter(new File(config));
		
		output.write("This is an automatically generated file by the Platinum plugin.\n");
		output.write("Consult the offical Bukkit.org forum listing (by insanj) to find instructions for configuration.\n");
		output.write("Do not tamper unless you know what you're doing!\n\n");		
		output.close();
		
	}//end writeFile(0

}//end Platinum