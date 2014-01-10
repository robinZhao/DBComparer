package com.github.robinzhao.dbcompare;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class DBLoginPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField connField;
	private JTextField userField;
	private JTextField pwdField;
	private JTextField schemaField;
	public DBLoginPanel() {
		super();
		JPanel labPanel=new JPanel();
		JPanel fieldPanel= new JPanel();
		labPanel.setLayout(new GridLayout(4,1));
		fieldPanel.setLayout(new GridLayout(4,1));
		  JLabel labelConn=new JLabel("数据库连接:");
		  JLabel labelUser=new JLabel("用户名:");
		  JLabel labelPwd=new JLabel("密码:");
		  JLabel labelSchema=new JLabel("schema:");
		connField= new JTextField();
		connField.setColumns(30);
		userField= new JTextField();
		pwdField= new JPasswordField();
		schemaField=new JTextField();
		labPanel.add(labelConn);
		fieldPanel.add(connField);
		labPanel.add(labelUser);
		fieldPanel.add(userField);
		labPanel.add(labelPwd);
		fieldPanel.add(pwdField);
		labPanel.add(labelSchema);
		fieldPanel.add(schemaField);
		this.setLayout(new BorderLayout());
		this.add(labPanel,BorderLayout.WEST);
		this.add(fieldPanel,BorderLayout.CENTER);
	}
	
	public String getUser(){
		return this.userField.getText();
	};
	public String getPwd(){
		return new String(this.pwdField.getText());
	};
	public String getUrl(){
		return this.connField.getText();
		
	};
	public String getSchema(){
		return this.schemaField.getText();
	}
}
