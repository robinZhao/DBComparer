package com.github.robinzhao.dbcompare;

import java.sql.SQLException;

import org.junit.Test;

public class DBCompareTest {
	@Test
	public void testDBcompare() throws SQLException{
		DBCompare dc = new DBCompare();
		dc.compare();
		System.out.println(dc.getResultHtml());
	}

}
