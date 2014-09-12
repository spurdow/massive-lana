package gtg.virus.gtpr.db;

public class Item {
	
	
	private long id;
	
	private String path;

	
	public static class SQItem{
		public final static String ID = "_id";
		public final static String PATH = "_path";
		public final static String TABLE_NAME = "item";
		
		
		public final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + 
		PATH + " TEXT );" ;
		
		public final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
	}
	
	public Item(long id, String path) {
		super();
		this.id = id;
		this.path = path;
	}

	public Item() {
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	
	
}
