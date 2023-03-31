package Jpeg;

import Enums.SamplingTypes;
import Jama.Matrix;

public class Quantization {

    private static final double[][] quantizationMatrix8Y = {
            {16, 11, 10, 16, 24, 40, 51, 61},
            {12, 12, 14, 19, 26, 58, 60, 55},
            {14, 13, 16, 24, 40, 57, 69, 56},
            {14, 17, 22, 29, 51, 87, 80, 62},
            {18, 22, 37, 56, 68, 109, 103, 77},
            {24, 35, 55, 64, 81, 104, 113, 92},
            {49, 64, 78, 87, 103, 121, 120, 101},
            {72, 92, 95, 98, 112, 100, 103, 99}
    };

    private static final double[][] quantizationMatrix8C = {
            {17, 18, 24, 47, 99, 99, 99, 99},
            {18, 21, 26, 66, 99, 99, 99, 99},
            {24, 26, 56, 99, 99, 99, 99, 99},
            {47, 66, 99, 99, 99, 99, 99, 99},
            {99, 99, 99, 99, 99, 99, 99, 99},
            {99, 99, 99, 99, 99, 99, 99, 99},
            {99, 99, 99, 99, 99, 99, 99, 99},
            {99, 99, 99, 99, 99, 99, 99, 99}
    };

    public static Matrix quantize(Matrix input, int blockSize, double quality, boolean matrixY) {

        Matrix output = new Matrix(input.getRowDimension(), input.getColumnDimension());

        Matrix quantizeMatrix = getQuantizationMatrix(blockSize, quality, matrixY);

        for (int i = 0; i < input.getRowDimension(); i += blockSize) {
            for (int j = 0; j < input.getColumnDimension(); j += blockSize) {
                Matrix subMatrix = input.getMatrix(i, i+blockSize-1, j, j+blockSize-1);

                for (int k = 0; k < subMatrix.getRowDimension(); k++) {
                    for (int h = 0; h < subMatrix.getColumnDimension(); h++) {
                        if(subMatrix.get(k,h) < 0) {
                            subMatrix.set(k, h, Math.ceil(subMatrix.get(k,h)/(quantizeMatrix.get(k,h))));
                        } else {
                            subMatrix.set(k, h, Math.floor(subMatrix.get(k,h)/(quantizeMatrix.get(k,h))));
                        }
                    }
                }

                output.setMatrix(i, i+blockSize-1, j, j+blockSize-1, subMatrix);
            }
        }
        return output;
    }

    public static Matrix inverseQuantize(Matrix input, int blockSize, double quality, boolean matrixY) {
        Matrix output = new Matrix(input.getRowDimension(), input.getColumnDimension());
        Matrix quantizeMatrix = getQuantizationMatrix(blockSize, quality, matrixY);
        for (int i = 0; i < input.getRowDimension(); i += blockSize) {
            for (int j = 0; j < input.getColumnDimension(); j += blockSize) {
                Matrix subMatrix = input.getMatrix(i, i+blockSize-1, j, j+blockSize-1);

                for (int k = 0; k < subMatrix.getRowDimension(); k++) {
                    for (int h = 0; h < subMatrix.getColumnDimension(); h++) {
                        subMatrix.set(k, h, subMatrix.get(k,h)*quantizeMatrix.get(k,h));
                    }
                }
                output.setMatrix(i, i+blockSize-1, j, j+blockSize-1, subMatrix);
            }
        }
        return output;
    }

    public static Matrix getQuantizationMatrix(int blockSize, double quality, boolean matrixY) {
        double alpha;
        if (1 < quality && quality < 50) {
            alpha = 50/quality;
        } else if (50 <= quality && quality <= 99) {
            alpha = 2 - ((2 * quality) / 100);
        } else if (quality == 100) {
            return new Matrix(blockSize, blockSize, 1);
        } else {
            throw new IllegalArgumentException();
        }
        Matrix quantizationMatrix;
        if(matrixY) {
            quantizationMatrix = new Matrix(quantizationMatrix8Y);
        } else {
            quantizationMatrix = new Matrix(quantizationMatrix8C);
        }

        if(blockSize < 8) {
            for(int i = 0; i < 4/blockSize; i++) {
                quantizationMatrix = Sampling.sampleDown(quantizationMatrix, SamplingTypes.S_4_2_0);
            }
        } else {
            for (int i = 0; i < blockSize/8; i++) {
                quantizationMatrix = Sampling.sampleUp(quantizationMatrix, SamplingTypes.S_4_2_0);
            }
        }

        for (int i = 0; i < quantizationMatrix.getColumnDimension(); i++) {
            for (int j = 0; j < quantizationMatrix.getRowDimension(); j++) {
                quantizationMatrix.set(i, j, alpha * quantizationMatrix.get(i,j));
            }
        }
        return quantizationMatrix;
    }
}