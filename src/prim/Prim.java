package prim;

import java.util.ArrayList;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

/**
 * Prim algorithm implementation (from Brassard and Bratley) and visualization
 * using GraphStream
 *
 * @author jgimitola
 */
public class Prim {

    private int N; // Vertices to use.

    private int INF = Integer.MAX_VALUE / 2;

    protected String styleSheet = "node {size: 16px, 16px; z-index: 2;"
            + "text-background-mode: rounded-box; text-background-color: #e0b020;"
            + "text-alignment: above; text-padding: 4;} edge.marked {fill-color: red; size: 3;}"
            + "edge {text-color: blue; text-alignment: along;} ";

    public static void main(String args[]) {
        System.setProperty("org.graphstream.ui", "swing");
        new Prim().prim();
    }

    public void prim() {
        Graph G = new SingleGraph("G");
        G.setAttribute("ui.stylesheet", styleSheet);
        G.setAttribute("ui.antialias");
        G.setAttribute("ui.quality");

//        int[][] L
//                = {{INF, 50, 30, 100, 10},
//                {50, INF, 5, 20, INF},
//                {30, 5, INF, 50, INF},
//                {100, 20, 50, INF, 10},
//                {10, INF, INF, 10, INF}};
        int[][] L
                = {{INF, 10, 20, INF, INF, INF},
                {10, INF, INF, 50, 10, INF},
                {20, INF, INF, 20, 33, INF},
                {INF, 50, 20, INF, 20, 2},
                {INF, 10, 33, 20, INF, 1},
                {INF, INF, INF, 2, 1, INF}};

        this.N = L.length;

        String[] tags = tagGenerator();
        Node[] nodes = new Node[N];
        for (int i = 0; i < N; i++) {
            nodes[i] = G.addNode(tags[i]);
            nodes[i].setAttribute("ui.label", nodes[i].getId());
        }

        for (int i = 0; i < N; i++) {
            for (int j = i; j < N; j++) {
                if (i != j && L[i][j] != INF) {
                    Edge e = G.addEdge(tags[i] + tags[j], nodes[i], nodes[j]);
                    e.setAttribute("ui.label", L[i][j]);
                }
            }
        }

        ArrayList<Edge> T = new ArrayList<>();
        int[] masProximo = new int[N];
        int[] distMin = new int[N];

        G.display();

        for (int i = 1; i < N; i++) {
            masProximo[i] = 0;
            distMin[i] = L[i][0];
        }
        for (int h = 0; h < N - 1; h++) {
            sleep(1200);

            int min = INF;
            int k = -1;
            for (int j = 1; j < N; j++) {
                if (0 <= distMin[j] && distMin[j] < min) {
                    min = distMin[j];
                    k = j;
                }
            }

            Edge e = nodes[masProximo[k]].getEdgeBetween(nodes[k]);
            e.setAttribute("ui.class", "marked");
            T.add(e);

            distMin[k] = -1;
            for (int j = 1; j < N; j++) {
                if (L[j][k] < distMin[j]) {
                    distMin[j] = L[j][k];
                    masProximo[j] = k;
                }
            }
        }
    }

    /**
     * Generate tags to use as name in the nodes.
     *
     * @return array of tags
     */
    public String[] tagGenerator() {
        String[] tags = new String[N];
        for (int i = 0; i < N; i++) {
            tags[i] = (i < 26 ? "" : tags[i - 26]) + (char) (i % 26 + 65);
        }
        return tags;
    }

    /**
     * Stop the thread to allow the animations.
     *
     * @param time time to stop in milliseconds
     */
    protected void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
        }
    }

}
