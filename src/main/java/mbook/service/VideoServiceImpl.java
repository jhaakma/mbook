package mbook.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import mbook.model.Video;

@Service
public class VideoServiceImpl implements VideoService {

    private static ArrayList<Video> videos = new ArrayList<Video>();
    static {
        Video cooking = Video.builder().name("Cooking").url("https://streamable.com/s/sjnfh/ypytmc").build();
        videos.add(cooking);
        Video duplicate = Video.builder().name("Duplicate").url("https://streamable.com/s/thzto/owznya").build();
        videos.add(duplicate);
        Video cooking2 = Video.builder().name("Old Cooking").url("https://streamable.com/s/1ip5p/ancioe").build();
        videos.add(cooking2);
    }
    
    
    @Override
    public ArrayList<Video> getVideos() {
        return videos;
    }
    
    
    @Override
    public Video getVideo(String id) {
        Integer index = Integer.parseInt(id);
        if ( index < videos.size() ) {
            return videos.get(index);
        }
        return null;
    }
    
    @Override
    public void createVideo(Video video) {
        videos.add(video);
    }
    
    @Override
    public void deleteVideo( String name ) {
        for ( Video vid : videos ) {
            if ( vid.getName().equals(name) ) {
                videos.remove(vid);
            }
        }
    }
}
