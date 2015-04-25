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
			super("Person \"" + p.name + "\" that goes to \"" + p.school + "\" not found.");
		}
	}

	public static class MinHeap<S,T extends Comparable<T>>{
		public static class HeapNode<S,T extends Comparable<T>> {
			S data;
			T weight;

			public HeapNode(S data, T weight) {
				this.data = data;
				this.weight = weight;
			}

			public String toString() {
				return "(" + data.toString() + ", " + weight.toString() + ")";
			}
		}

		private ArrayList<HeapNode<S,T>> heap;

		public MinHeap() {
			heap = new ArrayList<>();
		}

		public void insertNode(S data, T weight) {
			HeapNode newNode = new HeapNode(data, weight);
			heap.add(newNode);

			int pre = heap.size() - 1, cur = (pre - 1)/2;
			HeapNode current = heap.get(cur);
			while (cur >= 0 && current.weight.compareTo(newNode.weight) > 0) {
				heap.set(pre, current);
				heap.set(cur, newNode);

				pre = cur;
				cur = (cur - 1)/2;
				current = heap.get(cur);
			}
		}

		public boolean empty() { return heap.size() == 0; }

		public HeapNode<S,T> pop() {
			if (empty()) return null;

			HeapNode<S,T> retVal = heap.get(0);
			
			HeapNode last = heap.get(heap.size()-1);
			heap.remove(heap.size()-1);
			heap.set(0, last);

			int pre = 0, cur = pre*2 + 1;
			while ((cur < heap.size() && last.weight.compareTo(heap.get(cur).weight) > 0)
					|| (cur + 1 < heap.size() && last.weight.compareTo(heap.get(cur+1).weight) > 0)) {
				
				int minChild;
				if (cur + 1 >= heap.size()) minChild = cur;
				else minChild = (heap.get(cur).weight.compareTo(heap.get(cur+1).weight) < 0 ? cur : cur+1);

				heap.set(pre, heap.get(minChild));
				heap.set(minChild, last);

				pre = minChild;
				cur = pre*2 + 1;
			}


			return retVal;
		}

		public void printHeap() {
			int n = 1;
			for (int i = 0; i < heap.size(); n *= 2) {
				for (int j = 0; j < n && i + j < heap.size(); j++) {
					System.out.print(heap.get(i+j).toString());
					if (j+1 < n && i+j+1 < heap.size()) System.out.print(" , ");
				}
				i += n;
				System.out.println();
			}
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
			for (PersonNode neighbor = mEdgeIndex.get(top.data); neighbor != null; neighbor = neighbor.next) {
				if (!distances.containsKey(neighbor.data)) {
					prevPerson.put(neighbor.data, top.data);
					distances.put(neighbor.data, top.weight + 1);
				} else {
					int curDist = distances.get(neighbor.data),
					    newDist = top.weight + 1;
					if (newDist < curDist) {

					}
				}
			}
		}

		return null;
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