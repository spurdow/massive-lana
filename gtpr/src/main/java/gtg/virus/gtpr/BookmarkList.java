package gtg.virus.gtpr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import butterknife.InjectView;
import gtg.virus.gtpr.aaentity.AABook;
import gtg.virus.gtpr.aaentity.AABookmark;
import gtg.virus.gtpr.adapters.BookmarkAdapter;
import gtg.virus.gtpr.entities.PBook;
import gtg.virus.gtpr.parent.ParentFragment;
import gtg.virus.gtpr.utils.Utilities;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

import static gtg.virus.gtpr.utils.Utilities.PIN_EXTRA_PBOOK;

/**
 * Created by DavidLuvelleJoseph on 2/21/2015.
 */
public class BookmarkList extends ParentFragment implements AdapterView.OnItemClickListener {

    private static final String TAG = BookmarkList.class.getSimpleName();
    @InjectView(R.id.bookmark_list)
    ListView mListView;

    private BookmarkAdapter mAdapter;

    @Override
    public int resId() {
        return R.layout.bookmark_list;
    }

    @Override
    public boolean useButterKnife() {
        return true;
    }

    @Override
    public void overrideSetUpView(View createdView, LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        mAdapter = new BookmarkAdapter(getActivity());
        mListView.setAdapter(mAdapter);

        List<AABookmark> bookmarks = AABookmark.loadAll();

        for(AABookmark bMark : bookmarks){
            mAdapter.add(bMark);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AABookmark bookmark = mAdapter.getObject(position);
        AABook aabook = bookmark.book;
        PBook newBook = new PBook();

        EpubReader epubReader = new EpubReader();
        Book epubBook = null;

        File file = new File(aabook.path);
        try {
            epubBook = epubReader.readEpub(new FileInputStream(aabook.path));

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(epubBook != null){
            newBook = new PBook();
            final String title = epubBook.getTitle();
            for(Author auth : epubBook.getMetadata().getAuthors()){
                newBook.addAuthor(auth.getFirstname() + " " + auth.getLastname());
            }
            newBook.isEpub = true;
            newBook.setTitle(title);
            newBook.setPath(aabook.path);
            newBook.setFilename(file.getName());
            Bitmap page0 = null;
            try {
                page0 = BitmapFactory.decodeStream(epubBook.getCoverImage().getInputStream());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch(NullPointerException e){
                e.printStackTrace();
                page0 = BitmapFactory.decodeResource(getResources() , R.drawable.ic_content_paste);
            }
            if(page0 != null){
                newBook.setPage0(page0);
            }

            Intent intent = new Intent(getActivity(), GTGEpubViewer.class);
            intent.putExtra(PIN_EXTRA_PBOOK, newBook.toString());
            intent.putExtra(AbstractViewer.INDEX_KEY , bookmark.bookmark_page);
            getActivity().startActivity(intent);


        }else{
            Utilities.makeText(getActivity() , getString(R.string.failed_to_open_book));
        }


    }
}
