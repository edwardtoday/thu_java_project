/**
 * @(#)PaintPanel.java
 */
package ShapeTalk.TsPaint;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * 用来做绘制动作的主区域
 *
 * @author <a href="http://www.cnitblog.com/tsorgy">黑色珊瑚</a>
 * @version 1.00 2008/5/11
 */
public class PaintPanel extends JPanel {
	/**
     * 标志取色动作
     */
	public static final int GETCOLOR=99;
	/**
     * 画图板的实例对象
     * @see #PaintPanel(TsPaint)
     */
	public TsPaint instance;
	
	private static final int OBJECTCOUNT=65536;
	PopMenu popMenu;
	JPanel c;
	Color backColor;
	int width=400;
	int height=300;
	int changeSize;		//画布改变大小方式
	File fileName;
	
	Color nowColor;
	
	Color forColor;
	Color bacColor;
	Color midColor;
	boolean isFill;
	float stroke;
	int nowType;
	LinkedList<G2d> graphics;
	LinkedList<G2d> undoGraphics;
	
	boolean isDrawing;
	boolean isChanging;
	boolean isSquare;	//是否正二维图形
	int gradientPaint;	//渐变涂色样式
	int nowObject;		//保存选中物体
	int nowChangeGraphics;
	int nowRotate;
	int nowGraphics;
	int xOffset,yOffset;

	/**
     * 构造出绘制区域
     * @param o 画图板的实例对象
     */
	public PaintPanel(TsPaint o) {
		if (o==null)
			return;
		instance=o;

		setLayout(null);
		//初始化参数
		initParam();

		c=new JPanel() {
			public void paint(Graphics g) {
				//super.paint(g);

				if (nowType==G2d.PENCIL && isDrawing) {		//如果是铅笔的话
					((G2d)(graphics.getLast())).drawLastLine((Graphics2D)g);
					//graphics.get(graphics.size()-1)
				} else {
					Image buffer=createImage(width,height);
					Graphics2D gg=(Graphics2D)(buffer.getGraphics());
					//Graphics2D gg=(Graphics2D)g;
	
					gg.setColor(backColor);
					gg.fillRect(0,0,width,height);
					for (int j=0; j<graphics.size(); j++) {
						draw(gg,(G2d)(graphics.get(j)));
					}
					if (nowGraphics>=0) {
						nowObject=nowGraphics;
						drawFocus(gg,nowGraphics);
						setDelable(true);
					} else if (nowChangeGraphics>=0) {
						drawFocus(gg,nowChangeGraphics);
					} else {
						nowObject=-1;
						setDelable(false);
					}
					g.drawImage(buffer,0,0,this);
				}
			}
			void draw(Graphics2D g2d,G2d g) {
				g.draw(g2d);
			}
			private void drawFocus(Graphics2D gg,int index){
				if (nowType==-1) {
					try {
						G2d gh=(G2d)(graphics.get(index));
						gg.setColor(Color.BLACK);
						gh.drawMoveFocus(gg,Color.WHITE);
					} catch (Exception e) {		//可能产生Exception，比如操作太快，不知道为什么会产生NullPointerException
						System.out.println ("==============================");
						System.out.println ("drawFocus(Graphics2D,int)方法出现异常:");
						System.out.println ("gg="+gg.toString()+", index="+String.valueOf(index));
						System.out.println ("错误信息: "+e.toString());
						System.out.println ("==============================");
					}
				}
			}
		};
		c.setFocusable(true);
		c.setBackground(backColor);
		c.setBounds(6,6,width,height);
		c.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				c_mouseClicked(e);
			}
			public void mousePressed(MouseEvent e) {
				c_mousePressed(e);
			}
			public void mouseReleased(MouseEvent e) {
				c_mouseReleased(e);
			}
			public void mouseExited(MouseEvent e) {
				c_mouseExited(e);
			}
		});
		c.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				c_mouseDragged(e);
			}
			public void mouseMoved(MouseEvent e) {
				c_mouseMoved(e);
			}
		});
		c.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				c_keyTyped(e);
			}
		});
		//c.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		popMenu=new PopMenu(this);
		c.add(popMenu);

		add(c);
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				p_mouseDragged(e);
			}
			public void mouseMoved(MouseEvent e) {
				p_mouseMoved(e);
			}
		});
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				p_mousePressed(e);
			}
			public void mouseReleased(MouseEvent e) {
				p_mouseReleased(e);
			}
		});
		setBackground(Color.DARK_GRAY);
		setType(-1);
	}
	
	/**
     * 重写了 <code>JPanel</code> 的 <code>paint(Graphics)</code> 方法<br>
     * 使它能画出8个焦点小矩形
     */
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(new Color(0xFF,0xC2,0x0E));
		g.drawRect(1,1,4,4);
		g.drawRect(width/2,1,4,4);
		g.drawRect(width+6,1,4,4);
		g.drawRect(1,height+6,4,4);
		g.drawRect(1,height/2,4,4);
		
		g.fillRect(width+6,height/2,4,4);
		g.fillRect(width+6,height+6,4,4);
		g.fillRect(width/2,height+6,4,4);

		//redraw();
	}

	/**
     * 当鼠标在画布上单击时触发<br>
     * <b>完成:</b> 彩蛋绘制或弹出菜单
     */
	public void c_mouseClicked(MouseEvent e) {
		if (e.getButton()==e.BUTTON3) {
			if (nowObject>=0) {
				popMenu.show(this,e.getX(),e.getY());
			}
		}
		else if (e.getModifiersEx()==e.CTRL_DOWN_MASK) {
			/* 我的彩蛋1 - 随机色的小蛋蛋 *
			G2d gh=new ObjOval();
			graphics.add(gh);
			int iR=(int)(Math.random()*255);
			int iG=(int)(Math.random()*255);
			int iB=(int)(Math.random()*255);
			gh.addPoint(e.getX()-10,e.getY()-10);
			gh.addPoint(e.getX()+10,e.getY()+10);
			gh.filled=true;
			gh.color=new Color(iR,iG,iB);
			/* */
			
			/* 我的彩蛋2 - 随机角的星星 */
			G2d gh=new ObjPolygon((int)(Math.random()*9+3));
			int size=(int)(Math.random()*50+50);
			graphics.add(gh);
			int iR=(int)(Math.random()*255);
			int iG=(int)(Math.random()*255);
			int iB=(int)(Math.random()*255);
			gh.color=new Color(iR,iG,iB);
			gh.color2=new Color(iB,iG,iR);
			gh.gradientPaint=3;
			gh.stroke=stroke;

			gh.addPoint(e.getX(),e.getY());
			gh.changeSize(e.getX()+size,e.getY()+size);
			if ((Math.random()*10)>=5)
				gh.filled=true;
			else
				gh.filled=false;
			/* */

			redraw();
			
			instance.setState("创建了一个彩蛋~~ ^_^  Copyright 2008 (c) 黑色珊瑚");
		}
	}
	/**
     * 当鼠标在画布上按压时触发<br>
     * 确定当前动作，比如选中物体，改变物体大小或创建新物体
     */
	public void c_mousePressed(MouseEvent e) {
		if (nowType==-1) {
			redraw();
			if (nowChangeGraphics>=0) {
				isChanging=true;
				G2d gh=(G2d)(graphics.get(nowChangeGraphics));
				xOffset=e.getX()-gh.getPointX(1);		//gh.getMaxX();			//graphics[nowChangeGraphics].getPointX(1);
				yOffset=e.getY()-gh.getPointY(1);
			}
			else if (nowGraphics>=0) {
				G2d gh=(G2d)(graphics.get(nowGraphics));
				xOffset=e.getX()-gh.getMinX();
				yOffset=e.getY()-gh.getMinY();
				instance.setState("选中物体 "+String.valueOf(nowGraphics)+" : "+gh.toString());
			}
		}
		else {
			if (e.getButton()==e.BUTTON1) {		//左键
				if ((e.getModifiersEx() & e.CTRL_DOWN_MASK) == e.CTRL_DOWN_MASK) {		//按Ctrl键了
					nowColor=midColor;
				}
				else {
					nowColor=forColor;
				}
			}
			else if (e.getButton()==e.BUTTON3) {	//右键
				nowColor=bacColor;
			}
			G2d gh=createObject(e.getX(),e.getY());
			isDrawing=true;
			instance.setState("创建物体 "+String.valueOf(graphics.size()-1)+" : "+gh.toString());
		}
	}
	/**
     * 当鼠标在画布上弹起时触发<br>
     * 结束当前动作，比如创建完成，改变物体大小完成
     */
	public void c_mouseReleased(MouseEvent e) {
		if (isDrawing) {
			isDrawing=false;
			G2d gh=(G2d)(graphics.getLast());
			if (e.getX()==gh.getPointX(0) && e.getY()==gh.getPointY(0)) {
				graphics.removeLast();		//graphics.remove(counts);
				return;
			}
			instance.setState("一共创建了 "+String.valueOf(graphics.size())+" 个物体");
		}
		if (isChanging) {
			isChanging=false;
			c_mouseMoved(e);
		}
		
		redraw();
	}
	/**
     * 当鼠标移出画布时触发<br>
     * 结束当前动作，比如创建完成，改变物体大小完成
     */
	public void c_mouseExited(MouseEvent e) {
		if (nowGraphics<0 && nowChangeGraphics<0 && nowObject<0) {
			instance.setState("准备就绪");
		}
	}
	/**
     * 当鼠标在画布上拖动时触发<br><br>
     * 完成下列动作：<br>
     * 改变物体大小 {@link G2d#changeSize(int,int)} （包含创建新物体）<br>
     * 移动物体 {@link G2d#moveObject(int,int)}
     */
	public void c_mouseDragged(MouseEvent e) {
		if (nowType==-1) {
			int x=e.getX();
			int y=e.getY();
			if ((e.getModifiers() & e.BUTTON1_MASK)==e.BUTTON1_MASK) {		//左键
				if (nowChangeGraphics>=0) {
					G2d gh=(G2d)(graphics.get(nowChangeGraphics));
					gh.changeSize(x-xOffset,y-yOffset);
					int x1=gh.getPointX(0);
					int y1=gh.getPointY(0);
					if ((x>=x1&&y>=y1) || (x<=x1&&y<=y1)) {
						c.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
					}
					else if ((x>x1&&y<y1) || (x<x1&&y>y1)) {
						c.setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
					}

					instance.setState("正在改变大小 物体 "+String.valueOf(nowChangeGraphics)+" : "+
						String.valueOf(gh.getWidth())+" * "+String.valueOf(gh.getHeight()));
				}
				else if (nowGraphics>=0) {
					G2d gh=(G2d)(graphics.get(nowGraphics));
					int iBuffX=x-xOffset-gh.getMinX();		//.getPointX(0);
					int iBuffY=y-yOffset-gh.getMinY();
					gh.moveObject(iBuffX,iBuffY);
					instance.setState("正在移动物体 "+String.valueOf(nowGraphics)+" : "+gh.toString());
				}
			}
		}
		else {
			G2d gh=(G2d)(graphics.getLast());
			gh.changeSize(e.getX(),e.getY());
			instance.setState("正在创建物体 "+String.valueOf(graphics.size()-1)+" : "+gh.toString());
		}
		redraw();
	}
	/**
     * 当鼠标在画布上移动时触发<br><br>
     * 做以下判断：<br>
     * 是否可移动，参见 {@link G2d#isCursorIn(int,int)}<br>
     * 是否可改变大小，参见 {@link G2d#canResizeObject(int,int)}
     */
	public void c_mouseMoved(MouseEvent e) {
		if (nowType==-1) {
			G2d gh=null;
			int x=e.getX();
			int y=e.getY();
			nowGraphics=-1;
			nowChangeGraphics=-1;
//			nowRotate=-1;
			for (int i=graphics.size()-1; i>=0; i--) {
				gh=(G2d)(graphics.get(i));
/*				if (gh.canRotate(x,y)) {
					setCursor("rotate.gif",new Point(16,16),"rotate");
					nowRotate=i;
					break;
				}
*/				if (gh.canResizeObject(x,y)) {
					int x1=gh.getPointX(0);
					int y1=gh.getPointY(0);
					if ((x>=x1&&y>=y1) || (x<=x1&&y<=y1))
						c.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
					else if ((x>x1&&y<y1) || (x<x1&&y>y1))
						c.setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
					nowChangeGraphics=i;
					break;
				}
				if (gh.isCursorIn(x,y)) {
					c.setCursor(new Cursor(Cursor.MOVE_CURSOR));
					nowGraphics=i;
					//redraw();
					break;
				}
			}
			if (nowGraphics==-1 && nowChangeGraphics==-1) {		// && nowRotate==-1
				c.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			else {
/*				if (nowRotate>=0) {
					instance.setState("可旋转物体 "+String.valueOf(nowRotate)+" : "+graphics.get(nowRotate));
				} else*/
				if (nowGraphics>=0) {
					instance.setState("当前物体 "+String.valueOf(nowGraphics)+" : "+gh.toString());
				} else if (nowChangeGraphics>=0) {
					instance.setState("可以改变大小 物体 "+String.valueOf(nowChangeGraphics)+" : "+gh.toString());
				}
				return;
			}
			//redraw();
		}
		if (nowObject<0) {
			instance.setState("当前鼠标 ("+String.valueOf(e.getX())+","+String.valueOf(e.getY())+")");
		} else if (nowGraphics!=nowObject) {
			instance.setState("当前选中物体 "+String.valueOf(nowObject)+" : "+graphics.get(nowObject));
		}
	}
	//*鼠标处理事件结束
	
	/*
	 * Panel 鼠标处理事件
	 */
	private int resizeType(int x,int y) {
		if ((x>width+5) && (y>height/2) && (x<width+15) && (y<height/2+5))
			return 1;		//水平拉
		else if ((x>width/2) && (y>height+5) && (x<width/2+5) && (y<height+15))
			return 2;
		else if ((x>width+5) && (y>height+5) && (x<width+15) && (y<height+15))
			return 3;		//对角线拉
		else
			return 0;
	}
	/**
     * 当鼠标在主 <code>JPanel</code> 上按下的时候触发，判断是否可改变画布大小
     */
	public void p_mousePressed(MouseEvent e) {
		changeSize=resizeType(e.getX(),e.getY());
		if (changeSize>0) {
			xOffset=e.getX()-width-5;
			yOffset=e.getY()-height-5;
		}
	}
	/**
     * 当鼠标在主 <code>JPanel</code> 上拖动的时候触发，符合条件则改变画布大小
     */
	public void p_mouseDragged(MouseEvent e) {
		if (changeSize>0) {
			if (changeSize==1||changeSize==3)
				width=e.getX()-5-xOffset;
			if (changeSize==2||changeSize==3)
				height=e.getY()-5-yOffset;
			c.setSize(width,height);
			instance.setState("画布大小: "+String.valueOf(width)+" * "+String.valueOf(height));
			//repaint();
			paint(getGraphics());
		}
	}
	/**
     * 当鼠标在主 <code>JPanel</code> 上移动的时候触发，符合改变大小条件则改变鼠标指针样式
     */
	public void p_mouseMoved(MouseEvent e) {
		int bak=changeSize;
		changeSize=resizeType(e.getX(),e.getY());
		if (bak!=changeSize) {
			if (changeSize==1) {
				this.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
			} else if (changeSize==2) {
				this.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
			} else if (changeSize==3) {
				this.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
			} else {
				this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
	}
	/**
     * 当鼠标在主 <code>JPanel</code> 上弹起的时候触发，若正在改变大小则恢复
     */
	public void p_mouseReleased(MouseEvent e) {
		if (changeSize>0) {
			//setSize(width+10,height+10);
			c.setSize(width,height);
			changeSize=0;
			paint(getGraphics());
			c_mouseExited(e);
			p_mouseMoved(e);
		}
	}
	//* Panel 鼠标事件结束

	/**
     * 画布有焦点时，键盘按下触发事件<br>
     * 按下 <code>Delete</code> 键则删除选择物体
     */
	public void c_keyTyped(KeyEvent e) {
		//System.out.println (e);
		if (e.getKeyChar()==e.VK_DELETE){		//删除物体
			instance.toolPanel.deleteObject();
		}
	}
	//*键盘处理事件结束
	
	/**
     * 设置撤xiao钮的可用状tai
     */
	public void setUndoable(boolean b) {
		instance.mnuE_Undo.setEnabled(b);
		instance.toolbarPanel.jbtn[3].setEnabled(b);
	}
	/**
     * 设置删除按钮的可用状tai
     */
	public void setDelable(boolean b) {
		instance.mnuO_DelObject.setEnabled(b);
		instance.toolbarPanel.jbtn[6].setEnabled(b);
	}
	
	private G2d createObject(int x,int y) {
		G2d gh;
		if (nowType==G2d.LINE) {
			gh=new ObjLine();
		}
		else if (nowType==G2d.RECTANGLE) {
			gh=new ObjRectangle();
		}
		else if (nowType==G2d.ROUNDRECT) {
			gh=new ObjRoundRect();
		}
		else if (nowType==G2d.OVAL) {
			gh=new ObjOval();
		}
		else if (nowType==G2d.PENCIL) {
			gh=new ObjPolyline();
		}
		else { //if (nowType==G2d.POLYGON) {
			gh=new ObjPolygon();
		}
		
		graphics.add(gh);
		gh.type=nowType;
		gh.color=nowColor;
		gh.color2=bacColor;
		gh.gradientPaint=gradientPaint;
		gh.filled=isFill;
		//画正二维图形
		gh.isSquare=isSquare;
		gh.addPoint(x,y);
		gh.addPoint(x,y);
		gh.stroke=stroke;
		
		return gh;
	}
	
	/**
     * 设置画布鼠标指针样式
     * @param name 指针文件名
     *        pt 指针图像的热点
     *        curName 自定义指针的名称
     */
	public void setCursor(String name,Point pt,String curName) {
		try {
			Image imgCur=Toolkit.getDefaultToolkit().getImage(TsPaint.class.getResource("/tsorgy/tspaint/cursors/" + name));
			c.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(imgCur,pt,curName));
		}
		catch (Exception e) {
			System.out.println ("==============================");
			System.out.println ("setCursor(String,Point,String)方法出现异常:");
			System.out.println ("name="+name+", pt="+pt.toString()+", curName="+curName);
			System.out.println ("错误信息: "+e.toString());
			System.out.println ("==============================");
			c.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	/**
     * 设置当前鼠标动作
     * @param type 动作，参考 {@link G2d#PENCIL}，{@link G2d#LINE}，
     *                        {@link G2d#RECTANGLE}，{@link G2d#ROUNDRECT}，
     *                        {@link G2d#OVAL}，{@link G2d#POLYGON}，
     *                        (@link StatePanel#setNowType(int,boolean)}
     */
	public void setType(int type) {
		nowType=type;
		if (type==-1) {
			c.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			instance.setState("移动物件");
		}
		else {
			setCursor("cross.gif",new Point(15,16),"cross");
			if (type==G2d.PENCIL) {
				setCursor("pencil.gif",new Point(8,23),"pencil");
				instance.setState("铅笔");
			}
			else if (type==GETCOLOR) {
				setCursor("getcolor.gif",new Point(8,22),"getcolor");
				instance.setState("选取颜色");
			}
			else if (type==G2d.LINE)
				instance.setState("直线");
			else if (type==G2d.RECTANGLE) {
				if (isFill) {
					instance.setState("填充矩形");
				}
				else {
					instance.setState("矩形");
				}
			}
			else if (type==G2d.ROUNDRECT) {
				if (isFill) {
					instance.setState("填充圆角矩形");
				}
				else {
					instance.setState("圆角矩形");
				}
			}
			else if (type==G2d.OVAL) {
				if (isFill) {
					instance.setState("填充椭圆");
				}
				else {
					instance.setState("椭圆");
				}
			}
		}
		instance.statePanel.setNowType(type,isFill);
	}
	
	/**
     * 用来初始化变量
     */
	public void initParam() {
		nowColor=Color.BLACK;
		forColor=Color.BLACK;
		bacColor=Color.WHITE;
		midColor=Color.WHITE;
		//nowType=-1;		//MOVE
		nowGraphics=-1;
		nowObject=-1;
		nowChangeGraphics=-1;
		nowRotate=-1;
		
		isDrawing=false;
		isChanging=false;
		isSquare=false;
		gradientPaint=0;
		xOffset=0;
		yOffset=0;
		stroke=1.0f;
		
		changeSize=0;
		
		graphics=new LinkedList<G2d>();
		undoGraphics=new LinkedList<G2d>();
		if (c!=null)
			c.getGraphics().clearRect(0,0,width,height);
		
		backColor=Color.WHITE;
		instance.setState("准备就绪");
		
		instance.f.setTitle("未命名 - "+TsPaint.TITLE);
		//redraw();
		repaint();
	}
	/**
	 * 重画，调用 c.paint(Graphics)
	 */
	public void redraw() {
		if (c==null)
			return;
		
		c.paint(c.getGraphics());
		//c.repaint();
	}
	

	/**
     * 保存文件
     * @param showDialog 是否显示选择文件对话框
     */
	public void saveFile(boolean showDialog){
		if (fileName==null || showDialog) {
			JFileChooser jfc=new JFileChooser();
			jfc.setDialogTitle("保存文件");
			jfc.setDialogType(JFileChooser.SAVE_DIALOG);
			jfc.setFileFilter(new myFileFilter());
			jfc.showSaveDialog(instance.f);
			File fileName1=jfc.getSelectedFile();
			if (fileName1==null)
				return;
			String s=fileName1.getName();
			if (s.indexOf(".")<0) {
				fileName=new File(fileName1.getAbsolutePath()+".tsp");
			}
		}

		fileName.canWrite();
		
		if (fileName==null || "".equals(fileName.getName()))
			instance.setState("无效文件名");
		else{
			try {
				fileName.delete();
				FileOutputStream fos=new FileOutputStream(fileName);
				ObjectOutputStream output=new ObjectOutputStream(fos);
				output.writeInt(width);		//画板宽
				output.writeInt(height);	//画板高
				output.writeInt(graphics.size()-1);
				output.writeObject(backColor);
				//writeObject(backColor);
				//System.out.println (String.valueOf(width)+" * "+String.valueOf(height));
				//System.out.println (graphics.size()-1);
				
				for(int i=0;i<graphics.size(); i++){
					G2d p=(G2d)(graphics.get(i));
					//System.out.println (p.toString());
					G2dOperator.writeObject(output,p);
					output.flush();    //清除缓存，完全写入文件
				}
				output.close();
				fos.close();
				
				instance.f.setTitle(fileName.getName()+" - "+TsPaint.TITLE);
			}
			catch(IOException ioe){
				instance.setState(ioe.toString());
			}
		}
	}
	/**
     * 打開文件
     * @param strFile 当 <code>strFile</code> 为 <font color=blue><code>null</code></font> 时显示打開文件对话框，
     *                否则直接打開 <code>strFile</code> 指定文件
     */
	public void openFile(String strFile){
		if (strFile==null || "".equals(strFile)) {
			JFileChooser jfc=new JFileChooser();
			jfc.setDialogTitle("打开文件");
			jfc.setDialogType(JFileChooser.OPEN_DIALOG);
			jfc.setFileFilter(new myFileFilter());
			jfc.showOpenDialog(instance.f);
			File fileName1=jfc.getSelectedFile();
			if (fileName1==null)
				return;
			fileName=fileName1;
		}
		else {
			try {
				File fileName1=new File(strFile);
				fileName=fileName1;
			}
			catch (Exception e) {
				instance.setState(e.toString());
				return;
			}
		}
		fileName.canRead();
		if (fileName==null || "".equals(fileName.getName()))
			instance.setState("无效文件名");
		else {
			try {
				FileInputStream fis=new FileInputStream(fileName);
				ObjectInputStream input=new ObjectInputStream(fis);
				initParam();
				width=input.readInt();		//获取宽高
				height=input.readInt();
				int counts=input.readInt();
				backColor=(Color)input.readObject();
				//backColor=(Color)input.readObject();
				//System.out.println (String.valueOf(width)+" * "+String.valueOf(height));
				//System.out.println (counts);
				
				for(int i=0; i<=counts ;i++) {
					G2d gh=G2dOperator.readObject(input);
					graphics.add(gh);
					//System.out.println (graphics.get(i).toString());
				}
				input.close();
				c.setSize(width,height);
				c.setBackground(backColor);
				repaint();

				instance.f.setTitle(fileName.getName()+" - "+TsPaint.TITLE);
			}
			catch(EOFException eofException){
				instance.setState("文件结束, 不能创建文件!");
				System.out.println (eofException.toString());
			} catch (ClassNotFoundException classNotFoundException) {
				instance.setState("readObject时错误: "+classNotFoundException.toString());
				//System.out.println ("readObject时错误: "+classNotFoundException.toString());
			} catch (IOException ioException){
				instance.setState("读文件的时候错误!");
				System.out.println (ioException.getMessage());
			}
		}
	}
}

class myFileFilter extends javax.swing.filechooser.FileFilter {   
	/**
	* 通过这个过滤器过滤可接受类型
	* @Param file - 待判断的文件
	*/
	public boolean accept(File file) {
		if (file.isDirectory()) { // 是目录的话接受
			return true; 
		} 
		String fileName = file.getName(); 
		int periodIndex = fileName.lastIndexOf('.'); 
		
		boolean accepted = false; 
		
		if (periodIndex > 0 && periodIndex < fileName.length() - 1) { 
		String extension = fileName.substring(periodIndex + 1).toLowerCase(); 
		if ("tsp".equals(extension)) // 判断扩展名是不是 ".tsp" 
			accepted = true; 
		} 
		
		return accepted; 
	}
	
	/** 
	* 过滤器的描述
	* @return 返回过滤类型描述
	*/ 
	public String getDescription() {   
		return "珊瑚画板图片 (*.tsp)";
	}
}

class PopMenu extends JPopupMenu {
	JMenuItem mnu_TopMost,mnu_Above,mnu_Below,mnu_Bottom;
	private PaintPanel ptPanel;
	
	PopMenu(Object o) {
		super();
		if (o instanceof PaintPanel) {
			ptPanel=(PaintPanel)o;
		}
		else {
			return;
		}
		mnu_TopMost=new JMenuItem("置于顶层(T)");
		mnu_TopMost.setMnemonic('T');
		mnu_TopMost.setActionCommand("TopMost");
		mnu_Above=new JMenuItem("置于上一层(A)");
		mnu_Above.setMnemonic('A');
		mnu_Above.setActionCommand("Above");
		mnu_Below=new JMenuItem("置于下一层(B)");
		mnu_Below.setMnemonic('B');
		mnu_Below.setActionCommand("Below");
		mnu_Bottom=new JMenuItem("置于底层(M)");
		mnu_Bottom.setMnemonic('M');
		mnu_Bottom.setActionCommand("Bottom");
		add(mnu_TopMost);
		add(mnu_Above);
		add(mnu_Below);
		add(mnu_Bottom);
		Component[] comp=getComponents();
		for (int i=0; i<comp.length; i++) {
			if (comp[i] instanceof JMenuItem) {
				JMenuItem mnu=(JMenuItem)comp[i];
				if (mnu!=null)
					mnu.addActionListener(new PopMenuHandler(ptPanel));
			}
		}
		
	}
	
}
/**
 * 弹出菜单事件处理类
 */
class PopMenuHandler implements ActionListener {
	private PaintPanel ptPanel;
	
	public PopMenuHandler(Object o) {
		if (o instanceof PaintPanel) {
			ptPanel=(PaintPanel)o;
		}
		else {
			return;
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (ptPanel.nowGraphics<0)
			return;
		String s=e.getActionCommand();
		int counts=ptPanel.graphics.size()-1;
		int nowGraphics=ptPanel.nowGraphics;
		LinkedList<G2d> graphics=ptPanel.graphics;
		G2d gh=(G2d)(graphics.get(nowGraphics));
		if ("TopMost".equals(s)) {
			graphics.remove(nowGraphics);
			graphics.add(gh);
			ptPanel.nowGraphics=counts;
		}
		else if ("Above".equals(s)) {
			if (nowGraphics==counts) return;
			graphics.set(nowGraphics,graphics.get(nowGraphics+1));
			graphics.set(nowGraphics+1,gh);
			ptPanel.nowGraphics++;
		}
		else if ("Below".equals(s)) {
			if (nowGraphics==0) return;
			graphics.set(nowGraphics,graphics.get(nowGraphics-1));
			graphics.set(nowGraphics-1,gh);
			ptPanel.nowGraphics--;
		}
		else if ("Bottom".equals(s)) {
			graphics.remove(nowGraphics);
			graphics.addFirst(gh);
			ptPanel.nowGraphics=0;
		}
		ptPanel.repaint();
	}
}

class G2dOperator {
	public static G2d readObject(ObjectInputStream in) throws IOException,ClassNotFoundException {
		int type=in.readInt();
		G2d gh;
		switch (type) {
			case G2d.LINE:
				gh=new ObjLine();
				break;
			case G2d.RECTANGLE:
				gh=new ObjRectangle();
				break;
			case G2d.ROUNDRECT:
				gh=new ObjRoundRect();
				break;
			case G2d.OVAL:
				gh=new ObjOval();
				break;
			case G2d.PENCIL:
				gh=new ObjPolyline();
				break;
			case G2d.POLYGON:
				gh=new ObjPolygon();
				break;
			default:
				gh=new G2d();
				break;
		}
		gh.stroke=in.readFloat();
		gh.color=(Color)in.readObject();
		gh.color2=(Color)in.readObject();
		gh.gradientPaint=in.readInt();
		gh.p1=(Point)in.readObject();
		gh.p2=(Point)in.readObject();
		gh.filled=in.readBoolean();
		gh.isSquare=in.readBoolean();
		
		gh.X=(LinkedList<Integer>)in.readObject();
		gh.Y=(LinkedList<Integer>)in.readObject();
		
		if (gh.gradientPaint>0)
			gh.setGradientPaint(gh.p1,gh.p2);
		return gh;
	}
	public static void writeObject(ObjectOutputStream out, G2d gh) throws IOException {
		out.writeInt(gh.type);
		out.writeFloat(gh.stroke);
		out.writeObject(gh.color);
		out.writeObject(gh.color2);
		out.writeInt(gh.gradientPaint);
		out.writeObject(gh.p1);
		out.writeObject(gh.p2);
		out.writeBoolean(gh.filled);
		out.writeBoolean(gh.isSquare);
		out.writeObject(gh.X);
		out.writeObject(gh.Y);
	}
}
