package gtg.virus.gtpr.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import gtg.virus.gtpr.entities.PBook;

public class BookHelper extends AbstractHelper implements IHelper<Book> {


    public BookHelper(Context mContext) {
        super(mContext);
    }

    @Override
    public long add(Book item) {
        SQLiteDatabase db = mInstance.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Book.SQBook.PATH , item.getPath());
        cv.put(Book.SQBook.STATUS, item.getStatus());
        cv.put(Book.SQBook.TITLE , item.getTitle());

        return db.insertWithOnConflict(Book.SQBook.TABLE_NAME , null, cv, SQLiteDatabase.CONFLICT_REPLACE);

        //return ;
    }

    public Book getBook(String path){
        SQLiteDatabase db = mInstance.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + Book.SQBook.TABLE_NAME + " WHERE " + Book.SQBook.PATH + "=?", new String[]{path});

        if(c.moveToFirst()){
            Book b = new Book();
            b.setId(c.getLong(c.getColumnIndex(Book.SQBook.ID)));
            b.setTitle(c.getString(c.getColumnIndex(Book.SQBook.TITLE)));
            b.setPath(c.getString(c.getColumnIndex(Book.SQBook.PATH)));
            b.setStatus(c.getInt(c.getColumnIndex(Book.SQBook.STATUS)));

            return b;
        }
        return null;
    }

    @Override
    public List<Book> list() {

        SQLiteDatabase db = mInstance.getReadableDatabase();

        List<Book> books = new ArrayList<Book>();

        Cursor c = db.rawQuery("SELECT * FROM " + Book.SQBook.TABLE_NAME , null);

        if(c.moveToFirst()){
            Book b = new Book();
            b.setId(c.getLong(c.getColumnIndex(Book.SQBook.ID)));
            b.setTitle(c.getString(c.getColumnIndex(Book.SQBook.TITLE)));
            b.setPath(c.getString(c.getColumnIndex(Book.SQBook.PATH)));
            b.setStatus(c.getInt(c.getColumnIndex(Book.SQBook.STATUS)));

            books.add(b);
        }

        return books;
    }


}
