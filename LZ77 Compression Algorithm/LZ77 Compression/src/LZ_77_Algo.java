import java.io.*;
import java.util.*;






public class LZ_77_Algo {
    // Compress 
    public static void compress(String inputFile, String outputFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        // as a single line
        String text = reader.readLine();
        int searchBufferSize = 10;
        // hena bn5azen el triplet "tag"
        List<String> tags = new ArrayList<>();
        int i = 0;

        while (i < text.length()) {
            int matchDistance = 0;
            int matchLength = 0;
            String nextChar = "";
            // Limit search range based on buffer size
            int start = Math.max(0, i - searchBufferSize);
           
            // longest match
            for (int j = start; j < i; j++) {
                int length = 0;
                while (i + length < text.length() && text.charAt(j + length) == text.charAt(i + length)) {
                    length++;
                }
                // bn update lw l2a2ena longer match
                if (length > matchLength) {
                    matchDistance = i - j;
                    matchLength = length;
                }
            }
            // de Special case: bntaked en el  first character is stored separately
            if (i == 0) {
                nextChar = String.valueOf(text.charAt(i));
                tags.add("(0,0," + nextChar + ")");
                i++;
                continue;
            }
            if (matchLength > 0) {
                while (i + matchLength < text.length() && text.charAt(i + matchLength) == text.charAt(i)) {
                    matchLength++;
                }
            }
            // bnshof el next character el handefoh ba3d el match
            
            
            // bn5azen el compression tag 
            nextChar = (i + matchLength < text.length()) ? String.valueOf(text.charAt(i + matchLength)) : "null";
            tags.add("(" + matchDistance + "," + matchLength + "," + nextChar + ")");
            i += matchLength + 1;
        }
        for (String tag : tags) {
            writer.write(tag + " ");
        }



        reader.close();
        writer.close();
        System.out.println("Compression completed! Output saved in " + outputFile);
    }





    // Decompress 
    public static void decompress(String inputFile, String outputFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        String compressedText = reader.readLine();
        // bn Split el tags by space
        String[] tags = compressedText.split(" ");
        StringBuilder output = new StringBuilder();

        // bnloop 3ala kol compression tag we bn reconstruct el original text
        for (String tag : tags) {

            // bn7zef el parentheses men el tag
            tag = tag.replaceAll("[()]", ""); 

            // bnsplit tag ll triplet
            String[] parts = tag.split(",");
            int distance = Integer.parseInt(parts[0]);
            int length = Integer.parseInt(parts[1]);

            // bn handel el null case
            String nextChar = parts[2].equals("null") ? "" : parts[2];

            int start = output.length() - distance;

            // bn Reconstruct el repeated sequence
            for (int j = 0; j < length; j++) {
                output.append(output.charAt(start + j));
            }

            output.append(nextChar);
        }




        
        writer.write(output.toString());
        reader.close();
        writer.close();
        System.out.println("Decompression completed! Output saved in " + outputFile);
    }









    // Main 
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            // El options
            System.out.println("Choose an option:");
            System.out.println("1. Compress a file");
            System.out.println("2. Decompress a file");
            System.out.println("3. Exit");
            // bena5od el user choice
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            if (choice == 1) {
                System.out.print("Enter input file path: ");
                String inputFile = scanner.nextLine();
                System.out.print("Enter output file path: ");
                String outputFile = scanner.nextLine();
                compress(inputFile, outputFile);


            } else if (choice == 2) {
                System.out.print("Enter compressed file path: ");
                String inputFile = scanner.nextLine();
                System.out.print("Enter output file path: ");
                String outputFile = scanner.nextLine();
                decompress(inputFile, outputFile);


            } else if (choice == 3) {
                System.out.println("Exiting...");
                break;

                
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
        scanner.close();
    }
}
