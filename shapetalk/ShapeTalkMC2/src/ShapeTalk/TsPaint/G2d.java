/**
 * @(#)G2d.java
 */
package ShapeTalk.TsPaint;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

/**
 * <code>G2d</code> 是画图板中每个物体对象的父类<br>
 * 它是用来存储每个二维物体的
 *
 * @author <a href="http://www.cnitblog.com/tsorgy">黑色珊瑚</a>
 * @version 1.00 2008/5/11
 */
public class G2d {
	/**
     * 铅笔，或折线
     */
	public static final int PENCIL=0;
	/**
     * 直线
     */
	public static final int LINE=1;
	/**
     * 矩形
     */
	public static final int RECTANGLE=2;
	/**
     * 圆角矩形
     */
	public static final int ROUNDRECT=3;
	/**
     * 椭圆
     */
	public static final int OVAL=4;
	/**
     * 多边形
     */
	public static final int POLYGON=5;	/* -ing */

	/**
     * 二维图形的种类
     * @see #PENCIL
     * @see #LINE
     * @see #RECTANGLE
     * @see #ROUNDRECT
     * @see #OVAL
     * @see #POLYGON
     */
	public int type;
	/**
     * 非填充图形的画笔的粗细
     */
	public float stroke;
	/**
     * 画笔颜色
     */
	public Color color;
	/**
     * 第二填充色（当填充类型为渐变时）
     * @see #gradientPaint
     * @see #setGradientPaint(Point,Point)
     */
	public Color color2;
	/**
     * 渐变方式: 0-不渐变, 1-水平渐变, 2-垂直渐变, 3-对角渐变
     * @see #setGradientPaint(Point,Point)
     */
	public int gradientPaint;
	/**
     * 渐变的两个端点
     * @see #setGradientPaint(Point,Point)
     */
	public Point p1,p2;
	/**
     * 用来实现填充的 <code>Paint</code> 接口
     * @see #setGradientPaint(Point,Point)
     */
	protected Paint ptInterface;
	/**
     * 二维图形
     * @see #draw(Graphics2D)
     */
	protected Shape shape;
	/**
     * 是否填充的布尔值
     */
	public boolean filled;
	/**
     * 是否是正二维图形的布尔值
     */
	public boolean isSquare;
	/**
     * 保存每个点坐标的范型化 <code>LinkedList</code>
     * @see java.util.LinkedList
     */
	protected LinkedList<Integer> X,Y;
	
	/**
     * 构造二维图形，每个子类有不同构造方法
     */
	public G2d() {
		X=new LinkedList<Integer>();
		Y=new LinkedList<Integer>();
		stroke=1.0f;
		color=Color.BLACK;
		color2=Color.WHITE;
		gradientPaint=0;
		filled=false;
		isSquare=false;
		type=99;
	}
	
	/**
     * 判断当前鼠标坐标是否在二维图形范围之内
     *
     * @param x 当前鼠标横坐标
     * @param y 当前鼠标竖坐标
     * @return 返回 <code>true</code> 当鼠标在图形范围之内<br>
     *         返回 <code>false</code> 当鼠标不再范围内
     */
	public boolean isCursorIn(int x,int y) {
		try {
			if (shape.contains(x,y))
				return true;
			else
				return false;
		} catch (NullPointerException npe) {	//shape不存在
			return false;
		}
	}
	/**
     * 判断当前鼠标坐标是否在可以使二维图形旋转的范围之内
     *
     * @param x 当前鼠标横坐标
     * @param y 当前鼠标竖坐标
     * @return 返回 <code>true</code> 当鼠标在这个范围之内<br>
     *         返回 <code>false</code> 当鼠标不再范围内
     */
	public boolean canRotate(int x,int y) {
		int buff=(int)(stroke/2);
		int x1=(getMinX()+getMaxX())/2-2-buff;
		int y1=getMinY()-20-buff;
		if ((x>x1) && (y>y1) && (x<x1+6) && (y<y1+6))
			return true;
		else
			return false;
	}
	/**
     * 判断当前鼠标坐标是否在可以改变二维图形大小的范围之内
     *
     * @param x 当前鼠标横坐标
     * @param y 当前鼠标竖坐标
     * @return 返回 <code>true</code> 当鼠标在这个范围之内<br>
     *         返回 <code>false</code> 当鼠标不再范围内
     */
	public boolean canResizeObject(int x,int y) {
		int buff=(int)(3/2);
		//int x2=getMaxX()+buff;
		//int y2=getMaxY()+buff;
		int x2=getPointX(1);
		int y2=getPointY(1);
		if ((x>x2-4) && (y>y2-4) && (x<x2+4) && (y<y2+4))
			return true;
		else
			return false;
	}
	/**
     * 产生二维图形并绘制
     * @param g 用来呈现 <code>G2d</code> 的图形控制
     */
	public void draw(Graphics2D g) {}
	/**
     * 只绘制二维图形末尾的线
     * @param g 用来呈现 <code>G2d</code> 的图形控制
     */
	public void drawLastLine(Graphics2D g) {}
	/**
     * 向范型化的 <code>LinkedList</code> {@link #X}，{@link #Y} 里添加坐标点
     * @param x 坐标点的 <code>x</code> 值
     *        y 坐标点的 <code>y</code> 值
     */
	public void addPoint(int x,int y) {}
	/**
     * 设置范型化的 <code>LinkedList</code> {@link #X}，{@link #Y} 中的坐标点
     * @param i 坐标点的索引
     *        x 新坐标点的 <code>x</code> 值
     *        y 新坐标点的 <code>y</code> 值
     */
	public void setPoint(int i,int x,int y) {
		X.set(i,x);
		Y.set(i,y);
	}
	/**
     * 取得指定索引的坐标点的 {@link #X} 值
     * @param i 坐标点的索引
     * @return 坐标点的 {@link #X} 值
     */
	public int getPointX(int i) {
		return (int)(X.get(i));
	}
	/**
     * 取得指定索引的坐标点的 {@link #Y} 值
     * @param i 坐标点的索引
     * @return 坐标点的 {@link #Y} 值
     */
	public int getPointY(int i) {
		return (int)(Y.get(i));
	}
	/**
     * 获得坐标点个数，等于0表示无坐标点
     * @return 坐标点个数
     */
	public int getLength() {
		return X.size();
	}
	/**
     * 获得二维图形的宽度
     * @return 图形宽度
     */
	public int getWidth() {
		return Math.abs(getPointX(0)-getPointX(1));
	}
	/**
     * 获得二维图形的高度
     * @return 图形高度
     */
	public int getHeight() {
		return Math.abs(getPointY(0)-getPointY(1));
	}
	/**
     * 获得二维图形极小坐标点的 {@link #X} 值
     */
	public int getMinX() {
		return Math.min(getPointX(0),getPointX(1));
	}
	/**
     * 获得二维图形极小坐标点的 {@link #Y} 值
     */
	public int getMinY() {
		return Math.min(getPointY(0),getPointY(1));
	}
	/**
     * 获得二维图形极大坐标点的 {@link #X} 值
     */
	public int getMaxX() {
		return Math.max(getPointX(0),getPointX(1));
	}
	/**
     * 获得二维图形极大坐标点的 {@link #Y} 值
     */
	public int getMaxY() {
		return Math.max(getPointY(0),getPointY(1));
	}
	/**
     * 返回描述此二维图形的字符串
     */
	public String toString() {
		return "未知二维图形";
	}
	/**
     * 设置渐变填充样式
     * @param p1 起始渐变点
     *        p2 结束渐变点
     */
	public void setGradientPaint(Point p1,Point p2) {
		try {
			this.p1=p1;
			this.p2=p2;
			ptInterface=new GradientPaint(p1,color,p2,color2);
		} catch (Exception e) {	//有的图形不可填充但gradientPaint属性被修改，则异常
			System.out.println ("==============================");
			System.out.println ("setGradientPaint(Point,Point)方法中出现异常: ");
			System.out.println ("p1="+p1.toString()+", p2="+p2.toString());
			System.out.println ("物体: "+toString()+" 在实现Paint接口时异常，可能为非法修改gradientPaint值造成的。");
			System.out.println ("错误信息: "+e.toString());
			System.out.println ("==============================");
		}
	}
	/**
     * 改变二维图形大小
     * @param x 当前鼠标拖动坐标点的 {@link #X} 值
     *        y 当前鼠标拖动坐标点的 {@link #Y} 值
     */
	public void changeSize(int x,int y) {
		//判断二维图形
		if (isSquare) {
			int w=x-getPointX(0);
			int h=y-getPointY(0);
			int s=Math.min(Math.abs(w),Math.abs(h));
			if (w>0 && h>0)
				addPoint(getPointX(0)+s,getPointY(0)+s);
			else if (w>0 && h<0)
				addPoint(getPointX(0)+s,getPointY(0)-s);
			else if (w<0 && h>0)
				addPoint(getPointX(0)-s,getPointY(0)+s);
			else if (w<0 && h<0)
				addPoint(getPointX(0)-s,getPointY(0)-s);
				
		} else {
			addPoint(x,y);
		}
	}
	/**
     * 移动二维图形位置
     * @param xBuff 坐标点 {@link #X} 的偏移量
     *        yBuff 坐标点 {@link #Y} 的偏移量
     */
	public void moveObject(int xBuff,int yBuff) {
		for (int i=getLength()-1; i>=0; i--) {
			setPoint(i,getPointX(i)+xBuff,getPointY(i)+yBuff);
		}
		if (gradientPaint>0) {	//渐变填充处理
			try {
				p1=new Point(p1.x+xBuff,p1.y+yBuff);
				p2=new Point(p2.x+xBuff,p2.y+yBuff);
				setGradientPaint(p1,p2);
			} catch (Exception e) {		//没有两个点
				System.out.println ("==============================");
				System.out.println ("moveObject(int,int)方法出现异常:");
				System.out.println ("xBuff="+String.valueOf(xBuff)+", yBuff="+String.valueOf(yBuff));
				System.out.println ("物体: "+toString());
				System.out.println ("错误信息: "+e.toString());
				System.out.println ("==============================");
				//System.out.println (e);
			}
		}
	}
	/**
     * 绘制选中图形的焦点
     * @param g 用来呈现 <code>G2d</code> 的图形控制
     *        backColor 当前背景颜色，<code>setXORMode</code> 绘制用
     */
	public void drawMoveFocus(Graphics2D g,Color backColor) {
		int x1=getMinX();
		int y1=getMinY();
		int x2=getMaxX();
		int y2=getMaxY();
		int buff=0;
		if (!filled) {
			buff=(int)(stroke/2)+1;
		}
		g.setStroke(new BasicStroke(1.0f));
		g.setXORMode(backColor);
		g.drawRect(x1-6-buff,y1-6-buff,5,5);
		g.drawRect(x1-6-buff,y2+buff,5,5);
		g.drawRect(x2+buff,y1-6-buff,5,5);
		g.drawRect(x2+buff,y2+buff,5,5);
		
		g.drawRect((x1+x2)/2-2-buff,y1-6-buff,5,5);
		g.drawRect(x1-6-buff,(y1+y2)/2-2-buff,5,5);
		g.drawRect((x1+x2)/2-2-buff,y2+buff,5,5);
		g.drawRect(x2+buff,(y1+y2)/2-2-buff,5,5);

		if (isChangablePoint(x1,y1))
			g.fillRect(x1-5-buff,y1-5-buff,5,5);
		else if (isChangablePoint(x1,y2))
			g.fillRect(x1-5-buff,y2+buff,5,5);
		else if (isChangablePoint(x2,y1))
			g.fillRect(x2+buff,y1-5-buff,5,5);
		else if (isChangablePoint(x2,y2))
			g.fillRect(x2+buff,y2+buff,5,5);
		
		/* 绘制旋转点 *
		g.drawLine((x1+x2)/2-buff,y1-14-buff,(x1+x2)/2-buff,y1-7-buff);
		g.setColor(new Color(0x22,0xB1,0x4C));
		g.fillRect((x1+x2)/2-2-buff,y1-20-buff,6,6);
		/* */
		
		g.setXORMode(backColor);
	}

	/**
     * 判断坐标点是否是可移动点
     * @param x 坐标点的 {@link #X} 值
     *        y 坐标点的 {@link #Y} 值
     */
	protected boolean isChangablePoint(int x,int y) {
		if (x==getPointX(1) && y==getPointY(1))
			return true;
		else
			return false;
	}
	/**
     * 具体做绘制动作
     * @param g2d 用来呈现 <code>G2d</code> 的图形控制
     *        shape 要绘制的图形，参考 {@link #draw(Graphics2D)}
     */
	public void doDraw(Graphics2D g2d,Shape shape) {
		if (filled) {
			if (gradientPaint>0) {
				g2d.setTransform(new AffineTransform());
				g2d.setPaint(ptInterface);
			} else {
				g2d.setPaint(color);
			}
			g2d.fill(shape);
		} else {
			g2d.setPaint(color);
			g2d.setStroke(new BasicStroke(stroke));
			g2d.draw(shape);
		}
	}
}

//直线类
class ObjLine extends G2d {
	public ObjLine() {
		super();
		type=G2d.LINE;
	}
	public String toString() {
		String s="直线("+String.valueOf(getPointX(0))+","+String.valueOf(getPointY(0))+")";
		s+="-("+String.valueOf(getPointX(1))+","+String.valueOf(getPointY(1))+")";
		return s;
	}
	public void addPoint(int x,int y) {
		if (X.size()==0) {
			X.add(x);
			Y.add(y);
		} else {
			if (X.size()==1) {
				X.add(x);
				Y.add(y);
			} else {
				X.set(1,x);
				Y.set(1,y);
			}
		}
	}
	public void changeSize(int x,int y) {
		addPoint(x,y);
	}
	public void draw(Graphics2D g2d) {
		shape=new Line2D.Float(getPointX(0),getPointY(0),getPointX(1),getPointY(1));
		g2d.setPaint(color);
		g2d.setStroke(new BasicStroke(stroke,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL));
		//g2d.drawLine(getPointX(0),getPointY(0),getPointX(1),getPointY(1));
		g2d.draw(shape);
	}
}

//矩形类
class ObjRectangle extends G2d {
	public ObjRectangle() {
		super();
		type=G2d.RECTANGLE;
	}
	public String toString() {
		String s="矩形("+String.valueOf(getMinX())+","+String.valueOf(getMinY())+")";
		s+=" 宽: "+String.valueOf(getWidth())+", 高: "+String.valueOf(getHeight());
		if (filled)
			s+=", 填充";
		return s;
	}
	public void addPoint(int x,int y) {
		if (gradientPaint>0) {
			if (p1==null) {
				p1=new Point(x,y);
				p2=new Point(x,y);
			} else {
				if (X.size()==2) {
					int xB=getPointX(1);
					int yB=getPointY(1);
					if (gradientPaint==1) {
						p2.x+=x-xB;
					} else if (gradientPaint==2) {
						p2.y+=y-yB;
					} else if (gradientPaint==3) {
						p2.x+=x-xB;
						p2.y+=y-yB;
					}
				}
			}
			setGradientPaint(p1,p2);
		}
		if (X.size()==0) {
			X.add(x);
			Y.add(y);
		} else {
			if (X.size()==1) {
				X.add(x);
				Y.add(y);
			} else {
				X.set(1,x);
				Y.set(1,y);
			}
		}
	}
	public void draw(Graphics2D g2d) {
		shape=new Rectangle2D.Float(getMinX(),getMinY(),getWidth(),getHeight());
		doDraw(g2d,shape);
	}
}

//椭圆类
class ObjOval extends ObjRectangle {
	public ObjOval() {
		super();
		type=G2d.OVAL;
	}
	public String toString() {
		String s="椭圆("+String.valueOf(getMinX())+","+String.valueOf(getMinY())+")";
		s+=" 宽: "+String.valueOf(getWidth())+", 高: "+String.valueOf(getHeight());
		if (filled)
			s+=", 填充";
		return s;
	}
	public void draw(Graphics2D g2d) {
		shape=new Ellipse2D.Float(getMinX(),getMinY(),getWidth(),getHeight());
		doDraw(g2d,shape);
	}
}

//圆角矩形类
class ObjRoundRect extends ObjRectangle {
	int arcWidth=50, arcHeight=35;
	public ObjRoundRect() {
		super();
		type=G2d.ROUNDRECT;
	}
	public String toString() {
		String s="圆角矩形("+String.valueOf(getMinX())+","+String.valueOf(getMinY())+")";
		s+=" 宽: "+String.valueOf(getWidth())+", 高: "+String.valueOf(getHeight());
		if (filled)
			s+=", 填充";
		return s;
	}
	public void draw(Graphics2D g2d) {
		shape=new RoundRectangle2D.Float(getMinX(),getMinY(),getWidth(),getHeight(),arcWidth,arcHeight);
		doDraw(g2d,shape);
	}
}

//折线类
class ObjPolyline extends G2d {
	private int x1,y1,x2,y2;
	public ObjPolyline() {
		super();
		type=G2d.PENCIL;
	}
	public String toString() {
		return "折线 "+String.valueOf(getLength())+" 个点";
	}
	public void addPoint(int x,int y) {
		X.add(x);
		Y.add(y);
	}
	public void moveObject(int xBuff,int yBuff) {
		for (int i=getLength()-1; i>=0; i--) {
			setPoint(i,getPointX(i)+xBuff,getPointY(i)+yBuff);
		}
	}
	public void draw(Graphics2D g2d) {
		g2d.setPaint(color);
		g2d.setStroke(new BasicStroke(stroke,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL));
		
		/* 用 drawPolyline 画 */
		int len=getLength();
		int[] x=new int[len];
		int[] y=new int[len];
		for (int i=0; i<len; i++) {
			x[i]=(Integer)(X.get(i));
			y[i]=(Integer)(Y.get(i));
		}
		g2d.drawPolyline(x,y,len);
//		shape=new Polygon(x,y,len);		//封闭折线
//		g2d.draw(shape);
		/* */
		
		/* 循环画线 *
		for (int i=0; i<getLength()-1; i++) {
			int x1=getPointX(i);
			int y1=getPointY(i);
			int x2=getPointX(i+1);
			int y2=getPointY(i+1);
			g2d.drawLine(x1,y1,x2,y2);
		}
		/* */
		getXYs();
	}
	public void changeSize(int x,int y) {
		addPoint(x,y);
	}
	public void drawLastLine(Graphics2D g2d) {
		g2d.setPaint(color);
		g2d.setStroke(new BasicStroke(stroke,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL));
		int i=getLength()-1;
		g2d.drawLine(getPointX(i-1),getPointY(i-1),getPointX(i),getPointY(i));
	}
	public int getMinX() {
		return x1;
	}
	public int getMinY() {
		return y1;
	}
	public int getMaxX() {
		return x2;
	}
	public int getMaxY() {
		return y2;
	}
	public boolean isCursorIn(int x,int y) {
		getXYs();
		if (x>=x1 && x<=x2 && y>=y1 && y<=y2)
			return true;
		else
			return false;
	}
	public void setGradientPaint(Point x,Point y) {
		return;
	}
	public boolean canResizeObject(int x,int y) {
		return false;
	}
	private void getXYs() {
		x1=getPointX(0);
		y1=getPointY(0);
		x2=x1;
		y2=y1;
		for (int i=0; i<getLength(); i++) {
			int xx=getPointX(i);
			int yy=getPointY(i);
			if (xx<x1)
				x1=xx;
			if (yy<y1)
				y1=yy;
			if (xx>x2)
				x2=xx;
			if (yy>y2)
				y2=yy;
		}
	}
}

//星星类
class ObjPolygon extends G2d {
	private int x1,y1,x2,y2;
	private int xBase,yBase;
	private int radians;
	private int[] size={0,0};
	private double increase;
	public ObjPolygon() {
		this(5);
	}
	public ObjPolygon(int rad) {
		super();
		radians=rad;
		increase=Math.toRadians(360.0/(rad*2));
		for (int i=0; i<radians*2; i++) {
			X.add(0);
			Y.add(0);
		}
		type=G2d.POLYGON;
	}
	public String toString() {
		return "星星 "+String.valueOf(getLength()/2)+" 个角";
	}
	public void addPoint(int x,int y) {
		xBase=x;
		yBase=y;
	}
	public void moveObject(int xBuff,int yBuff) {
		super.moveObject(xBuff,yBuff);
		xBase+=xBuff;
		yBase+=yBuff;
	}
	public void draw(Graphics2D g2d) {
		/* 用 drawPolyline 画 */
		int len=getLength();
		int[] x=new int[len];
		int[] y=new int[len];
		for (int i=0; i<len; i++) {
			x[i]=(Integer)(X.get(i));
			y[i]=(Integer)(Y.get(i));
		}
//		g2d.drawPolyline(x,y,len);
		shape=new Polygon(x,y,len);
		/* */

		doDraw(g2d,shape);
		getXYs();
	}
	public void changeSize(int x,int y) {
		size[1]=Math.max(Math.abs(x-xBase),Math.abs(y-yBase));
		size[0]=size[1]/2;
		double rad=0.0;
		for (int i=0; i<radians*2; i++) {
			X.set(i,(int)(size[i%2]*Math.cos(rad))+xBase);
			Y.set(i,(int)(size[i%2]*Math.sin(rad))+yBase);
			rad+=increase;
		}
		getXYs();
		if (gradientPaint>0) {
			setGradientPaint(new Point(getMinX(),getMinY()),new Point(getMaxX(),getMaxY()));
		}
	}
	public int getMinX() {
		return x1;
	}
	public int getMinY() {
		return y1;
	}
	public int getMaxX() {
		return x2;
	}
	public int getMaxY() {
		return y2;
	}
	public boolean canResizeObject(int x,int y) {
		int x2=getMaxX();
		int y2=getMaxY();
		if ((x>x2-4) && (y>y2-4) && (x<x2+4) && (y<y2+4))
			return true;
		else
			return false;
	}
	private void getXYs() {
		x1=getPointX(0);
		y1=getPointY(0);
		x2=x1;
		y2=y1;
		for (int i=0; i<getLength(); i++) {
			int xx=getPointX(i);
			int yy=getPointY(i);
			if (xx<x1)
				x1=xx;
			if (yy<y1)
				y1=yy;
			if (xx>x2)
				x2=xx;
			if (yy>y2)
				y2=yy;
		}
	}
	protected boolean isChangablePoint(int x,int y) {
		if (x==getMaxX() && y==getMaxY())
			return true;
		else
			return false;
	}
}
