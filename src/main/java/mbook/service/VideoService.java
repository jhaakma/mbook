package mbook.service;

import java.util.ArrayList;

import mbook.model.Video;

public interface VideoService {
    public abstract ArrayList<Video> getVideos();
    public abstract Video getVideo(String index);
    public abstract void createVideo( Video video );
    public abstract void deleteVideo( String name );
}
