package com.UCH.UAContentHub.Service.Interface;

import com.UCH.UAContentHub.Entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    //створення посту
    void CreatePost(Post post);
    //оновлення посту
    Post updatePost(Post post);
    //видалення посту
    void deletePost(int postId);
    //получення всіх постів
    List<Post> getAllPostsByCreators();
    //получення постів створених креаторами, за яких підписан користувач
    List<Post> getPostsBySubscribedCreators(int userId);
    //лайк користувача на пост
    void likeThePost(int postId,int userId);
    //відміна лайку користувача на посту
    void CancelTheLikeForThePost(int postId,int userId);
    //репорт на пост
    void reportPost(int postid,int whoComplainedId,String reason);
    Post getPostById(int postid);
    //получення постів креатора
    List<Post> getPostsByUserSortedByDate(int userId);
    void CreatePostwithImages(Post post, MultipartFile[] images);
}
