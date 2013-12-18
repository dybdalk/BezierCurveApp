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
	private JRadioButton movePoints;
	private JCheckBox drawPoly;
	
	private JRadioButton bezierButton;
	private JRadioButton bSplineButton;

	private Boolean isDrawing;
	private Boolean isEditing;
	private Boolean isDragging;
	private Boolean isBezier;
	private Boolean hasPolygon;

	private boolean bezier;
	private boolean bSpline;
	private boolean nurbs;
	
	private Point clicked;

	private int width;
	private int height;
	
	private int XOFF = -11;
	private int YOFF = -34;

	/**
	 * the window that holds it all together
	 * @param w width
	 * @param h height
	 */
	public CurveWindow(int w, int h)
	{
		height = h;
		width = w;

		isDrawing = true;
		isBezier = true;
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

		movePoints = new JRadioButton("Move point");
		movePoints.addActionListener(this);

		drawPoly = new JCheckBox("Draw Polygon");
		drawPoly.addActionListener(this);
		
		bezierButton = new JRadioButton("Bezier Curve");
		bezierButton.addActionListener(this);
		bezierButton.setSelected(true);

		bSplineButton = new JRadioButton("B-Spline");
		bSplineButton.addActionListener(this);

		/*groups editing radio buttons together*/
		ButtonGroup editGroup = new ButtonGroup();
		editGroup.add(addPoints);
		editGroup.add(removePoints);
		editGroup.add(movePoints);
		
		ButtonGroup modeGroup = new ButtonGroup();
		modeGroup.add(bezierButton);
		modeGroup.add(bSplineButton);
		
		

		buttonPanel.add(clearButton);
		buttonPanel.add(addPoints);
		buttonPanel.add(removePoints);
		buttonPanel.add(movePoints);
		buttonPanel.add(drawPoly);
		buttonPanel.add(bezierButton);
		buttonPanel.add(bSplineButton);
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
			drawer.drawControlPoints();
			drawer.drawSplinePoints();
			drawer.drawCurve(drawer.controlPoints);
			if(hasPolygon){
				drawer.drawPolygon();
			}
			repaint();
		}

	}
	@Override
	public void mousePressed(MouseEvent e) {
		//draw a new point
		//if(drawer.canAddPoints){
		if(isDrawing && drawer.canAddPoints){
			if(e.getX()+XOFF < width && e.getY()+YOFF < height){
			drawer.drawPoint(e.getX()+XOFF, e.getY()+YOFF);
			if(hasPolygon){
				drawer.drawPolygon();
			}
			repaint();
			}
		}
		//the point to be dragged
		else if(!isDrawing && isEditing){
			clicked = drawer.checkPoint(e.getX()+XOFF, e.getY()+YOFF, drawer.controlPoints);
		}
		//remove a point
		else if(!isDrawing && !isEditing){
			clicked = drawer.checkPoint(e.getX()+XOFF, e.getY()+YOFF, drawer.controlPoints);
			drawer.removePoint(clicked);
			if(hasPolygon){
				drawer.drawPolygon();
			}
			repaint();
		}
		else alert();
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
		else if(e.getSource()==movePoints){
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
		else if(e.getSource()==bezierButton){
			isBezier = true;
		}
		else if(e.getSource() == bSplineButton){
			isBezier = false;
			drawer.constructNewSpline();
		}


	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(!isDrawing && drawer.checkPoint(e.getX()+XOFF, e.getY()+YOFF, drawer.controlPoints) != null){
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
		else{
			setCursor(Cursor.getDefaultCursor());
		}
	}
	public void alert(){
		JOptionPane.showMessageDialog(null, "Too many dots! Please clear screen or remove a dot", "InfoBox: " , JOptionPane.INFORMATION_MESSAGE);
	}
}


