class TestDriver {
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

		Graph.MinHeap<String,Integer> mh = new Graph.MinHeap<>();

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
		
		System.out.println();
	}
}