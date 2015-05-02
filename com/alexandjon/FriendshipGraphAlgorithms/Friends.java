package com.alexandjon.FriendshipGraphAlgorithms;

import java.lang.*;
import java.util.*;
import java.io.*;


public class Friends {
	public static Graph makeGraph(Scanner sc) {
		Graph retVal = new Graph();

		sc.useDelimiter("\n");
		while (sc.hasNext()) {
			String raw = sc.nextLine();
			String[] line = raw.split("\\|");

			if (line.length == 3 || (line.length == 2 && line[1].equals("n"))) {
				final String name = line[0].toLowerCase();
				final String school = (line.length == 2 ? null : line[2].toLowerCase());
				Graph.Person p = new Graph.Person(name, school);
				//System.out.println("Adding " + p.toString());
				try {
					retVal.addPerson(p);
				} catch (Graph.DuplicatePersonException e) {
					
				}
			} else if (line.length == 2) {
				try {
					final Graph.Person p1 = retVal.nameQuery(line[0]);
					final Graph.Person p2 = retVal.nameQuery(line[1]);
					//System.out.println("Adding edge from " + p1.toString() + " to " + p2.toString());
					retVal.addEdge(p1, p2);
				} catch (Graph.PersonNotFoundException e) {
					e.printStackTrace();
				}
			} else {
				// This shouldn't happen.
			}
		}

		return retVal;
	}


	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		Graph g;

		// Get input file name.
		boolean gotFile = false;
		while (!gotFile) {
			System.out.print("Enter a file to parse: ");
			String fileName = input.nextLine();

			try {
				File f = new File(fileName);
				Scanner sc = new Scanner(f);
				g = makeGraph(sc);
				gotFile = true;
			} catch (FileNotFoundException e) {
				System.out.println("File not found.");
			}
		}

		boolean quit = false;
		while (!quit) {
			System.out.println("path, cliques, connectors, quit");
			System.out.print("$ ");
			String choice = input.nextLine();
			if (choice.equalsIgnoreCase("path")) {

			} else if (choice.equalsIgnoreCase("cliques")) {
				System.out.print("School: ");
				String school = input.nextLine();
				if (g.schoolQuery(school) == null) {
					System.out.println("No school with that name...");
					continue;
				}
				ArrayList<Graph> cliques = Algorithms.getAllCliques(g, school);
				for (int i = 0; i < cliques.size(); i++) {
					System.out.println("Clique "+i);
					g.printConnections();
				}
			} else if (choice.equalsIgnoreCase("connectors")) {
				ArrayList<Graph.Person> connectors = new ArrayList<>(Algorithms.getConnectors(g));
				for (Graph.Person p : connectors) {
					System.out.print(p.name + " , ");
				}
				System.out.println();
			} else if (choice.equalsIgnoreCase("quit")) {

			} else {
				System.out.println("Didn't quite catch that...");
			}
		}
	}
}