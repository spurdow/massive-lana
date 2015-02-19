package gtg.virus.gtpr.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DavidLuvelleJoseph on 2/19/2015.
 */
public class RemoteBook {

    /**
     "status": "Success",
     "entities": {
     "ebooks": [
     {
     "id": "20",
     "user_id": "47",
     "division_id": "52",
     "title": "TEst",
     "synopsis": "test",
     "author": "test",
     "format": "epub",
     "filename": "Meyer, Stephenie-Breaking Dawn.epub",
     "uploaded_date": {
     "datetime ": "2015-02-19 22:55:59",
     "date": "Feb 19, 2015",
     "time": "22:55:59",
     "timestamp": 1424357759
     },
     "status": "1",
     "ebook_genre": [],
     "path": "assets/components/47/Default Shelf/Default Division/",
     "status_word": "Active"
     }
     ]
     }
     */
    @SerializedName("id")
    private long id;
    @SerializedName("user_id")
    private long user_id;
    @SerializedName("division_id")
    private long division_id;
    @SerializedName("title")
    private String title;
    @SerializedName("synopsis")
    private String synopsis;
    @SerializedName("author")
    private String author;
    @SerializedName("format")
    private String format;
    @SerializedName("filename")
    private String filename;
    @SerializedName("status")
    private int status;
    @SerializedName("path")
    private String path;
    @SerializedName("status_word")
    private String status_word;

    public RemoteBook() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getDivision_id() {
        return division_id;
    }

    public void setDivision_id(long division_id) {
        this.division_id = division_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStatus_word() {
        return status_word;
    }

    public void setStatus_word(String status_word) {
        this.status_word = status_word;
    }
}
