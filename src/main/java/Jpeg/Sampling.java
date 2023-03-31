package Jpeg;

import Enums.SamplingTypes;
import Jama.Matrix;

public class Sampling {

    public static Matrix sampleDown(Matrix inputMatrix, SamplingTypes samplingType) {
        Matrix sampledMatrix = null;
        switch (samplingType) {
            case S_4_4_4:
                sampledMatrix = inputMatrix;
                break;
            case S_4_2_2:
                sampledMatrix = Sampling.sampleDown(inputMatrix);
                break;
            case S_4_2_0:
                sampledMatrix = Sampling.sampleDown(Sampling.sampleDown(inputMatrix).transpose()).transpose();
                break;
            case S_4_1_1:
                sampledMatrix = Sampling.sampleDown(Sampling.sampleDown(inputMatrix));
                break;
            default: System.out.println("Worng argument downSample");
                break;
        }
        return sampledMatrix;

    }

    public static Matrix sampleUp(Matrix inputMatrix, SamplingTypes samplingType) {
        Matrix sampledMatrix = null;
        switch (samplingType) {
            case S_4_4_4:
                sampledMatrix = inputMatrix;
                break;
            case S_4_2_2:
                sampledMatrix = Sampling.sampleUp(inputMatrix);
                break;
            case S_4_2_0:
                sampledMatrix = Sampling.sampleUp(Sampling.sampleUp(inputMatrix).transpose()).transpose();
                break;
            case S_4_1_1:
                sampledMatrix = Sampling.sampleUp(Sampling.sampleUp(inputMatrix));
                break;
            default: System.out.println("Worng argument upSample");
                break;
        }
        return sampledMatrix;

    }

    private static Matrix sampleDown(Matrix mat) {
        int newMatrixDim_w = (int) Math.ceil(mat.getColumnDimension()/2);
        //int newMatrixDim_w = cR.getColumnDimension();
        //int newMatrixDim_h = (int) Math.ceil(cR.getRowDimension()/2/2);
        int newMatrixDim_h = mat.getRowDimension();

        int[] indexesW = new int[newMatrixDim_w];
        int[] indexesH = new int[newMatrixDim_h];

        for (int i = 0; i < newMatrixDim_w; i++) {
            indexesW[i] = i*2;
        }
        for (int i = 0; i < newMatrixDim_h; i++) {
            indexesH[i] = i;
        }
        return mat.getMatrix(indexesH,indexesW);
    }

    private static Matrix sampleUp(Matrix inputMatrix) {
        Matrix upSample = new Matrix(inputMatrix.getRowDimension(), inputMatrix.getColumnDimension()*2);
        for (int i = 0; i < inputMatrix.getRowDimension(); i++) {
            for (int j = 0; j < inputMatrix.getColumnDimension(); j++) {
                for (int h = 0; h < 2; h++) {
                    upSample.set(i, j+j+h, inputMatrix.get(i,j));
                }
            }
        }
        return upSample;
    }

}