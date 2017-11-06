package org.bracelet.common.bp;

import org.bracelet.common.utils.ByteUtils;

import java.util.Arrays;

public class Main {
	public static void main(String[] args) {
		// 三层神经网络，每层神经元个数分别是3，5，8
		NeuronNet bpnn = new NeuronNet (new int [] {3, 5, 8});
		
		// 数据说明，求二进制X[i]的十进制表示Y[i]
		double[][] X = {
				{0,0,0},
				{0,0,1},
				{0,1,0},
				{0,1,1},
				{1,0,0},
				{1,0,1},
				{1,1,0}
		};
		double [][] Y = {
				{1, 0, 0, 0, 1, 0, 0, 0},
				{0, 1, 0, 0, 0, 1, 0, 0},
				{0, 0, 1, 0, 0, 0, 1, 0},
				{0, 0, 0, 1, 0, 0, 0, 1},
				{0, 0, 0, 1, 1, 0, 0, 0},
				{0, 0, 1, 0, 0, 1, 0, 0},
				{0, 1, 0, 0, 0, 0, 1, 0}
		};

		int [] values = { 0x88, 0x44, 0x22, 0x11, 0x18, 0x24, 0x42 };

		bpnn.train(X, Y);
		
		for (int i = 0; i < 8; ++ i) {
			double [] output = bpnn.predict(X[i]);
			double max = -1;
			int pos = -1;
			double value = 0.0;
			// 求最接近的神经元
			for (int j = 0; j < output.length; ++ j) {
				if (max < output[j]) {
					max = output[j];
					pos = j;
				}
				value += values[7-j] * output[j];
				output[j] = Math.round(output[j]);
			}
			System.out.print (X[i][0]);
			for (int j = 1; j < X[i].length; ++ j) {
				System.out.print (", " + X[i][j]);
			}
			System.out.println(" = " + value + " = " + Arrays.toString(output));
		}
	}
}
