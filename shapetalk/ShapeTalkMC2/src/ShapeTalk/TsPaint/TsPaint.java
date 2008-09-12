/**
 * @(#)TsPaint.java
 */
package ShapeTalk.TsPaint;

import java.awt.*;
import javax.swing.*;

/**
 * 画图板主窗体架构类，主函数入口<br>
 * <br>
 * 画图板是个矢量图形绘制板，可以绘制基本图形，多边形，折线等二维图形<br>
 * 并且可以改变各个物体的大小及位置，填充方式也有渐变填充和实心填充两种模式<br>
 * <br>
 * 黑色珊瑚 写于 2008年5月17日
 *
 * @author <a href="http://www.cnitblog.com/tsorgy">黑色珊瑚</a>
 * @version 1.00 2008/5/11
 */
public class TsPaint {
	/**
     * 画图板的标题
     */
	public static final String TITLE="珊瑚的画板";
	/**
     * 用于工具栏背景的黑色
     */
	public static final Color GRAYCOLOR=new Color(240,240,240);
	/**
     * 绘制区域默认大小
     */
	public static int Width=700,Height=550;
	/**
     * 主窗口的实例对象
     */
	public static TsPaint myApp;
	
	JFrame f;
	JPanel pnMain;
	ToolbarPanel toolbarPanel;
	ColorPanel colorPanel;

	ToolPanel toolPanel;
	/**
     * 画图板
     */
	PaintPanel paintPanel;
	StatePanel statePanel;
	
	JMenuBar menuBar;
	JMenu menuFile;
	JMenuItem mnuF_New;
	JMenuItem mnuF_Open;
	JMenuItem mnuF_Save;
	JMenuItem mnuF_SaveAs;
	JMenuItem mnuF_Exit;
	
	JMenu menuEdit;
	JMenuItem mnuE_Undo;
	JMenuItem mnuE_Redo;
	JMenuItem mnuE_Clone;
	
	JMenu menuObject;
	JMenuItem[] mnuO_Objs;
	JMenuItem mnuO_DelObject;
	
	JMenu menuConfig;
	JMenuItem mnuC_SetSize;
	JMenuItem mnuC_BackColor;
	JMenuItem mnuC_Stroke;
	
	JMenu menuHelp;
	JMenuItem mnuH_About;

	/**
     * 构造出主窗口，默认大小，窗口置于屏幕中心，设置窗口图标
     */
	public TsPaint() {
		f=new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLayout(new BorderLayout());
		statePanel=new StatePanel();

		paintPanel=new PaintPanel(this);
		//工具按钮
		toolPanel=new ToolPanel(paintPanel);
		toolbarPanel=new ToolbarPanel(toolPanel);
		
		pnMain=new JPanel(new BorderLayout());
		colorPanel=new ColorPanel(this);
		pnMain.add("Center",paintPanel);
		pnMain.add("North",colorPanel);

		//初始化菜单
		initMenus();
		
		//f.setContentPane(p);
		f.add("West",toolPanel);
		f.add("Center",pnMain);
		f.add("South",statePanel);
		f.add("North",toolbarPanel);
		f.setJMenuBar(menuBar);
		//f.pack();
		Dimension screen=Toolkit.getDefaultToolkit().getScreenSize();//得到屏幕的大小
		f.setBounds((int)((screen.getWidth()-Width)/2),(int)((screen.getHeight()-Height)/2),Width,Height);
		ImageIcon imgIcon=new ImageIcon("resources/icons/tspaint.gif");
		f.setIconImage(imgIcon.getImage());
		f.setVisible(true);
	}
	
	/**
     * 用来初始化菜单
     */
	private void initMenus() {
		menuBar=new JMenuBar();
		menuFile=new JMenu("文件(F)");
		menuFile.setMnemonic('F');
		mnuF_New=new JMenuItem("新建(N)");
		mnuF_New.setMnemonic('N');
		mnuF_New.setActionCommand("NewFile");
		mnuF_Open=new JMenuItem("打开(O)",toolbarPanel.imgs[1]);
		mnuF_Open.setMnemonic('O');
		mnuF_Open.setActionCommand("OpenFile");
		mnuF_Save=new JMenuItem("保存(S)",toolbarPanel.imgs[2]);
		mnuF_Save.setMnemonic('S');
		mnuF_Save.setActionCommand("SaveFile");
		mnuF_SaveAs=new JMenuItem("另存为(A)...");
		mnuF_SaveAs.setMnemonic('A');
		mnuF_SaveAs.setActionCommand("SaveAsFile");
		
		mnuF_Exit=new JMenuItem("退出(X)");
		mnuF_Exit.setMnemonic('X');
		mnuF_Exit.setActionCommand("Exit");
		menuFile.add(mnuF_New);
		menuFile.add(mnuF_Open);
		menuFile.add(mnuF_Save);
		menuFile.add(mnuF_SaveAs);
		menuFile.addSeparator();
		menuFile.add(mnuF_Exit);

		menuEdit=new JMenu("编辑(E)");
		menuEdit.setMnemonic('E');
		mnuE_Undo=new JMenuItem("撤销(U)",toolbarPanel.imgs[3]);
		mnuE_Undo.setMnemonic('U');
		mnuE_Undo.setEnabled(false);
		mnuE_Undo.setActionCommand("Undo");
		mnuE_Redo=new JMenuItem("重做(R)",toolbarPanel.imgs[4]);
		mnuE_Redo.setMnemonic('R');
		mnuE_Redo.setEnabled(false);
		mnuE_Redo.setActionCommand("Redo");
		mnuE_Clone=new JMenuItem("克隆物体(C)",toolbarPanel.imgs[5]);
		mnuE_Clone.setMnemonic('C');
		mnuE_Clone.setEnabled(false);
		mnuE_Clone.setActionCommand("Clone");
		menuEdit.add(mnuE_Undo);
		menuEdit.add(mnuE_Redo);
		menuEdit.addSeparator();
		menuEdit.add(mnuE_Clone);

		//{"move","line","rect","roundrect","oval"}
		menuObject=new JMenu("对象(O)");
		menuObject.setMnemonic('O');
		mnuO_Objs=new JMenuItem[toolPanel.sCmds.length];
		for (int i=0; i<toolPanel.sCmds.length; i++) {
			mnuO_Objs[i]=new JMenuItem(toolPanel.sTexts[i],toolPanel.imgIcons[i]);
			mnuO_Objs[i].setActionCommand(toolPanel.sCmds[i]);
			menuObject.add(mnuO_Objs[i]);
		}
		mnuO_DelObject=new JMenuItem("删除物体(D)",toolbarPanel.imgs[6]);
		mnuO_DelObject.setMnemonic('D');
		mnuO_DelObject.setEnabled(false);
		mnuO_DelObject.setActionCommand("DelObject");
		menuObject.addSeparator();
		menuObject.add(mnuO_DelObject);

		menuConfig=new JMenu("设置(C)");
		menuConfig.setMnemonic('C');
		mnuC_SetSize=new JMenuItem("画布大小(C)",toolbarPanel.imgs[7]);
		mnuC_SetSize.setMnemonic('C');
		mnuC_SetSize.setActionCommand("SetCanvas");
		mnuC_BackColor=new JMenuItem("背景色(B)",toolbarPanel.imgs[8]);
		mnuC_BackColor.setMnemonic('B');
		mnuC_BackColor.setActionCommand("SetBackColor");
		mnuC_Stroke=new JMenuItem("画笔粗细(S)",toolbarPanel.imgs[9]);
		mnuC_Stroke.setMnemonic('S');
		mnuC_Stroke.setActionCommand("SetStroke");
		menuConfig.add(mnuC_SetSize);
		menuConfig.add(mnuC_BackColor);
		menuConfig.add(mnuC_Stroke);

		menuHelp=new JMenu("帮助(H)");
		menuHelp.setMnemonic('H');
		mnuH_About=new JMenuItem("关于(A)",toolbarPanel.imgs[10]);
		mnuH_About.setMnemonic('A');
		mnuH_About.setActionCommand("AboutMe");
		menuHelp.add(mnuH_About);

		menuBar.add(menuFile);
		menuBar.add(menuEdit);
		menuBar.add(menuObject);
		menuBar.add(menuConfig);
		menuBar.add(menuHelp);

		for (int j=0; j<menuBar.getMenuCount(); j++) {
			JMenu menu = menuBar.getMenu(j);
			for (int i=0; i<menu.getItemCount(); i++) {
				JMenuItem mnu=menu.getItem(i);
				if (mnu!=null)
					mnu.addActionListener(toolPanel);
			}
		}
	}
	
	/**
     * 设置状態栏文字
     * @param s 待设置的字符串
     */
	public void setState(String s) {
		statePanel.setState(s);
	}
	
	/**
     * 主函数入口，可以带一个参数，为要打开的图像文件名
     */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName());
		}
		catch ( Exception e ) {
			System.out.println ("无法将界面设置为windows风格");
		}
		myApp=new TsPaint();
		if (args.length>0) {
			myApp.paintPanel.openFile(args[0]);
		}
	}
}

class StatePanel extends JPanel {
	JLabel lblState;
	JLabel lblType;
	
	public void setState(String s) {
		lblState.setText(" "+s);
	}
	public void setNowType(int type,boolean fill) {
		String s="当前状态: ";
		if (type==G2d.PENCIL) {
			s+="铅笔";
		}
		else if (type==G2d.LINE) {
			s+="直线";
		}
		else if (type==G2d.RECTANGLE) {
			s+="矩形";
		}
		else if (type==G2d.ROUNDRECT) {
			s+="圆角矩形";
		}
		else if (type==G2d.OVAL) {
			s+="椭圆";
		}
		else {
			s+="移动";
		}
		s+=" ";
		if (fill && type>=0) {
			s+="填充 ";
		}
		lblType.setText(s);
	}

	public StatePanel() {
		setLayout(new BorderLayout());
		lblState=new JLabel();
		lblState.setPreferredSize(new Dimension(450,23));
		lblType=new JLabel();
		//lblState.setAlignment(Label.LEFT);
		add("West",lblState);
		add("Center",lblType);
		setBackground(TsPaint.GRAYCOLOR);
	}
}

class ToolbarPanel extends JToolBar {
	
	String[] sbtn={"NewFile","OpenFile","SaveFile","Undo","Redo","Clone","DelObject","SetCanvas","SetBackColor","SetStroke","AboutMe"};
	String[] sTexts={"新建图片","打开图片","保存图片","撤销","重做","克隆物体","删除物体","设置画布大小","设置背景色","设置画笔粗细","关于 "+TsPaint.TITLE};
	String[] sFiles={"new","open","save","undo","redo","clone","delobject","resize","backcolor","stroke","about"};
	JButton[] jbtn=new JButton[sbtn.length];
	public ImageIcon[] imgs=new ImageIcon[sbtn.length];
	
	public ToolbarPanel(Object instance) {
		if (instance instanceof ToolPanel) {
			for (int i=0; i<jbtn.length; i++) {
				imgs[i]=new ImageIcon("resources/icons/"+sFiles[i]+".gif");
				jbtn[i]=new JButton("",imgs[i]);
				jbtn[i].setActionCommand(sbtn[i]);
				jbtn[i].setFocusable(false);
				jbtn[i].setToolTipText(sTexts[i]);
				jbtn[i].setPreferredSize(new Dimension(25,25));
				jbtn[i].addActionListener((ToolPanel)instance);
				add(jbtn[i]);
				if (i==2||i==5||i==6||i==9) {
					addSeparator();
				}
				
				if (i==3||i==4||i==5||i==6) {
					jbtn[i].setEnabled(false);
				}
			}
		}
		//setFloatable(false);
		//setPreferredSize(new Dimension(jbtn.length*25+50,30));
	}
}
