package com.worldcretornica;

import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {

	public static Map<Short, POENode> nodes = new ConcurrentHashMap<>();
	public static Queue<POEPath> paths = null;
	public static Set<POEPath> finishedpaths = new HashSet<>();
		
	public static void main(String [] args)
	{	
		nodes = POEDatabase.getAllNodes();
		
		if (!nodes.isEmpty()) {

			paths = new ConcurrentLinkedQueue<>();
			
			POEPath firstpath = new POEPath();
			firstpath.unlock((short) 1);
			firstpath.unlock((short) 2);
			firstpath.unlock((short) 3);
			firstpath.unlock((short) 4);
			firstpath.unlock((short) 5);
			firstpath.unlock((short) 6);
			paths.add(firstpath);
			
			while (!paths.isEmpty())
			{
				POEPath path = paths.poll();
				
				while (!path.emptyUnlockedNodes())
				{
					Short currentNodeId = path.pollUnlockedNode();
					
					POEPath clone = path.clone();
					if (!paths.contains(clone))
						paths.add(clone);
					
					//POENode currentNode = nodes.get(currentNodeId);
					path.consume(currentNodeId);
										
					if (path.size() >= 80 || path.emptyUnlockedNodes()) {
						finishedpaths.add(path);
						path.clearUnlockedNodes();
						
						String strpath = "";
						
						for(Short i : path.getNodes())
						{
							strpath = strpath + i + ",";
						}
						info("  " + strpath);
					}
				}
			}
		}
		
		info("Path finding finished");
		info(" " + finishedpaths.size() + " paths found!");
		
		/*for(POEPath path : finishedpaths)
		{
			String strpath = "";
			
			for(Integer i : path.getNodes())
			{
				strpath = strpath + i + ",";
			}
			info("  " + strpath);
		}*/
	}
	
	
	private static void info(String msg)
	{
		System.out.println(msg);
	}
	 
}
