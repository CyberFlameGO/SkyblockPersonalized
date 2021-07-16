package com.cobble.sbp.gui.screen.dwarven;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.List;

import com.cobble.sbp.SBP;
import com.cobble.sbp.core.config.DataGetter;
import com.cobble.sbp.gui.menu.settings.SettingGlobal;
import com.cobble.sbp.simplejson.JSONObject;
import com.cobble.sbp.simplejson.parser.JSONParser;
import com.cobble.sbp.utils.ColorUtils;
import com.cobble.sbp.utils.Colors;
import com.cobble.sbp.utils.Reference;
import com.cobble.sbp.utils.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class DwarvenGui extends Gui{

	public static int posX = DataGetter.findInt("dwarvenGuiX");
	public static int posY = DataGetter.findInt("dwarvenGuiY");
	
	public static String currString = "";
	public static String currCommissions = "";

	public static double scale = DataGetter.findInt("dwarvenGuiScale")/10d;

	//Commissions
	public static Boolean commTrackToggle = DataGetter.findBool("dwarvenTrackToggle");
	public static Boolean commTrackBarToggle = DataGetter.findBool("dwarvenTrackBarToggle");
	public static Boolean commNums = DataGetter.findBool("dwarvenTrackNumsToggle");
	public static String commBorderColorID = DataGetter.findStr("dwarvenTrackBorderColor");
	public static String commYesColorID = DataGetter.findStr("dwarvenTrackYesColor");
	public static String commNoColorID = DataGetter.findStr("dwarvenTrackNoColor");
	public static String commNameID = DataGetter.findStr("dwarvenTrackQuestName");
	public static String commissionID = DataGetter.findStr("dwarvenTrackCommissionColor");

	public static Boolean sentryToggle = DataGetter.findBool("starSentryHelper");
	public static int sentryX = DataGetter.findInt("starSentryHelperX");
	public static int sentryY = DataGetter.findInt("starSentryHelperY");
	public static String sentryLast = "Unknown";
	//Drill Fuel

	public static Boolean fuelToggle =  DataGetter.findBool("dwarvenFuelToggle");
	public static String fuelGuiDrillFuel = DataGetter.findStr("dwarvenFuelDrillColor");
	public static String fuelGuiPrimaryFull = DataGetter.findStr("dwarvenFuelGuiPrimeFullColor");
	public static String fuelGuiSecondaryFull = DataGetter.findStr("dwarvenFuelGuiSecondFullColor");
	public static String fuelGuiPrimaryHalf = DataGetter.findStr("dwarvenFuelGuiPrimeHalfColor");
	public static String fuelGuiSecondaryHalf = DataGetter.findStr("dwarvenFuelGuiSecondHalfColor");
	public static String fuelGuiPrimaryTen = DataGetter.findStr("dwarvenFuelGuiPrimeTenColor");
	public static String fuelGuiSecondaryTen = DataGetter.findStr("dwarvenFuelGuiSecondTenColor");
	
	//Mithril Powder
	public static Boolean mithrilToggle = DataGetter.findBool("dwarvenMithrilDisplay");
	public static String mithrilTextColor = DataGetter.findStr("dwarvenMithrilTextColor");
	public static String mithrilCountColor = DataGetter.findStr("dwarvenMithrilCountColor");
	public static String gemTextColor = DataGetter.findStr("dwarvenGemstoneTextColor");
	public static String gemCountColor = DataGetter.findStr("dwarvenGemstoneCountColor");
	
	public static int searchForItem = 0;

	private static final ResourceLocation dwarvenComms = new ResourceLocation(Reference.MODID, "data/dwarven_mining_commissions.json");
	private static final ResourceLocation crystalComms = new ResourceLocation(Reference.MODID, "data/crystal_mining_commissions.json");

	public DwarvenGui(int x, int y) {

		GlStateManager.pushMatrix();
		GlStateManager.scale(scale, scale, scale);
		String[] stringArray = currString.split(";");
		int aO = 0;
		if(x > SBP.width/2) {
			aO=108;
		}
		Utils.drawString(stringArray, (int) Math.round((posX+aO)/scale), (int) (Math.round(posY/scale)), SettingGlobal.textStyle, true);

		GlStateManager.popMatrix();

		if(sentryToggle) {

			if(DataGetter.findBool("starSentryOnlyWithComm")) {
				if(currCommissions.contains("Star Sentry")) {
					Utils.drawString(Colors.LIGHT_PURPLE+"Last Star Sentries: "+Colors.AQUA+sentryLast, sentryX, sentryY);
				}
			} else {
				Utils.drawString(Colors.LIGHT_PURPLE+"Last Star Sentries: "+Colors.AQUA+sentryLast, sentryX, sentryY);
			}



		}

	}

	
	public static void manageDrillFuel() {
		try {
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			String fuelBorderColor;
			String fuelFuelColor;
			List<String> lore = player.getHeldItem().getTooltip(player, false);
			for(int u=0;u<lore.size();u++) { 
				String cLore = Utils.unformatAllText(lore.get(u)).toLowerCase();
				if(cLore.startsWith("fuel:")) { DwarvenGui.searchForItem=u; }
			}
			
			
			if(DwarvenGui.searchForItem != 0) {
				
				String curr = Utils.unformatAllText(lore.get(DwarvenGui.searchForItem));
				lore.clear(); curr = curr.replace(",", "").replace("k", "000").replace("Fuel: ", "");
				double fuelPercent; double currFuel; double totalFuel; DecimalFormat df = new DecimalFormat("#.##");
				try {
				String[] temp = curr.split("/"); 
				currFuel = Integer.parseInt(temp[0]); 
				totalFuel = Integer.parseInt(temp[1]);
				
				fuelPercent = currFuel/totalFuel*100; 
				} catch(Exception e) { fuelPercent = 0.0; currFuel = 0; totalFuel = 0; }
				
				int finalCurrFuel = (int) Math.round(currFuel); int finalTotalFuel = (int) Math.round(totalFuel); String finalFuelPercent = df.format((Math.round(fuelPercent))); int barProgress = Integer.parseInt(finalFuelPercent);
				if(barProgress <= 10) { fuelFuelColor = fuelGuiPrimaryTen; fuelBorderColor = fuelGuiSecondaryTen; } else if(barProgress > 50) { fuelFuelColor = fuelGuiPrimaryFull; fuelBorderColor = fuelGuiSecondaryFull; } else { fuelFuelColor = fuelGuiPrimaryHalf; fuelBorderColor = fuelGuiSecondaryHalf; }

				String color1 = ColorUtils.textColor(fuelBorderColor);
				String color2 = ColorUtils.textColor(fuelFuelColor);
				StringBuilder barString = new StringBuilder(color1 + "[" + color2);
				
				

				for(int f=0;f<50;f++) {
					if (barProgress <= f * 2) {
						if (barProgress == f * 2 || barProgress == (f * 2) - 1) {
							barString.append(Colors.DARK_GRAY);
						}
					}
					barString.append("|");
				} barString.append(color1).append("]");
				String drillFuelStringColor = ColorUtils.textColor(fuelGuiDrillFuel);
				DwarvenGui.currString+=drillFuelStringColor+"Drill Fuel: "+color2+barProgress+"% "+color1+"("+color2+finalCurrFuel+color1+"/"+color2+finalTotalFuel+color1+");";
				DwarvenGui.currString+=barString+Colors.WHITE+";";

				
				/*if(DwarvenGui.fuelDurr) {
					ItemStack held = Minecraft.getMinecraft().thePlayer.getHeldItem();
					held.getItem().setDamage(held, (int) ((Math.round(totalFuel))-currFuel));
					held.getItem().setMaxDamage((int) Math.round(totalFuel));
				
				}*/
				
				
			}
			
	 		}catch(Exception ignored) {}
	}
	
	public static void manageCommissions(String name) {
		boolean space = DataGetter.findBool("dwarvenHideCommissionWord");
		String name2 = name;
		name = name.toLowerCase(); 
		if(name.endsWith("%") || name.endsWith("done")) {

			//if(name.contains("slayer") || name.contains("mithril") || name.contains("titanium") || name.contains("raffle") || name.contains("star") || name.contains("goblin") || name.contains("ghast")) {
				String[] temp3 = name2.split(":");
				String text = "";
				StringBuilder percentbar = new StringBuilder();
				double percent;
				try {
					percent = Double.parseDouble(temp3[1].replace("%","").replace(" ", ""));
					if(percent >= 1) {
						percent -=1;
					}
				} catch(NumberFormatException e) {
					percent = 100;
				}
				String nameColor = ColorUtils.textColor(DwarvenGui.commNameID);
				text+=nameColor+temp3[0]+":";


				String color = Colors.RED; 
				if(name.contains("done") || percent > 74) { color = Colors.GREEN; 
				} else if(percent > 49) { color = Colors.YELLOW;
				} else if(percent > 24) { color = Colors.GOLD; }
				if(percent > 0) {percent++;}

				
				text+=(color+temp3[1]);


				String commID;
				try {

					commID = " "+text.toLowerCase().replace(" ", "_");
					commID = Utils.unformatAllText(commID);
					commID = commID.substring(2, commID.lastIndexOf(":"));

				} catch(Exception e) {
					return;
				}
				if(space) {
					//Utils.print("Before: "+text);
					StringBuilder output = new StringBuilder();
					boolean found = false;
					for(char c : text.toCharArray()) {
						if((c+"").equals(" ") && !found) {
							found = true;
						} else {
							output.append(c);
						}
					}
					text=output.toString();
				}
				//Utils.print(commID);
				JSONObject obj;
				Object[] keyList;
				try {
					ResourceLocation loc;
					if(SBP.sbLocation.equals("crystalhollows")) {
						loc = crystalComms;
					} else {
						loc = dwarvenComms;
					}

					loc = dwarvenComms;

					InputStream in = Minecraft.getMinecraft().getResourceManager().getResource(loc).getInputStream();

					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder commInfo = new StringBuilder();
					String currLine = reader.readLine();
					while(currLine != null) {
						commInfo.append(currLine.replace(" ", ""));
						currLine=reader.readLine();
					}
					obj = (JSONObject) (new JSONParser().parse(commInfo.toString()));
					keyList = obj.keySet().toArray();
				} catch(Exception e) { return; }

				boolean foundCurrComm = false;
				for (Object value : keyList) {
					if (commID.endsWith(value + "")) {
						//Utils.print("'"+commID+"' : "+value);

						foundCurrComm = true;
						break;
					}
				}
				if(!foundCurrComm) {
					//currString+=Colors.DARK_RED+Colors.BOLD+commID+" "+percent+"%;";
					return;
				}

				//Utils.sendMessage(commNums);
				if(commNums) {

					try {
								boolean foundID = false;
								String numKey = "";
								for (Object o : keyList) {
									String curr = o+"";
									if (commID.equals(curr)) {
										foundID = true;
										numKey=o+"";
										break;

									}
								}
								if(!foundID) {
									for (Object o : keyList) {
										if (commID.endsWith(o + "")) {
											foundID = true;
											numKey=o+"";
											break;

										}
									}
								}
								Utils.print("'"+numKey+"'");
								if(foundID && !numKey.equals("")) {
									try {
										long amtNeeded;
										amtNeeded = (long) obj.get(numKey);

										long currAmt = (long) (amtNeeded*percent/100);
										if(currAmt > amtNeeded) {currAmt = amtNeeded;}
										String res = ": "+color+currAmt+"/"+amtNeeded;
										text=text.substring(0, text.indexOf(":"))+res;
									} catch(Exception ignored) {}
								}

					} catch(Exception ignored) { }
				}


				DwarvenGui.currCommissions+=";"+text;
				if(commTrackToggle) { DwarvenGui.currString+=text+";"; }
				
				if(DwarvenGui.commTrackBarToggle && DwarvenGui.commTrackToggle) {
					percent = Math.round(percent);
					
					String borderColor = ColorUtils.textColor(DwarvenGui.commBorderColorID);
					String yesColor = ColorUtils.textColor(DwarvenGui.commYesColorID);
					String noColor = ColorUtils.textColor(DwarvenGui.commNoColorID);
					
					
					
					percentbar.append(borderColor).append("[").append(yesColor);
					for(int f=0;f<50;f++) {
						if (percent <= f * 2) {

							//Utils.print(percent);
							if (percent == f * 2 || percent == (f * 2) - 1) {
								percentbar.append(noColor);
							}


						}
						percentbar.append("|");
					}
					percentbar.append(borderColor).append("]");
					String finalBar = percentbar.toString();
					if(space) {
						finalBar = " "+finalBar;
					}

					DwarvenGui.currString+=finalBar+";";
					
					
					
				} 
				
				
				
				}
			//}
	}
	
	public static String manageMithril(String name) {
			String color1 = ColorUtils.textColor(mithrilTextColor);
			String color2 = ColorUtils.textColor(mithrilCountColor);
			String num = name.replace(" Mithril Powder: ", "");
			return color1+"Mithril Powder: "+color2+num+Colors.WHITE;
	}
	public static String manageGemstone(String name) {
		String color1 = ColorUtils.textColor(gemTextColor);
		String color2 = ColorUtils.textColor(gemCountColor);
		String num = name.replace(" Gemstone Powder: ", "");
		return color1+"Gemstone Powder: "+color2+num+Colors.WHITE;
	}
	
}
