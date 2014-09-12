package gtg.virus.gtpr.entities;

import android.graphics.Bitmap;

public class Menu {
	
	private Bitmap image;
	private String title;
	
	
	
	/**
	 * @param image
	 * @param title
	 */
	public Menu(Bitmap image, String title) {
		super();
		this.image = image;
		this.title = title;
	}
	/**
	 * @return the image
	 */
	public Bitmap getImage() {
		return image;
	}
	/**
	 * @param image the image to set
	 */
	public void setImage(Bitmap image) {
		this.image = image;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	
	
}
