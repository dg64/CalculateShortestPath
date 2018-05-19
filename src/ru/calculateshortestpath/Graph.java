package ru.calculateshortestpath;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;

public class Graph {
	private Set<Node> nodes = new HashSet();
	
	public void addNode(Node nodeA) {
		nodes.add(nodeA);
	}

	public Set<Node> getNodes() {
		return nodes;
	}

	public void setNodes(Set<Node> nodes) {
		this.nodes = nodes;
	}

	public Graph dijkstraCalculateShortestPathAll(Graph graph, Node source) {
		
		resetGraph(graph.getNodes());
		
		source.setDistance(0);
		Set<Node> settledNodes =new HashSet();
		Set<Node> unsettledNodes =new HashSet();
		
		unsettledNodes.add(source);
		
		while(unsettledNodes.size() != 0) {
			Node currentNode = getLowestDistanceNode(unsettledNodes);
			unsettledNodes.remove(currentNode);
		
			for (Entry < Node, Integer> adjacencyPair: 
		          currentNode.getAdjacentNodes().entrySet()) {
				Node adjacentNode = adjacencyPair.getKey();
	            Integer edgeWeight = adjacencyPair.getValue();
	            if (!settledNodes.contains(adjacentNode)) {
	                calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
	                unsettledNodes.add(adjacentNode);
	            }				
			}
			settledNodes.add(currentNode);			
		}
		
		return graph;
	}
	
	public Graph dijkstraCalculateShortestPath(Graph graph, Node source, Node target) {
		resetGraph(graph.getNodes());
		
		source.setDistance(0);
		Set<Node> settledNodes =new HashSet();
		Set<Node> unsettledNodes =new HashSet();
		
		unsettledNodes.add(source);
		
		while(unsettledNodes.size() != 0) {
			Node currentNode = getLowestDistanceNode(unsettledNodes);
			unsettledNodes.remove(currentNode);
		
			for (Entry < Node, Integer> adjacencyPair: 
		          currentNode.getAdjacentNodes().entrySet()) {
				Node adjacentNode = adjacencyPair.getKey();
	            Integer edgeWeight = adjacencyPair.getValue();
	            if (!settledNodes.contains(adjacentNode)) {
	                calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
	                unsettledNodes.add(adjacentNode);
	            }				
			}
			settledNodes.add(currentNode);
			
			if(currentNode.getName().equals(target.getName())) {
				break;
			}
		}

		return graph;
	}

	private static Node getLowestDistanceNode(Set<Node> unsettledNodes) {
		Node lowestDistanceNode = null;
		int lowestDistance = Integer.MAX_VALUE;
		
		for(Node node: unsettledNodes) {
			int nodeDistance = node.getDistance();
			if (nodeDistance < lowestDistance) {
				lowestDistance = nodeDistance;
				lowestDistanceNode = node;
			}
		}
		
		return lowestDistanceNode;
		
	}
	
	private  void calculateMinimumDistance(Node evaluationNode,
		Integer edgeWeigh, Node sourceNode) {
	    Integer sourceDistance = sourceNode.getDistance();

	    if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
	        evaluationNode.setDistance(sourceDistance + edgeWeigh);
	        LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
	        shortestPath.add(sourceNode);
	        evaluationNode.setShortestPath(shortestPath);
	    }
	}
	
	private void resetGraph(Set<Node> nodes) {
		for(Node node: nodes) {
			node.setDistance(Integer.MAX_VALUE);
			node.getShortestPath().clear();
		}
	}
}
 