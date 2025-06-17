public class Quantizer {
    public static int quantize(int residual, int quantizationLevels) {
        // the total range of possible residual values. The range of residuals is from
        // -255 to +255, so the total range is 511 (i.e., 255 - (-255) = 511).
        int range = 511; // Total range of possible residual values

        // The step size is the difference between two adjacent quantization levels.
        // It's computed by dividing the total range (511) by the number of quantization
        // levels.
        int stepSize = range / quantizationLevels;

        // Shift residual to positive range (0 to 510)
        int shiftedResidual = residual + 255;

        // Quantize
        int quantizedValue = (shiftedResidual / stepSize) * stepSize;

        // Shift back to original range
        return quantizedValue - 255;
    }
}
