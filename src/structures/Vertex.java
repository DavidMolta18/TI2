package structures;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Vertex<T> {
    private T value;
    private final List<Edge<T>> adjacent;

    public Vertex(T value) {
        this.value = value;
        adjacent = new ArrayList<>();
    }

    public T getValue() {
        return value;
    }

    public void addEdge(Edge<T> edge) {
        adjacent.add(edge);
    }

    public List<Edge<T>> getAdjacent() {
        return adjacent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex<?> vertex = (Vertex<?>) o;
        return Objects.equals(value, vertex.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
