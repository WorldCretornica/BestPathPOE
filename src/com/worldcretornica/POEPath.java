package com.worldcretornica;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

public class POEPath implements Cloneable {

	private ConcurrentSkipListSet<Short> nodes = new ConcurrentSkipListSet<>();
	
	private Queue<Short> unlockedNodes = new ConcurrentLinkedQueue<>();
	
	@Override
	protected POEPath clone() {
		POEPath clone = new POEPath();
		clone.unlockedNodes = new ConcurrentLinkedQueue<Short>(this.unlockedNodes);
		clone.nodes = new ConcurrentSkipListSet<Short>(this.nodes);
		
		return clone;
	}
	
	public void consume(Short node) 
	{
		nodes.add(node);
		
		for(Short n : Main.nodes.get(node).neighbors)
		{
			unlock(n);
		}
	}
	
	public void unlock(Short n)
	{
		if (!containsUnlockedNode(n) && !nodes.contains(n))
		{
			unlockedNodes.add(n);
		}
	}
	
	public Short pollUnlockedNode()
	{
		return unlockedNodes.poll();
	}
	
	public boolean emptyUnlockedNodes()
	{
		return unlockedNodes.isEmpty();
	}
	
	public boolean contains(Short i)
	{
		return nodes.contains(i);
	}
	
	public Integer size()
	{
		return nodes.size();
	}
	
	public boolean containsUnlockedNode(Short node)
	{
		return unlockedNodes.contains(node);
	}
	
	public void clearUnlockedNodes()
	{
		unlockedNodes.clear();
	}
	
	public Set<Short> getNodes()
	{
		//Set<Short> nodelist = new HashSet<Short>(nodes); 
		return nodes;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		return super.equals(obj);
	}
}
