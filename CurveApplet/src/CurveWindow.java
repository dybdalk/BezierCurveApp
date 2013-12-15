import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.*;

public class CurveWindow extends JPanel implements MouseListener, MouseMotionListener, ActionListener{

	private CurveDrawer drawer;
	
	private JButton clearButton;
	private JRadioButton addPoints;
	private JRadioButton removePoints;
	private JRadioButton editPoints;
	
	private Boolean isDrawing;
	private Boolean isEditing;
	private Boolean isDragging;
	
	private Point clicked;
	
	private int XOFF = -11;
	private int YOFF = -34;


	public CurveWindow(int w, int h)
	{
		
		isDrawing = true;
		isEditing = false;
		isDragging = false;
		this.setPreferredSize(new Dimension(w,h));
		
		JPanel buttonPanel = new JPanel();
		clearButton = new JButton("Clear");
		clearButton.addActionListener(this);
		
		addPoints = new JRadioButton("Add new Point");
		addPoints.addActionListener(this);
		addPoints.setSelected(true);
		
		removePoints = new JRadioButton("Remove point");
		removePoints.addActionListener(this);
		
		editPoints = new JRadioButton("Edit point");
		editPoints.addActionListener(this);
		
		
		ButtonGroup group = new ButtonGroup();
	    group.add(addPoints);
	    group.add(removePoints);
	    group.add(editPoints);
	    
		buttonPanel.add(clearButton);
		buttonPanel.add(addPoints);
		buttonPanel.add(removePoints);
	    buttonPanel.add(editPoints);
		buttonPanel.setVisible(true);
		drawer = new CurveDrawer(w,h);
		JFrame frame = new JFrame("Curve Applet");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		frame.pack();
		frame.setVisible(true);
		frame.addMouseListener(this);
		frame.addMouseMotionListener(this);

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
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseDragged(MouseEvent e){
		if(isEditing && clicked != null){
			drawer.eraseCurve();
			drawer.erasePoint(clicked);
			int prevX = clicked.x;
			clicked.x = e.getX()+XOFF;
			clicked.y = e.getY()+YOFF;
			int newX = clicked.x;
			System.out.println("old x: " + prevX + ", new x: " + newX);
			drawer.drawPoint(clicked);
			drawer.drawCurve();
			repaint();
		}
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if(isDrawing){
			drawer.drawPoint(e.getX()+XOFF, e.getY()+YOFF);
			repaint();
		}
		else if(!isDrawing && isEditing){
			clicked = drawer.clickedPoint(e.getX()+XOFF, e.getY()+YOFF);
			if(clicked != null){
				isDragging = true;
				//System.out.println("dragging");
			}
		}
		else{
			Point clicked = drawer.clickedPoint(e.getX()+XOFF, e.getY()+YOFF);
			drawer.removePoint(clicked);
			repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		isDragging = false;
		//System.out.println("no drag");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==clearButton){
			drawer.clear();
			repaint();
		}
		else if(e.getSource()==removePoints){
			isDrawing = false;
			isEditing = false;
			
		}
		else if(e.getSource() == addPoints){
			isDrawing = true;
			isEditing = false;
		}
		else if(e.getSource()==editPoints){
			isDrawing = false;
			isEditing = true;
			
		}
		
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		}
}


