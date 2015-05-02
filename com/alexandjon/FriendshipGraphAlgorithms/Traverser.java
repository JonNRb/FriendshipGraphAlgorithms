package com.alexandjon.FriendshipGraphAlgorithms;

import java.lang.*;
import java.util.*;
import java.io.*;


public abstract class Traverser {
	private Graph graph;

	public Traverser(Graph graph) {
		this.graph = graph;
	}

	private boolean mEndTraversal;
	public void endTraversal() {
		mEndTraversal = true;
	}

	abstract boolean onVisitForward(Graph.Person current, Graph.Person prev);
	abstract boolean onVisitBackward(Graph.Person current, Graph.Person prev);

	public void dfs(Graph.Person start) throws Graph.PersonNotFoundException {
		mEndTraversal = false;
		dfs(start, null);
	}

	public void dfs(Graph.Person start, Graph.Person prev) throws Graph.PersonNotFoundException {
		if (start == null) return;
		if (!graph.inGraph(start)) throw new Graph.PersonNotFoundException(start);

		if (prev == null) onVisitForward(start, null);
		if (mEndTraversal) return;

		for (Graph.PersonNode i = graph.getEdge(start.vnum); i != null; i = i.next) {
			//if (i.data.equals(prev)) continue;

			if (onVisitForward(i.data, start)) continue;
			if (mEndTraversal) return;

			dfs(i.data, start);

			if (onVisitBackward(start, i.data)) break;
			if (mEndTraversal) return;
		}

		return;
	}

	public void bfs(Graph.Person start) throws Graph.PersonNotFoundException {
		mEndTraversal = false;
		bfs(start, null);
	}

	private void bfs(Graph.Person start, Graph.Person prev) throws Graph.PersonNotFoundException {
		if (start == null) return;
		if (!graph.inGraph(start)) throw new Graph.PersonNotFoundException(start);

		if (prev == null) onVisitForward(start, null);
		if (mEndTraversal) return;

		ArrayList<Boolean> retVals = new ArrayList<>(graph.size());

		for (Graph.PersonNode i = graph.getEdge(start.vnum); i != null; i = i.next) {
			//if (i.data.equals(prev)) continue;
			retVals.set(i.data.vnum, onVisitForward(i.data, start));
			if (mEndTraversal) return;
		}

		for (Graph.PersonNode i = graph.getEdge(start.vnum); i != null; i = i.next) {
			//if (i.data.equals(prev)) continue;

			if (retVals.get(i.data.vnum)) continue;

			bfs(i.data, start);
			if (mEndTraversal) return;
		}

		for (Graph.PersonNode i = graph.getEdge(start.vnum); i != null; i = i.next) {
			//if (i.data.equals(prev)) continue;
			if (onVisitBackward(start, i.data)) break;
			if (mEndTraversal) return;
		}

		return;
	}
}