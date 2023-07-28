package com.geekster.Blogging.API.service;

import com.geekster.Blogging.API.model.Post;
import com.geekster.Blogging.API.model.User;
import com.geekster.Blogging.API.repository.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostService {
    @Autowired
    PostRepo postRepo;
    public String createBloggingPost(Post post) {
        post.setPostTime(LocalDateTime.now());
        postRepo.save(post);
        return "Post uploaded!!!!";
    }
    public String removeBloggingPost(Long postId, User user) {

        Post post  = postRepo.findById(postId).orElse(null);
        if(post != null && post.getPostOwner().equals(user))
        {
            postRepo.deleteById(postId);
            return "Removed successfully";
        }
        else if (post == null)
        {
            return "Post to be deleted does not exist";
        }
        else{
            return "Un-Authorized delete detected....Not allowed";
        }
    }
    public boolean validatePost(Post instaPost) {
        return (instaPost!=null && postRepo.existsById(instaPost.getPostId()));
    }


    public Post getPostById(Long id) {
        return postRepo.findById(id).orElse(null);
    }
}
