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
				final String name = line[0];
				final String school = (line.length == 2 ? null : line[2]);
				Graph.Person p = new Graph.Person(name, school);
				System.out.println("Adding " + p.toString());
				retVal.addPerson(p);
			} else if (line.length == 2) {
				try {
					final Graph.Person p1 = retVal.nameQuery(line[0]);
					final Graph.Person p2 = retVal.nameQuery(line[1]);
					System.out.println("Adding edge from " + p1.toString() + " to " + p2.toString());
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
}