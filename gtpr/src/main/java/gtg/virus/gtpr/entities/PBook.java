package gtg.virus.gtpr.entities;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.radaee.pdf.Page;

public class PBook {
	
	private List<PPage> pages;

    @Expose(serialize = true, deserialize = true)
	@SerializedName("b_title")
	private String title = "NA";

    @Expose(serialize = true, deserialize = true)
	@SerializedName("b_authors")
	private List<String> authors;

    @Expose(serialize = true, deserialize = true)
	@SerializedName("b_path")
	private String path = "NA";

    @Expose(serialize = true, deserialize = true)
	@SerializedName("b_fname")
	private String filename = "NA";

    @Expose(serialize = true, deserialize = true)
	@SerializedName("b_tag")
	private Object tag;

    @Expose(serialize = true, deserialize = true)
	@SerializedName("b_isEpub")
	public boolean isEpub;

    @Expose(serialize = true, deserialize = true)
	@SerializedName("b_isPdf")
	public boolean isPdf;


    private Bitmap page0;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("status")
    public int status= 0;


    /**
	 * @param pages
	 * @param title
	 * @param author
	 * @param path
	 */
	public PBook(List<PPage> pages, String title, String author, String path , Object tag , Bitmap frontPage) {
		super();
		this.authors = new ArrayList<String>();
		this.pages = pages;
		this.title = title;
		this.path = path;
		this.tag = tag;
		this.page0 = frontPage;
		this.authors.add(author);
		
	}

	public PBook() {
		// TODO Auto-generated constructor stub
		this.authors = new ArrayList<String>();
	}

	/**
	 * @return the pages
	 */
	public List<PPage> getPages() {
		return pages;
	}

	/**
	 * @param pages the pages to set
	 */
	public void setPages(List<PPage> pages) {
		this.pages = pages;
	}

	/**
	 * @return the name
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param name the name to set
	 */
	public void setTitle(String name) {
		this.title = name;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @param author the author to set
	 */
	public void addAuthor(String author) {
		if(authors == null){
			authors = new ArrayList<String>();
		}
		authors.add(author);
	}

	/**
	 * @return the tag
	 */
	public Object getTag() {
		return tag;
	}

	/**
	 * @param tag the tag to set
	 */
	public void setTag(Object tag) {
		this.tag = tag;
	}

	public Bitmap getPage0() {
		return page0;
	}

	public void setPage0(Bitmap page0) {
		this.page0 = page0;
	}

	public List<String> getAuthors() {
		return authors;
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}
	

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(this, PBook.class);
	}
	
	

	
}
