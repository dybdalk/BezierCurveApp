import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class CurveWindow extends JPanel implements MouseListener{

	private CurveDrawer drawer;
	
	private int XOFF = -11;
	private int YOFF = -34;


	public CurveWindow(int w, int h)
	{
		this.setPreferredSize(new Dimension(w,h));

		drawer = new CurveDrawer(w,h);
		JFrame frame = new JFrame("Curve Applet");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.pack();
		frame.setVisible(true);
		frame.addMouseListener(this);

		drawer.drawBackground();
		repaint();
	}

	public void paintComponent(Graphics g)
	{
		g.drawImage(drawer.getImage(), 0, 0, null);
	}
	

	public static void main(String[] args)
	{
		new CurveWindow(900,600);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
//		System.out.println("click");
		drawer.drawPoint(e.getX()+XOFF, e.getY()+YOFF);
		repaint();
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}


