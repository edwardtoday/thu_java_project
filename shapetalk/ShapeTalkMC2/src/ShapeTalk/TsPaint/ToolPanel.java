/**
 * @(#)ToolPanel.java
 */
package ShapeTalk.TsPaint;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

/**
 * 工具栏的类，实现 <code>ActionListener</code> 接口，用来处理事件
 *
 * @author <a href="http://www.cnitblog.com/tsorgy">黑色珊瑚</a>
 * @version 1.00 2008/5/11
 */
public class ToolPanel extends JToolBar implements ActionListener {
	/**
     * 动作按钮的提示文本
     */
	public String[] sTexts={"移动","选取颜色","铅笔","直线","矩形","填充矩形","圆角矩形","填充圆角","椭圆","填充椭圆"};
	/**
     * 动作按钮的命令（也是不含扩展名的文件名）
     */
	public String[] sCmds={"move","getcolor","pencil","line","rect","fillrect","roundrect","fillroundrect","oval","filloval"};
	/**
     * 动作按钮的 <code>ImageIcon</code> 数组
     */
	public ImageIcon[] imgIcons=new ImageIcon[sCmds.length];

	PaintPanel instance;
	JPanel pnTool;
	JToggleButton[] btn=new JToggleButton[sCmds.length];

	JPanel pnButton;
	StrokePanel pnStroke;
	SquarePanel pnSquare;
	WelcomePanel welcomePanel;
	PaintModePanel paintModePanel;
	
	/**
     * 构造工具栏，传入主绘图区域的实例对象
     * @param o 绘图区区域实例对象
     */
	public ToolPanel(PaintPanel o) {
		super(JToolBar.VERTICAL);
		if (o==null)
			return;
		instance = o;
		
		for (int i=0; i<btn.length; i++) {
			try {
				
				imgIcons[i]=new ImageIcon("resources/icons/"+sCmds[i]+".gif");
			}
			catch (Exception e) {
				System.out.println ("==============================");
				System.out.println ("Toolpanel(Object)方法出现异常:");
				System.out.println ("o="+o.toString());
				System.out.println ("错误信息: "+e.toString());
				System.out.println ("==============================");
			}
			btn[i]=new JToggleButton("",imgIcons[i]);
			btn[i].setToolTipText(sTexts[i]);
			btn[i].setActionCommand(sCmds[i]);
		}
/* 空布局方式 */
		pnButton=new JPanel(null);
		for (int i=0; i<btn.length; i++) {
			btn[i].setBounds((i%2)*27+2,(i/2)*30+2,25,25);
			pnButton.add(btn[i]);
		}
		pnStroke=new StrokePanel(6);
		pnStroke.setLocation(4,(btn.length/2)*30+5);
		pnStroke.setVisible(false);
		pnButton.add(pnStroke);

		pnSquare=new SquarePanel(G2d.RECTANGLE);
		pnSquare.setLocation(4,pnStroke.getLocation().y+pnStroke.getHeight()+10);
		pnSquare.setVisible(false);
		pnButton.add(pnSquare);
		
		paintModePanel=new PaintModePanel();
		paintModePanel.setLocation(4,pnStroke.getLocation().y);
		//paintModePanel.setVisible(false);
		pnButton.add(paintModePanel);
		
		welcomePanel=new WelcomePanel();
		welcomePanel.setLocation(pnStroke.getLocation());
		welcomePanel.setVisible(false);
		pnButton.add(welcomePanel);
		
		pnButton.setPreferredSize(new Dimension(58,(btn.length/2)*30+pnStroke.getHeight()+pnSquare.getHeight()+20));
/* */

/* 流布局方式 *
		pnButton=new JPanel(new FlowLayout());
		for (int i=0; i<btn.length; i++) {
			btn[i].setPreferredSize(new Dimension(25,25));
			pnButton.add(btn[i]);
		}
		pnStroke=new StrokePanel(6);
		pnStroke.setPreferredSize(new Dimension(50,l*12+6));
		pnButton.add(pnStroke);
		pnButton.setPreferredSize(new Dimension(68,(int)(pnStroke.getPreferredSize().getHeight())));
/* */

		add(pnButton);
		Object[] objComponents=pnButton.getComponents();
		for (int i=0; i<objComponents.length; i++) {
			if (objComponents[i] instanceof JToggleButton) {
				JToggleButton btn=((JToggleButton)objComponents[i]);
				btn.setFocusable(false);
				btn.addActionListener(this);
			}
		}
		setToggleButton(0);
	}
	
	/**
     * 动作处理方法
     */
	public void actionPerformed(ActionEvent e) {
		String s=e.getActionCommand();
		
		if ("OpenFile".equals(s)) {
			instance.openFile(null);
		}
		else if ("SaveAsFile".equals(s)) {
			instance.saveFile(true);
		}
		else if ("SaveFile".equals(s)) {
			instance.saveFile(false);
		}
		else if ("NewFile".equals(s)) {
			instance.initParam();
			instance.fileName=null;
		}
		else if ("Exit".equals(s)) {
			System.exit(0);
		}
		else if ("AboutMe".equals(s)) {
			JOptionPane.showMessageDialog(null,"黑色珊瑚 TsOrgY Copyright(c) 2008","关于 "+TsPaint.TITLE,JOptionPane.INFORMATION_MESSAGE);
		}
		else if ("SetBackColor".equals(s)) {
			Color c=JColorChooser.showDialog(instance,"背景色",instance.backColor);
			if (c!=null) {
				instance.backColor=c;
				instance.c.setBackground(c);
				instance.repaint();
			}
		}
		else if ("SetStroke".equals(s)) {
			String input=JOptionPane.showInputDialog(instance,"输入粗细数值 (>0)",instance.stroke);
			if (input==null) return;
			float stroke=1.0f;
			try {
				stroke=Float.parseFloat(input);
			}
			catch (Exception ee) {
				System.out.println (ee);
			}
			setStroke(stroke);
			pnStroke.setStroke(stroke);
		}
		else if ("SetCanvas".equals(s)) {
			String inputW=JOptionPane.showInputDialog(instance,"输入画布宽 (>0)",instance.width);
			if (inputW==null) return;
			String inputH=JOptionPane.showInputDialog(instance,"输入画布高 (>0)",instance.height);
			if (inputH==null) return;
			int width=instance.width;
			int height=instance.height;
			try {
				width=Integer.parseInt(inputW);
				height=Integer.parseInt(inputH);
			}
			catch (Exception ee) {
				System.out.println (ee);
			}
			instance.width=width;
			instance.height=height;
			instance.c.setSize(width,height);
			instance.repaint();
		}
		else if ("Undo".equals(s)) {
			if (instance.undoGraphics.size()==0) return;
			instance.graphics.add(instance.undoGraphics.getLast());
			instance.undoGraphics.removeLast();
			if (instance.undoGraphics.size()==0) {
				instance.setUndoable(false);
			}
			instance.repaint();
		}
		else if ("DelObject".equals(s)) {
			deleteObject();
		}
		
		else {
			//改变按钮选择 state
			if ("move".equals(s)) {
				instance.setType(-1);
				setToggleButton(0);
			}
			else if ("getcolor".equals(s)) {
				instance.setType(PaintPanel.GETCOLOR);
				setToggleButton(1);
			}
			else if ("pencil".equals(s)) {
				instance.setType(G2d.PENCIL);
				setToggleButton(2);
			}
			else if ("line".equals(s)) {
				instance.setType(G2d.LINE);
				setToggleButton(3);
			}
			else if ("rect".equals(s)) {
				instance.isFill=false;
				instance.setType(G2d.RECTANGLE);
				setToggleButton(4);
			}
			else if ("fillrect".equals(s)) {
				instance.isFill=true;
				instance.setType(G2d.RECTANGLE);
				setToggleButton(5);
			}
			else if ("roundrect".equals(s)) {
				instance.isFill=false;
				instance.setType(G2d.ROUNDRECT);
				setToggleButton(6);
			}
			else if ("fillroundrect".equals(s)) {
				instance.isFill=true;
				instance.setType(G2d.ROUNDRECT);
				setToggleButton(7);
			}
			else if ("oval".equals(s)) {
				instance.isFill=false;
				instance.setType(G2d.OVAL);
				setToggleButton(8);
			}
			else if ("filloval".equals(s)) {
				instance.isFill=true;
				instance.setType(G2d.OVAL);
				setToggleButton(9);
			}
		}
	}
	/**
     * 删除当前选中物体
     */
	public void deleteObject() {
		if (instance.nowObject>=0) {
			instance.undoGraphics.add(instance.graphics.remove(instance.nowObject));
			instance.setUndoable(true);
			instance.instance.setState("已删除物体 "+String.valueOf(instance.nowObject)+" !");
			instance.nowObject=-1;
			instance.nowGraphics=-1;
			instance.redraw();
			instance.setDelable(false);
		}
	}
	/**
     * 设置画笔粗细
     * @param f 画笔粗细
     */
	public void setStroke(float f) {
		instance.stroke=f;
	}
	/**
     * 设置是否绘制正二维图形
     * @param square 是否绘制正二维图形
     */
	public void setSquare(boolean square) {
		instance.isSquare=square;
	}
	/**
     * 设置渐变填充样式
     * @param type 渐变样式，0-不渐变，1-水平渐变，2-垂直渐变，3-对角渐变
     * @see G2d#gradientPaint
     * @see G2d#setGradientPaint(Point,Point)
     */
	public void setPaintMode(int type) {
		instance.gradientPaint=type;
	}
	private void setToggleButton(int index) {
		for (int i=0; i<btn.length; i++)
			btn[i].setSelected(false);
		btn[index].setSelected(true);
		String s=sCmds[index];
		welcomePanel.setVisible(false);
		pnStroke.setVisible(false);
		pnSquare.setVisible(false);
		paintModePanel.setVisible(false);

		if ("move".equals(s) || "getcolor".equals(s)) {
			welcomePanel.setVisible(true);
		} else if ("pencil".equals(s) || "line".equals(s)) {
			pnStroke.setVisible(true);
		} else {
			if (s.startsWith("fill")) {
				paintModePanel.setVisible(true);
				if ("fillrect".equals(s)) {
					pnSquare.setType(G2d.RECTANGLE);
				} else if ("fillroundrect".equals(s)) {
					pnSquare.setType(G2d.ROUNDRECT);
				} else if ("filloval".equals(s)) {
					pnSquare.setType(G2d.OVAL);
				}
				pnSquare.setVisible(true);
			} else {
				pnStroke.setVisible(true);
				if ("rect".equals(s)) {
					pnSquare.setType(G2d.RECTANGLE);
				} else if ("roundrect".equals(s)) {
					pnSquare.setType(G2d.ROUNDRECT);
				} else if ("oval".equals(s)) {
					pnSquare.setType(G2d.OVAL);
				}
				pnSquare.setVisible(true);
			}
		}
	}
}

//选择画笔粗细的Panel
class StrokePanel extends JPanel {
	private int length;
	private int size;
	private float stroke;
	
	public StrokePanel(int l) {
		stroke=1.0f;
		length=l;
		size=12;
		setSize(50,l*12+6);
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int y=e.getY()-4;
				y/=size;
				y++;
				setStroke((float)y);
				Container c=getParent().getParent();
				if (c instanceof ToolPanel) {
					ToolPanel t=(ToolPanel)c;
					t.setStroke((float)y);
				}
			}
		});
	}
	
	public void setStroke(float f) {
		if (f>(float)length) f=(float)length;
		stroke=f;
		repaint();
	}
	public float getStrok() {
		return stroke;
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		int w=getWidth()-1;
		int h=getHeight()-1;
		g.setColor(Color.GRAY);
		g.drawLine(0,0,w,0);
		g.drawLine(0,0,0,h);
		g.setColor(Color.WHITE);
		g.drawLine(0,h,w,h);
		g.drawLine(w,0,w,h);
		
		int is=(int)stroke;
		if (is>length) is=length;
		is--;
		g.setColor(new Color(0x33,0x99,0xff));
		g.fillRect(3,is*size+3,w-5,size-1);
		
		g.setColor(Color.BLACK);
		for (int i=0; i<length; i++) {
			if (is==i) g.setColor(Color.WHITE);
			for (int j=0; j<=i; j++) {
				int y=(int)(i*(size-0.5))+j+8;
				g.drawLine(5,y,w-7,y);
			}
			if (is==i) g.setColor(Color.BLACK);
		}
	}
}

//选择是否是正二维图形的Panel
class SquarePanel extends JPanel {
	private boolean isSquare;
	private int type;
	
	public SquarePanel(int l) {
		type=G2d.RECTANGLE;
		isSquare=false;
		setSize(50,64);
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int y=e.getY();
				if (y>=4&&y<32)
					setSquare(false);
				else if (y>32&&y<=60)
					setSquare(true);
				Container c=getParent().getParent();
				if (c instanceof ToolPanel) {
					ToolPanel t=(ToolPanel)c;
					t.setSquare(isSquare);
				}
			}
		});
	}
	public void setType(int i) {
		type=i;
		repaint();
	}
	public void setSquare(boolean square) {
		isSquare=square;
		repaint();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		int w=getWidth()-1;
		int h=getHeight()-1;
		g.setColor(Color.GRAY);
		g.drawLine(0,0,w,0);
		g.drawLine(0,0,0,h);
		g.setColor(Color.WHITE);
		g.drawLine(0,h,w,h);
		g.drawLine(w,0,w,h);
		
		int is=0;
		if (isSquare)
			is=1;
		g.setColor(new Color(0x33,0x99,0xff));
		g.fillRect(3,is*28+3,w-5,28);
		
		if (is==0) {
			g.setColor(Color.WHITE);
			if (type==G2d.RECTANGLE)
				g.drawRect((w-36)/2,7,36,18);
			else if (type==G2d.ROUNDRECT)
				g.drawRoundRect((w-36)/2,7,36,18,8,8);
			else if (type==G2d.OVAL)
				g.drawOval((w-36)/2,7,36,18);
			g.setColor(Color.BLACK);
			if (type==G2d.RECTANGLE)
				g.drawRect((w-18)/2,35,18,18);
			else if (type==G2d.ROUNDRECT)
				g.drawRoundRect((w-18)/2,35,18,18,8,8);
			else if (type==G2d.OVAL)
				g.drawOval((w-18)/2,35,18,18);
		} else {
			g.setColor(Color.BLACK);
			if (type==G2d.RECTANGLE)
				g.drawRect((w-36)/2,7,36,18);
			else if (type==G2d.ROUNDRECT)
				g.drawRoundRect((w-36)/2,7,36,18,8,8);
			else if (type==G2d.OVAL)
				g.drawOval((w-36)/2,7,36,18);
			g.setColor(Color.WHITE);
			if (type==G2d.RECTANGLE)
				g.drawRect((w-18)/2,35,18,18);
			else if (type==G2d.ROUNDRECT)
				g.drawRoundRect((w-18)/2,35,18,18,8,8);
			else if (type==G2d.OVAL)
				g.drawOval((w-18)/2,35,18,18);
		}
		
	}
}

//欢迎提示的Panel
class WelcomePanel extends JPanel {
	
	public WelcomePanel() {
		JLabel lbl1=new JLabel("按Ctrl再");
		JLabel lbl2=new JLabel("点击鼠标");
		JLabel lbl3=new JLabel("会有彩蛋");
		JLabel lbl4=new JLabel("哦~~");
		setLayout(new GridLayout(0,1));
		setSize(50,75);
		add(lbl1);
		add(lbl2);
		add(lbl3);
		add(lbl4);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		int w=getWidth()-1;
		int h=getHeight()-1;
		g.setColor(Color.GRAY);
		g.drawLine(0,0,w,0);
		g.drawLine(0,0,0,h);
		g.setColor(Color.WHITE);
		g.drawLine(0,h,w,h);
		g.drawLine(w,0,w,h);
	}
}

//选择填充方式的Panel
class PaintModePanel extends JPanel {
	private int type;	//0-标准填充, 1-水平渐变, 2-垂直渐变, 3-对角渐变
	private Color c1,c2;
	
	public PaintModePanel() {
		type=0;
		setSize(50,64);
		c1=Color.BLACK;
		c2=Color.WHITE;
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int x=e.getX();
				int y=e.getY();
				type=(y/32)*2;
				type+=(x/25);
				if (type>3)
					type=3;
				setPaintMode(type);
				Container c=getParent().getParent();
				if (c instanceof ToolPanel) {
					ToolPanel tp=(ToolPanel)c;
					tp.setPaintMode(type);
				}
			}
		});
	}
	public void setPaintMode(int i) {
		type=i;
		repaint();
	}
	public void changeColor(Color t1,Color t2) {
		if (t1!=null&&t2!=null) {
			c1=t1;
			c2=t2;
			repaint();
		}
	}
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d=(Graphics2D)g;
		//g2d.setTransform(new AffineTransform());
		int w=getWidth()-1;
		int h=getHeight()-1;
		g2d.setColor(Color.GRAY);
		g2d.drawLine(0,0,w,0);
		g2d.drawLine(0,0,0,h);
		g2d.setColor(Color.WHITE);
		g2d.drawLine(0,h,w,h);
		g2d.drawLine(w,0,w,h);
		
		g2d.setColor(new Color(0x33,0x99,0xff));
		g2d.fillRect((type%2)*22+3,(type/2)*28+3,22,28);
		
		g2d.setColor(c1);
		g2d.fillRect(6,6,16,22);
		
		g2d.setPaint(new GradientPaint(new Point(28,6),c1,new Point(44,6),c2));
		g2d.fillRect(28,6,16,22);
		
		g2d.setPaint(new GradientPaint(new Point(6,34),c1,new Point(6,56),c2));
		g2d.fillRect(6,34,16,22);
		
		g2d.setPaint(new GradientPaint(new Point(28,34),c1,new Point(44,56),c2));
		g2d.fillRect(28,34,16,22);

		//repaint();
	}
}
