import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class BTreeNode {
    public static final int DEGREE = 10;           // Minimum degree t=10
    public static final int MAX_KEYS = 2 * DEGREE - 1; // 19
    public static final int MAX_CHILDREN = 2 * DEGREE; // 20
    public static final int BLOCK_SIZE = 512;

    public long blockID;
    public long parentID;
    public int numKeys;

    public long[] keys = new long[MAX_KEYS];
    public long[] values = new long[MAX_KEYS];
    public long[] children = new long[MAX_CHILDREN];

    public BTreeNode(long blockID, long parentID) {
        this.blockID = blockID;
        this.parentID = parentID;
        this.numKeys = 0;

        Arrays.fill(keys, 0);
        Arrays.fill(values, 0);
        Arrays.fill(children, 0);
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(BLOCK_SIZE);
        buffer.order(ByteOrder.BIG_ENDIAN);

        buffer.putLong(blockID);
        buffer.putLong(parentID);
        buffer.putLong(numKeys);

        for (long key : keys) buffer.putLong(key);
        for (long value : values) buffer.putLong(value);
        for (long child : children) buffer.putLong(child);

        // pad remaining bytes with 0 (ByteBuffer does this automatically)
        return buffer.array();
    }

    public static BTreeNode fromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.BIG_ENDIAN);

        long blockID = buffer.getLong();
        long parentID = buffer.getLong();
        int numKeys = (int) buffer.getLong();

        BTreeNode node = new BTreeNode(blockID, parentID);
        node.numKeys = numKeys;

        for (int i = 0; i < MAX_KEYS; i++) node.keys[i] = buffer.getLong();
        for (int i = 0; i < MAX_KEYS; i++) node.values[i] = buffer.getLong();
        for (int i = 0; i < MAX_CHILDREN; i++) node.children[i] = buffer.getLong();

        return node;
    }
}
