/**
 * ���±�������+���ܣ�
 */
package com.test4;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class NotePad extends JFrame implements ActionListener {

	JTextArea jta=null;
	JMenuBar jmb=null;
	JMenu jm1=null;
	JMenuItem jmi1=null;
	JMenuItem jmi2=null;
	
	public NotePad() {
		// TODO �Զ����ɵĹ��캯�����
		jta=new JTextArea();
		jmb=new JMenuBar();
		jm1=new JMenu("�ļ�");
		jm1.setMnemonic('F');
		jmi1=new JMenuItem("��",new ImageIcon("new.gif"));
		jmi1.addActionListener(this);
		jmi1.setActionCommand("open");
		jmi2=new JMenuItem("����");
		jmi2.addActionListener(this);
		jmi2.setActionCommand("save");
		
		this.setJMenuBar(jmb);
		jmb.add(jm1);
		jm1.add(jmi1);
		jm1.add(jmi2);
		this.add(jta);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setSize(400, 300);
	}

	public static void main(String[] args) {
		// TODO �Զ����ɵķ������
		NotePad mynp=new NotePad();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO �Զ����ɵķ������
		if(e.getActionCommand()=="open")
		{
			System.out.println("open");
			//��ѡ���ļ��Ŀ�
			JFileChooser jfc1=new JFileChooser();
			jfc1.setDialogTitle("��ѡ���ļ�...");
			jfc1.showOpenDialog(null);
			jfc1.setVisible(true);
			//�õ�·��
			String filename=jfc1.getSelectedFile().getAbsolutePath();
			System.out.println(filename);
			FileReader fr=null;
			BufferedReader br=null;
			
			try {
				fr=new FileReader(filename);
				br=new BufferedReader(fr);
				//��ȡ��Ϣ����ʾ
				String s="";
				String allCon="";
				while((s=br.readLine())!=null)
				{
					allCon+=s+"\r\n";
				}
				jta.setText(allCon);
			} catch (Exception e1) {
				// TODO �Զ����ɵ� catch ��
				e1.printStackTrace();
			}finally{
				try {
					br.close();
					fr.close();
				} catch (IOException e1) {
					// TODO �Զ����ɵ� catch ��
					e1.printStackTrace();
				}
				
			}
			
		}
		if(e.getActionCommand()=="save")
		{
			System.out.println("save");
			JFileChooser jfc2=new JFileChooser();
			jfc2.setDialogTitle("���Ϊ...");
			jfc2.showSaveDialog(null);
			jfc2.setVisible(true);
			String filename=jfc2.getSelectedFile().getAbsolutePath();
			System.out.println(filename);
			FileWriter fw=null;
			BufferedWriter bw=null;
			
			try {
				fw=new FileWriter(filename);
				bw=new BufferedWriter(fw);
				//��ȡ��Ϣ����ʾ,���Ż�
				bw.write(this.jta.getText());
					
			} catch (Exception e1) {
				// TODO �Զ����ɵ� catch ��
				e1.printStackTrace();
			}finally{
				try {
					bw.close();
					fw.close();
				} catch (IOException e1) {
					// TODO �Զ����ɵ� catch ��
					e1.printStackTrace();
				}
		}
	}
	}
}

