package wechesspresso;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


class Graph {
    private final List<Vertex3> vertexes;
    private final List<Edge3> edges;
    
    public Graph(List<Vertex3> vertexes, List<Edge3> edges) {
        this.vertexes = vertexes;
        this.edges = edges;
    }
    
    public List<Vertex3> getVertex3es() {
        return vertexes;
    }
    
    public List<Edge3> getEdge3s() {
        return edges;
    }
}

class Vertex3 {
    
    final private String id;
    final private String name;
    
    
    public Vertex3(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
    
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vertex3 other = (Vertex3) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
    
    public String toString() {
        return name;
    }
}

class Edge3 {
    private final String id;
    private final Vertex3 source;
    private final Vertex3 destination;
    private final int weight;
    
    public Edge3(String id, Vertex3 source, Vertex3 destination, int weight) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }
    
    public String getId() {
        return id;
    }
    public Vertex3 getDestination() {
        return destination;
    }
    
    public Vertex3 getSource() {
        return source;
    }
    public int getWeight() {
        return weight;
    }
  
    public String toString() {
        return source + " " + destination;
    }
}



public class Dijkstra3 {
    
    private final List<Vertex3> nodes;
    private final List<Edge3> edges;
    private Set<Vertex3> settledNodes;
    private Set<Vertex3> unSettledNodes;
    private Map<Vertex3, Vertex3> predecessors;
    private Map<Vertex3, Integer> distance;
    
    public Dijkstra3(Graph graph) {
        // create a copy of the array so that we can operate on this array
        this.nodes = new ArrayList<Vertex3>(graph.getVertex3es());
        this.edges = new ArrayList<Edge3>(graph.getEdge3s());
    }
    
    public void execute(Vertex3 source) {
        settledNodes = new HashSet<Vertex3>();
        unSettledNodes = new HashSet<Vertex3>();
        distance = new HashMap<Vertex3, Integer>();
        predecessors = new HashMap<Vertex3, Vertex3>();
        distance.put(source, 0);
        unSettledNodes.add(source);
        while (unSettledNodes.size() > 0) {
            Vertex3 node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            findMinimalDistances(node);
        }
    }
    
    private void findMinimalDistances(Vertex3 node) {
        List<Vertex3> adjacentNodes = getNeighbors(node);
        for (Vertex3 target : adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node)
                    + getDistance(node, target)) {
                distance.put(target, getShortestDistance(node)
                        + getDistance(node, target));
                predecessors.put(target, node);
                unSettledNodes.add(target);
            }
        }
        
    }
    
    private int getDistance(Vertex3 node, Vertex3 target) {
        for (Edge3 edge : edges) {
            if (edge.getSource().equals(node)
                    && edge.getDestination().equals(target)) {
                return edge.getWeight();
            }
        }
        throw new RuntimeException("Should not happen");
    }
    
    private List<Vertex3> getNeighbors(Vertex3 node) {
        List<Vertex3> neighbors = new ArrayList<Vertex3>();
        for (Edge3 edge : edges) {
            if (edge.getSource().equals(node)
                    && !isSettled(edge.getDestination())) {
                neighbors.add(edge.getDestination());
            }
        }
        return neighbors;
    }
    
    private Vertex3 getMinimum(Set<Vertex3> vertexes) {
        Vertex3 minimum = null;
        for (Vertex3 vertex : vertexes) {
            if (minimum == null) {
                minimum = vertex;
            } else {
                if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                    minimum = vertex;
                }
            }
        }
        return minimum;
    }
    
    private boolean isSettled(Vertex3 vertex) {
        return settledNodes.contains(vertex);
    }
    
    private int getShortestDistance(Vertex3 destination) {
        Integer d = distance.get(destination);
        if (d == null) {
            return Integer.MAX_VALUE;
        } else {
            return d;
        }
    }
    
    /*
    * This method returns the path from the source to the selected target and
    * NULL if no path exists
    */
    public LinkedList<Vertex3> getPath(Vertex3 target) {
        LinkedList<Vertex3> path = new LinkedList<Vertex3>();
        Vertex3 step = target;
        // check if a path exists
        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }
        // Put it into the correct order
        Collections.reverse(path);
        return path;
    }
    
}


