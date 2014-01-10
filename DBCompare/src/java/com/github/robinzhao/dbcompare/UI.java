package com.github.robinzhao.dbcompare;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class UI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6543090632389524972L;
	DBLoginPanel db1=new DBLoginPanel();
	DBLoginPanel db2=new DBLoginPanel();
	JLabel error= new JLabel();
	JButton btn = new JButton("比较");
	String result="";
	public UI() {
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.add(error);
		this.add(new JLabel("数据库1"));
		this.add(db1);
		this.add(new JLabel("数据库2"));
		this.add(db2);
		this.add(btn);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				UI.this.compare();
			}
		});
		this.setResizable(false);
		this.setSize(500, 300);
		
	}
	
	public void compare(){
		String url1 = this.db1.getUrl();
		String user1 = this.db1.getUser();
		String pwd1 = this.db1.getPwd();
		String schema1 = this.db1.getSchema();
		String url2 =  this.db2.getUrl();
		String user2 = this.db2.getUser();
		String pwd2 =  this.db2.getPwd();
		String schema2 =  this.db2.getSchema();
		DBCompare compare = new DBCompare(url1,user1,pwd1,schema1,url2,user2,pwd2,schema2);
		try {
			compare.compare();
			this.result=compare.getResultHtml();
			File tmp = File.createTempFile("dbcompare", ".html");
			PrintWriter pw = new PrintWriter(tmp,"utf-8");
			pw.write(this.result);
			pw.close();
			Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+tmp.toURI().toString());  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			error.setText("比较错误");
			error.setBackground(Color.RED);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	public static void main(String[] args) {
		new UI().setVisible(true);
	}
}
