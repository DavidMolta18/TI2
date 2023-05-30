import java.util.*;

public class AdjacencyListGraph<T> implements Graf<T> {
    private final Map<Vertex<T>, List<Edge<T>>> adjacencyList;

    private static final int WHITE = 0;
    private static final int GRAY = 1;
    private static final int BLACK = 2;

    public AdjacencyListGraph() {
        adjacencyList = new HashMap<>();
    }

    public Map<Vertex<T>, List<Edge<T>>> getAdjacencyList() {
        return adjacencyList;
    }

    public List<Edge<T>> getAdjacencyListEdges(Vertex<T> vertex) {
        return adjacencyList.get(vertex);
    }

    @Override
    public void addVertex(T data) {
        Vertex<T> vertex = new Vertex<>(data);
        adjacencyList.putIfAbsent(vertex, new ArrayList<>());
    }

    @Override
    public void addEdge(T source, T destination, int weight) {
        Vertex<T> sourceVertex = getVertex(source);
        Vertex<T> destinationVertex = getVertex(destination);

        Edge<T> edge = new Edge<>(sourceVertex, destinationVertex, weight);
        sourceVertex.addEdge(edge);
        destinationVertex.addEdge(edge);
    }

    @Override
    public void removeVertex(T data) {
        Vertex<T> vertexToRemove = new Vertex<>(data);
        List<Edge<T>> edgesToRemove = adjacencyList.remove(vertexToRemove);

        if (edgesToRemove != null) {
            for (Edge<T> edge : edgesToRemove) {
                Vertex<T> neighbor = edge.getDestination();
                adjacencyList.get(neighbor).remove(edge);
            }
        }
    }

    @Override
    public void removeEdge(T sourceData, T destData) {
        Vertex<T> source = getVertex(sourceData);
        Vertex<T> dest = getVertex(destData);

        List<Edge<T>> sourceEdges = adjacencyList.get(source);
        List<Edge<T>> destEdges = adjacencyList.get(dest);

        if (sourceEdges != null) {
            sourceEdges.removeIf(edge -> edge.getDestination().equals(dest));
        }

        if (destEdges != null) {
            destEdges.removeIf(edge -> edge.getDestination().equals(source));
        }
    }

    @Override
    public List<T> bfs(T startVertex) {
        List<T> result = new ArrayList<>();
        Map<Vertex<T>, Integer> colors = new HashMap<>();
        for (Vertex<T> vertex : adjacencyList.keySet()) {
            colors.put(vertex, WHITE);
        }

        Vertex<T> source = getVertex(startVertex);
        colors.put(source, GRAY);
        Queue<Vertex<T>> queue = new LinkedList<>();
        queue.add(source);

        while (!queue.isEmpty()) {
            Vertex<T> currentVertex = queue.poll();
            result.add(currentVertex.getValue());

            List<Edge<T>> edges = adjacencyList.get(currentVertex);
            for (Edge<T> edge : edges) {
                Vertex<T> neighbor = edge.getDestination();
                if (colors.get(neighbor) == WHITE) {
                    colors.put(neighbor, GRAY);
                    queue.add(neighbor);
                }
            }

            colors.put(currentVertex, BLACK);
        }

        return result;
    }

    @Override
    public List<T> dfs(T startVertex) {
        List<T> result = new ArrayList<>();
        Map<Vertex<T>, Integer> colors = new HashMap<>();
        for (Vertex<T> vertex : adjacencyList.keySet()) {
            colors.put(vertex, WHITE);
        }

        Vertex<T> source = getVertex(startVertex);
        dfsVisit(source, colors, result);

        return result;
    }

    private void dfsVisit(Vertex<T> vertex, Map<Vertex<T>, Integer> colors, List<T> result) {
        colors.put(vertex, GRAY);
        result.add(vertex.getValue());

        List<Edge<T>> edges = adjacencyList.get(vertex);
        if (edges != null) {
            for (Edge<T> edge : edges) {
                Vertex<T> neighbor = edge.getDestination();
                if (colors.get(neighbor) == WHITE) {
                    dfsVisit(neighbor, colors, result);
                }
            }
        }

        colors.put(vertex, BLACK);
    }

    @Override
    public List<T> dijkstra(T startVertex, T endVertex) {
        Vertex<T> source = getVertex(startVertex);
        Vertex<T> destination = getVertex(endVertex);

        Map<Vertex<T>, Integer> distances = new HashMap<>();
        Map<Vertex<T>, Vertex<T>> previous = new HashMap<>();
        PriorityQueue<Vertex<T>> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        for (Vertex<T> vertex : adjacencyList.keySet()) {
            distances.put(vertex, Integer.MAX_VALUE);
            previous.put(vertex, null);
            queue.add(vertex);
        }

        distances.put(source, 0);

        while (!queue.isEmpty()) {
            Vertex<T> currentVertex = queue.poll();

            if (currentVertex.equals(destination)) {
                break;
            }

            int currentDistance = distances.get(currentVertex);

            if (currentDistance == Integer.MAX_VALUE) {
                break;
            }

            List<Edge<T>> edges = adjacencyList.get(currentVertex);
            for (Edge<T> edge : edges) {
                Vertex<T> neighbor = edge.getDestination();
                int weight = edge.getWeight();
                int distanceThroughCurrent = currentDistance + weight;

                if (distanceThroughCurrent < distances.get(neighbor)) {
                    queue.remove(neighbor);
                    distances.put(neighbor, distanceThroughCurrent);
                    previous.put(neighbor, currentVertex);
                    queue.add(neighbor);
                }
            }
        }

        List<T> path = new ArrayList<>();
        Vertex<T> current = destination;

        while (current != null) {
            path.add(current.getValue());
            current = previous.get(current);
        }

        Collections.reverse(path);

        return path;
    }

    @Override
    public int[][] floydWarshall() {
        int size = adjacencyList.size();
        int[][] distances = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    distances[i][j] = 0;
                } else {
                    distances[i][j] = Integer.MAX_VALUE;
                }
            }
        }

        Map<Vertex<T>, Integer> vertexIndices = new HashMap<>();
        int index = 0;
        for (Vertex<T> vertex : adjacencyList.keySet()) {
            vertexIndices.put(vertex, index);
            index++;
        }

        for (Vertex<T> vertex : adjacencyList.keySet()) {
            int sourceIndex = vertexIndices.get(vertex);

            List<Edge<T>> edges = adjacencyList.get(vertex);
            for (Edge<T> edge : edges) {
                Vertex<T> destination = edge.getDestination();
                int destinationIndex = vertexIndices.get(destination);
                int weight = edge.getWeight();
                distances[sourceIndex][destinationIndex] = weight;
            }
        }

        for (int k = 0; k < size; k++) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (distances[i][k] != Integer.MAX_VALUE && distances[k][j] != Integer.MAX_VALUE) {
                        int throughK = distances[i][k] + distances[k][j];
                        if (throughK < distances[i][j]) {
                            distances[i][j] = throughK;
                        }
                    }
                }
            }
        }

        return distances;
    }

    private Vertex<T> getVertex(T data) {
        for (Vertex<T> vertex : adjacencyList.keySet()) {
            if (vertex.getValue().equals(data)) {
                return vertex;
            }
        }
        return null;
    }
}

