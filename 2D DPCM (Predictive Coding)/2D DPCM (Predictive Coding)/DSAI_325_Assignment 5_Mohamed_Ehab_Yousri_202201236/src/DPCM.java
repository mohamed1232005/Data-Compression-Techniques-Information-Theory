public class DPCM {
    public static void main(String[] args) {
        // Path to the input image
        String imagePath = "C:\\Users\\user\\Downloads\\image.jpeg"; // Modify as per your image path

        // Define predictors and quantization levels
        int[] predictors = { 1, 2, 3 }; // 1: Order-1, 2: Order-2, 3: Adaptive
        int[] quantizationLevels = { 8, 16, 32 };

        System.out.println("Predictor Type | Quantization Levels | MSE | Compression Ratio");
        System.out.println("-------------- | ------------------- | --- | -----------------");

        for (int predictor : predictors) {
            for (int levels : quantizationLevels) {
                // Initialize the DPCM encoding/decoding
                Encoder encoder = new Encoder(imagePath, predictor, levels);
                encoder.encode();

                // Calculate metrics using the Metrics class
                double mse = Metrics.calculateMSE(encoder.getOriginalImage(), encoder.getReconstructedImage(),
                        encoder.getWidth(), encoder.getHeight());
                double compressionRatio = Metrics.calculateCompressionRatio(encoder.getWidth(), encoder.getHeight(),
                        levels);

                // Save reconstructed image
                String predictorName = switch (predictor) {
                    case 1 -> "Order1";
                    case 2 -> "Order2";
                    case 3 -> "Adaptive";
                    default -> "Unknown";
                };
                String outputPath = "reconstructed_images/reconstructed_" + predictorName + "_" + levels + ".png";
                encoder.saveReconstructedImage(outputPath);

                // Print results
                System.out.printf("%-14s | %-19d | %.2f | %.2f\n", predictorName, levels, mse, compressionRatio);
            }
        }
    }
}
