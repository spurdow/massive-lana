package gtg.virus.gtpr.db;

import gtg.virus.gtpr.db.Item.SQItem;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ItemHelper extends AbstractHelper implements IHelper<Item>{


    public ItemHelper(Context mContext) {
        super(mContext);
    }

    @Override
	public long add(Item item) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = mInstance.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		//cv.put(SQItem.ID, item.getId());
		cv.put(SQItem.PATH, item.getPath());
		
		long id = db.insertWithOnConflict(SQItem.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
		return id;
	}

	@Override
	public List<Item> list() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = mInstance.getReadableDatabase();
		
		Cursor c = db.rawQuery("SELECT * FROM " + SQItem.TABLE_NAME, null);
		List<Item> items = new ArrayList<Item>();
		if(c.moveToFirst()){
			do{
				Item item = new Item();
				item.setId(c.getLong(c.getColumnIndex(SQItem.ID)));
				item.setPath(c.getString(c.getColumnIndex(SQItem.PATH)));
				
				items.add(item);
				
			}while(c.moveToNext());
		}
		
		return items;
	}
	
	

}
