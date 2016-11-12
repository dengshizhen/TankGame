/**
 * ���ܣ�̹����Ϸ2.0
 * 1.����̹��
 * 2.����ҵ�̹�˿������������ƶ�
 * 3.���������ӵ�,���5��
 * 4.���ҵ��ӵ����е���̹��ʱ������̹����ʧ����ը��
 * 5.�����ӵ������Լ����Լ�̹����ʧ
 * 6.��ֹ����̹���ص�
 *  6.1���ж�д��EnemyTank����
 * 7.���Էֹ�
 *  7.1��һ����ʼ��panel������һ���յģ�������ʾ
 *  7.2��˸Ч��
 * 8.������ͣ�ͼ���
 *  8.1�����ͣʱ�����ӵ����ٶȺ�̹�˵��ٶ���Ϊ0������̹�˵ķ���Ҫ�仯
 * 9.���Լ�¼��ҵĳɼ�
 *   9.1���ļ����ķ�ʽ
 *   9.2��дһ����¼��
 *   9.3����ɱ��湲�����˶��ٵ���̹��
 *   9.4�����˳������Լ�¼�˳�ʱ����̹�����꣬�������˳�
 * 10.��������
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
	//���忪ʼ���
	MyStartPanel msp=null;
	//������Ҫ�Ĳ˵�
	JMenuBar jmb=null;
	//��ʼ��Ϸ
	JMenu jm1=null;
	JMenuItem jmi1=null;
	JMenuItem jmi2=null;
	JMenuItem jmi3=null;
	JMenuItem jmi4=null;
	
	public MyTankGame5() {
		// TODO �Զ����ɵĹ��캯�����
		//mp=new MyPanel();
		//����mp�߳�
		//Thread t=new Thread(mp);
		//t.start();
		
		//this.add(mp);
		//ע�����
		//this.addKeyListener(mp);
		//�����˵���ѡ��
		jmb=new JMenuBar();
		jm1=new JMenu("��Ϸ(G)");
		jm1.setMnemonic('G');
		jmi1=new JMenuItem("��ʼ����Ϸ(N)");
		jmi1.addActionListener(this);
		jmi1.setActionCommand("newgame");
		jmi1.setMnemonic('N');
		
		jmi2=new JMenuItem("�˳���Ϸ(E)");
		jmi2.setMnemonic('E');
		jmi2.addActionListener(this);
		jmi2.setActionCommand("exit");
		
		jmi3=new JMenuItem("�����˳���Ϸ(C)");
		jmi3.setMnemonic('C');
		jmi3.addActionListener(this);
		jmi3.setActionCommand("saveExit");
		
		jmi4=new JMenuItem("���Ͼ���Ϸ(R)");
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
		// TODO �Զ����ɵķ������
		MyTankGame5 mt=new MyTankGame5();

	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO �Զ����ɵķ������
		if(e.getActionCommand()=="newgame")
		{
			mp=new MyPanel("newGame");
			//����mp�߳�
			Thread t=new Thread(mp);
			t.start();
			//��ɾ���ɵ����
			this.remove(msp);
			this.add(mp);
			//ע�����
			this.addKeyListener(mp);
			this.setVisible(true);
		}else if(e.getActionCommand()=="exit")
		{
			//�����������
			Recorder.keepRecording();
			System.exit(0);
		}else if(e.getActionCommand()=="saveExit")
		{
			//���̹���,������ٵ���������ʣ��̹������
			Recorder rd=new Recorder();
			rd.setEts(mp.ets);
			rd.keepRecAndEnemy();
		    
			//�˳�
			System.exit(0);
		}else if(e.getActionCommand().equals("conGame"))
		{
			//mp.flag="con";
			
			mp=new MyPanel("con");
		
			//����mp�߳�
			Thread t=new Thread(mp);
			t.start();
			//��ɾ���ɵ����
			this.remove(msp);
			this.add(mp);
			//ע�����
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
		Font myFont=new Font("������κ",Font.BOLD,30);
		g.setFont(myFont);
		g.drawString("stage:1", 150, 150);
		}
	}

	@Override
	public void run() {
		// TODO �Զ����ɵķ������
		while(true)
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			times++;
			this.repaint();
		}
	}
}
//�ҵ����
class MyPanel extends JPanel implements KeyListener,Runnable
{
	
	
	//����һ���ҵ�̹��
	Hero hero=null;
	
	//�ж������Ͼ֣�������Ϸ
	//String flag="newGame";
	//�������̹����
	Vector<EnemyTank> ets=new Vector<EnemyTank>();
	Vector<Node> nodes=new Vector<Node>();
	//����ը������
	Vector<Bomb> bomb=new Vector<Bomb>();
	int ensize=3;
	
	//��������ͼƬ,���һ��ը��
	Image image1=null;
	Image image2=null;
	Image image3=null;
	//���캯��
	public MyPanel(String flag)
	{
		//�ָ���¼
		Recorder.getRecording();
		
		hero=new Hero(190,230);
		
		//��ʼ�����˵�̹��
		if(flag.equals("newGame"))
		{
			for(int i=0;i<ensize;i++)
			{
				//��������̹�˶���
				EnemyTank et=new EnemyTank((i+1)*50,0);
				et.setColor(0);
				et.setDirect(2);
		
			//��MyPanel�ĵ���̹�����������õ���̹��,����
			et.setEts(ets);
			
			Thread t=new Thread(et);
			t.start();
			//����ӵ�
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
				//��������̹�˶���
				EnemyTank et=new EnemyTank(node.x,node.y);
				et.setColor(0);
				et.setDirect(node.direct);
			
				//��MyPanel�ĵ���̹�����������õ���̹��,����
				et.setEts(ets);
				
				Thread t=new Thread(et);
				t.start();
				//����ӵ�
				Shot s=new Shot(et.x+10,et.y+30,et.direct);
				et.ss.add(s);
				Thread t2=new Thread(s);
				t2.start();
				ets.add(et);
			}
		}
		//��ʼ��ͼƬ,Toolkit�ᵼ�µ�һ��ͼ��ʾ������
		/*image1=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_1.gif"));
		image2=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_2.gif"));
		image3=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_3.gif"));
	*/
		try {
			image1=ImageIO.read(new File("bomb_1.gif"));
			image2=ImageIO.read(new File("bomb_2.gif"));
			image3=ImageIO.read(new File("bomb_3.gif"));
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		//���ſ�ս����
		AePlayWave apw=new AePlayWave("111.wav");
		apw.start();
				}
	//������ʾ��Ϣ,��װ
	public void showInfo(Graphics g)
	{
		//������ʾ��Ϣ
				this.drawTank(80, 330, g, 0, 0);
				g.setColor(Color.black);
				g.drawString(Recorder.getEnNum()+"", 110,350);
				this.drawTank(135, 330, g, 0, 1);
				g.setColor(Color.black);
				g.drawString(Recorder.getMyLife()+"", 160,350);
				
				//�����ܳɼ�
				g.setColor(Color.black);
				Font f=new Font("����",Font.BOLD,20);
				g.setFont(f);
				g.drawString("�����ܳɼ�", 420, 30);
				this.drawTank(420, 60, g, 0, 0);
				g.setColor(Color.black);
				g.drawString(Recorder.getAllEnNum()+"", 460, 80);
	}
	//����paint
	public void paint(Graphics g)
	{
		super.paint(g);
		
		g.setColor(Color.black);
		g.fillRect(0, 0, 400, 300);
		//�����Լ�̹��
		if(hero.isLive){
		g.setColor(Color.yellow);
	this.drawTank(hero.getX(), hero.getY(), g, hero.direct, 1);
		}
		this.showInfo(g);
	   //�����ӵ�
		/*if(hero.s!=null&&hero.s.isLive==true)
		{
			g.draw3DRect(hero.s.x,hero.s.y, 1, 1, false);
			
		}*/
		//��ss��ȡ���ӵ�������
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
				//��ss��ɾ�����ӵ�
				hero.ss.remove(myShot);
			}
		}
		//������ը
		for(int i=0;i<bomb.size();i++)
		{
			System.out.println(bomb.size());
			//ȡ��ը��
			Bomb b=bomb.get(i);
			if(b.life>6)
			{
				System.out.println(b.life);
				g.drawImage(image1, b.x, b.y,30,30,this);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO �Զ����ɵ� catch ��
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
		//��������̹��
		for(int i=0;i<ets.size();i++)
	    {
			EnemyTank et=ets.get(i);
			if(et.isLive)
			{
	    	this.drawTank(ets.get(i).getX(), ets.get(i).getY(), g, ets.get(i).getDirect(), 0);
			for(int j=0;j<et.ss.size();j++)
			{
				//ȡ���ӵ�
				Shot enemyShot=et.ss.get(j);
				if(enemyShot.isLive)
				{
					g.draw3DRect(enemyShot.x,enemyShot.y, 1, 1, false);
				}else{
					//�����ӵ���������ssȥ��
					et.ss.remove(enemyShot);
				}
			}
			}
		}
	}
	//�ж��ҵ��ӵ��Ƿ���е���̹��
	public void hitEnemyTank()
	{
		//�ж��Ƿ���е���
		for(int i=0;i<hero.ss.size();i++)
		{
			//ȡ���ӵ�
			Shot myShot=hero.ss.get(i);
			//�ж��ӵ�isLive
			if(myShot.isLive)
			{
				//ȡ��ÿһ������̹��
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
	//�жϵ��˵��ӵ��Ƿ�����ҵ�̹��
	public void hitHeroTank()
	{
		for(int i=0;i<ets.size();i++)
		{
			EnemyTank et=ets.get(i);
			//ȡ������̹�˵��ӵ�
			for(int j=0;j<et.ss.size();j++){
			Shot enemyShot=et.ss.get(j);
			//�ж��ӵ�isLive
			if(enemyShot.isLive)
			{
				//ȡ��ÿһ������̹��
				
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
	
	//дһ������ר���ж��Ƿ���е���̹��
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
				//���У��ӵ�������̹������
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
				//���У��ӵ�������̹������
				s.isLive=false;
				et.isLive=false;
				bool=true;
				
			}
			break;
		
		
		}
		return bool;
		
	}
	/*//дһ������ר���ж��Ƿ�����Լ�
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
					//���У��ӵ�������̹������
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
					//���У��ӵ�������̹������
					s.isLive=false;
					et.isLive=false;
					et.live--;
					et.setX(190);
					et.setY(230);
					et.setDirect(0);
					
				}
			
			
			}
			
		}*/
	
	//����̹�˵ĺ���(��װ����չ
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
			//�����ҵ�̹��(��ʱ�ٷ�װ��һ������)
			//1.������ߵľ���
			g.fill3DRect(x, y, 5, 30,false);
			//2.�����ұߵľ���
			g.fill3DRect(x+15, y, 5, 30,false);
			//3.�����м����
		    g.fill3DRect(x+5, y+5, 10, 20,false);
		    //4.����Բ��
		    g.fillOval(x+5, y+10, 10, 10);
		    //5������
		    g.drawLine(x+10, y+10, x+10, y);
			break;
		case 1:
			g.fill3DRect(x, y, 30, 5,false);
			//2.�����ұߵľ���
			g.fill3DRect(x+5, y+5, 20, 10,false);
			//3.�����м����
		    g.fill3DRect(x, y+15, 30, 5,false);
		    //4.����Բ��
		    g.fillOval(x+10, y+5, 10, 10);
		    //5������
		    g.drawLine(x+15, y+10, x+30, y+10);
		    break;
		case 2:
			//1.������ߵľ���
			g.fill3DRect(x, y, 5, 30,false);
			//2.�����ұߵľ���
			g.fill3DRect(x+15, y, 5, 30,false);
			//3.�����м����
		    g.fill3DRect(x+5, y+5, 10, 20,false);
		    //4.����Բ��
		    g.fillOval(x+5, y+10, 10, 10);
		    //5������
		    g.drawLine(x+10, y+10, x+10, y+30);
			break;
		case 3:
			g.fill3DRect(x, y, 30, 5,false);
			//2.�����ұߵľ���
			g.fill3DRect(x+5, y+5, 20, 10,false);
			//3.�����м����
		    g.fill3DRect(x, y+15, 30, 5,false);
		    //4.����Բ��
		    g.fillOval(x+10, y+5, 10, 10);
		    //5������
		    g.drawLine(x+15, y+10, x, y+10);
			break;
		}
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO �Զ����ɵķ������
		
	}
	@Override
	//�����´���a��d��w��s��
	public void keyPressed(KeyEvent e) {
		// TODO �Զ����ɵķ������
		if(e.getKeyCode()==KeyEvent.VK_W){
			//�����ҵ�̹�˵ķ���
			this.hero.setDirect(0);
			this.hero.moveUp();
		}else if(e.getKeyCode()==KeyEvent.VK_D){
			//�����ҵ�̹�˵ķ���
			this.hero.setDirect(1);
			this.hero.moveRight();
		}else if(e.getKeyCode()==KeyEvent.VK_S){
			//�����ҵ�̹�˵ķ���
			this.hero.setDirect(2);
			this.hero.moveDown();
		}else if(e.getKeyCode()==KeyEvent.VK_A){
			//�����ҵ�̹�˵ķ���
			this.hero.setDirect(3);
			this.hero.moveLeft();
		}
		if(e.getKeyCode()==KeyEvent.VK_J){
			//����
			if(this.hero.ss.size()<=5){
			this.hero.shotEnemy();
			}
		}
		//�ػ�
		this.repaint();
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO �Զ����ɵķ������
		
	}
	@Override
	public void run() {
		// TODO �Զ����ɵķ������
		//ÿ��һ�ٺ����ػ�
		while(true)
		{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			this.hitEnemyTank();
			
			//�ж��Ƿ�����Լ�
			//ȡ��ÿ������̹��
			this.hitHeroTank();
			
			this.repaint();
			
		}
	}
}
