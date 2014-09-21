package gtg.virus.gtpr.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ScheduleHelper extends AbstractHelper implements IHelper<ScheduledBooks> , IForeignQuery<ScheduledBooks> {
    public ScheduleHelper(Context mContext) {
        super(mContext);
    }


    @Override
    public long add(ScheduledBooks item) {
        SQLiteDatabase db = mInstance.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(ScheduledBooks.SQScheduledBooks.BOOK_ID, item.getBook_id());
        cv.put(ScheduledBooks.SQScheduledBooks.TIME_TO_ALARM , item.getTimeToAlarm());
        cv.put(ScheduledBooks.SQScheduledBooks.INFO , item.getInfo());
        cv.put(ScheduledBooks.SQScheduledBooks.BIT_WEEK , item.getBitWeek());
        cv.put(ScheduledBooks.SQScheduledBooks.STATUS , item.getStatus());

        return db.insertWithOnConflict(ScheduledBooks.SQScheduledBooks.TABLE_NAME , null, cv, SQLiteDatabase.CONFLICT_REPLACE);

    }

    public ScheduledBooks getSchedBook(long book_id){

        SQLiteDatabase db = mInstance.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + ScheduledBooks.SQScheduledBooks.TABLE_NAME + " WHERE " + ScheduledBooks.SQScheduledBooks.BOOK_ID +" =?", new String[]{String.valueOf(book_id)});

        if(c.moveToFirst()){

                ScheduledBooks book = new ScheduledBooks();
                book.setId(c.getLong(c.getColumnIndex(ScheduledBooks.SQScheduledBooks.ID)));
                book.setBook_id(c.getLong(c.getColumnIndex(ScheduledBooks.SQScheduledBooks.BOOK_ID)));
                book.setInfo(c.getString(c.getColumnIndex(ScheduledBooks.SQScheduledBooks.INFO)));
                book.setTimeToAlarm(c.getString(c.getColumnIndex(ScheduledBooks.SQScheduledBooks.TIME_TO_ALARM)));
                book.setBitWeek(c.getString(c.getColumnIndex(ScheduledBooks.SQScheduledBooks.BIT_WEEK)));
                book.setStatus(c.getInt(c.getColumnIndex(ScheduledBooks.SQScheduledBooks.STATUS)));

            return book;
        }


        return null;
    }

    @Override
    public List<ScheduledBooks> list() {
        List<ScheduledBooks> sBooks = new ArrayList<ScheduledBooks>();
        SQLiteDatabase db = mInstance.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + ScheduledBooks.SQScheduledBooks.TABLE_NAME, null);

        if(c.moveToFirst()){
            do{
                ScheduledBooks book = new ScheduledBooks();
                book.setId(c.getLong(c.getColumnIndex(ScheduledBooks.SQScheduledBooks.ID)));
                book.setBook_id(c.getLong(c.getColumnIndex(ScheduledBooks.SQScheduledBooks.BOOK_ID)));
                book.setInfo(c.getString(c.getColumnIndex(ScheduledBooks.SQScheduledBooks.INFO)));
                book.setTimeToAlarm(c.getString(c.getColumnIndex(ScheduledBooks.SQScheduledBooks.TIME_TO_ALARM)));
                book.setBitWeek(c.getString(c.getColumnIndex(ScheduledBooks.SQScheduledBooks.BIT_WEEK)));
                book.setStatus(c.getInt(c.getColumnIndex(ScheduledBooks.SQScheduledBooks.STATUS)));
                sBooks.add(book);
            }while(c.moveToNext());
        }


        return sBooks;
    }

    @Override
    public List<ScheduledBooks> list(long foreign_key_id) {
        List<ScheduledBooks> sBooks = new ArrayList<ScheduledBooks>();
        SQLiteDatabase db = mInstance.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + ScheduledBooks.SQScheduledBooks.TABLE_NAME + " WHERE " + ScheduledBooks.SQScheduledBooks.BOOK_ID +" =?", new String[]{String.valueOf(foreign_key_id)});

        if(c.moveToFirst()){
            do{
                ScheduledBooks book = new ScheduledBooks();
                book.setId(c.getLong(c.getColumnIndex(ScheduledBooks.SQScheduledBooks.ID)));
                book.setBook_id(c.getLong(c.getColumnIndex(ScheduledBooks.SQScheduledBooks.BOOK_ID)));
                book.setInfo(c.getString(c.getColumnIndex(ScheduledBooks.SQScheduledBooks.INFO)));
                book.setTimeToAlarm(c.getString(c.getColumnIndex(ScheduledBooks.SQScheduledBooks.TIME_TO_ALARM)));
                book.setBitWeek(c.getString(c.getColumnIndex(ScheduledBooks.SQScheduledBooks.BIT_WEEK)));
                book.setStatus(c.getInt(c.getColumnIndex(ScheduledBooks.SQScheduledBooks.STATUS)));
                sBooks.add(book);
            }while(c.moveToNext());
        }


        return sBooks;
    }

}
