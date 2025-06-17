public class Metrics {
    public static double calculateMSE(int[][] originalImage, int[][] reconstructedImage, int width, int height) {
        // to store squared differences between the corresponding pixels of the original
        // and reconstructed images.
        double sum = 0;
        // Loops through each pixel in the image. The outer loop iterates over the rows
        // (height),
        // and the inner loop iterates over the columns (width).
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // diff calculates the difference between the pixel values of the original and
                // reconstructed images.
                int diff = originalImage[i][j] - reconstructedImage[i][j];
                sum += diff * diff;
            }
        }
        return sum / (width * height);
    }

    public static double calculateCompressionRatio(int width, int height, int quantizationLevels) {
        // Calculates the original size of the image in bits
        int originalSize = width * height * 8; // Original size in bits
        // Calculates the number of bits required to represent each pixel in the
        // compressed image after quantization.
        int bitsPerPixel = (int) Math.ceil(Math.log(quantizationLevels) / Math.log(2));
        // Calculates the compressed size of the image in bits
        int compressedSize = width * height * bitsPerPixel; // Compressed size in bits
        // Returns the compression ratio
        return (double) originalSize / compressedSize;
    }
}
