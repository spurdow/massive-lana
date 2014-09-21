package gtg.virus.gtpr.db;


import android.content.Context;

import java.util.List;

import gtg.virus.gtpr.entities.PBook;

public class BookHelper extends AbstractHelper implements IHelper<Book> {


    public BookHelper(Context mContext) {
        super(mContext);
    }

    @Override
    public long add(Book item) {


        return 0;
    }

    @Override
    public List<Book> list() {
        return null;
    }


}
