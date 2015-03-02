package com.fly.rss.model;

import com.ab.db.orm.annotation.Column;
import com.ab.db.orm.annotation.Id;
import com.ab.db.orm.annotation.Table;

@Table(name = "tb_rssSite")
public class RssSite {
	@Id
	@Column(name = "_id")
	private int id = 0;
	
	@Column(name = "rssTitle")
	private String rssTitle = "";
	
	@Column(name = "rssLink")
	private String rssLink = "";
	
	@Column(name = "rssClassId")
	private int rssClassId = 0;
	
	public String getRssTitle() {
		return rssTitle;
	}
	public void setRssTitle(String rssTitle) {
		this.rssTitle = rssTitle;
	}
	public String getRssLink() {
		return rssLink;
	}
	public void setRssLink(String rssLink) {
		this.rssLink = rssLink;
	}
	public int getRssClassId() {
		return rssClassId;
	}
	public void setRssClassId(int rssClassId) {
		this.rssClassId = rssClassId;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer buffer = new StringBuffer();
		buffer.append("站点名称:"+getRssTitle()).append("站点链接:"+getRssLink()).append("\n");
		return buffer.toString();
	}
}
