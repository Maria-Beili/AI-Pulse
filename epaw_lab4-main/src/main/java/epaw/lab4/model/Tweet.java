package epaw.lab4.model;

import java.sql.Timestamp;

public class Tweet implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private int uid;
	private String uname;
	private String upicture;
	private Timestamp postDateTime;
	private String content;
	private String image;
	private Integer pid;
	private int likesCount;
	private boolean likedByMe;

	public Tweet() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getUid() {
		return this.uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getUname() {
		return this.uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getUpicture() {
		return this.upicture;
	}

	public void setUpicture(String upicture) {
		this.upicture = upicture;
	}

	public Timestamp getPostDateTime() {
		return this.postDateTime;
	}

	public void setPostDateTime(Timestamp postDateTime) {
		this.postDateTime = postDateTime;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public int getLikesCount() {
		return likesCount;
	}

	public void setLikesCount(int likesCount) {
		this.likesCount = likesCount;
	}

	public boolean isLikedByMe() {
		return likedByMe;
	}

	public void setLikedByMe(boolean likedByMe) {
		this.likedByMe = likedByMe;
	}
}
