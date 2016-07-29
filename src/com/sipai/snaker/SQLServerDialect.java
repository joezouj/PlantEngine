package com.sipai.snaker;

import org.snaker.engine.access.Page;
import org.snaker.engine.access.dialect.Dialect;

/**
 * MSSQL2008以下版本方言辅助类
 */
public class SQLServerDialect implements Dialect {
	private static final String STR_ORDERBY = " order by ";
	public String getPageSql(String sql, Page<?> page) {
		int orderIdx = sql.indexOf(STR_ORDERBY);
        String orderStr = null;
        String orderStr2 = null;
		if(orderIdx != -1) {
            orderStr = sql.substring(orderIdx + 10);
			sql = sql.substring(0, orderIdx);
			orderStr2 = orderStr.substring(0, orderStr.indexOf(" "));
			if(orderStr.indexOf(" desc") != -1) {
				orderStr2 += " asc";
			} else {
				orderStr2 += " desc";
			}
		}
		
		if(orderStr == null) {
			orderStr = "id";
		}
		if(orderStr2 == null) {
			orderStr2 = "id";
		}
		
		StringBuffer pageSql = new StringBuffer();
		pageSql.append("select * from (");
		pageSql.append("select top ").append(page.getPageSize()).append(" * from (");
		pageSql.append("select top ").append(page.getPageNo() * page.getPageSize()).append(" * from (");
		pageSql.append(sql);
		pageSql.append(") t1 ").append(STR_ORDERBY).append(orderStr);
		pageSql.append(") t2 ").append(STR_ORDERBY).append(orderStr2);
		pageSql.append(") t3 ").append(STR_ORDERBY).append(orderStr);
		return pageSql.toString();
	}
}
