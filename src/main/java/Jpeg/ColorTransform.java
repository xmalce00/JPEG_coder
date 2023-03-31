package Jpeg;

import Jama.Matrix;

public class ColorTransform {

    public static Matrix[] convertOriginalRGBtoYcBcR(int[][]red, int[][]green, int[][]blue) {
        int height = red.length;
        int width = red[0].length;
        System.out.println("Size " + height + ',' + width);
        System.out.println("Convert to YcRcB");

        Matrix originalY = new Matrix(height, width);
        Matrix originalCb = new Matrix(height, width);
        Matrix originalCr = new Matrix(height, width);

        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                originalY.set(i,j,(0.257*red[i][j] + 0.504*green[i][j] + 0.098*blue[i][j] + 16));
                originalCb.set(i,j,(-0.148*red[i][j] - 0.291*green[i][j] + 0.439*blue[i][j] + 128));
                originalCr.set(i,j,(0.439*red[i][j] - 0.368*green[i][j] - 0.071*blue[i][j] + 128));
            }
        }
        return new Matrix[]{originalY, originalCb, originalCr};
    }

    public static Object[] convertModifiedYcBcRtoRGB(Matrix Y, Matrix Cr, Matrix Cb) {

        int height = Y.getRowDimension();
        int width = Y.getColumnDimension();

        System.out.println("Size " + height + ',' + width);
        System.out.println("Convert to RGB");

        int[][] modifiedRed = new int[height][width];
        int[][] modifiedGreen = new int[height][width];
        int[][] modifiedBlue = new int[height][width];

        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                int red = (int) Math.round(1.164*(Y.get(i,j) - 16) + 1.596*(Cr.get(i,j) - 128));
                modifiedRed[i][j] = ((red > 255) || (red < 0) ? (red>255?255:0) : red);

                int green = (int) Math.round(1.164*(Y.get(i,j) - 16) - 0.813*(Cr.get(i,j) - 128) - 0.391*(Cb.get(i,j) - 128));
                modifiedGreen[i][j] = ((green > 255) || (green < 0) ? (green>255?255:0) : green);

                int blue = (int) Math.round(1.164*(Y.get(i,j) - 16) + 2.018*(Cb.get(i,j) - 128));
                modifiedBlue[i][j] = ((blue > 255) || (blue < 0) ? (blue>255?255:0) : blue);
            }
        }
        return new Object[]{modifiedRed, modifiedGreen, modifiedBlue};
    }
}
