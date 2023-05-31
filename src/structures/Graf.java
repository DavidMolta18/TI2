package structures;

import java.util.List;

public interface Graf<T> {
    void addVertex(T data);
    void addEdge(T source, T destination, int weight);
    void removeVertex(T data);
    void removeEdge(T sourceData, T destData);
    List<T> bfs(T startVertex);
    List<T> dfs(T startVertex);
    // Agregar métodos para los algoritmos de Dijkstra y Floyd Warshall
    List<T> dijkstra(T startVertex, T endVertex);
    int[][] floydWarshall();
}
