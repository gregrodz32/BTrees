import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java Main <command> <filename> [args...]");
            return;
        }

        String command = args[0].toLowerCase();
        String fileName = args[1];

        try {
            switch (command) {
                case "create":
                    IndexFile.create(fileName);
                    break;
                case "insert":
                    if (args.length != 4) {
                        System.err.println("Usage: insert <file> <key> <value>");
                        return;
                    }
                    long key = Long.parseUnsignedLong(args[2]);
                    long value = Long.parseUnsignedLong(args[3]);
                    IndexFile.insert(fileName, key, value);
                    break;
                case "search":
                    if (args.length != 3) {
                        System.err.println("Usage: search <file> <key>");
                        return;
                    }
                    long searchKey = Long.parseUnsignedLong(args[2]);
                    IndexFile.search(fileName, searchKey);
                    break;
                case "load":
                    if (args.length != 3) {
                        System.err.println("Usage: load <file> <csvfile>");
                        return;
                    }
                    IndexFile.load(fileName, args[2]);
                    break;
                case "print":
                    IndexFile.print(fileName);
                    break;
                case "extract":
                    if (args.length != 3) {
                        System.err.println("Usage: extract <file> <csvfile>");
                        return;
                    }
                    IndexFile.extract(fileName, args[2]);
                    break;
                default:
                    System.err.println("Unknown command: " + command);
            }
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
