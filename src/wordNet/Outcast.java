import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private final WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) { this.wordNet = wordnet; }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        String outcast  = "";
        int distance = -1;

        for(String s : nouns){
            int sum = 0;
            for(String m: nouns){
                sum += wordNet.distance(s, m);
            }
            if(distance < sum){
                distance = sum;
                outcast = s;
            }
        }
        return outcast;
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
