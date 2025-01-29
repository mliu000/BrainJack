package muyel.utility;

/*
 * @Mu Ye Liu - Jan 2025
 * 
 * Represents a templated pair class, inspired by c++. 
 */
public class Pair<K, V> {

    // Stores the keys and values (first and second)
    private K first; 
    private V second; 
    
    // Constructs a pair with the given parameters
    public Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }

    ///// SETTER METHODS /////
    
    public void setFirst(K first) { this.first = first; }
    public void setSecond(V second) { this.second = second; }

    ///// GETTER METHODS /////
    
    public K getFirst() { return first; }
    public V getSecond() { return second; }
} 
