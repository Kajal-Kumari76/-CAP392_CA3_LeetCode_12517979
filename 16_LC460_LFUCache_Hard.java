class LFUCache {
    class Node {
        int key, value, freq;
        Node(int key, int value) {
            this.key = key;
            this.value = value;
            this.freq = 1;
        }
    }
    Map<Integer, Node> cache;
    Map<Integer, LinkedHashSet<Integer>> frequency;
    int capacity;
    int minFreq;
    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.minFreq = 0;
        cache = new HashMap<>();
        frequency = new HashMap<>();
    }
    public int get(int key) {
        if (!cache.containsKey(key)) {
            return -1;
        }
        Node node = cache.get(key);
        updateFrequency(node);
        return node.value;
    }
    public void put(int key, int value) {
        if (capacity == 0) {
            return;
        }
        if (cache.containsKey(key)) {
            Node node = cache.get(key);
            node.value = value;
            updateFrequency(node);
            return;
        }
        if (cache.size() == capacity) {
            LinkedHashSet<Integer> set = frequency.get(minFreq);
            int removeKey = set.iterator().next();
            set.remove(removeKey);
            cache.remove(removeKey);
        }
        Node node = new Node(key, value);
        cache.put(key, node);
        frequency.putIfAbsent(1, new LinkedHashSet<>());
        frequency.get(1).add(key);
        minFreq = 1;
    }
    private void updateFrequency(Node node) {
        int freq = node.freq;
        frequency.get(freq).remove(node.key);
        if (frequency.get(freq).isEmpty()) {
            frequency.remove(freq);
            if (minFreq == freq) {
                minFreq++;
            }
        }
        node.freq++;
        frequency.putIfAbsent(node.freq, new LinkedHashSet<>());
        frequency.get(node.freq).add(node.key);
    }
}
