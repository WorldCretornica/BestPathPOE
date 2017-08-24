package com.worldcretornica;

import java.util.HashSet;
import java.util.Set;

public class POENode {

	public Set<Integer> neighbors = new HashSet<>();
	
	public String name;
	
	public POENode(Set<Integer> n) {
		neighbors = n;
	}
}
