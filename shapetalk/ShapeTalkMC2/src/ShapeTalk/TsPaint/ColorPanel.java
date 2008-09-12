/**
 * @(#)ColorPanel.java
 */
package ShapeTalk.TsPaint;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class ColorPanel extends JToolBar implements MouseListener {
	private TsPaint instance;

	ColorShowPanel csPanel;
	ColorButton[] coBtns=new ColorButton[28];
	int[] rColors={0x00,0x46,0x78,0x99,0xED,0xFF,0xFF,0xFF,0xA8,0x22,0x60,0x4D,0x2F,0x6F,
				   0xFF,0xDC,0xB4,0x9C,0xFF,0xE5,0xF5,0xFF,0x03,0x9D,0x95,0x70,0x54,0xB5};
	int[] gColors={0x00,0x46,0x78,0x00,0x1C,0x7E,0xC2,0xF2,0xE6,0xB1,0xB7,0x60,0x36,0x31,
				   0xFF,0xDC,0xB4,0x5A,0xA3,0xAA,0xE4,0xF9,0xF9,0xBB,0xD9,0x9A,0x6D,0xA5};
	int[] bColors={0x00,0x46,0x78,0x30,0x24,0x00,0x0E,0x00,0x1D,0x4C,0xEF,0xF3,0x99,0x98,
				   0xFF,0xDC,0xB4,0x3C,0xB1,0x7A,0x9C,0xBD,0xBC,0x61,0xEA,0xD1,0x8E,0xD5};
	JPanel pnColor;
	/**
     * 创建颜色选择板实例，并传递画图板实例
     * @param o 画图板实例
     */
	public ColorPanel(TsPaint o) {
		if (o==null)
			return;
		instance=o;
		
		pnColor=new JPanel(null);

		csPanel=new ColorShowPanel();
		csPanel.setBounds(0,1,30,31);
		pnColor.add(csPanel);
		
		for (int i=0; i<28; i++) {
			Color c=new Color(rColors[i],gColors[i],bColors[i]);
			coBtns[i]=new ColorButton(c);
			int x=31+16*((i<14)?i:(i-14));
			int y=1+16*(i/14);
			coBtns[i].setBounds(x,y,15,15);
			coBtns[i].addMouseListener(this);
			pnColor.add(coBtns[i]);
		}
		
		add(pnColor);
		pnColor.setPreferredSize(new Dimension(280,36));
		setBackground(TsPaint.GRAYCOLOR);
	}

	/**
     * 当点击颜色按钮时触发
     */
	public void mouseClicked(MouseEvent e) {
		Object o=e.getSource();
		if (o instanceof ColorButton) {
			ColorButton btn=(ColorButton)o;
			if (e.getClickCount()==2) {
				Color color=JColorChooser.showDialog(instance.f,"编辑颜色",btn.color);
				if (color==null)
					return;
				btn.setColor(color);
				instance.paintPanel.forColor=color;
				csPanel.setForwordColor(color);
			}
			else {
				if (e.getButton()==e.BUTTON1) {		//左键
					if ((e.getModifiersEx() & e.CTRL_DOWN_MASK) == e.CTRL_DOWN_MASK) {		//按住Ctrl键
						instance.paintPanel.midColor=btn.color;
						csPanel.setMiddleColor(btn.color);
					}
					else {
						instance.paintPanel.forColor=btn.color;
						csPanel.setForwordColor(btn.color);
					}
				}
				else if (e.getButton()==e.BUTTON3) {	//右键
					instance.paintPanel.bacColor=btn.color;
					csPanel.setBackColor(btn.color);
				}
				//csPanel.repaint();
			}
			int r=btn.color.getRed();
			int g=btn.color.getGreen();
			int b=btn.color.getBlue();
			instance.setState("已应用颜色: RGB("+String.valueOf(r)+","+String.valueOf(g)+","+String.valueOf(b)+")");

			instance.toolPanel.paintModePanel.changeColor(csPanel.f_color,csPanel.b_color);
		}
	}

	/**
     * 实现 <code>MouseListener</code> 的必要空方法
     */
	public void mousePressed(MouseEvent e) {}
	/**
     * 实现 <code>MouseListener</code> 的必要空方法
     */
	public void mouseReleased(MouseEvent e) {}
	/**
     * 实现 <code>MouseListener</code> 的必要空方法
     */
	public void mouseEntered(MouseEvent e) {}
	/**
     * 实现 <code>MouseListener</code> 的必要空方法
     */
	public void mouseExited(MouseEvent e) {}


	private class ColorShowPanel extends JPanel {
		Color f_color,b_color,m_color;
	
		ColorShowPanel() {
			m_color=Color.WHITE;
			f_color=Color.BLACK;
			b_color=Color.WHITE;
			
			setPreferredSize(new Dimension(30,30));
		}
		
		void setForwordColor(Color c) {
			f_color=c;
			paint(getGraphics());
		}
		void setBackColor(Color c) {
			b_color=c;
			paint(getGraphics());
		}
		void setMiddleColor(Color c) {
			m_color=c;
			paint(getGraphics());
		}
		
		public void paint(Graphics g) {
			//g.clearRect(0,0,getWidth(),getHeight());
			int w=getWidth();
			int h=getHeight();
			g.setColor(Color.GRAY);
			g.drawLine(0,0,0,h);
			g.drawLine(0,0,w,0);
			g.setColor(new Color(230,230,230));
			g.drawRect(1,1,w-2,h-2);
			g.setColor(m_color);
			g.fillRect(2,2,w-3,h-3);
			
			drawButton(b_color,g,11,12,15,15);
			drawButton(f_color,g,4,5,15,15);
		}
	
		private void drawButton(Color color,Graphics g,int x,int y,int width,int height) {
			g.setColor(new Color(230,230,230));
			g.drawRect(x+1,y+1,width-3,height-3);
			g.setColor(Color.GRAY);
			g.drawLine(x+1,y+height-1,x+width-1,y+height-1);
			g.drawLine(x+width-1,y+1,x+width-1,y+height-1);
			g.setColor(color);
			g.fillRect(x+2,y+2,width-4,height-4);
		}
	}
	
	private class ColorButton extends JPanel {
		Color color;
		
		ColorButton(Color c) {
			setColor(c);
			setSize(15,15);
		}
		void setColor(Color c) {
			color=c;
			//repaint();
			paint(getGraphics());
		}
		public void paint(Graphics g) {
			//g.clearRect(0,0,getWidth(),getHeight());
			if (g==null) return;
			int width=getWidth();
			int height=getHeight();
			
			g.setColor(Color.GRAY);
			g.drawLine(0,0,0,height);
			g.drawLine(0,0,width,0);
			g.setColor(new Color(230,230,230));
			g.drawRect(1,1,width-2,height-2);
			g.setColor(color);
			g.fillRect(2,2,width-3,height-3);
		}
	}
}
