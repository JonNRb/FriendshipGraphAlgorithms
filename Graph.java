import java.util.*;
import java.lang.*;

public class Graph {

	////////////////////////////////////////////////////////////////////////////////
	public static class Person {
		String name;
		String school;

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

	public static class PersonNotFoundException extends Exception {
		public PersonNotFoundException() {
			super();
		}
		public PersonNotFoundException(String s) {
			super(s);
		}
		public PersonNotFoundException(Person p) {
			if (p != null)
				super("Person \"" + p.name + "\" that goes to \"" + p.school + "\" not found.");
			else
				super();
		}
	}

	private class MinHeap<S,T implements Comparable><{
		private class HeapNode<S,T implements Comparable> {
			S data;
			T weight;

			public HeapNode<S,T>(S data, T weight) {
				this.data = data;
				this.weight = weight;
			}
		}

		private ArrayList<HeapNode<S,T>> heap;

		public MinHeap<S,T>() {
			heap = new ArrayList<>();
		}

		public void insertNode(S data, T weight) {
			HeapNode newNode = new HeapNode(data, weight);
			heap.add(newNode);

			int pre = heap.size() - 1
			int cur = (cur - 1)/2;
			HeapNode current = heap.get(cur);
			while (cur > 0 && current.weight > newNode.weight) {
				heap.set(pre, current);
				heap.set(cur, newNode);

				pre = cur;
				cur = (cur - 1)/2;
				HeapNode current = heap.get(cur);
			}
		}

		public Pair<S,T> pop() {
			Pair<S,T> retVal = new Pair<>(heap.get(0).data, heap.get(0).weight);
			
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

	List<Person> shortestPath(Person start, Person finish) throws PersonNotFoundException {
		BSTN<Person,Integer> fringe = new BSTN<>();
		HashMap<Person,Integer> distances = new HashMap<>();
		HashMap<Person,Person> prevPerson = new HashMap<>();

		if (!mPersonIndex.containsKey(start)) throw new PersonNotFoundException(start);
		if (!mPersonIndex.containsKey(finish)) throw new PersonNotFoundException(finish);

		distances.put(start, 0);
		for (PersonNode p = mEdgeIndex.get(start); p != null; p = p.next) {
			distances.put(p.data, 1);
			fringe.insertNode(p.data, 1);
		}

		while (!fringe.empty()) {

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