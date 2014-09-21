package gtg.virus.gtpr.db;

import com.google.gson.annotations.SerializedName;

public class Item {
	

    @SerializedName("id")
	private long id;

    @SerializedName("path")
	private String path;

    @SerializedName("status")
    private int status;

	
	public static class SQItem{
		public final static String ID = "_id";
		public final static String PATH = "_path";
		public final static String TABLE_NAME = "item";
        public final static String STATUS = "status";
		
		
		public final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + 
		STATUS + " INTEGER , " +PATH + " TEXT );" ;
		
		public final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
	}
	
	public Item(long id, String path , int status) {
		super();
		this.id = id;
		this.path = path;
        this.status = status;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
