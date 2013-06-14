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
		// 窗体X坐标＝(屏幕宽度－窗体宽度)/2，Y坐标亦然。
		int thisX = (screenWidth - thisWidth) / 2;
		int thisY = (screenHeight - thisHeight) / 2;
		f.setBounds(thisX, thisY, thisWidth, thisHeight);
		// 主题窗口大小固定
		f.setResizable(false);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
	}

}
