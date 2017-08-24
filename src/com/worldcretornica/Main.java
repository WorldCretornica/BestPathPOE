package com.worldcretornica;

import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {

	public static Map<Integer, POENode> nodes = new ConcurrentHashMap<>();
	public static Queue<POEPath> paths = null;
	public static Set<POEPath> finishedpaths = new HashSet<>();
		
	public static void main(String [] args)
	{	
		nodes = POEDatabase.getAllNodes();
		
		if (!nodes.isEmpty()) {

			paths = new ConcurrentLinkedQueue<>();
			
			POEPath firstpath = new POEPath();
			firstpath.unlock(1);
			paths.add(firstpath);
			
			while (!paths.isEmpty())
			{
				POEPath path = paths.poll();
				
				while (!path.emptyUnlockedNodes())
				{
					Integer currentNodeId = path.pollUnlockedNode();
					
					paths.add(path.clone());
					
					POENode currentNode = nodes.get(currentNodeId);
					path.consume(currentNodeId, currentNode);
										
					if (path.size() >= 50 || path.emptyUnlockedNodes()) {
						finishedpaths.add(path);
						path.clearUnlockedNodes();
					}
				}
			}
		}
		
		info("Path finding finished");
		info(" " + finishedpaths.size() + " paths found!");
		
		for(POEPath path : finishedpaths)
		{
			String strpath = "";
			
			for(Integer i : path.getNodes())
			{
				strpath = strpath + i + ",";
			}
			info("  " + strpath);
		}
	}
	
	
	private static void info(String msg)
	{
		System.out.println(msg);
	}
	 
}
