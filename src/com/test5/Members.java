package com.test5;

import java.util.*;
import java.io.*;
//�ָ���
class Node
{
	int x;
	int y;
	int direct;
	public Node(int x,int y,int direct)
	{
		this.x=x;
		this.y=y;
		this.direct=direct;
	}
}
//��¼��,��������
class Recorder
{
	//��¼ÿ���ж��ٵ���
	private static int enNum=20;
	//�������ж��ٿ��õ���
		private static int myLife=3;
	//	��¼�ܹ����������
		private static int allEnNum=0;
		
		private static FileWriter fw=null;
		private static BufferedWriter bw=null;
		private static FileReader fr=null;
		private static BufferedReader br=null;
		
		//���ļ��лظ���¼
		 static Vector<Node> nodes=new Vector<Node>();
		private  Vector<EnemyTank> ets=new Vector<EnemyTank>();
		
		//��ɶ�ȡ
		public  Vector<Node> getNodeAndEnNums()
		{
			try {
				fr=new FileReader("e:\\myRecording.txt");
				br=new BufferedReader(fr);
				String n="";
				//�ȶ���һ��
				n=br.readLine();
				allEnNum=Integer.parseInt(n);
				while((n=br.readLine())!=null)
				{
					String[] xyz=n.split(" ");
						Node node=new Node(Integer.parseInt(xyz[0]),
								      Integer.parseInt(xyz[1]),
								      Integer.parseInt(xyz[2]));
									nodes.add(node);			
				}
				
				
			} catch (Exception e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}finally{
				try {
					br.close();
					fr.close();
				} catch (IOException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			}
			return nodes;
		}
		//������ٵ��˵������͵��˵�����ͷ���
		public  void keepRecAndEnemy()
		{
			try {
				fw=new FileWriter("e:\\myRecording.txt");
				bw=new BufferedWriter(fw);
				
				bw.write(allEnNum+"\r\n");
				//���浱ǰ����̹�˵�����ͷ���
				for(int i=0;i<ets.size();i++)
				{
					EnemyTank et=ets.get(i);
					if(et.isLive)
					{
						String recorde=et.x+" "+et.y+" "+et.direct;
						//д�뵽�ļ�
						bw.write(recorde+"\r\n");
					}
				}
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}finally{
				try {
					bw.close();
					fw.close();
				} catch (IOException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			}
			
		}
		//���ļ��ж�ȡ��¼
		public static void getRecording()
		{
			try {
				fr=new FileReader("e:\\myRecording.txt");
				br=new BufferedReader(fr);
				String n=br.readLine();
				allEnNum=Integer.parseInt(n);
			} catch (Exception e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}finally{
				try {
					br.close();
					fr.close();
				} catch (IOException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			}
		}
		
	//�ѻ����������浽�ļ���
		public static void keepRecording()
		{
			try {
				fw=new FileWriter("e:\\myRecording.txt");
				bw=new BufferedWriter(fw);
				
				bw.write(allEnNum+"\r\n");
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}finally{
				try {
					bw.close();
					fw.close();
				} catch (IOException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			}
			
		}
		
		
	public static int getAllEnNum() {
			return allEnNum;
		}
		public static void setAllEnNum(int allEnNum) {
			Recorder.allEnNum = allEnNum;
		}
	public static int getEnNum() {
		return enNum;
	}
	public static void setEnNum(int enNum) {
		Recorder.enNum = enNum;
	}
	public static int getMyLife() {
		return myLife;
	}
	public static void setMyLife(int myLife) {
		Recorder.myLife = myLife;
	}
	public  Vector<EnemyTank> getEts() {
		return ets;
	}
	public  void setEts(Vector<EnemyTank> ets1) {
		this.ets = ets1;
	}
	//���ٵ�����
	public static void reduceEnNum()
	{
		enNum--;
	}
	public static void addEnNumRec()
	{
		allEnNum++;
	}
}
//ը����
class Bomb
{
	int x;
	int y;
	//ը��������
	int life=9;
	boolean isLive=true;
	public Bomb(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	//��������
	public void lifeDown()
{
		if(life>0)
		{
			life--;
		}else{		{
			this.isLive=false;
		}
}
	
}
}
//�ӵ���
class Shot implements Runnable
{
	int x;
	int y;
	int direct;
	int speed=2;
	//�Ƿ����
	boolean isLive=true;
	public Shot(int x,int y,int direct)
	{
		this.x=x;
		this.y=y;
		this.direct=direct;
	}
	@Override
	public void run() {
		// TODO �Զ����ɵķ������
		while(true)
		{
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			switch(direct)
			{
			case 0:
				//��
				y-=speed;
				break;
			case 1:
				//��
				x+=speed;
				break;
			case 2:
				y+=speed;
				break;
			case 3:
				x-=speed;
				break;
			}
			//�ӵ���ʱ����
			//�ж��ӵ��Ƿ�������Ե
			if(x<0||x>400||y<0||y>300)
			{
				this.isLive=false;
				break;
			}
		}
	}
}
//̹����
class Tank
{
	//��ʾ̹�˵ĺ�����
	int x=0;
	//��ʾ̹�˵�������
		int y=0;
		
		//��ʾ̹�˷���0:��1��2��3��
		int direct=0;
		//����̹�˵��ٶ�
		int speed=1;
		int color=0;
		boolean isLive=true;
	/*public boolean isLive() {
			return isLive;
		}
		public void setLive(boolean isLive) {
			this.isLive = isLive;
		}*/
	public int getColor() {
			return color;
		}
		public void setColor(int color) {
			this.color = color;
		}
	public int getSpeed() {
			return speed;
		}
		public void setSpeed(int speed) {
			this.speed = speed;
		}
	public int getDirect() {
			return direct;
		}
		public void setDirect(int direct) {
			this.direct = direct;
		}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public Tank(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
}
//���˵�̹��,�����߳�
class EnemyTank extends Tank implements Runnable
{
	
	int times=0;
	//�ӵ�
	//����һ���������Է���MyPanel�����е���̹��
	Vector<EnemyTank> ets=new Vector<EnemyTank>();
	
		Vector<Shot> ss=new Vector<Shot>();
	public EnemyTank(int x,int y)
	{
		super(x, y);
	}
	//�õ�MyPanel�ĵ���̹������
	public void setEts(Vector<EnemyTank> vv)
	{
		this.ets=vv;
	}
	
	//�ж��Ƿ������˱�ĵ���̹��
	public boolean isTouchOtherEnemy()
	{
		boolean b=false;
		switch(this.direct)
		{
		case 0:
			//�ҵ�̹�����ϣ�ȡ�����еĵ���̹��
			for(int i=0;i<ets.size();i++)
			{
				EnemyTank et=ets.get(i);
				//�ų��Լ�
				if(et!=this){
					//����������»�����
				if(et.direct==0||et.direct==2)
				{
					if(this.x>=et.x&&this.x<=et.x+20
							&&this.y>=et.y&&this.y<=et.y+30)
					{
						return true;
					}
					if(this.x+20>=et.x&&this.x+20<=et.x+20
							&&this.y>=et.y&&this.y<=et.y+30)
					{
						return true;
					}
						
				}
				if(et.direct==1||et.direct==3)
				{
					if(this.x>=et.x&&this.x<=et.x+30
							&&this.y>=et.y&&this.y<=et.y+20)
					{
						return true;
					}
					if(this.x+20>=et.x&&this.x+20<=et.x+30
							&&this.y>=et.y&&this.y<=et.y+20)
					{
						return true;
					}
				}
				}
			}
			break;
		case 1:
			//�ҵ�̹�����ң�ȡ�����еĵ���̹��
			for(int i=0;i<ets.size();i++)
			{
				EnemyTank et=ets.get(i);
				//�ų��Լ�
				if(et!=this){
					//����������»�����
				if(et.direct==0||et.direct==2)
				{
					if(this.x+30>=et.x&&this.x+30<=et.x+20
							&&this.y>=et.y&&this.y<=et.y+30)
					{
						return true;
					}
					if(this.x+30>=et.x&&this.x+30<=et.x+20
							&&this.y+20>=et.y&&this.y+20<=et.y+30)
					{
						return true;
					}
						
				}
				if(et.direct==1||et.direct==3)
				{
					if(this.x+30>=et.x&&this.x+30<=et.x+30
							&&this.y>=et.y&&this.y<=et.y+20)
					{
						return true;
					}
					if(this.x+30>=et.x&&this.x+30<=et.x+30
							&&this.y+20>=et.y&&this.y+20<=et.y+20)
					{
						return true;
					}
				}
				}
			}
			break;
		case 2:
			//�ҵ�̹�����£�ȡ�����еĵ���̹��
			for(int i=0;i<ets.size();i++)
			{
				EnemyTank et=ets.get(i);
				//�ų��Լ�
				if(et!=this){
					//����������»�����
				if(et.direct==0||et.direct==2)
				{
					if(this.x>=et.x&&this.x<=et.x+20
							&&this.y+30>=et.y&&this.y+30<=et.y+30)
					{
						return true;
					}
					if(this.x>=et.x&&this.x<=et.x+20
							&&this.y+30>=et.y&&this.y+30<=et.y+30)
					{
						return true;
					}
						
				}
				if(et.direct==1||et.direct==3)
				{
					if(this.x+20>=et.x&&this.x+20<=et.x+30
							&&this.y+30>=et.y&&this.y+30<=et.y+20)
					{
						return true;
					}
					if(this.x+20>=et.x&&this.x+20<=et.x+30
							&&this.y+30>=et.y&&this.y+30<=et.y+20)
					{
						return true;
					}
				}
				}
			}
			break;
		case 3:
			//�ҵ�̹������ȡ�����еĵ���̹��
			for(int i=0;i<ets.size();i++)
			{
				EnemyTank et=ets.get(i);
				//�ų��Լ�
				if(et!=this){
					//����������»�����
				if(et.direct==0||et.direct==2)
				{
					if(this.x>=et.x&&this.x<=et.x+20
							&&this.y>=et.y&&this.y<=et.y+30)
					{
						return true;
					}
					if(this.x>=et.x&&this.x<=et.x+20
							&&this.y>=et.y&&this.y<=et.y+30)
					{
						return true;
					}
						
				}
				if(et.direct==1||et.direct==3)
				{
					if(this.x>=et.x&&this.x<=et.x+30
							&&this.y+20>=et.y&&this.y+20<=et.y+20)
					{
						return true;
					}
					if(this.x>=et.x&&this.x<=et.x+30
							&&this.y+20>=et.y&&this.y+20<=et.y+20)
					{
						return true;
					}
				}
				}
			}
			break;
		}
		return b;
	}
	@Override
	public void run() {
		// TODO �Զ����ɵķ������
		while(true)
		{
			
			
			switch(this.direct)
			{
			case 0:
				for(int i=0;i<30;i++)
				{
					if(y>0&&!this.isTouchOtherEnemy()){
				y-=speed;
					}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
				}
				break;
			case 1:
				for(int i=0;i<30;i++)
				{
					if(x<300&&!this.isTouchOtherEnemy()){
				x+=speed;
					}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
				
				}
				break;
			case 2:
				for(int i=0;i<30;i++)
				{
					if(y<400&&!this.isTouchOtherEnemy()){
				y+=speed;
					}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
				
				}
				break;
			case 3:
				for(int i=0;i<30;i++)
				{
					if(x>0&&!this.isTouchOtherEnemy()){
				x-=speed;
					}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
				
				}
				break;
			}
			this.times++;
			//�жϵ����ӵ��Ƿ�û����
			/*for(int i=0;i<ets.size();i++)
				{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
				    EnemyTank et=ets.get(i);*/
			if(times%2==0){
				
			if(isLive)
			{
			if(ss.size()<5)
				
			{
					Shot s=null;
					switch(direct)
					{
					case 0:
						s=new Shot(x+10,y,0);
						ss.add(s);
						break;
						case 1:
							s=new Shot(x+30,y+10,1);
							ss.add(s);
							break;
						case 2:
							s=new Shot(x+10,y+30,2);
							ss.add(s);
							break;
						case 3:
							s=new Shot(x,y+10,3);
							ss.add(s);
							break;
					}
					Thread t=new Thread(s);
					t.start();
				}
			}
			}
			//�������
			this.direct=(int)(Math.random()*4);
			if(this.isLive==false)
			{
				break;
			}
			
		}
	}
}
//�ҵ�̹��
class Hero extends Tank
{
	//�ӵ�
	Vector<Shot> ss=new Vector<Shot>();
	Shot s=null;
	int live=3;
	
	public Hero(int x,int y)
	{
		super(x,y);
	}
	//����
	public void shotEnemy()
	{
		
		switch(this.direct){
		case 0:
		s=new Shot(x+10,y,0);
		ss.add(s);
		break;
		case 1:
			s=new Shot(x+30,y+10,1);
			ss.add(s);
			break;
		case 2:
			s=new Shot(x+10,y+30,2);
			ss.add(s);
			break;
		case 3:
			s=new Shot(x,y+10,3);
			ss.add(s);
			break;
		}
		Thread t=new Thread(s);
		t.start();
	}
	//̹�������ƶ�
	public void moveUp()
	{
	       y-=speed;
	}
	public void moveRight()
	{
	       x+=speed;
	}
	public void moveDown()
	{
	       y+=speed;
	}
	public void moveLeft()
	{
	       x-=speed;
	}
}

