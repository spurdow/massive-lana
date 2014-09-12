package gtg.virus.gtpr.entities;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class User {
	

	private long id;
	
	@SerializedName("fullname")
	private String fullname;
	
	@SerializedName("facebook_id")
	private String facebook_id;
	
	@SerializedName("photo")
	private String photo;
	
	@SerializedName("status")
	private int status;
	
	

	/**
	 * @param id
	 * @param fullname
	 * @param facebook_id
	 * @param photo
	 * @param status
	 */
	public User(long id, String fullname, String facebook_id, String photo,
			int status) {
		super();
		this.id = id;
		this.fullname = fullname;
		this.facebook_id = facebook_id;
		this.photo = photo;
		this.status = status;
	}

	
	/**
	 * 
	 */
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}


	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the fullname
	 */
	public String getFullname() {
		return fullname;
	}

	/**
	 * @param fullname the fullname to set
	 */
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	/**
	 * @return the facebook_id
	 */
	public String getFacebook_id() {
		return facebook_id;
	}

	/**
	 * @param facebook_id the facebook_id to set
	 */
	public void setFacebook_id(String facebook_id) {
		this.facebook_id = facebook_id;
	}

	/**
	 * @return the photo
	 */
	public String getPhoto() {
		return photo;
	}

	/**
	 * @param photo the photo to set
	 */
	public void setPhoto(String photo) {
		this.photo = photo;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return new Gson().toJson(this, User.class);
	}
	
	
	
}
