package de.freesoccerhdx.testingstuff.main.utils;

import java.util.HashMap;
import java.util.Random;

public class WeightedRandom {

	
	
	public static <T> T getWeightedRandom(HashMap<T,Double> weighteditem){
		
		double totalweight = 0.0;
		
		for(double d : weighteditem.values()) {
			totalweight += d;
		}
		
		
		double randomweight = (new Random()).nextDouble() * totalweight;
		
		int index = -1;
		
		for(int i = 0; i <= weighteditem.values().size(); i++) {
			double weight = (double) weighteditem.values().toArray()[i];
			
			randomweight -= weight;
			
			if(randomweight <= 0.0) {
				index = i;
				break;
			}
			
		}
		
		if(index != -1) {
			return (T) weighteditem.keySet().toArray()[index];
		}
		
		return null;
	}
	
	
}
