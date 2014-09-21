package gtg.virus.gtpr.db;



import gtg.virus.gtpr.db.Item.SQItem;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public final class DatabaseHelper extends SQLiteOpenHelper{

	public final static int DATABASE_VERSION = 3;
	
	public final static String DATABASE_NAME = "pinbook.db";
	
	private static DatabaseHelper mInstance = null;
	
	private DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	
	}
	
	public static DatabaseHelper getInstance(Context context){
		if(mInstance == null){
			mInstance = new DatabaseHelper(context);
		}
		return mInstance;
	}
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(SQItem.CREATE_TABLE);
        db.execSQL(Book.SQBook.CREATE_TABLE);
        db.execSQL(ScheduledBooks.SQScheduledBooks.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if(newVersion > oldVersion){
			db.execSQL(SQItem.DROP_TABLE);
            db.execSQL(Book.SQBook.DROP_TABLE);
            db.execSQL(ScheduledBooks.SQScheduledBooks.DROP_TABLE);
		}
		onCreate(db);
	}

	
	

}
