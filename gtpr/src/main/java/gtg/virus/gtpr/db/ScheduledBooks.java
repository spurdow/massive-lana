package gtg.virus.gtpr.db;


import com.google.gson.annotations.SerializedName;

public class ScheduledBooks extends Item{

    @SerializedName("book_id")
    private long book_id;

    @SerializedName("time")
    private String timeToAlarm;

    @SerializedName("info")
    private String info;

    @SerializedName("bit_week")
    private String bitWeek;

    public final class SQScheduledBooks extends SQItem{
        public final static String BOOK_ID = "_book_id";
        public final static String TIME_TO_ALARM = "_time_to_alarm";
        public final static String INFO = "info";
        public final static String BIT_WEEK = "bit_week";

        public final static String TABLE_NAME = "scheduled_books";

        public final static String KEY = "FOREIGN KEY (" + BOOK_ID + ") REFERENCES " + Book.SQBook.TABLE_NAME + "(" +Book.SQBook.ID+ ")";

        public final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                PATH + " TEXT , " + BOOK_ID + " INTEGER , " +TIME_TO_ALARM + " TEXT NOT NULL, " + INFO + " TEXT , " + STATUS + " INTEGER , " + BIT_WEEK + " TEXT , " +KEY+ " );"  ;

        public final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    }

    public ScheduledBooks() {
    }

    public long getBook_id() {
        return book_id;
    }

    public void setBook_id(long book_id) {
        this.book_id = book_id;
    }

    public String getTimeToAlarm() {
        return timeToAlarm;
    }

    public void setTimeToAlarm(String timeToAlarm) {
        this.timeToAlarm = timeToAlarm;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getBitWeek() {
        return bitWeek;
    }

    public void setBitWeek(String bitWeek) {
        this.bitWeek = bitWeek;
    }
}
