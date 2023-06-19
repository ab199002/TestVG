import java.util.HashMap;
import java.util.Map;

class SongNode {
    String song;
    String user;
    SongNode prev;
    SongNode next;

    public SongNode(String song, String user) {
        this.song = song;
        this.user = user;
        this.prev = null;
        this.next = null;
    }
}

class RecentlyPlayedStore {
    private int capacity;
    private int currentSize;
    private Map<String, SongNode> recentlyPlayed;
    private SongNode head;
    private SongNode tail;

    public RecentlyPlayedStore(int initialCapacity) {
        this.capacity = initialCapacity;
        this.currentSize = 0;
        this.recentlyPlayed = new HashMap<>();
        this.head = null;
        this.tail = null;
    }

    public void addSong(String song, String user) {
        if (recentlyPlayed.containsKey(user)) {
            removeSong(user);
        } else if (currentSize >= capacity) {
            evictLeastRecentlyPlayed();
        }

        SongNode newNode = new SongNode(song, user);
        recentlyPlayed.put(user, newNode);

        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }

        currentSize++;
    }

    public void removeSong(String user) {
        if (!recentlyPlayed.containsKey(user)) {
            return;
        }

        SongNode node = recentlyPlayed.get(user);
        recentlyPlayed.remove(user);

        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }

        currentSize--;
    }

    public void evictLeastRecentlyPlayed() {
        if (tail == null) {
            return;
        }

        String lruUser = tail.user;
        removeSong(lruUser);
    }

    public String getRecentlyPlayedSong(String user) {
        if (recentlyPlayed.containsKey(user)) {
            SongNode node = recentlyPlayed.get(user);
            removeSong(user);
            addSong(node.song, user);
            return node.song;
        }

        return null;
    }
}

public class Main {
    public static void main(String[] args) {
        RecentlyPlayedStore store = new RecentlyPlayedStore(3);  // Initialize with a capacity of 3

        store.addSong("S1", "User");
        store.addSong("S2", "User");
        store.addSong("S3", "User");
        store.addSong("S4", "User");
        store.addSong("S2", "User");
        store.addSong("S1", "User");

        String recentlyPlayedSong = store.getRecentlyPlayedSong("User");
        System.out.println(recentlyPlayedSong);  // Output: "S1"

        store.addSong("S5", "User");  // Evicts "S3" as the capacity is reached

        recentlyPlayedSong = store.getRecentlyPlayedSong("User");
        System.out.println(recentlyPlayedSong);  // Output: "S2"

        recentlyPlayedSong = store.getRecentlyPlayedSong("User");
        System.out.println(recentlyPlayedSong);  // Output: "S1"
    }
}