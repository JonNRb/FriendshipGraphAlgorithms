public class Graph {
	public class Person {
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
	}

	public class PersonNode {
		Person data;
		PersonNode next;

		public PersonNode(Person data, PersonNode next) {
			this.data = data;
			this.next = next;
		}
	}

	public class PersonNotFoundException extends Exception {
		public PersonNotFoundException(String s) {
			super(s);
		}
	}

	
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

	public Person nameQuery(String name) {
		if (mPersonNameIndex.containsKey(name)) {
			return mPersonNameIndex.get(name);
		} else return null;
	}
}