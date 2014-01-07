package com.github.robinzhao.dbcompare;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class DBCompare {
	String url1 = "jdbc:sqlserver://JD18\\SQL_2008_R2;DatabaseName=PO_BOM_QA";
	String user1 = "bom";
	String pwd1 = "app";
	String schema1 = "bom";
	String url2 = "jdbc:sqlserver://JD18\\SQL_2008_R2;DatabaseName=ISPM_QA";
	String user2 = "sa";
	String pwd2 = "sa123";
	String schema2 = "bom";
	
	String resultHtml = "";
	
	
	public DBCompare(String url1, String user1, String pwd1, String schema1,
			String url2, String user2, String pwd2, String schema2){
		this.url1=url1;
		this.user1=user1;
		this.pwd1=pwd1;
		this.schema1=schema1;
		this.url2=url2;
		this.user2=user2;
		this.pwd2=pwd2;
		this.schema2=schema2;
		
	}
	
	public DBCompare(){
	}

	public Connection getConn() throws SQLException {
		Connection conn = DriverManager.getConnection(url1, "bom", "app");
		return conn;
	}

	public List<String> getTables(Connection conn, String schema) throws SQLException {
		DatabaseMetaData metaData = conn.getMetaData();
		List<String> tables = new ArrayList<String>();
		ResultSet tableSet = metaData.getTables(conn.getCatalog(), schema,
				null, new String[] { "TABLE" });
		while (tableSet.next()) {
			tables.add(tableSet.getString("TABLE_NAME"));
		}
		return tables;
	}

	public List<String> getColumns(Connection conn, String schema, String tableName)
			throws SQLException {
		DatabaseMetaData metaData = conn.getMetaData();
		List<String> columns = new ArrayList<String>();
		ResultSet columnSet = metaData.getColumns(conn.getCatalog(), schema,
				tableName, null);
		while (columnSet.next()) {
			columns.add(columnSet.getString("COLUMN_NAME"));
		}
		return columns;
	}

	private void printCollection(Collection<String> c) {
		if (null == c || c.size() == 0) {
			this.resultHtml+=("&nbsp;");
			return;
		}
		for (Object tn : c) {
			this.resultHtml+=(tn + "</br>");
		}
	}

	// 返回交集
	public void compareList(List<String> list1, List<String> list2, Collection<String> list1Short,
			Collection<String> list2Short, Collection<String> commonList) {
		HashSet<String> allSet = new HashSet<String>();
		allSet.addAll(list1);
		allSet.addAll(list2);
		// tables1中缺少的
		list1Short.addAll(allSet);
		list1Short.removeAll(list1);

		list2Short.addAll(allSet);
		list2Short.removeAll(list2);
		commonList.addAll(allSet);
		commonList.removeAll(list1Short);
		commonList.removeAll(list2Short);
	}

	public void compare(String url1, String user1, String pwd1, String schema1,
			String url2, String user2, String pwd2, String schema2)
			throws SQLException {
		System.out.println(user1+"\t"+pwd1);
		Connection conn1 = DriverManager.getConnection(url1, user1, pwd1);
		System.out.println(user2+"\t"+pwd2);
		Connection conn2 = DriverManager.getConnection(url2, user2, pwd2);
		String db1 = conn1.getCatalog();
		String db2 = conn2.getCatalog();
		List<String> list1 = this.getTables(conn1, schema1);
		List<String> list2 = this.getTables(conn2, schema2);
		this.resultHtml+=("<table border='0' cellspacing='0'><tr class='head1'><td colspan='3'>");
		this.resultHtml+=("表比较 " + db1 + "," + db2);
		this.resultHtml+=("</td></tr>\r\n");
		Collection<String> list1Short = new ArrayList<String>();
		Collection<String> list2Short = new ArrayList<String>();
		Collection<String> commonList = new ArrayList<String>();
		this.compareList(list1, list2, list1Short, list2Short, commonList);
		this.resultHtml+=("<tr class='head3'><td>&nbsp;</td><td>" + db1 + "比"
				+ db2 + "少的表</td><td>" + db1 + "比" + db2 + "多的表</td></tr>\r\n");
		this.resultHtml+=("<tr><td>&nbsp;</td><td>");
		this.printCollection(list1Short);
		this.resultHtml+=("</td><td>");
		this.printCollection(list2Short);
		this.resultHtml+=("</td></tr>\r\n");
		this.resultHtml+=("<tr class='head2'><td colspan='3'>列比较</td></tr>\r\n");
		this.resultHtml+=("<tr  class='head3'><td>表名</td><td>" + db1 + "比"
				+ db2 + "少的列</td><td>" + db1 + "比" + db2 + "多的列</td></tr>\r\n");
		for (Object tableName : commonList) {
			this.tableColumnCompare(conn1, conn2, schema1, schema2,
					(String) tableName, (String) tableName);
		}
		this.resultHtml+=("</table>");
	}

	public void tableColumnCompare(Connection conn1, Connection conn2,
			String schema1, String schema2, String tableName1, String tableName2)
			throws SQLException {
		List<String> columns1 = this.getColumns(conn1, schema1, tableName1);
		List<String> columns2 = this.getColumns(conn2, schema2, tableName2);
		Collection<String> list1Short = new ArrayList<String>();
		Collection<String> list2Short = new ArrayList<String>();
		Collection<String> commonList = new ArrayList<String>();
		this.compareList(columns1, columns2, list1Short, list2Short, commonList);
		if (!list1Short.isEmpty() || !list2Short.isEmpty()) {
			this.resultHtml+=("<tr><td class='head4'>" + tableName1 + "</td><td>");
			this.printCollection(list1Short);
			this.resultHtml+=("</td><td>");
			this.printCollection(list2Short);
			this.resultHtml+=("</td></tr>\r\n");
		}
	}

	public void ColumnDetailCompare() {

	}

	public void compare() throws SQLException {
		this.resultHtml+=("<html><head>" + "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" /><style type='text/css'>"
				+ ".head1{" + "background:#cccccc;" + "font-size:20px;"
				+ "font-weight:bold;" + "}" + ".head2{" + "background:#cccccc;"
				+ "font-size:15px;" + "font-weight:bold;" + "}" + ".head3{"
				+ "background:#cccccc;" + "font-size:12px;"
				+ "font-weight:bold;" + "}" +".head4{"
				+ "background:#cccccc;" + "font-size:12px;"
				+ "font-weight:bold;" + "}" + "table {"
				+ "border: solid 1px #666666;" + "}" + "table td{"
				+ "border: solid 1px #666666;" + "}" + "</style>" + "</head>"
				+ "<body>");
		this.compare(url1, user1, pwd1, schema1, url2, user2, pwd2, schema2);
		this.resultHtml+=("</body></html>");
	}
	
	public String getResultHtml(){
		return this.resultHtml;
	}

}
