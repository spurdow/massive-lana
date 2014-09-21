package gtg.virus.gtpr.db;


import com.google.gson.annotations.SerializedName;

public class Book extends Item{

    @SerializedName("ebook_title")
    private String title;



    public static class SQBook extends SQItem{

        public final static String TITLE = "_title";

        public final static String TABLE_NAME = "book";


        public final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                PATH + " TEXT UNIQUE, " + TITLE + " TEXT , "  + STATUS + " INTEGER )";

        public final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public Book() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
