import java.awt.BorderLayout;
import java.awt.Cursor;
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
	private JCheckBox drawPoly;
	
	private Boolean isDrawing;
	private Boolean isEditing;
	private Boolean isDragging;
	private Boolean hasPolygon;
	
	private Point clicked;
	
	private int XOFF = -11;
	private int YOFF = -34;

	/**
	 * the window that holds it all together
	 * @param w width
	 * @param h height
	 */
	public CurveWindow(int w, int h)
	{
		
		isDrawing = true;
		isEditing = false;
		isDragging = false;
		hasPolygon = false;
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
		
		drawPoly = new JCheckBox("Draw Polygon");
		drawPoly.addActionListener(this);
		
		/*groups radio buttons together*/
		ButtonGroup group = new ButtonGroup();
	    group.add(addPoints);
	    group.add(removePoints);
	    group.add(editPoints);
	    
		buttonPanel.add(clearButton);
		buttonPanel.add(addPoints);
		buttonPanel.add(removePoints);
	    buttonPanel.add(editPoints);
	    buttonPanel.add(drawPoly);
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
	
	/**
	 * main method
	 * @param args
	 */
	public static void main(String[] args)
	{
		new CurveWindow(900,600);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {		
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e){
		//changes the curve if a point is being dragged
		if(isEditing && clicked != null){
			drawer.clear();
			int prevX = clicked.x;
			clicked.x = e.getX()+XOFF;
			clicked.y = e.getY()+YOFF;
			int newX = clicked.x;
			drawer.drawAllPoints();
			drawer.drawCurve();
			if(hasPolygon){
				drawer.drawPolygon();
			}
			repaint();
		}
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		//draw a new point
		if(isDrawing){
			drawer.drawPoint(e.getX()+XOFF, e.getY()+YOFF);
			if(hasPolygon){
				drawer.drawPolygon();
			}
			repaint();
		}
		//the point to be dragged
		else if(!isDrawing && isEditing){
			clicked = drawer.checkPoint(e.getX()+XOFF, e.getY()+YOFF);
		}
		//remove a point
		else{
			clicked = drawer.checkPoint(e.getX()+XOFF, e.getY()+YOFF);
			drawer.removePoint(clicked);
			if(hasPolygon){
				drawer.drawPolygon();
			}
			repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * handles all actions
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==clearButton){
			drawer.reset();
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
		else if(e.getSource()==drawPoly){
			if(hasPolygon == true){
				hasPolygon = false;
				drawer.erasePolygon();
				repaint();
			}
			else{
				hasPolygon = true;
				drawer.drawPolygon();
				repaint();
			}
		}


	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(drawer.checkPoint(e.getX()+XOFF, e.getY()+YOFF) != null){
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
		else{
			setCursor(Cursor.getDefaultCursor());
		}
	}
}


