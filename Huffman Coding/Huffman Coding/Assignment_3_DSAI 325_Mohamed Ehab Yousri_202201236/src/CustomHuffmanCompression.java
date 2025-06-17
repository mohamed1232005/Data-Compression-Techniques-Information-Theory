import java.io.*;
import java.util.*;

public class CustomHuffmanCompression {



    // Calculate the Shannon entropy of the text based on symbol probabilities
    public static double computeEntropy(Map<Character, Integer> freqMap, int totalSymbols) {

        double entropy = 0.0;

        for (var entry : freqMap.entrySet()) {

            double probability = (double) entry.getValue() / totalSymbols;

            entropy -= probability * (Math.log(probability) / Math.log(2));
        }
        return entropy;
    }




    // Calculate the average code length for the Huffman codes
    public static double calculateAvgCodeLength(Map<Character, String> codeMap, Map<Character, Integer> freqMap, int totalSymbols) {
        double avgLength = 0.0;

        for (var entry : codeMap.entrySet()) {

            char symbol = entry.getKey();

            double prob = (double) freqMap.get(symbol) / totalSymbols;

            avgLength += prob * entry.getValue().length();
        }
        return avgLength;
    }




    static class TreeNode implements Comparable<TreeNode> {
        char symbol;
        int count;
        TreeNode left, right;


        public TreeNode(int count) {
            this.count = count;
        }

        @Override
        public int compareTo(TreeNode other) {
            return Integer.compare(this.count, other.count);
        }
    }



    // Build the tree structure based on symbol frequencies
    public static TreeNode constructHuffmanTree(Map<Character, Integer> freqMap) {
        PriorityQueue<TreeNode> pq = new PriorityQueue<>();
        for (var entry : freqMap.entrySet()) {
            TreeNode node = new TreeNode(entry.getValue());
            node.symbol = entry.getKey();
            pq.add(node);
        }



        while (pq.size() > 1) {
            TreeNode left = pq.poll();
            TreeNode right = pq.poll();
            TreeNode parent = new TreeNode(left.count + right.count);
            parent.left = left;
            parent.right = right;
            pq.add(parent);
        }


        return pq.poll();
    }



    // Generate the Huffman codes for each symbol
    public static void generateCodes(TreeNode root, String code, Map<Character, String> codeMap) {

        if (root == null) return;
        if (root.left == null && root.right == null) {
            codeMap.put(root.symbol, code);

        }
        generateCodes(root.left, code + "0", codeMap);
        generateCodes(root.right, code + "1", codeMap);
    }




    // Encode the input string using the generated Huffman codes
    public static String encodeString(String inputText, Map<Character, String> codeMap) {

        StringBuilder encodedStr = new StringBuilder();
        for (char symbol : inputText.toCharArray()) {
            encodedStr.append(codeMap.get(symbol));

        }
        return encodedStr.toString();
    }




    // Write the Huffman codes to a file
    public static void saveCodesToFile(TreeNode root, String fileName) throws IOException {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

            writeCodesRecursively(root, "", writer);
        }
    }



    // Helper method to recursively save the codes in the Huffman tree
    private static void writeCodesRecursively(TreeNode root, String code, BufferedWriter writer) throws IOException {
        if (root == null) return;
        


        // If it's a leaf node (contains a symbol), write it to the file
        if (root.left == null && root.right == null) {
            writer.write(root.symbol + ":" + code);
            writer.newLine();
        }


        
        // Continue traversing left and right children
        writeCodesRecursively(root.left, code + "0", writer);
        writeCodesRecursively(root.right, code + "1", writer);
    }



    // Save the encoded binary data to a file
    public static void saveEncodedToFile(String encodedText, String fileName) throws IOException {
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileName))) {
            int bit;
            for (int i = 0; i < encodedText.length(); i++) {
                bit = encodedText.charAt(i) == '0' ? 0 : 1;
                outputStream.write(bit);
            }
        }
    }



    // Read the binary file and convert it to a string of '0' and '1'
    public static String readBinaryData(String binaryFile) throws IOException {
        StringBuilder binaryData = new StringBuilder();
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(binaryFile))) {
            int bit;
            while ((bit = inputStream.read()) != -1) {
                binaryData.append(bit == 0 ? '0' : '1');
            }
        }
        return binaryData.toString();
    }



    // Rebuild the Huffman tree from the stored Huffman codes
    public static TreeNode rebuildTree(Map<Character, String> codeMap) {
        TreeNode root = new TreeNode(0);
        for (var entry : codeMap.entrySet()) {
            TreeNode current = root;
            for (char bit : entry.getValue().toCharArray()) {
                if (bit == '0') {
                    if (current.left == null) current.left = new TreeNode(0);
                    current = current.left;
                } else {
                    if (current.right == null) current.right = new TreeNode(0);
                    current = current.right;
                }
            }
            current.symbol = entry.getKey();
        }
        return root;
    }




    // Decode the binary string using the Huffman tree
    public static String decodeBinaryString(TreeNode root, String encodedData) {
        StringBuilder decodedStr = new StringBuilder();
        TreeNode current = root;
        for (char bit : encodedData.toCharArray()) {
            current = (bit == '0') ? current.left : current.right;
            if (current.left == null && current.right == null) {
                decodedStr.append(current.symbol);
                current = root;
            }
        }
        return decodedStr.toString();
    }




    // Main method to execute encoding or decoding
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Encode Text");
            System.out.println("2. Decode Text");
            System.out.println("3. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline character

            if (choice == 1) {
                System.out.print("Enter the text file to encode: ");
                String inputFile = scanner.nextLine();
                StringBuilder inputText = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        inputText.append(line);
                    }
                }




                // Count symbol frequencies
                Map<Character, Integer> symbolFreqMap = new HashMap<>();
                for (char c : inputText.toString().toCharArray()) {
                    symbolFreqMap.put(c, symbolFreqMap.getOrDefault(c, 0) + 1);
                }

                int totalSymbols = inputText.length();
                double entropy = computeEntropy(symbolFreqMap, totalSymbols);
                System.out.println("Entropy of the text: " + entropy);

                // Build Huffman tree
                TreeNode root = constructHuffmanTree(symbolFreqMap);

                // Generate Huffman codes
                Map<Character, String> huffmanCodeMap = new HashMap<>();
                generateCodes(root, "", huffmanCodeMap);

                // Calculate average code length
                double avgCodeLength = calculateAvgCodeLength(huffmanCodeMap, symbolFreqMap, totalSymbols);
                System.out.println("Average code length: " + avgCodeLength + " bits/symbol");

                // Save Huffman codes
                saveCodesToFile(root, "Huffman_Codes.txt");

                // Encode the text
                String encodedText = encodeString(inputText.toString(), huffmanCodeMap);

                // Save the encoded binary text
                saveEncodedToFile(encodedText, "encoded.bin");




                System.out.println("Text encoded and saved to encoded.bin.");
            } else if (choice == 2) {
                System.out.print("Enter the binary file to decode: ");
                String binaryFile = scanner.nextLine();
                String binaryString = readBinaryData(binaryFile);

                // Read Huffman codes from file
                Map<Character, String> huffmanCodesFromFile = new HashMap<>();
                try (BufferedReader reader = new BufferedReader(new FileReader("Huffman_Codes.txt"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(":");
                        huffmanCodesFromFile.put(parts[0].charAt(0), parts[1]);
                    }
                }



                
                // Reconstruct Huffman tree
                TreeNode root = rebuildTree(huffmanCodesFromFile);

                // Decode the binary string
                String decodedText = decodeBinaryString(root, binaryString);

                // Save decoded text
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("decoded.txt"))) {
                    writer.write(decodedText);
                }

                System.out.println("Decoded text saved to decoded.txt.");
            } else if (choice == 3) {
                System.out.println("Exiting the program.");
                break;
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }
        scanner.close();
    }
}
