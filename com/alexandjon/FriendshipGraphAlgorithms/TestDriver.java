package com.alexandjon.FriendshipGraphAlgorithms;

import java.lang.*;
import java.util.*;
import java.io.*;


public class TestDriver {
	public static void main(String[] argv) {
		/*Graph g = new Graph();

		g.addPerson(new Graph.Person("jon", "rutgers"));
		g.addPerson(new Graph.Person("nicole", "rutgers"));
		g.addPerson(new Graph.Person("alex", "rutgers"));
		g.addPerson(new Graph.Person("tori", "cornell"));
		try {
			g.addEdge(g.nameQuery("jon"), g.nameQuery("nicole"));
			g.addEdge(g.nameQuery("jon"), g.nameQuery("alex"));
			g.addEdge(g.nameQuery("alex"), g.nameQuery("nicole"));
			g.addEdge(g.nameQuery("nicole"), g.nameQuery("tori"));
		} catch (Graph.PersonNotFoundException e) {
			e.printStackTrace();
		}

		g.printConnections();*/

		/*MinHeap<String,Integer> mh = new MinHeap<>();

		mh.insertNode("jon", 1);
		mh.insertNode("alex", 2);
		mh.insertNode("nicole", -1);
		mh.insertNode("awef", 12);
		mh.insertNode("BILL", 96);
		mh.insertNode("charlzz", -1234578);
		mh.insertNode("awefefw", 123654);
		mh.insertNode("BOB", 0);

		mh.printHeap();

		System.out.println();
		mh.pop();
		mh.printHeap();

		System.out.println();
		mh.pop();
		mh.printHeap();
		
		System.out.println();*/

		try {
			File f = new File("testgraph.txt");
			Scanner sc = new Scanner(f);

			Graph g = Friends.makeGraph(sc);

			//g.addPerson(new Graph.Person("A", null));
			//g.addPerson(new Graph.Person("B", null));
			//g.addPerson(new Graph.Person("C", null));
			//g.addPerson(new Graph.Person("D", null));
			//g.addPerson(new Graph.Person("E", null));
			//g.addPerson(new Graph.Person("F", null));

			//g.addEdge(g.nameQuery("A"), g.nameQuery("B"));
			//g.addEdge(g.nameQuery("C"), g.nameQuery("B"));
			//g.addEdge(g.nameQuery("E"), g.nameQuery("B"));
			//g.addEdge(g.nameQuery("E"), g.nameQuery("D"));
			//g.addEdge(g.nameQuery("C"), g.nameQuery("D"));
			//g.addEdge(g.nameQuery("F"), g.nameQuery("D"));

			g.printConnections();

			//System.out.println(g.shortestPath(g.nameQuery("samir"), g.nameQuery("nick")));

			//g.testDfs(g.nameQuery("samir"));

			//Algorithms.testDfs(g, g.nameQuery("jane"));

			System.out.println(Algorithms.shortestPath(g, g.nameQuery("heather"), g.nameQuery("tom")));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}