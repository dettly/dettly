package org.hlc.rpm;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RPMInstallUI f = new RPMInstallUI();
		Dimension dem = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int) dem.getWidth(), screenHeight = (int) dem
				.getHeight();
		int thisWidth = 950, thisHeight = 650;
		// ����X���ꣽ(��Ļ��ȣ�������)/2��Y������Ȼ��
		int thisX = (screenWidth - thisWidth) / 2;
		int thisY = (screenHeight - thisHeight) / 2;
		f.setBounds(thisX, thisY, thisWidth, thisHeight);
		// ���ⴰ�ڴ�С�̶�
		f.setResizable(false);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
	}

}
