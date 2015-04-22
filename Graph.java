import java.util.*;

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
		public PersonNotFoundException(String s) {
			super(s);
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
			throw new PersonNotFoundException("Person \"" + p2.name + "\" that goes to \"" + p2.school + "\" not found.");
		}

		mEdgeIndex.put(p1, new PersonNode(p2, (mEdgeIndex.containsKey(p1) ? mEdgeIndex.get(p1) : null)));
		mEdgeIndex.put(p2, new PersonNode(p1, (mEdgeIndex.containsKey(p2) ? mEdgeIndex.get(p2) : null)));
	}

	public Person nameQuery(String name) {
		if (mPersonNameIndex.containsKey(name)) {
			return mPersonNameIndex.get(name);
		} else return null;
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