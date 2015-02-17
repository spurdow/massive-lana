package gtg.virus.gtpr.entities;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class Menu {
	
	private Drawable image;
	private String title;
	
	
	
	/**
	 * @param image
	 * @param title
	 */
	public Menu(Drawable image, String title) {
		super();
		this.image = image;
		this.title = title;
	}
	/**
	 * @return the image
	 */
	public Drawable getImage() {
		return image;
	}
	/**
	 * @param image the image to set
	 */
	public void setImage(Drawable image) {
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
