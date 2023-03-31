package Jpeg;

import Jama.Matrix;

public class Quality {

    public static double[][] convertIntToDouble(int[][] intArray) {
        double[][] doubleArray = new double[intArray.length][intArray[0].length];
        for (int i = 0; i < intArray.length; i++) {
            for (int j = 0; j < intArray[0].length; j++) {
                doubleArray[i][j] = intArray[i][j];
            }
        }
        return doubleArray;
    }

    public static double countMSE(double [][] original, double [][] modified) {
        double sum = 0;
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[0].length; j++) {
                sum += Math.pow((original[i][j] - modified[i][j]),2);
            }
        }
        return (1.0/(original.length * original[0].length)) * sum;
    }

    public static double countMAE(double [][] original, double [][] modified) {
        double sum = 0;
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[0].length; j++) {
                sum += Math.abs(original[i][j] - modified[i][j]);
            }
        }
        return  (1.0/(original.length * original[0].length)) * sum;
    }

    public static double countSAE(double [][] original, double [][] modified) {
        double sae = 0;
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[0].length; j++) {
                sae += Math.abs(original[i][j] - modified[i][j]);
            }
        }
        return sae;
    }

    public static double countPSNR(double MSE) {
        return 10 * Math.log10((Math.pow((Math.pow(2,8) - 1),2))/MSE);
    }

//    public static double countPSNRforRGB(double mseRed, double mseGreen, double mseBlue) {
//        return countPSNR((mseRed+mseGreen+mseBlue)/3.0);
//    }

    public static double countSSIM(Matrix original, Matrix modified) {

        double [] originalArray = original.getColumnPackedCopy();
        double [] modifiedArray = modified.getColumnPackedCopy();

        double c1 = Math.pow((0.01 * 255),2);
        double c2 = Math.pow((0.03 * 255),2);

        double sumMiX = 0, sumMiY = 0, sumSigmaX = 0, sumSigmaY = 0,sumSigmaXY = 0;
        for (int i = 0; i < originalArray.length; i++) {
            sumMiX += originalArray[i];
            sumMiY += modifiedArray[i];
        }
        double miX = (1.0/originalArray.length) * sumMiX;
        double miY = (1.0/originalArray.length) * sumMiY;

        for (int i = 0; i < originalArray.length; i++) {
            sumSigmaX += Math.pow((originalArray[i] - miX), 2);
            sumSigmaY += Math.pow((modifiedArray[i] - miY), 2);
            sumSigmaXY += (originalArray[i] - miX) * (modifiedArray[i] - miY);
        }
        double sigmaX = Math.pow((1.0/(originalArray.length - 1)) * sumSigmaX, (1.0/2.0));
        double sigmaY = Math.pow((1.0/(modifiedArray.length - 1)) * sumSigmaY, (1.0/2.0));
        double sigmaXY = (1.0/(originalArray.length)) * sumSigmaXY;
        
        return ((2*miX*miY+c1)*(2*sigmaXY+c2))/((Math.pow(miX,2) + Math.pow(miY,2) + c1)*(Math.pow(sigmaX,2) + Math.pow(sigmaY,2) + c2));
    }

    public static double countMSSIM(Matrix original, Matrix modified) {
        throw new RuntimeException("Not implemented yet.");
    }

}
