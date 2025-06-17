// The Predictor class is responsible for predicting the value of a pixel based on a specific prediction method (i.e., Order-1, Order-2, or Adaptive prediction).
//  This class is critical for the encoding process in image compression (specifically DPCM),
//   where we try to predict the current pixel value using neighboring pixels, and then calculate the residual (the difference between the original and predicted values).

public class Predictor {
    public static int predict(int i, int j, int[][] reconstructedImage, int predictorType) {

        // Handle boundary cases
        // Handles boundary conditions when the current pixel is at the top or left
        // edges of the image.
        // For boundary pixels, we don't have both previous row and previous column
        // values, so we need special handling.

        if (i <= 0 || j <= 0) {
            // If both i and j are zero (i.e., the first pixel in the image),
            // return a default value of 128 (a reasonable assumption for grayscale images).

            if (i <= 0 && j <= 0)
                return 128; // Default value for first pixel

            // (i.e., the first row), the predicted value comes from the pixel directly to
            // the left (j - 1).
            if (i <= 0)
                return reconstructedImage[i][j - 1]; // First row
            // (i.e., the first column), the predicted value comes from the pixel directly
            // above (i - 1).
            if (j <= 0)
                return reconstructedImage[i - 1][j]; // First column
        }
        // to select which prediction method
        switch (predictorType) {
            // Predicts the current pixel value based on the pixel to the left
            case 1:
                return reconstructedImage[i][j - 1]; // ORDER_1
            case 2:
                return reconstructedImage[i][j - 1] + reconstructedImage[i - 1][j] - reconstructedImage[i - 1][j - 1]; // ORDER_2
            // the current pixel value is predicted based on the three neighboring pixels
            case 3:
                int A = reconstructedImage[i][j - 1]; // Left
                int B = reconstructedImage[i - 1][j - 1]; // Upper-left
                int C = reconstructedImage[i - 1][j]; // Upper

                if (B < Math.min(A, C)) {
                    return Math.max(A, C);
                } else if (B >= Math.max(A, C)) {
                    return Math.min(A, C);
                } else {
                    return A + C - B; // ADAPTIVE
                }
            default:
                return reconstructedImage[i][j - 1]; // Default to ORDER_1
        }
    }
}
