class TestDriver {
	public static void main(String[] argv) {
		Graph g = new Graph();

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

		g.printConnections();
	}
}