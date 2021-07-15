package freesoccerhdx.survivalplus.enchants;

public class EnchantLimits {

	
	public static int getEnchantLimit(String s) {
		s = s.replace("§7", "");
		
		
		if(s.equals("Elytra-Boost")) {return 20;}
		
		if(s.equals("Ausbesserung")) {return 20;}
		if(s.equals("Erstschlag")) {return 10;}
		if(s.equals("Riesenschlag")) {return 10;}
		if(s.equals("Sturmschlag")) {return 10;}
		
		if(s.equals("Doppelfischen")) {return 5;}
		
		if(s.equals("Auto-Smelt")) {return 5;}
		if(s.equals("Timber")) {return 10;}
		if(s.equals("Veinminer")) {return 10;}
		
		if(s.equals("Heranziehen")) {return 20;}
		if(s.equals("Wegschleudern")) {return 20;}
		if(s.equals("Festhalten")) {return 10;}
		
		if(s.equals("Rüstungsdurchdringung")) {return 10;}
		if(s.equals("Lebensraub")) {return 10;}
		if(s.equals("Verlangsamung")) {return 10;}
		if(s.equals("Wither")) {return 10;}
		if(s.equals("Vergiftung")) {return 10;}
		
		
		if(s.equals("Explosion")) {return 10;}
		if(s.equals("Donnerblitz")) {return 10;}
		if(s.equals("Härte")) {return 10;}
		if(s.equals("Schwebe")) {return 10;}
		if(s.equals("Abprallen")) {return 10;}
		if(s.equals("Zielsuchend")) {return 10;}
		
		if(s.equals("Leben")) {return 10;}
		if(s.equals("Dornenschutz")) {return 5;}
		if(s.equals("Leichtigkeit")) {return 10;}
		
		if(s.equals("Sättigung")) {return 10;}
		
		if(s.equals("Gleiter")) {return 1;}
		
		if(s.equals("Schwimmboost")) {return 10;}
		
		if(s.equals("Sprungkraft")) {return 10;}
		
		
		return 0;
	}
	
	
}
