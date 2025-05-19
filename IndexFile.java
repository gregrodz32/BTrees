import java.io.*;
import java.util.*;

public class IndexFile {

    public static void create(String filename) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            byte[] header = new byte[512];
            Arrays.fill(header, (byte) 0);

            byte[] magic = Utils.getMagicNumberBytes();
            System.arraycopy(magic, 0, header, 0, 8);

            // Root offset = 512
            byte[] rootOffset = Utils.longToBytes(512);
            System.arraycopy(rootOffset, 0, header, 8, 8);

            byte[] nextOffset = Utils.longToBytes(1024);
            System.arraycopy(nextOffset, 0, header, 16, 8);

            // Node count = 1
            byte[] nodeCount = Utils.longToBytes(1);
            System.arraycopy(nodeCount, 0, header, 24, 8);

            fos.write(header);
        }

        // Write empty root node at offset 512
        try (RandomAccessFile raf = new RandomAccessFile(filename, "rw")) {
            raf.seek(512);
            BTreeNode root = new BTreeNode(1, 0); // ID 1, parent 0
            raf.write(root.toBytes());
        }

        System.out.println("Index file created: " + filename);
    }

    public static void insert(String filename, long key, long value) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(filename, "rw")) {
            raf.seek(8);
            long rootOffset = raf.readLong();
            raf.seek(rootOffset);

            byte[] nodeBytes = new byte[BTreeNode.BLOCK_SIZE];
            raf.readFully(nodeBytes);
            BTreeNode root = BTreeNode.fromBytes(nodeBytes);

            if (root.numKeys >= BTreeNode.MAX_KEYS) {
                System.out.println("Root is full. Splitting not implemented yet.");
                return;
            }

            // Find insert position
            int i = 0;
            while (i < root.numKeys && key > root.keys[i]) {
                i++;
            }

            if (i < root.numKeys && root.keys[i] == key) {
                System.out.println("Duplicate key. Ignoring insert.");
                return;
            }

            for (int j = root.numKeys; j > i; j--) {
                root.keys[j] = root.keys[j - 1];
                root.values[j] = root.values[j - 1];
            }

            root.keys[i] = key;
            root.values[i] = value;
            root.numKeys++;

            raf.seek(rootOffset);
            raf.write(root.toBytes());

            System.out.println("Inserted key " + key + " with value " + value);
        }
    }

    public static void search(String filename, long key) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(filename, "r")) {
            raf.seek(8);
            long rootOffset = raf.readLong();
            raf.seek(rootOffset);

            byte[] nodeBytes = new byte[BTreeNode.BLOCK_SIZE];
            raf.readFully(nodeBytes);
            BTreeNode root = BTreeNode.fromBytes(nodeBytes);

            for (int i = 0; i < root.numKeys; i++) {
                if (root.keys[i] == key) {
                    System.out.println("Found key: " + key + " value: " + root.values[i]);
                    return;
                }
            }

            System.out.println("Key not found: " + key);
        }
    }

    // Print all key/value pairs (from root only)
    public static void print(String filename) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(filename, "r")) {
            raf.seek(8);
            long rootOffset = raf.readLong();
            raf.seek(rootOffset);

            byte[] nodeBytes = new byte[BTreeNode.BLOCK_SIZE];
            raf.readFully(nodeBytes);
            BTreeNode root = BTreeNode.fromBytes(nodeBytes);

            System.out.println("Index contents:");
            for (int i = 0; i < root.numKeys; i++) {
                System.out.println(root.keys[i] + " -> " + root.values[i]);
            }
        }
    }

    // Loading values from CSV
    public static void load(String filename, String csvFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 2) continue;
                long key = Long.parseLong(parts[0].trim());
                long value = Long.parseLong(parts[1].trim());
                insert(filename, key, value);
            }
        }
    }

    // Extract all key/value pairs to a CSV file
    public static void extract(String filename, String csvFile) throws IOException {
        File file = new File(csvFile);
        if (file.exists()) {
            System.err.println("Output file already exists. Aborting.");
            return;
        }

        try (
            RandomAccessFile raf = new RandomAccessFile(filename, "r");
            PrintWriter writer = new PrintWriter(new FileWriter(file))
        ) {
            raf.seek(8);
            long rootOffset = raf.readLong();
            raf.seek(rootOffset);

            byte[] nodeBytes = new byte[BTreeNode.BLOCK_SIZE];
            raf.readFully(nodeBytes);
            BTreeNode root = BTreeNode.fromBytes(nodeBytes);

            for (int i = 0; i < root.numKeys; i++) {
                writer.println(root.keys[i] + "," + root.values[i]);
            }

            System.out.println("Extracted to: " + csvFile);
        }
    }
}
