import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import java.util.TreeMap;
import java.util.TreeSet;

public class WordNet {

    private Digraph wordNet;
    private TreeMap<String, TreeSet<Integer>> synNouns;
    private TreeMap<Integer, String> synEntries;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms)
    {
        if(synsets == null || hypernyms == null) throw new IllegalArgumentException();

        synEntries = new TreeMap<>();
        synNouns = new TreeMap<>();

        initSynset(synsets);
        initHypernym(hypernyms);

        // check if rooted DAG
        DirectedCycle dc = new DirectedCycle(wordNet);
        if(dc.hasCycle()) throw new IllegalArgumentException();
        int numRoots = 0;

        for(int i = 0; i < wordNet.V(); i++)
            if(wordNet.outdegree(i) == 0) numRoots++;

        if(numRoots != 1) throw new IllegalArgumentException();
        sap = new SAP(wordNet);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() { return synNouns.keySet(); }

    // is the word a WordNet noun?
    public boolean isNoun(String word)
    {
        if(word == null) throw new IllegalArgumentException();
        return synNouns.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB)
    {
        validateNoun(nounA, nounB);

        // retrieve synset vertices of each noun
        TreeSet<Integer> A = synNouns.get(nounA);
        TreeSet<Integer> B = synNouns.get(nounB);
        return sap.length(A,B);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB)
    {
        validateNoun(nounA, nounB);

        // retrieve synset vertices of each noun
        TreeSet<Integer> A = synNouns.get(nounA);
        TreeSet<Integer> B = synNouns.get(nounB);
        int ancestor = sap.ancestor(A,B);
        return synEntries.get(ancestor);
    }

    private void initSynset(String synsets)
    {
        In in = new In(synsets);
        String line = in.readLine();
        while(line != null)
        {
            String synEntry[] = line.split(",");
            int index = Integer.parseInt(synEntry[0]);
            synEntries.put(index, synEntry[1]);
            String nouns[] = synEntry[1].split(" ");
            for(String s : nouns)
            {
                if(!synNouns.containsKey(s)) {
                    TreeSet<Integer> vertices = new TreeSet<>();
                    vertices.add(index);
                    synNouns.put(s, vertices);
                }else{
                    // append vertex into list of existing synset vertices
                    synNouns.get(s).add(index);
                }
            }
            line = in.readLine();
        }
    }

    private void initHypernym(String hypernyms)
    {
        wordNet = new Digraph(synEntries.size());
        In in = new In(hypernyms);
        String line = in.readLine();
        while(line != null)
        {
            String hypEntry[] = line.split(",");
            for(int i = 1; i < hypEntry.length; i++)
            {
                int a = Integer.parseInt(hypEntry[0]);
                int b = Integer.parseInt(hypEntry[i]);
                wordNet.addEdge(a,b);
            }
            line = in.readLine();
        }
    }

    private void validateNoun(String a, String b)
    {
        if(a == null || b == null || !isNoun(a) || !isNoun(b))
            throw new IllegalArgumentException();
    }

    // do unit testing of this class
    public static void main(String[] args)
    {
        WordNet wordNet = new WordNet("/Users/raghav/Desktop/synsets.csv","/Users/raghav/Desktop/hypernyms.csv");
        System.out.println();
        System.out.println("The list of all synset nouns is: ");
        for(String s : wordNet.nouns())
            System.out.println(s);

        System.out.println();

        System.out.println("Is 'Red' a synset noun? " +
                                   wordNet.isNoun("Red"));
        System.out.println("Is 'AIDS' a synset noun? " +
                                   wordNet.isNoun("AIDS"));

        System.out.println();

        System.out.println("Distance between AIDS and Herpes: " +
                                   wordNet.distance("AIDS", "Herpes"));
        System.out.println("Distance between Gonorrhea and Blue: " +
                                   wordNet.distance("Gonorrhea","Blue"));

        System.out.println();

        System.out.println("Shortest common ancestor of AIDS and Herpes: " +
                                   wordNet.sap("AIDS", "Herpes"));
        System.out.println("Shortest common ancestor of Gonorrhea and Blue: " +
                                   wordNet.sap("Gonorrhea", "Blue"));

    }
}
