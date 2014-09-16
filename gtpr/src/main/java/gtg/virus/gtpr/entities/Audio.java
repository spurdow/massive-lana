package gtg.virus.gtpr.entities;



public class Audio {

    private long id;

    private String title;

    private String path;

    private String details;

    public Audio(long id, String title, String path, String details) {
        this.id = id;
        this.title = title;
        this.path = path;
        this.details = details;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
