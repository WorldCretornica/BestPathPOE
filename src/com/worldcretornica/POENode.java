package com.worldcretornica;

import java.util.HashSet;
import java.util.Set;

public class POENode {

	public Set<Short> neighbors = new HashSet<>();
	
	public String name;
	
	public POENode(Set<Short> n) {
		neighbors = n;
	}
}
