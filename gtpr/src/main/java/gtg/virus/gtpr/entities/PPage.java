package gtg.virus.gtpr.entities;

public class PPage {
	
	private long id;
	
	private int page_no;

	private int book_marked;

	public PPage(long id, int page_no, int book_marked) {
		super();
		this.id = id;
		this.page_no = page_no;
		this.book_marked = book_marked;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getPage_no() {
		return page_no;
	}

	public void setPage_no(int page_no) {
		this.page_no = page_no;
	}

	public int getBook_marked() {
		return book_marked;
	}

	public void setBook_marked(int book_marked) {
		this.book_marked = book_marked;
	}
	
	
}
