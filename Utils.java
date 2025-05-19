import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Utils {
    public static byte[] longToBytes(long value) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putLong(value);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.BIG_ENDIAN);
        return buffer.getLong();
    }

    public static byte[] getMagicNumberBytes() {
        return "4348PRJ3".getBytes();  // 8 ASCII bytes
    }

    public static boolean isMagicValid(byte[] magic) {
        return new String(magic).equals("4348PRJ3");
    }
}
