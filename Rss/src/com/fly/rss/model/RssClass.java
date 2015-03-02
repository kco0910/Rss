package com.fly.rss.model;

import java.util.List;

import com.ab.db.orm.annotation.Column;
import com.ab.db.orm.annotation.Id;
import com.ab.db.orm.annotation.Relations;
import com.ab.db.orm.annotation.Table;
@Table(name = "tb_rssClass")
public class RssClass {
	/** 分类名称*/
	@Column(name = "className")
	private String className = "";
	
	@Id
	@Column(name = "_id")
	private int _id = 0;
	
	/** 分类ID*/
	@Column(name = "rssClassId")
	private int classId = 0;
	
	@Relations(name = "sites",action="query_insert",foreignKey="rssClassId",type="one2many")
	private List<RssSite> sites = null;
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public int getClassId() {
		return classId;
	}
	public void setClassId(int classId) {
		this.classId = classId;
	}
	public List<RssSite> getSites() {
		return sites;
	}
	public void setSites(List<RssSite> sites) {
		this.sites = sites;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer buffer = new StringBuffer();
		buffer.append("分类名称:"+className+",分类ID:"+classId);
		if(sites != null){
			for(RssSite rssSite : sites){
				buffer.append(rssSite.toString());
			}
		}else{
			buffer.append("--- > 站点为空");
		}
		return buffer.toString();
	}
	
	public int get_id() {
		return _id;
	}
	
	
	public void set_id(int _id) {
		this._id = _id;
	}
	
}
