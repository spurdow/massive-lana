package gtg.virus.gtpr.entities;



public class Audio {

    private long id;

    private String title;

    private String path;

    private String details;

    private long time;

    public Audio(long id, String title, String path, String details , long time) {
        this.id = id;
        this.title = title;
        this.path = path;
        this.details = details;
        this.time = time;
    }

    public Audio() {
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    protected boolean isPlay = false;

    public void setIsPlay(boolean isPlay){
        this.isPlay = isPlay;
    }

    public boolean getIsPlay(){
        return isPlay;
    }
}
