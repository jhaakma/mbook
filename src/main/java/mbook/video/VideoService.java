package mbook.video;

import java.util.ArrayList;

public interface VideoService {
    public abstract ArrayList<Video> getVideos();
    public abstract Video getVideo(String index);
    public abstract void createVideo( Video video );
    public abstract void deleteVideo( String name );
}
