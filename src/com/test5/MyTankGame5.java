/**
 * 功能：坦克游戏2.0
 * 1.画出坦克
 * 2.完成我的坦克可以上下左右移动
 * 3.可以连发子弹,最多5颗
 * 4.当我的子弹击中敌人坦克时，敌人坦克消失（爆炸）
 * 5.敌人子弹击中自己，自己坦克消失
 * 6.防止敌人坦克重叠
 *  6.1把判断写到EnemyTank类中
 * 7.可以分关
 *  7.1做一个开始的panel，它是一个空的，用于提示
 *  7.2闪烁效果
 * 8.可以暂停和继续
 *  8.1点击暂停时，把子弹的速度和坦克的速度射为0，并让坦克的方向不要变化
 * 9.可以记录玩家的成绩
 *   9.1用文件流的方式
 *   9.2单写一个记录类
 *   9.3线完成保存共击毁了多少敌人坦克
 *   9.4存盘退出，可以记录退出时敌人坦克坐标，并可以退出
 * 10.出现声音
 *   10.1
 * 
 */
package com.test5;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class MyTankGame5 extends JFrame implements ActionListener{

	MyPanel mp=null;
	//定义开始面板
	MyStartPanel msp=null;
	//做出需要的菜单
	JMenuBar jmb=null;
	//开始游戏
	JMenu jm1=null;
	JMenuItem jmi1=null;
	JMenuItem jmi2=null;
	JMenuItem jmi3=null;
	JMenuItem jmi4=null;
	
	public MyTankGame5() {
		// TODO 自动生成的构造函数存根
		//mp=new MyPanel();
		//启动mp线程
		//Thread t=new Thread(mp);
		//t.start();
		
		//this.add(mp);
		//注册监听
		//this.addKeyListener(mp);
		//创建菜单及选项
		jmb=new JMenuBar();
		jm1=new JMenu("游戏(G)");
		jm1.setMnemonic('G');
		jmi1=new JMenuItem("开始新游戏(N)");
		jmi1.addActionListener(this);
		jmi1.setActionCommand("newgame");
		jmi1.setMnemonic('N');
		
		jmi2=new JMenuItem("退出游戏(E)");
		jmi2.setMnemonic('E');
		jmi2.addActionListener(this);
		jmi2.setActionCommand("exit");
		
		jmi3=new JMenuItem("存盘退出游戏(C)");
		jmi3.setMnemonic('C');
		jmi3.addActionListener(this);
		jmi3.setActionCommand("saveExit");
		
		jmi4=new JMenuItem("续上局游戏(R)");
		jmi4.setMnemonic('R');
		jmi4.addActionListener(this);
		jmi4.setActionCommand("conGame");
		jm1.add(jmi1);
		jm1.add(jmi2);
		jm1.add(jmi3);
		jm1.add(jmi4);
		jmb.add(jm1);
		msp=new MyStartPanel();
		Thread t=new Thread(msp);
		t.start();

		this.setJMenuBar(jmb);
		
		this.add(msp);
		this.setSize(600,500);
		this.setVisible(true);
			
	}
	

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		MyTankGame5 mt=new MyTankGame5();

	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自动生成的方法存根
		if(e.getActionCommand()=="newgame")
		{
			mp=new MyPanel("newGame");
			//启动mp线程
			Thread t=new Thread(mp);
			t.start();
			//先删除旧的面板
			this.remove(msp);
			this.add(mp);
			//注册监听
			this.addKeyListener(mp);
			this.setVisible(true);
		}else if(e.getActionCommand()=="exit")
		{
			//保存敌人数量
			Recorder.keepRecording();
			System.exit(0);
		}else if(e.getActionCommand()=="saveExit")
		{
			//存盘工作,保存击毁敌人数量，剩下坦克坐标
			Recorder rd=new Recorder();
			rd.setEts(mp.ets);
			rd.keepRecAndEnemy();
		    
			//退出
			System.exit(0);
		}else if(e.getActionCommand().equals("conGame"))
		{
			//mp.flag="con";
			
			mp=new MyPanel("con");
		
			//启动mp线程
			Thread t=new Thread(mp);
			t.start();
			//先删除旧的面板
			this.remove(msp);
			this.add(mp);
			//注册监听
			this.addKeyListener(mp);
			this.setVisible(true);
		}
	}

}
//
class MyStartPanel extends JPanel implements Runnable
{
	int times=0;
	public void paint(Graphics g)
	{	super.paint(g);
		g.fillRect(0, 0, 400, 300);
		if(times%2==0)
		{
		g.setColor(Color.yellow);
		Font myFont=new Font("华文新魏",Font.BOLD,30);
		g.setFont(myFont);
		g.drawString("stage:1", 150, 150);
		}
	}

	@Override
	public void run() {
		// TODO 自动生成的方法存根
		while(true)
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			times++;
			this.repaint();
		}
	}
}
//我的面板
class MyPanel extends JPanel implements KeyListener,Runnable
{
	
	
	//定义一个我的坦克
	Hero hero=null;
	
	//判断是续上局，或新游戏
	//String flag="newGame";
	//定义敌人坦克组
	Vector<EnemyTank> ets=new Vector<EnemyTank>();
	Vector<Node> nodes=new Vector<Node>();
	//定义炸弹集合
	Vector<Bomb> bomb=new Vector<Bomb>();
	int ensize=3;
	
	//定义三张图片,组成一颗炸弹
	Image image1=null;
	Image image2=null;
	Image image3=null;
	//构造函数
	public MyPanel(String flag)
	{
		//恢复记录
		Recorder.getRecording();
		
		hero=new Hero(190,230);
		
		//初始化敌人的坦克
		if(flag.equals("newGame"))
		{
			for(int i=0;i<ensize;i++)
			{
				//创建敌人坦克对象
				EnemyTank et=new EnemyTank((i+1)*50,0);
				et.setColor(0);
				et.setDirect(2);
		
			//将MyPanel的敌人坦克向量交给该敌人坦克,传递
			et.setEts(ets);
			
			Thread t=new Thread(et);
			t.start();
			//添加子弹
			Shot s=new Shot(et.x+10,et.y+30,et.direct);
			et.ss.add(s);
			Thread t2=new Thread(s);
			t2.start();
			ets.add(et);
		}
		}else{
			nodes=new Recorder().getNodeAndEnNums();
			for(int i=0;i<nodes.size();i++)
			{
				Node node=nodes.get(i);
				//创建敌人坦克对象
				EnemyTank et=new EnemyTank(node.x,node.y);
				et.setColor(0);
				et.setDirect(node.direct);
			
				//将MyPanel的敌人坦克向量交给该敌人坦克,传递
				et.setEts(ets);
				
				Thread t=new Thread(et);
				t.start();
				//添加子弹
				Shot s=new Shot(et.x+10,et.y+30,et.direct);
				et.ss.add(s);
				Thread t2=new Thread(s);
				t2.start();
				ets.add(et);
			}
		}
		//初始化图片,Toolkit会导致第一张图显示不正常
		/*image1=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_1.gif"));
		image2=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_2.gif"));
		image3=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_3.gif"));
	*/
		try {
			image1=ImageIO.read(new File("bomb_1.gif"));
			image2=ImageIO.read(new File("bomb_2.gif"));
			image3=ImageIO.read(new File("bomb_3.gif"));
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		//播放开战声音
		AePlayWave apw=new AePlayWave("111.wav");
		apw.start();
				}
	//画出提示信息,封装
	public void showInfo(Graphics g)
	{
		//画出提示信息
				this.drawTank(80, 330, g, 0, 0);
				g.setColor(Color.black);
				g.drawString(Recorder.getEnNum()+"", 110,350);
				this.drawTank(135, 330, g, 0, 1);
				g.setColor(Color.black);
				g.drawString(Recorder.getMyLife()+"", 160,350);
				
				//画出总成绩
				g.setColor(Color.black);
				Font f=new Font("宋体",Font.BOLD,20);
				g.setFont(f);
				g.drawString("您的总成绩", 420, 30);
				this.drawTank(420, 60, g, 0, 0);
				g.setColor(Color.black);
				g.drawString(Recorder.getAllEnNum()+"", 460, 80);
	}
	//重新paint
	public void paint(Graphics g)
	{
		super.paint(g);
		
		g.setColor(Color.black);
		g.fillRect(0, 0, 400, 300);
		//画出自己坦克
		if(hero.isLive){
		g.setColor(Color.yellow);
	this.drawTank(hero.getX(), hero.getY(), g, hero.direct, 1);
		}
		this.showInfo(g);
	   //画出子弹
		/*if(hero.s!=null&&hero.s.isLive==true)
		{
			g.draw3DRect(hero.s.x,hero.s.y, 1, 1, false);
			
		}*/
		//从ss中取出子弹并画出
		for(int i=0;i<this.hero.ss.size();i++)
		{
			Shot myShot=hero.ss.get(i);
			if(myShot!=null&&myShot.isLive==true)
			{
				g.setColor(Color.yellow);
				g.draw3DRect(myShot.x,myShot.y, 1, 1, false);
				
			}
			if(myShot.isLive==false)
			{
				//从ss中删除该子弹
				hero.ss.remove(myShot);
			}
		}
		//画出爆炸
		for(int i=0;i<bomb.size();i++)
		{
			System.out.println(bomb.size());
			//取出炸弹
			Bomb b=bomb.get(i);
			if(b.life>6)
			{
				System.out.println(b.life);
				g.drawImage(image1, b.x, b.y,30,30,this);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}	
			}else if(b.life>3)
			{
				System.out.println(b.life);
				g.drawImage(image2, b.x, b.y,30,30,this);
			}else
			{
				System.out.println(b.life);
				g.drawImage(image3, b.x, b.y,30,30,this);
			}
			b.lifeDown();
			if(b.life==0)
			{
				bomb.remove(b);
			}
		}
		//画出敌人坦克
		for(int i=0;i<ets.size();i++)
	    {
			EnemyTank et=ets.get(i);
			if(et.isLive)
			{
	    	this.drawTank(ets.get(i).getX(), ets.get(i).getY(), g, ets.get(i).getDirect(), 0);
			for(int j=0;j<et.ss.size();j++)
			{
				//取出子弹
				Shot enemyShot=et.ss.get(j);
				if(enemyShot.isLive)
				{
					g.draw3DRect(enemyShot.x,enemyShot.y, 1, 1, false);
				}else{
					//敌人子弹死亡，从ss去掉
					et.ss.remove(enemyShot);
				}
			}
			}
		}
	}
	//判断我的子弹是否击中敌人坦克
	public void hitEnemyTank()
	{
		//判断是否击中敌人
		for(int i=0;i<hero.ss.size();i++)
		{
			//取出子弹
			Shot myShot=hero.ss.get(i);
			//判断子弹isLive
			if(myShot.isLive)
			{
				//取出每一个敌人坦克
				for(int j=0;j<ets.size();j++)
				{
					EnemyTank et=ets.get(j);
					if(et.isLive)
					{
						if(this.hitTank(myShot, et))
						{
							Recorder.reduceEnNum();
							Recorder.addEnNumRec();
							if(Recorder.getEnNum()>0)
							{
								et.setX(50);
								et.setY(50);
								et.setDirect(2);
								et.isLive=true;
								
							}
						}
					}
				}
			}
		}
	}
	//判断敌人的子弹是否击中我的坦克
	public void hitHeroTank()
	{
		for(int i=0;i<ets.size();i++)
		{
			EnemyTank et=ets.get(i);
			//取出敌人坦克的子弹
			for(int j=0;j<et.ss.size();j++){
			Shot enemyShot=et.ss.get(j);
			//判断子弹isLive
			if(enemyShot.isLive)
			{
				//取出每一个敌人坦克
				
					if(hero.isLive)
					{
						if(this.hitTank(enemyShot, hero))
						{
							Recorder.setMyLife(Recorder.getMyLife()-1);
						if(Recorder.getMyLife()>0){
							hero.setX(190);
							hero.setY(230);
							hero.setDirect(0);
							
							hero.isLive=true;
						}
						}
					}
				
			}
			}
			
		}
	}
	
	//写一个函数专门判断是否击中敌人坦克
	public boolean hitTank(Shot s,Tank et)
	{
		boolean bool=false;
		switch(et.direct)
		{
		case 0:
		case 2:
			if(s.x>et.x&&s.x<et.x+20&&s.y>et.y&&s.y<et.y+30)
			{
				Bomb b=new Bomb(et.x,et.y);
				bomb.add(b);
				//击中，子弹死亡，坦克死亡
				s.isLive=false;
				et.isLive=false;
				bool=true;
				
			}
			break;
		case 1:
		case 3:
			if(s.x>et.x&&s.x<et.x+30&&s.y>et.y&&s.y<et.y+20)
			{
				Bomb b=new Bomb(et.x,et.y);
				bomb.add(b);
				//击中，子弹死亡，坦克死亡
				s.isLive=false;
				et.isLive=false;
				bool=true;
				
			}
			break;
		
		
		}
		return bool;
		
	}
	/*//写一个函数专门判断是否击中自己
		public void hitHero(Shot s,Hero et)
		{
			switch(et.direct)
			{
			case 0:
			case 2:
				if(s.x>et.x&&s.x<et.x+20&&s.y>et.y&&s.y<et.y+30)
				{
					Bomb b=new Bomb(et.x,et.y);
					bomb.add(b);
					//击中，子弹死亡，坦克死亡
					s.isLive=false;
					et.isLive=false;
					et.live--;
					et.setX(190);
					et.setY(230);
					et.setDirect(0);
					
				}
			case 1:
			case 3:
				if(s.x>et.x&&s.x<et.x+30&&s.y>et.y&&s.y<et.y+20)
				{
					Bomb b=new Bomb(et.x,et.y);
					bomb.add(b);
					//击中，子弹死亡，坦克死亡
					s.isLive=false;
					et.isLive=false;
					et.live--;
					et.setX(190);
					et.setY(230);
					et.setDirect(0);
					
				}
			
			
			}
			
		}*/
	
	//画出坦克的函数(封装）扩展
	public void drawTank(int x,int y,Graphics g,int direct,int type)
	{
		switch(type)
		{
		case 0:
			g.setColor(Color.cyan);
			break;
		case 1:
			g.setColor(Color.yellow);
			break;
		}
		switch(direct)
		{
		case 0:
			//画出我的坦克(到时再封装成一个函数)
			//1.画出左边的矩形
			g.fill3DRect(x, y, 5, 30,false);
			//2.画出右边的矩形
			g.fill3DRect(x+15, y, 5, 30,false);
			//3.画出中间矩形
		    g.fill3DRect(x+5, y+5, 10, 20,false);
		    //4.画出圆形
		    g.fillOval(x+5, y+10, 10, 10);
		    //5画出线
		    g.drawLine(x+10, y+10, x+10, y);
			break;
		case 1:
			g.fill3DRect(x, y, 30, 5,false);
			//2.画出右边的矩形
			g.fill3DRect(x+5, y+5, 20, 10,false);
			//3.画出中间矩形
		    g.fill3DRect(x, y+15, 30, 5,false);
		    //4.画出圆形
		    g.fillOval(x+10, y+5, 10, 10);
		    //5画出线
		    g.drawLine(x+15, y+10, x+30, y+10);
		    break;
		case 2:
			//1.画出左边的矩形
			g.fill3DRect(x, y, 5, 30,false);
			//2.画出右边的矩形
			g.fill3DRect(x+15, y, 5, 30,false);
			//3.画出中间矩形
		    g.fill3DRect(x+5, y+5, 10, 20,false);
		    //4.画出圆形
		    g.fillOval(x+5, y+10, 10, 10);
		    //5画出线
		    g.drawLine(x+10, y+10, x+10, y+30);
			break;
		case 3:
			g.fill3DRect(x, y, 30, 5,false);
			//2.画出右边的矩形
			g.fill3DRect(x+5, y+5, 20, 10,false);
			//3.画出中间矩形
		    g.fill3DRect(x, y+15, 30, 5,false);
		    //4.画出圆形
		    g.fillOval(x+10, y+5, 10, 10);
		    //5画出线
		    g.drawLine(x+15, y+10, x, y+10);
			break;
		}
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	//键按下处理a左d右w上s下
	public void keyPressed(KeyEvent e) {
		// TODO 自动生成的方法存根
		if(e.getKeyCode()==KeyEvent.VK_W){
			//设置我的坦克的方向
			this.hero.setDirect(0);
			this.hero.moveUp();
		}else if(e.getKeyCode()==KeyEvent.VK_D){
			//设置我的坦克的方向
			this.hero.setDirect(1);
			this.hero.moveRight();
		}else if(e.getKeyCode()==KeyEvent.VK_S){
			//设置我的坦克的方向
			this.hero.setDirect(2);
			this.hero.moveDown();
		}else if(e.getKeyCode()==KeyEvent.VK_A){
			//设置我的坦克的方向
			this.hero.setDirect(3);
			this.hero.moveLeft();
		}
		if(e.getKeyCode()==KeyEvent.VK_J){
			//开火
			if(this.hero.ss.size()<=5){
			this.hero.shotEnemy();
			}
		}
		//重绘
		this.repaint();
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void run() {
		// TODO 自动生成的方法存根
		//每隔一百毫秒重绘
		while(true)
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			this.hitEnemyTank();
			
			//判断是否击中自己
			//取出每个敌人坦克
			this.hitHeroTank();
			
			this.repaint();
			
		}
	}
}
