import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

public class SAP {

    private final Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G)
    {
        if (G == null) throw new IllegalArgumentException();
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validateVertex(v, w);
        int shortAncestor = -10, SAP = Integer.MAX_VALUE;

        // Calculate shortest paths between v, w and all other vertices in G
        BreadthFirstDirectedPaths bfdp1 = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfdp2 = new BreadthFirstDirectedPaths(G, w);

        for (int i = 0; i < G.V(); i++) {
            if (bfdp1.hasPathTo(i) && bfdp2.hasPathTo(i)) {
                if (SAP > bfdp1.distTo(i) + bfdp2.distTo(i)) {
                    SAP = bfdp1.distTo(i) + bfdp2.distTo(i);
                    shortAncestor = i;
                }
            }
        }
        if (SAP == Integer.MAX_VALUE) return -1;
        else return SAP;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        validateVertex(v, w);
        int shortAncestor = -10, SAP = Integer.MAX_VALUE;

        // Calculate shortest paths between v, w and all other vertices in G
        BreadthFirstDirectedPaths bfdp1 = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfdp2 = new BreadthFirstDirectedPaths(G, w);

        for (int i = 0; i < G.V(); i++) {
            if (bfdp1.hasPathTo(i) && bfdp2.hasPathTo(i)) {
                if (SAP > bfdp1.distTo(i) + bfdp2.distTo(i)) {
                    SAP = bfdp1.distTo(i) + bfdp2.distTo(i);
                    shortAncestor = i;
                }
            }
        }
        if (shortAncestor >= 0) return shortAncestor;
        else return -1;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validateSources(v, w);
        int shortSubAncestor = -10, SubSAP = Integer.MAX_VALUE;

        // Find paths between vertices in G and all members of v and w respectively
        BreadthFirstDirectedPaths vPaths = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths wPaths = new BreadthFirstDirectedPaths(G, w);

        for (int i = 0; i < G.V(); i++) {
            if ((vPaths.hasPathTo(i) && wPaths.hasPathTo(i))) {
                if (SubSAP > vPaths.distTo(i) + wPaths.distTo(i)) {
                    SubSAP = vPaths.distTo(i) + wPaths.distTo(i);
                    shortSubAncestor = i;
                }
            }
        }

        if (SubSAP == Integer.MAX_VALUE) return -1;
        else return SubSAP;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateSources(v, w);
        int shortSubAncestor = -10, SubSAP = Integer.MAX_VALUE;

        // Find paths between vertices in G and all members of v and w respectively
        BreadthFirstDirectedPaths vPaths = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths wPaths = new BreadthFirstDirectedPaths(G, w);

        for (int i = 0; i < G.V(); i++) {
            if ((vPaths.hasPathTo(i) && wPaths.hasPathTo(i))) {
                if (SubSAP > vPaths.distTo(i) + wPaths.distTo(i)) {
                    SubSAP = vPaths.distTo(i) + wPaths.distTo(i);
                    shortSubAncestor = i;
                }
            }
        }
        if (shortSubAncestor >= 0)
            return shortSubAncestor;
        else return -1;
    }

    private void validateVertex(int v, int w) {
         if( !((Integer.valueOf(v) != null && Integer.valueOf(w) != null) &&
                (v >= 0 && v < G.V()) && (w >= 0 && w < G.V())) )
             throw new IllegalArgumentException();
    }

    private void validateSources(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        for (Integer x : v) {
            if (!(x >= 0 && x < G.V()) || x == null)
                throw new IllegalArgumentException();
        }
        for (Integer x : w) {
            if (!(x >= 0 && x < G.V()) || x == null)
                throw new IllegalArgumentException();
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {

        /*
        Digraph G = new Digraph(7);
        G.addEdge(1,0);
        G.addEdge(2,0);
        G.addEdge(3,1);
        G.addEdge(4,1);
        G.addEdge(5,2);
        G.addEdge(6,2);
        System.out.println("Initiating Digraph: ");
        System.out.println(G.toString());

        SAP sap = new SAP(G);
        System.out.println("The shortest common ancestor between 4 and 5 is: " +
                                   sap.ancestor(4,5));
        System.out.println("The length of SAP is: " +
                                   sap.length(4,5));

        System.out.println();
        Queue<Integer> A, B;
        A = new Queue<>();
        B = new Queue<>();
        A.enqueue(1);
        A.enqueue(3);
        B.enqueue(4);
        System.out.println("SCA of {1, 3} and {4} is: " + sap.ancestor(A, B));
        System.out.println("Length of SubSAP: " + sap.length(A, B));
        */

        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);

        }
    }
}