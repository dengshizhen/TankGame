package com.test5;

import java.util.*;
import java.io.*;
//恢复点
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
//记录类,保存设置
class Recorder
{
	//记录每关有多少敌人
	private static int enNum=20;
	//设置我有多少可用的人
		private static int myLife=3;
	//	记录总共消灭敌人数
		private static int allEnNum=0;
		
		private static FileWriter fw=null;
		private static BufferedWriter bw=null;
		private static FileReader fr=null;
		private static BufferedReader br=null;
		
		//从文件中回复记录
		 static Vector<Node> nodes=new Vector<Node>();
		private  Vector<EnemyTank> ets=new Vector<EnemyTank>();
		
		//完成读取
		public  Vector<Node> getNodeAndEnNums()
		{
			try {
				fr=new FileReader("e:\\myRecording.txt");
				br=new BufferedReader(fr);
				String n="";
				//先读第一行
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
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}finally{
				try {
					br.close();
					fr.close();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			return nodes;
		}
		//保存击毁敌人的数量和敌人的坐标和方向
		public  void keepRecAndEnemy()
		{
			try {
				fw=new FileWriter("e:\\myRecording.txt");
				bw=new BufferedWriter(fw);
				
				bw.write(allEnNum+"\r\n");
				//保存当前活着坦克的坐标和方向
				for(int i=0;i<ets.size();i++)
				{
					EnemyTank et=ets.get(i);
					if(et.isLive)
					{
						String recorde=et.x+" "+et.y+" "+et.direct;
						//写入到文件
						bw.write(recorde+"\r\n");
					}
				}
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}finally{
				try {
					bw.close();
					fw.close();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			
		}
		//从文件中读取记录
		public static void getRecording()
		{
			try {
				fr=new FileReader("e:\\myRecording.txt");
				br=new BufferedReader(fr);
				String n=br.readLine();
				allEnNum=Integer.parseInt(n);
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}finally{
				try {
					br.close();
					fr.close();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}
		
	//把击毁数量保存到文件中
		public static void keepRecording()
		{
			try {
				fw=new FileWriter("e:\\myRecording.txt");
				bw=new BufferedWriter(fw);
				
				bw.write(allEnNum+"\r\n");
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}finally{
				try {
					bw.close();
					fw.close();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
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
	//减少敌人数
	public static void reduceEnNum()
	{
		enNum--;
	}
	public static void addEnNumRec()
	{
		allEnNum++;
	}
}
//炸弹类
class Bomb
{
	int x;
	int y;
	//炸弹的生命
	int life=9;
	boolean isLive=true;
	public Bomb(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	//减少生命
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
//子弹类
class Shot implements Runnable
{
	int x;
	int y;
	int direct;
	int speed=2;
	//是否活着
	boolean isLive=true;
	public Shot(int x,int y,int direct)
	{
		this.x=x;
		this.y=y;
		this.direct=direct;
	}
	@Override
	public void run() {
		// TODO 自动生成的方法存根
		while(true)
		{
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			switch(direct)
			{
			case 0:
				//上
				y-=speed;
				break;
			case 1:
				//右
				x+=speed;
				break;
			case 2:
				y+=speed;
				break;
			case 3:
				x-=speed;
				break;
			}
			//子弹何时死亡
			//判断子弹是否碰到边缘
			if(x<0||x>400||y<0||y>300)
			{
				this.isLive=false;
				break;
			}
		}
	}
}
//坦克类
class Tank
{
	//表示坦克的横坐标
	int x=0;
	//表示坦克的纵坐标
		int y=0;
		
		//表示坦克方向0:上1右2下3左
		int direct=0;
		//设置坦克的速度
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
//敌人的坦克,做成线程
class EnemyTank extends Tank implements Runnable
{
	
	int times=0;
	//子弹
	//定义一个向量可以访问MyPanel上所有敌人坦克
	Vector<EnemyTank> ets=new Vector<EnemyTank>();
	
		Vector<Shot> ss=new Vector<Shot>();
	public EnemyTank(int x,int y)
	{
		super(x, y);
	}
	//得到MyPanel的敌人坦克向量
	public void setEts(Vector<EnemyTank> vv)
	{
		this.ets=vv;
	}
	
	//判断是否碰到了别的敌人坦克
	public boolean isTouchOtherEnemy()
	{
		boolean b=false;
		switch(this.direct)
		{
		case 0:
			//我的坦克向上，取出所有的敌人坦克
			for(int i=0;i<ets.size();i++)
			{
				EnemyTank et=ets.get(i);
				//排除自己
				if(et!=this){
					//如果敌人向下或向上
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
			//我的坦克向右，取出所有的敌人坦克
			for(int i=0;i<ets.size();i++)
			{
				EnemyTank et=ets.get(i);
				//排除自己
				if(et!=this){
					//如果敌人向下或向上
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
			//我的坦克向下，取出所有的敌人坦克
			for(int i=0;i<ets.size();i++)
			{
				EnemyTank et=ets.get(i);
				//排除自己
				if(et!=this){
					//如果敌人向下或向上
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
			//我的坦克向左，取出所有的敌人坦克
			for(int i=0;i<ets.size();i++)
			{
				EnemyTank et=ets.get(i);
				//排除自己
				if(et!=this){
					//如果敌人向下或向上
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
		// TODO 自动生成的方法存根
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
					// TODO 自动生成的 catch 块
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
					// TODO 自动生成的 catch 块
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
					// TODO 自动生成的 catch 块
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
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				
				}
				break;
			}
			this.times++;
			//判断敌人子弹是否没有了
			/*for(int i=0;i<ets.size();i++)
				{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
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
			//随机方向
			this.direct=(int)(Math.random()*4);
			if(this.isLive==false)
			{
				break;
			}
			
		}
	}
}
//我的坦克
class Hero extends Tank
{
	//子弹
	Vector<Shot> ss=new Vector<Shot>();
	Shot s=null;
	int live=3;
	
	public Hero(int x,int y)
	{
		super(x,y);
	}
	//开火
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
	//坦克向上移动
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

