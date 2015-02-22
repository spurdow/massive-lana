package gtg.virus.gtpr.async;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.radaee.pdf.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import gtg.virus.gtpr.R;
import gtg.virus.gtpr.aaentity.AABook;
import gtg.virus.gtpr.entities.PBook;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

import static gtg.virus.gtpr.utils.Utilities.STORAGE_SUFFIX;
import static gtg.virus.gtpr.utils.Utilities.bookCache;
import static gtg.virus.gtpr.utils.Utilities.copy;
import static gtg.virus.gtpr.utils.Utilities.isEpub;
import static gtg.virus.gtpr.utils.Utilities.isMp3;
import static gtg.virus.gtpr.utils.Utilities.isPdf;
import static gtg.virus.gtpr.utils.Utilities.isTxt;
import static gtg.virus.gtpr.utils.Utilities.isValideBook;
import static gtg.virus.gtpr.utils.Utilities.makeText;
import static gtg.virus.gtpr.utils.Utilities.renderPage;

/**
 * Created by DavidLuvelleJoseph on 2/16/2015.
 */
public class AddNewBookTask extends AsyncTask<String , Void , PBook> {

    private static final String TAG = AddNewBookTask.class.getSimpleName();
    private Context mContext;
    private ProgressDialog mProgress;

    private Document mDoc;
    private OnFinishTask onFinishTask;

    public interface OnFinishTask{
        void onFinish(PBook pbook);
    }

    private State mState = State.None;
    public enum State{
        None,
        Pdf,
        Epub,
        Txt,
        Mp3
    }

    public AddNewBookTask(Context mContext , OnFinishTask onFinishTask) {
        this.mContext = mContext;
        this.onFinishTask = onFinishTask;
    }

    public AddNewBookTask(Context mContext, OnFinishTask onFinishTask, State mState) {
        this.mContext = mContext;
        this.mState = mState;
        this.onFinishTask = onFinishTask;
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        mProgress = new ProgressDialog(mContext);
        mProgress.setMessage("Please wait...");
        mProgress.setIndeterminate(true);
        mProgress.show();
    }
    @Override
    protected PBook doInBackground(String... params) {
        final String path  = params[0];
        File newPdf = new File(path);
        Log.w(TAG, "File " + newPdf.getName());
        File toStorage = new File(Environment.getExternalStorageDirectory() +"/"+ STORAGE_SUFFIX + "/" + newPdf.getName() );

        try {
            copy( newPdf , toStorage );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            // unsuccessful copy
            makeText(mContext , "Theres a problem with the disk.");
            return null;
        }

        // Get the File path from the Uri

        PBook b = null;
        Log.i(TAG, "Path " + path);
        // Alternatively, use FileUtils.getFile(Context, Uri)
        if(path != null && isValideBook(path) && FileUtils.isLocal(path) && mState != State.None) {
            if(isMp3(path) && mState == State.Mp3){
                b = new PBook();
                MediaMetadataRetriever meta = new MediaMetadataRetriever();
                meta.setDataSource(path);

                byte[] art = meta.getEmbeddedPicture();
                Bitmap songImage = null;
                try {
                    songImage = BitmapFactory.decodeByteArray(art, 0, art.length);
                }catch(Exception ex){

                }
                b.setPage0(songImage);
                b.addAuthor(meta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR));
                b.setFilename(newPdf.getName());
                b.setPath(path);
                b.setTitle(newPdf.getName());

                meta.release();
                Log.w(TAG , "mp3 file added. ");
            }
        }else if (path != null && isValideBook(path) && FileUtils.isLocal(path)) {
            Log.w(TAG, "Valid Book");
            if(isPdf(path)){


                mDoc = new Document();
                int index = mDoc.Open(path, "");

                if( index == 0){
                    String author = mDoc.GetMeta("Author");
                    String title = mDoc.GetMeta("Title");
                    String subject = mDoc.GetMeta("Subject");

                    Log.i(TAG,"Meta " + author + " " + title + " " + subject);
                    b = new PBook();
                    b.addAuthor(author);
                    b.setTitle(title);
                    b.setPath(path);;
                    Bitmap page0 = renderPage(mDoc , 100, 100);
                    b.setPage0(page0);
                    b.isPdf= true;
                    b.setFilename(newPdf.getName());

                    // we are not accepting file if the file doesnt give us the title

                    if(title == null){
                        makeText(mContext , "File is invalid");
                        b = null;
                    }else if(title.equals("")){
                        makeText(mContext , "File is invalid");
                        b = null;
                    }

                }else if(index == -1){
                    makeText(mContext , "Password needed");
                }else if(index == -2){
                    makeText(mContext , "Unknown Encryption");
                }else if(index == -3){
                    makeText(mContext , "Damage or Invalid Format");
                }else if(index == -10){
                    makeText(mContext , "Access Denied");
                }

            }else if(isEpub(path)){
                EpubReader epubReader = new EpubReader();
                Book epubBook = null;
                try {
                    epubBook = epubReader.readEpub(new FileInputStream(path));
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if(epubBook != null){
                    b = new PBook();
                    final String title = epubBook.getTitle();
                    for(Author auth : epubBook.getMetadata().getAuthors()){
                        b.addAuthor(auth.getFirstname() + " " + auth.getLastname());
                    }
                    b.isEpub = true;
                    b.setTitle(title);
                    b.setPath(path);
                    b.setFilename(newPdf.getName());
                    Bitmap page0 = null;
                    try {
                        page0 = BitmapFactory.decodeStream(epubBook.getCoverImage().getInputStream());

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if(page0 != null){
                        b.setPage0(page0);
                    }
                }
            }else if(isTxt(path)){
                b = new PBook();
                b.setTitle("TextFile");
                Bitmap page0 = BitmapFactory.decodeResource(mContext.getResources() , R.drawable.ic_content_paste);
                b.setPage0(page0);
                b.setPath(path);
                b.setFilename(newPdf.getName());
                Log.w(TAG , "txt file added");
            }else if(isMp3(path)){
                b = new PBook();
                MediaMetadataRetriever meta = new MediaMetadataRetriever();
                meta.setDataSource(path);

                byte[] art = meta.getEmbeddedPicture();
                Bitmap songImage = null;
                try {
                    songImage = BitmapFactory.decodeByteArray(art, 0, art.length);
                }catch(Exception ex){

                }
                b.setPage0(songImage);
                b.addAuthor(meta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR));
                b.setFilename(newPdf.getName());
                b.setPath(path);
                b.setTitle(newPdf.getName());

                meta.release();
                Log.w(TAG , "mp3 file added. ");
            }

        }else if(!isValideBook(path)){
            makeText(mContext, "File is not a pdf/epub/txt/audio");
        }
        Log.w(TAG, "doInBackground");
        return b;
    }

    @Override
    protected void onPostExecute(PBook result) {
        super.onPostExecute(result);

        if(onFinishTask != null){
            if(result != null){
                AABook newBook = new AABook();
                newBook.title = result.getTitle();
                newBook.path = result.getPath();
                newBook.status = 1;


                long id = newBook.save();
                if(id > 0){

                    /*mShelfAdapter.addBook(result);*/
                    onFinishTask.onFinish(result);
                    bookCache.put(result.getFilename(), result);
                    Log.w(TAG , "Added Bookid => " + id) ;
                }else{
                    AlertDialog alert = new AlertDialog.Builder(mContext)
                            .setMessage(mContext.getString(R.string.provide_correct_ebook))
                            .setNegativeButton(R.string.ok , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                    alert.show();
                }


            }
        }

        mProgress.dismiss();

        if(mDoc != null){
            mDoc.Close();
            mDoc = null;
        }
    }
}
