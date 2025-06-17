import java.io.*;
import java.util.*;


public class LZ78Compression {
    // Compression
    public static void compress(String inputFilePath, String outputFilePath, String tagsFilePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
        StringBuilder inputText = new StringBuilder();
        int ch;
        while ((ch = reader.read()) != -1) {
            inputText.append((char) ch);
        }
        reader.close();

// dictionary -- index , substrings
        Map<String, Integer> dictionary = new HashMap<>();
        List<int[]> tags = new ArrayList<>();

        // Counter for the next available dictionary index.
        int dictIndex = 1;
        String prefix = "";


        // Generate Tags
        for (char c : inputText.toString().toCharArray()) {
            String combined = prefix + c;
            if (!dictionary.containsKey(combined)) {
                int index = dictionary.getOrDefault(prefix, 0);
               
                tags.add(new int[]{index, c});
                dictionary.put(combined, dictIndex++);
                prefix = "";
            } else {
                // If combined exists, update prefix to search for a longer match.
                prefix = combined;
            }
        }
        // Handle Remaining Prefix, If the loop ends but prefix has a value
        if (!prefix.isEmpty()) {
            int index = dictionary.getOrDefault(prefix, 0);
            // we must save the last part of the text
            tags.add(new int[]{index, 0});
        }

        writeBinaryFile(outputFilePath, tags);
        writeTagsFile(tagsFilePath, tags);
        
        System.out.println("Compression completed. Compressed file: " + outputFilePath);
        System.out.println("Tags saved in: " + tagsFilePath);
    }



//   LZ78 compressed tags as binary data
    private static void writeBinaryFile(String outputFilePath, List<int[]> tags) throws IOException {
//  Opens the binary file
        FileOutputStream fos = new FileOutputStream(outputFilePath);
        DataOutputStream dos = new DataOutputStream(fos);

        for (int[] tag : tags) {
            dos.writeInt(tag[0]);
            dos.writeChar(tag[1]);
        }

        dos.close();
        fos.close();
    }



    // LZ78 compressed tags -->tags.txt
    private static void writeTagsFile(String tagsFilePath, List<int[]> tags) throws IOException {

        FileWriter writer = new FileWriter(tagsFilePath);
        for (int[] tag : tags) {
            writer.write("<" + tag[0] + ", '" + (char) tag[1] + "'>\n");
        }
        writer.close();
    }





// Decompression
    public static void decompress(String inputFilePath, String outputFilePath) throws IOException {
        // Opens the file in binary mode.
        FileInputStream fis = new FileInputStream(inputFilePath); 
        // LZ78 tags are stored as (Index, Character).
        // DataInputStream allows reading int (Index) and char (Character) directly.
        DataInputStream dis = new DataInputStream(fis);

        List<String> dictionary = new ArrayList<>();
        dictionary.add("");

        StringBuilder decompressedText = new StringBuilder();


        // Keeps reading until all binary data is processed.
        while (dis.available() > 0) {
            // dictionary reference.
            int index = dis.readInt();
            // next character to append.
            char nextChar = dis.readChar();

// Concatenates nextChar if it's not '\0' (null character).
// If nextChar is 0 (null character), it appends an empty string.
            String entry = dictionary.get(index) + (nextChar != 0 ? nextChar : "");
            decompressedText.append(entry);
            dictionary.add(entry);
        }


        dis.close();
        fis.close();

        FileWriter writer = new FileWriter(outputFilePath);
        writer.write(decompressedText.toString());
        writer.close();

        System.out.println("Decompression completed. Decompressed file: " + outputFilePath);
    }

    


    // Main
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\nLZ78 Compression & Decompression");
            System.out.println("1. Compress a file");
            System.out.println("2. Decompress a file");
            System.out.println("3. Read binary file (View compressed.bin contents)");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  

            if (choice == 1) {
                System.out.print("Enter input text file path: ");
                String inputFile = scanner.nextLine();
                System.out.print("Enter output binary file path: ");
                String outputFile = scanner.nextLine();
                System.out.print("Enter tags file path : ");
                String tagsFile = scanner.nextLine();
                try {
                    compress(inputFile, outputFile, tagsFile);
                } catch (IOException e) {
                    System.out.println("Error during compression: " + e.getMessage());
                }


            } else if (choice == 2) {
                System.out.print("Enter input binary file path: ");
                String inputFile = scanner.nextLine();
                System.out.print("Enter output text file path: ");
                String outputFile = scanner.nextLine();
                try {
                    decompress(inputFile, outputFile);
                } catch (IOException e) {
                    System.out.println("Error during decompression: " + e.getMessage());
                }


            } else if (choice == 3) {
                System.out.println("Reading binary file: compressed.bin");
                ReadBinaryFile.readBinaryFile("compressed.bin");



            } else if (choice == 4) {
                System.out.println("Exiting program.");
                break;


            } else {
                System.out.println("Invalid choice! Please try again.");
            }
        }

        scanner.close();
    }
}

// Class 3ashan n  Read w n Print el Binary Data
class ReadBinaryFile {
    public static void readBinaryFile(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            int byteData;
            System.out.println("Binary Contents of " + filePath + ":");
            while ((byteData = fis.read()) != -1) {
                System.out.print(Integer.toBinaryString(byteData) + " ");
            }
            System.out.println("\nEnd of Binary File.");
        } catch (IOException e) {
            System.out.println("Error reading binary file: " + e.getMessage());
        }
    }
}