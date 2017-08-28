package com.worldcretornica;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class POEPath implements Cloneable {

	private Map<Integer, Integer> nodes = new ConcurrentHashMap<>();
	
	private Queue<Integer> unlockedNodes = new ConcurrentLinkedQueue<>();
	
	@Override
	protected POEPath clone() {
		POEPath clone = new POEPath();
		clone.unlockedNodes = new ConcurrentLinkedQueue<Integer>(this.unlockedNodes);
		clone.nodes = new ConcurrentHashMap<Integer, Integer>(this.nodes);
		
		return clone;
	}
	
	public void consume(Integer i, Integer node) 
	{
		nodes.put(i, node);
		
		for(Integer n : Main.nodes.get(node).neighbors)
		{
			unlock(n);
		}
	}
	
	public void unlock(Integer n)
	{
		if (!containsUnlockedNode(n) && !nodes.containsKey(n))
		{
			unlockedNodes.add(n);
		}
	}
	
	public Integer pollUnlockedNode()
	{
		return unlockedNodes.poll();
	}
	
	public boolean emptyUnlockedNodes()
	{
		return unlockedNodes.isEmpty();
	}
	
	public boolean containsNode(Integer i)
	{
		return nodes.containsKey(i);
	}
	
	public Integer size()
	{
		return nodes.size();
	}
	
	public boolean containsUnlockedNode(Integer node)
	{
		return unlockedNodes.contains(node);
	}
	
	public void clearUnlockedNodes()
	{
		unlockedNodes.clear();
	}
	
	public List<Integer> getNodes()
	{
		List<Integer> nodelist = new ArrayList<Integer>(nodes.keySet()); 
		return nodelist;
	}
}
