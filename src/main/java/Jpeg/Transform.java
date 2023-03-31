package Jpeg;

import Enums.TransformTypes;
import Jama.Matrix;

public class Transform {

    public static Matrix transform(Matrix input, TransformTypes transformType, int blockSize) {

        Matrix transformMatrix;

        if(transformType == TransformTypes.DCT) {
            transformMatrix = getTransformMatrix(TransformTypes.DCT, blockSize);
        } else if (transformType == TransformTypes.WHT) {
            transformMatrix = getTransformMatrix(TransformTypes.WHT, blockSize);
        } else {
            throw new IllegalStateException("Unexpected value: " + transformType);
        }

        Matrix output = new Matrix(input.getRowDimension(), input.getColumnDimension());
        for (int i = 0; i < input.getRowDimension(); i += blockSize) {
            for (int j = 0; j < input.getColumnDimension(); j += blockSize) {
                Matrix subMatrix = input.getMatrix(i, i+blockSize-1, j, j+blockSize-1);
                output.setMatrix(i, i+blockSize-1, j, j+blockSize-1, transformMatrix.times(subMatrix).times(transformMatrix.transpose()));
            }
        }
        return output;
    }

    public static Matrix inverseTransform(Matrix input, TransformTypes transformType, int blockSize) {
        Matrix transformMatrix;
        if(transformType == TransformTypes.DCT) {
            transformMatrix = getTransformMatrix(TransformTypes.DCT, blockSize);
        } else if (transformType == TransformTypes.WHT) {
            transformMatrix = getTransformMatrix(TransformTypes.WHT, blockSize);
        } else {
            throw new IllegalStateException("Unexpected value: " + transformType);
        }

        Matrix output = new Matrix(input.getRowDimension(), input.getColumnDimension());
        for (int i = 0; i < input.getRowDimension(); i += blockSize) {
            for (int j = 0; j < input.getColumnDimension(); j += blockSize) {
                Matrix subMatrix = input.getMatrix(i, i+blockSize-1, j, j+blockSize-1);
                output.setMatrix(i, i+blockSize-1, j, j+blockSize-1, transformMatrix.transpose().times(subMatrix.times(transformMatrix)));
            }
        }
        return output;
    }

    public static Matrix getTransformMatrix(TransformTypes transformType, int blockSize) {

        if(transformType == TransformTypes.DCT) {
            Matrix dctMatrix = new Matrix(blockSize, blockSize);
            for (int j = 0; j < blockSize; j++) {
                dctMatrix.set(0,j,Math.sqrt(1.0/blockSize));
            }
            for (int i = 1; i < blockSize; i++) {
                for (int j = 0; j < blockSize; j++) {
                    dctMatrix.set(i, j, Math.sqrt(2.0/blockSize) * Math.cos(((2*j + 1) * i * Math.PI)/(2*blockSize)));
                }
            }
            return dctMatrix;

        } else if (transformType == TransformTypes.WHT) {
            Matrix base = new Matrix(2,2);
            double x = 1;
            base.set(0,0,x);
            base.set(0,1,x);
            base.set(1,0,x);
            base.set(1,1,-x);
            Matrix old;
            Matrix wht = base;
            for (int i = 2; i < blockSize+1; i = i * 2) {
                old = wht;
                wht = new Matrix(i,i);
                wht.setMatrix(0,i/2-1, 0, i/2-1, old);
                wht.setMatrix(0, i/2-1, i/2,i-1, old);
                wht.setMatrix(i/2, i-1, 0,i/2-1, old);
                wht.setMatrix(i/2, i-1, i/2,i-1, old.times(-1.0));
            }
            return wht.times(1.0/Math.sqrt(blockSize));
        } else {
            throw new IllegalStateException("Unexpected value: " + transformType);
        }
    }
}