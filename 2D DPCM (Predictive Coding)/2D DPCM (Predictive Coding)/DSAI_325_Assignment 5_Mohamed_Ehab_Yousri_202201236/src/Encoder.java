import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Encoder {
    private int[][] originalImage;
    private int[][] reconstructedImage;
    private int width;
    private int height;
    private int predictorType;
    private int quantizationLevels;

    public Encoder(String imagePath, int predictorType, int quantizationLevels) {
        try {
            // Read the image
            BufferedImage image = ImageIO.read(new File(imagePath));
            width = image.getWidth();
            height = image.getHeight();

            // Convert to grayscale and store as 2D array
            originalImage = new int[height][width];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int rgb = image.getRGB(j, i);
                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = rgb & 0xFF;
                    // Convert to grayscale using luminance formula
                    originalImage[i][j] = (int) (0.299 * r + 0.587 * g + 0.114 * b);
                }
            }

            this.predictorType = predictorType;
            this.quantizationLevels = quantizationLevels;

            // Initialize arrays
            reconstructedImage = new int[height][width];
        } catch (IOException e) {
            System.err.println("Error reading image: " + e.getMessage());
        }
    }

    public void encode() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // Predict current pixel
                int predicted = Predictor.predict(i, j, reconstructedImage, predictorType);

                // Calculate residual
                int residual = originalImage[i][j] - predicted;

                // Quantize residual
                int quantizedResidual = Quantizer.quantize(residual, quantizationLevels);

                // Update reconstructed image (for next predictions)
                reconstructedImage[i][j] = Math.min(255, Math.max(0, predicted + quantizedResidual));
            }
        }
    }

    // Save the reconstructed image to the specified output path
    public void saveReconstructedImage(String outputPath) {
        try {
            // Ensure the directory exists, create it if it doesn't
            File outputDir = new File(outputPath).getParentFile();
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            // Create a new BufferedImage to save
            BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            // Convert the 2D array to BufferedImage
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int gray = reconstructedImage[i][j];
                    int rgb = (gray << 16) | (gray << 8) | gray; // Convert grayscale to RGB
                    output.setRGB(j, i, rgb); // Set pixel value
                }
            }

            // Save the image as PNG in the specified path
            ImageIO.write(output, "png", new File(outputPath));
            System.out.println("Reconstructed image saved to: " + outputPath);
        } catch (IOException e) {
            System.err.println("Error saving reconstructed image: " + e.getMessage());
        }
    }

    // Getter methods to provide data to the Metrics class for calculation
    public int[][] getOriginalImage() {
        return originalImage;
    }

    public int[][] getReconstructedImage() {
        return reconstructedImage;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
