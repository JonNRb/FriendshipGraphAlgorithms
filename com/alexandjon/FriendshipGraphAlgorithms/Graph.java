package com.alexandjon.FriendshipGraphAlgorithms;

import java.lang.*;
import java.util.*;
import java.io.*;


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

	private abstract class Traverser {
		private boolean mEndTraversal;
		public void endTraversal() {
			mEndTraversal = true;
		}

		abstract boolean onVisitForward(Person current, Person prev);
		abstract boolean onVisitBackward(Person current, Person prev);

		public void dfs(Person start) throws PersonNotFoundException {
			mEndTraversal = false;
			dfs(start, null);
		}

		public void dfs(Person start, Person prev) throws PersonNotFoundException {
			if (start == null) return;
			if (!mPersonIndex.containsKey(start)) throw new PersonNotFoundException(start);

			if (prev == null) onVisitForward(start, null);
			if (mEndTraversal) return;

			for (PersonNode i = mEdgeIndex.get(start); i != null; i = i.next) {
				//if (i.data.equals(prev)) continue;

				if (onVisitForward(i.data, start)) continue;
				if (mEndTraversal) return;

				dfs(i.data, start);

				if (onVisitBackward(start, i.data)) break;
				if (mEndTraversal) return;
			}

			return;
		}

		public void bfs(Person start) throws PersonNotFoundException {
			mEndTraversal = false;
			bfs(start, null);
		}

		private void bfs(Person start, Person prev) throws PersonNotFoundException {
			if (start == null) return;
			if (!mPersonIndex.containsKey(start)) throw new PersonNotFoundException(start);

			if (prev == null) onVisitForward(start, null);
			if (mEndTraversal) return;

			HashMap<Person,Boolean> retVals = new HashMap<>();

			for (PersonNode i = mEdgeIndex.get(start); i != null; i = i.next) {
				//if (i.data.equals(prev)) continue;
				retVals.put(i.data, onVisitForward(i.data, start));
				if (mEndTraversal) return;
			}

			for (PersonNode i = mEdgeIndex.get(start); i != null; i = i.next) {
				//if (i.data.equals(prev)) continue;

				if (retVals.get(i.data)) continue;

				bfs(i.data, start);
				if (mEndTraversal) return;
			}

			for (PersonNode i = mEdgeIndex.get(start); i != null; i = i.next) {
				//if (i.data.equals(prev)) continue;
				if (onVisitBackward(start, i.data)) break;
				if (mEndTraversal) return;
			}

			return;
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
		if (mPersonNameIndex.containsKey(person.name.toLowerCase()) && mPersonNameIndex.get(person.name.toLowerCase()).equals(person)) return;

		mEdgeIndex.put(person, null);
		mPersonIndex.put(person, person);
		mPersonNameIndex.put(person.name.toLowerCase(), person);
	}

	public void addEdge(Person p1, Person p2) throws PersonNotFoundException {
		if (p1 == null || p2 == null) return;
		if (!mPersonIndex.containsKey(p1)) {
			throw new PersonNotFoundException(p1);
		} else if (!mPersonIndex.containsKey(p2)) {
			throw new PersonNotFoundException(p2);
		}

		mEdgeIndex.put(p1, new PersonNode(p2, (mEdgeIndex.containsKey(p1) ? mEdgeIndex.get(p1) : null)));
		mEdgeIndex.put(p2, new PersonNode(p1, (mEdgeIndex.containsKey(p2) ? mEdgeIndex.get(p2) : null)));
	}

	public Person nameQuery(String name) {
		if (mPersonNameIndex.containsKey(name.toLowerCase())) {
			return mPersonNameIndex.get(name.toLowerCase());
		} else return null;
	}

	ArrayList<Person> shortestPath(Person start, Person finish) throws PersonNotFoundException {
		MinHeap<Person,Integer> fringe = new MinHeap<>();
		HashMap<Person,Integer> distances = new HashMap<>();
		HashMap<Person,Person> prevPerson = new HashMap<>();

		if (!mPersonIndex.containsKey(start)) throw new PersonNotFoundException(start);
		if (!mPersonIndex.containsKey(finish)) throw new PersonNotFoundException(finish);

		// Filter.
		start = mPersonIndex.get(start);
		finish = mPersonIndex.get(finish);

		if (!mEdgeIndex.containsKey(start) || !mEdgeIndex.containsKey(finish)) {
			return null;
		}

		distances.put(start, 0);
		for (PersonNode p = mEdgeIndex.get(start); p != null; p = p.next) {
			prevPerson.put(p.data, start);
			distances.put(p.data, 1);
			fringe.insertNode(p.data, 1);
		}
		
		MinHeap.HeapNode<Person,Integer> top;
		while ((top = fringe.pop()) != null) {
			if (top.data == finish) {
				break;
			}

			if (mEdgeIndex.containsKey(top.data))
			for (PersonNode neighbor = mEdgeIndex.get(top.data); neighbor != null; neighbor = neighbor.next) {
				if (!distances.containsKey(neighbor.data)) {
					int newDist = distances.get(top.data) + 1;
					prevPerson.put(neighbor.data, top.data);
					distances.put(neighbor.data, newDist);

					fringe.insertNode(neighbor.data, newDist);
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
		for (Person p = finish; p != start; p = prevPerson.get(p)) {
			if (p == null) {while (true);}
			path.add(0, p);
		}
		path.add(0, start);

		return path;
	}

	public Set<Person> getConnectors() {
		final HashMap<Person,Person> retVal = new HashMap<>();

		final HashMap<Person,Integer> dfsNum = new HashMap<>();
		final HashMap<Person,Integer> back = new HashMap<>();

		final HashMap<Person,Person> visited = new HashMap<>();

		final ArrayList<Person> people = new ArrayList<Person>(mPersonIndex.keySet());

		do {
			while (people.size() != 0 && visited.containsKey(people.get(0))) people.remove(0);
			if (people.size() == 0) break;
			final Person start = people.remove(0);

			Traverser t = new Traverser() {
				int dfsNumCounter = 0;
				Person start = null;

				boolean onVisitForward(Person cur, Person prev) {
					if (prev == null) {
						System.out.println(">>>STARTING<<<");
						dfsNumCounter = 0;
						start = cur;
					}

					System.out.print("---> " + cur.toString() + " from " + (prev != null ? prev.toString() : null));

					if (!visited.containsKey(cur)) {
						dfsNum.put(cur, ++dfsNumCounter);
						back.put(cur, dfsNumCounter);
						System.out.println(" (" + dfsNum.get(cur) + "," + back.get(cur) + ")");
					} else {
						back.put(prev, Math.min(back.get(prev), dfsNum.get(cur)));
						System.out.println(" ALREADY VISITED, " + prev.name + " (" + dfsNum.get(prev) + "," + back.get(prev) + ")");
						return true;
					}

					visited.put(cur, cur);
					return false;
				}

				boolean onVisitBackward(Person cur, Person prev) {
					System.out.print("<--- " + cur.toString() + " from " + (prev != null ? prev.toString() : null));

					boolean awef = false, awef2 = false;
					if (dfsNum.get(cur) > back.get(prev)) {
						awef2 = true;
						back.put(cur, Math.min(back.get(cur), back.get(prev)));
					} else if (!cur.equals(start)) {
						awef = true;
						retVal.put(cur, cur);
					}

					System.out.println(" (" + dfsNum.get(cur) + "," + back.get(cur) + ")");

					if (awef) System.out.println("CONNECTOR FOUND: " + cur.name);
					if (awef2) System.out.println("dfsNum.get(" + cur.name + ") > back.get(" + prev.name + ")");

					return false;
				}
			};

			try {
				t.dfs(start);
				
				PersonNode neighbor = mEdgeIndex.get(start);
				if (neighbor != null) {
					visited.clear();
					dfsNum.clear();
					back.clear();
					
					t.dfs(neighbor.data);
				}
			} catch (PersonNotFoundException e) {
				e.printStackTrace();
			}

		} while (people.size() > 0);

		for (Person p : dfsNum.keySet()) {
			System.out.println(p.name + "\t :" + dfsNum.get(p) + " , " + back.get(p));
		}

		return retVal.keySet();
	}

	public void testDfs(Person start) {
		final HashMap<Person,Person> visited = new HashMap<>();

		Traverser t = new Traverser() {
			boolean onVisitForward(Person p, Person prev) {
				if (visited.containsKey(p)) return true;
				System.out.println("---> " + p.toString() + " from " + (prev != null ? prev.toString() : null));
				visited.put(p, p);
				return false;
			}

			boolean onVisitBackward(Person p, Person prev) {
				System.out.println("<--- " + p.toString() + " from " + (prev != null ? prev.toString() : null));
				return false;
			}
		};

		System.out.println("TESTING DFS");
		try {
			t.dfs(start);
		} catch (PersonNotFoundException e) {
			e.printStackTrace();
		}
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