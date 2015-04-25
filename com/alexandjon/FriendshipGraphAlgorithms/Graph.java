package com.alexandjon.FriendshipGraphAlgorithms;

import java.util.*;
import java.lang.*;


public class Graph {
	////////////////////////////////////////////////////////////////////////////////
	public static class Person {
		String name;
		String school; // If no school, set to null.

		Person(String name, String school) {
			this.name = name;
			this.school = school;
		}

		@Override
		public boolean equals(Object object) {
			if (object instanceof Person && ((Person)object).name == this.name && ((Person)object).school == this.school)
				return true;
			else
				return false;
		}

		@Override
		public String toString() {
			return "(" + name + (school != null ? ", " + school : "") + ")";
		}
	}

	public static class PersonNode {
		Person data;
		PersonNode next;

		public PersonNode(Person data, PersonNode next) {
			this.data = data;
			this.next = next;
		}
	}

	@SuppressWarnings("serial")
	public static class PersonNotFoundException extends Exception {
		public PersonNotFoundException() {
			super();
		}
		public PersonNotFoundException(String s) {
			super(s);
		}
		public PersonNotFoundException(Person p) {
			super("Person \"" + p.name + "\" that goes to \"" + p.school + "\" not found.");
		}
	}
	////////////////////////////////////////////////////////////////////////////////

	
	private HashMap<Person,PersonNode> mEdgeIndex;
	private HashMap<Person,Person> mPersonIndex;
	private HashMap<String,Person> mPersonNameIndex;

	public Graph() {
		mEdgeIndex = new HashMap<>();
		mPersonIndex = new HashMap<>();
		mPersonNameIndex = new HashMap<>();
	}

	public void addPerson(Person person) {
		if (mPersonNameIndex.containsKey(person.name) && mPersonNameIndex.get(person.name).equals(person)) return;

		mPersonIndex.put(person, person);
		mPersonNameIndex.put(person.name, person);
	}

	public void addEdge(Person p1, Person p2) throws PersonNotFoundException {
		if (!mPersonIndex.containsKey(p1)) {
			throw new PersonNotFoundException("Person \"" + p1.name + "\" that goes to \"" + p1.school + "\" not found.");
		} else if (!mPersonIndex.containsKey(p2)) {
			throw new PersonNotFoundException(p2);
		}

		mEdgeIndex.put(p1, new PersonNode(p2, (mEdgeIndex.containsKey(p1) ? mEdgeIndex.get(p1) : null)));
		mEdgeIndex.put(p2, new PersonNode(p1, (mEdgeIndex.containsKey(p2) ? mEdgeIndex.get(p2) : null)));
	}

	public Person nameQuery(String name) {
		if (mPersonNameIndex.containsKey(name)) {
			return mPersonNameIndex.get(name);
		} else return null;
	}

	ArrayList<Person> shortestPath(Person start, Person finish) throws PersonNotFoundException {
		MinHeap<Person,Integer> fringe = new MinHeap<>();
		HashMap<Person,Integer> distances = new HashMap<>();
		HashMap<Person,Person> prevPerson = new HashMap<>();

		if (!mPersonIndex.containsKey(start)) throw new PersonNotFoundException(start);
		if (!mPersonIndex.containsKey(finish)) throw new PersonNotFoundException(finish);

		distances.put(start, 0);
		for (PersonNode p = mEdgeIndex.get(start); p != null; p = p.next) {
			distances.put(p.data, 1);
			fringe.insertNode(p.data, 1);
		}
		
		MinHeap.HeapNode<Person,Integer> top;
		while ((top = fringe.pop()) != null) {
			if (top.data == finish) {
				break;
			}

			for (PersonNode neighbor = mEdgeIndex.get(top.data); neighbor != null; neighbor = neighbor.next) {
				if (!distances.containsKey(neighbor.data)) {
					prevPerson.put(neighbor.data, top.data);
					distances.put(neighbor.data, distances.get(top.data) + 1);
				} else {
					int curDist = distances.get(neighbor.data), newDist = distances.get(top.data) + 1;
					if (newDist < curDist) {
						distances.put(neighbor.data, newDist);
						prevPerson.put(neighbor.data, top.data);
					}
				}
			}
		}

		if (!prevPerson.containsKey(finish)) return null;

		ArrayList<Person> path = new ArrayList<>();
		for (Person p = finish; p != start; p = prevPerson.get(p))
			path.add(0, p);
		path.add(0, start);

		return path;
	}

	public void printConnections() {
		for (Person p : mEdgeIndex.keySet()) {
			System.out.print(p.name);

			PersonNode root = mEdgeIndex.get(p);
			if (root != null) {
				System.out.print(":\t( ");
				while (root != null) {
					System.out.print(root.data.toString());
					if (root.next != null) System.out.print(" , ");
					root = root.next;
				}
				System.out.print(" )");
			}

			System.out.println();
		}
	}
}