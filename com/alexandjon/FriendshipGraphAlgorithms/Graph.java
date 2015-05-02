package com.alexandjon.FriendshipGraphAlgorithms;

import java.lang.*;
import java.util.*;
import java.io.*;


public class Graph {
	////////////////////////////////////////////////////////////////////////////////
	public static class Person {
		String name;
		String school; // If no school, set to null.
		int vnum;

		Person(String name, String school) {
			this.name = name;
			this.school = school;
		}

		Person(String name, String school, int vnum) {
			this.name = name;
			this.school = school;
			this.vnum = vnum;
		}

		Person(Person person) {
			this.name = person.name;
			this.school = person.school;
			this.vnum = vnum;
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

		@Override
		public String toString() {
			return data.toString() + (next != null ? "-->" + next.toString() : "");
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

	@SuppressWarnings("serial")
	public static class DuplicatePersonException extends Exception {
		public DuplicatePersonException() {
			super();
		}
		public DuplicatePersonException(String s) {
			super(s);
		}
		public DuplicatePersonException(Person p) {
			super("Person \"" + p.name + "\" that goes to \"" + p.school + "\" already exists.");
		}
	}
	////////////////////////////////////////////////////////////////////////////////

	private ArrayList<Person> mVertices;
	private ArrayList<PersonNode> mEdges;
	
	private HashMap<String,Person> mPersonNameIndex;
	private HashMap<String,PersonNode> mSchoolIndex;

	public Graph() {
		mVertices = new ArrayList<>();
		mEdges = new ArrayList<>();

		mPersonNameIndex = new HashMap<>();
		mSchoolIndex = new HashMap<>();
	}

	public void addPerson(Person person) throws DuplicatePersonException {
		if (mPersonNameIndex.containsKey(person.name.toLowerCase())) throw new DuplicatePersonException(person);

		Person scrub = new Person(person);
		scrub.vnum = mVertices.size();

		mVertices.add(scrub);
		mEdges.add(null);

		mPersonNameIndex.put(scrub.name.toLowerCase(), scrub);

		if (scrub.school != null) {
			mSchoolIndex.put(
				scrub.school.toLowerCase(),
				new PersonNode(
					scrub,
				(mSchoolIndex.containsKey(scrub.school.toLowerCase()) ? 
						mSchoolIndex.get(scrub.school.toLowerCase()) : null)));
		}
	}

	public void addEdge(Person p1, Person p2) throws PersonNotFoundException {
		if (p1 == null || p2 == null) return;
		if (!inGraph(p1)) {
			throw new PersonNotFoundException(p1);
		} else if (!inGraph(p2)) {
			throw new PersonNotFoundException(p2);
		}

		mEdges.set(p1.vnum, new PersonNode(p2, mEdges.get(p1.vnum)));
		mEdges.set(p2.vnum, new PersonNode(p1, mEdges.get(p2.vnum)));
	}

	public boolean inGraph(Person p) {
		return mVertices.get(p.vnum) == p;
	}

	public Person nameQuery(String name) {
		if (mPersonNameIndex.containsKey(name.toLowerCase())) {
			return mPersonNameIndex.get(name.toLowerCase());
		} else return null;
	}
	
	public PersonNode schoolQuery(String school)
	{
		if(mSchoolIndex.containsKey(school.toLowerCase()))
		{
			return mSchoolIndex.get(school.toLowerCase());
		}
		else
			return null;
	}
	PersonNode getEdge(int vnum) {
		return mEdges.get(vnum);
	}
	
	Person getVertex(int vnum) {
		return mVertices.get(vnum);
	}

	int size() { return mVertices.size(); }

	public void printConnections() {
		System.out.println(size());

		for (Person p : mVertices) {
			System.out.println(p.name + "|" + (
				p.school != null ? "y|" + p.school : "n"
				));
		}

		for (Person p : mVertices) {
			for (PersonNode c = mEdges.get(p.vnum); c != null; c = c.next) {
				if (c.data.vnum > p.vnum) {
					System.out.println(p.name + "|" + c.data.name);
				}
			}
		}
	}
}