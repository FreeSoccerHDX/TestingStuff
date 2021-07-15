package freesoccerhdx.survivalplus.haupt;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.material.MaterialData;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.OctaveGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

public class DungeonChunkGenerator extends ChunkGenerator {

	/*
	 public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ,BiomeGrid biome){

		ChunkData chunk = createChunkData(world);
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
		
		generator.setScale(0.005d);
		
		for(int X = 0; X < 16; X++) {
		    for(int Z = 0; Z < 16; Z++) {
		    	biome.setBiome(X, Z, Biome.PLAINS);
		    	
		    	chunk.setBlock(X, 5, Z, Material.WATER);
		    	chunk.setBlock(X, 4, Z, Material.WATER);
		    	chunk.setBlock(X, 3, Z, Material.WATER);
		    	chunk.setBlock(X, 2, Z, Material.WATER);
		    	
		    	chunk.setBlock(X, 1, Z, Material.SAND);
		    	chunk.setBlock(X, 0, Z, Material.BEDROCK);
		    	
		    	
		    }
		}
		return chunk;
	}
	*/
	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		List<BlockPopulator> pops = new ArrayList<>();
		pops.add(new TreePopulator());
		pops.add(new EndTowerPopulator());
	//	pops.add(new LakePopulator());
		return pops;
		
	    
	}	
	  @Override
	    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
		  //	return Bukkit.getWorld("world").getGenerator().generateChunkData(world, random, chunkX, chunkZ, biome);
		 
		  ChunkData chunk = createChunkData(world);
		
		
		  
		
		  	
	    	SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 32);
	        generator.setScale(0.008D);
	        SimplexOctaveGenerator antigenerator = new SimplexOctaveGenerator(new Random(world.getSeed()), 24);
	        antigenerator.setScale(0.02D);
	        SimplexOctaveGenerator antiKgenerator = new SimplexOctaveGenerator(new Random(world.getSeed()), 16);
	        antiKgenerator.setScale(0.02D);
	        
	        
	       
	        
	        
	        for (int X = 0; X < 16; X++) {
	            for (int Z = 0; Z < 16; Z++) {
	            	biome.setBiome(X, 1, Z,Biome.PLAINS);
	            	
	                int currentHeight = (int) (generator.noise(chunkX*16+X, chunkZ*16+Z, 0.5D, 0.5D)*15D+100D);
	                int antiheight =  (int) (antigenerator.noise(chunkX*16+X, chunkZ*16+Z, 0.5D, 0.5D)*15D+5D);
	                int antikheight =  (int) (antiKgenerator.noise(chunkX*16+X, chunkZ*16+Z, 0.5D, 0.9D)*30D+5D);
	                
	                chunk.setBlock(X, currentHeight, Z, Material.GRASS_BLOCK);
	                chunk.setBlock(X, currentHeight-1, Z, Material.DIRT);
	                chunk.setBlock(X, currentHeight-2, Z, Material.DIRT);
	                chunk.setBlock(X, currentHeight-3, Z, Material.DIRT);
	                
	                for (int i = currentHeight-4; i > 0; i--) {
	                	if(i-20 < antiheight || i-25 > antikheight || i==currentHeight-4) {
	                		chunk.setBlock(X, i, Z, Material.STONE);
	                	}
	                }
	                chunk.setBlock(X, 0, Z, Material.BEDROCK);
	            }
	        }
	       
	        
	        if(chunkX%6 == 0 && chunkZ%6 == 0) {
				  
				  for (int X = 0; X < 16; X++) {
			            for (int Z = 0; Z < 16; Z++) {
			            	chunk.setBlock(X, 90, Z, Material.GRASS_BLOCK);
			            }
			  		}
				  
				  chunk.setRegion(0, 1, 0, 16, 20, 16, Material.GLASS);
				  chunk.setRegion(0, 21, 0, 16, 40, 16, Material.BLACK_STAINED_GLASS);
				  chunk.setRegion(0, 41, 0, +16, 60, 16, Material.RED_STAINED_GLASS);
				  chunk.setRegion(0, 61, 0, +16, 80, 16, Material.GREEN_STAINED_GLASS);
				  
				  
			  }
	        return chunk;
	          
	    }
	  
			
}
