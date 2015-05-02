package com.alexandjon.FriendshipGraphAlgorithms;

import java.lang.*;
import java.util.*;
import java.io.*;


public class Algorithms {
	public static Set<Graph.Person> getConnectors(Graph graph) {
		final HashMap<Graph.Person,Graph.Person> retVal = new HashMap<>();

		final ArrayList<Integer> dfsNum = new ArrayList<>(graph.size());
		final ArrayList<Integer> back = new ArrayList<>(graph.size());

		final ArrayList<Boolean> visited = new ArrayList<>(graph.size());

		HashMap<Graph.Person,Graph.Person> people = new HashMap<>(graph.size());
		for (int i = 0; i < graph.size(); i++) people.put(graph.getVertex(i), graph.getVertex(i));

		do {
			int startVnum = 0;
			while (startVnum < graph.size() && (visited.get(startVnum) || people.containsKey(graph.getVertex(startVnum)))) {
				if (people.containsKey(graph.getVertex(startVnum))) people.remove(graph.getVertex(startVnum));
				startVnum++;
			}
			if (startVnum >= graph.size()) break;
			final Graph.Person start = graph.getVertex(startVnum);

			Traverser t = new Traverser(graph) {
				int dfsNumCounter = 0;
				Graph.Person start = null;

				boolean onVisitForward(Graph.Person cur, Graph.Person prev) {
					if (prev == null) {
						dfsNumCounter = 0;
						start = cur;
					}

					if (!visited.get(cur.vnum)) {
						dfsNum.set(cur.vnum, ++dfsNumCounter);
						back.set(cur.vnum, dfsNumCounter);
					} else {
						back.set(prev.vnum, Math.min(back.get(prev.vnum), dfsNum.get(cur.vnum)));
						return true;
					}

					visited.set(cur.vnum, true);
					return false;
				}

				boolean onVisitBackward(Graph.Person cur, Graph.Person prev) {
					if (dfsNum.get(cur.vnum) > back.get(prev.vnum)) {
						back.set(cur.vnum, Math.min(back.get(cur.vnum), back.get(prev.vnum)));
					} else if (!cur.equals(start)) {
						retVal.put(cur, cur);
					}

					return false;
				}
			};

			try {
				t.dfs(start);
				
				Graph.PersonNode neighbor = graph.getEdge(start.vnum);
				if (neighbor != null) {
					for (int i = 0; i < visited.size(); i++) visited.set(i, false);
					for (int i = 0; i < dfsNum.size(); i++) dfsNum.set(i, 0);
					for (int i = 0; i < back.size(); i++) back.set(i, 0);
					
					t.dfs(neighbor.data);
				}
			} catch (Graph.PersonNotFoundException e) {
				e.printStackTrace();
			}

		} while (people.size() > 0);

		return retVal.keySet();
	}

	public static ArrayList<Graph.Person> shortestPath(Graph graph, Graph.Person start, Graph.Person finish) throws Graph.PersonNotFoundException {
		MinHeap<Graph.Person,Integer> fringe = new MinHeap<>();
		int[] distances = new int[graph.size()];
		Graph.Person[] prevPerson = new Graph.Person[graph.size()];

		if (!graph.inGraph(start)) throw new Graph.PersonNotFoundException(start);
		if (!graph.inGraph(finish)) throw new Graph.PersonNotFoundException(finish);

		if (graph.getEdge(start.vnum) == null || graph.getEdge(finish.vnum) == null) {
			return null;
		}

		for (int i = 0; i < distances.length; i++) distances[i] = -1;

		distances[start.vnum] = 0;
		for (Graph.PersonNode p = graph.getEdge(start.vnum); p != null; p = p.next) {
			prevPerson[p.data.vnum] = start;
			distances[p.data.vnum] = 1;
			fringe.insertNode(p.data, 1);
		}
		
		MinHeap.HeapNode<Graph.Person,Integer> top;
		while ((top = fringe.pop()) != null) {
			if (top.data == finish) {
				break;
			}

			if (graph.getEdge(top.data.vnum) != null)
			for (Graph.PersonNode neighbor = graph.getEdge(top.data.vnum); neighbor != null; neighbor = neighbor.next) {
				if (distances[neighbor.data.vnum] == -1) {
					int newDist = distances[top.data.vnum] + 1;
					prevPerson[neighbor.data.vnum] = top.data;
					distances[neighbor.data.vnum] = newDist;

					fringe.insertNode(neighbor.data, newDist);
				} else {
					int curDist = distances[neighbor.data.vnum], newDist = distances[top.data.vnum] + 1;
					if (newDist < curDist) {
						distances[neighbor.data.vnum] = newDist;
						prevPerson[neighbor.data.vnum] = top.data;
					}
				}
			}
		}

		if (prevPerson[finish.vnum] == null) return null;

		ArrayList<Graph.Person> path = new ArrayList<>();
		for (Graph.Person p = finish; p != start; p = prevPerson[p.vnum])
			path.add(0, p);
		path.add(0, start);

		return path;
	}

	public static void testDfs(Graph graph, Graph.Person start) {
		final boolean[] visited = new boolean[graph.size()];

		Traverser t = new Traverser(graph) {
			boolean onVisitForward(Graph.Person p, Graph.Person prev) {
				if (visited[p.vnum]) return true;
				System.out.println("---> " + p.toString() + " from " + (prev != null ? prev.toString() : null));
				visited[p.vnum] = true;
				return false;
			}

			boolean onVisitBackward(Graph.Person p, Graph.Person prev) {
				System.out.println("<--- " + p.toString() + " from " + (prev != null ? prev.toString() : null));
				return false;
			}
		};

		System.out.println("TESTING DFS");
		try {
			t.dfs(start);
		} catch (Graph.PersonNotFoundException e) {
			e.printStackTrace();
		}
	}
}