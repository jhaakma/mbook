package mbook.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mbook.model.Video;
import mbook.repository.VideoRepository;

@Service
public class MongoVideoService implements VideoService {

    @Autowired
    private VideoRepository videoRepository;
    
    
    @Override
    public ArrayList<Video> getVideos() {
        return (ArrayList<Video>) videoRepository.findAll();
    }

    @Override
    public Video getVideo(String index) {
        return videoRepository.findByIndex(index);
    }

    @Override
    public void createVideo(Video video) {
        //Increment index
        if ( video.getIndex() == null) {
            video.setIndex( String.valueOf(videoRepository.count()));
        }
        videoRepository.save(video);
    }

    @Override
    public void deleteVideo(String index) {
        videoRepository.delete(videoRepository.findByIndex(index));
    }

}
