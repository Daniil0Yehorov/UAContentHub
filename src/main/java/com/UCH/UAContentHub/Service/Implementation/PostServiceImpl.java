package com.UCH.UAContentHub.Service.Implementation;

import com.UCH.UAContentHub.Entity.*;
import com.UCH.UAContentHub.Entity.Enum.ComplaintStatus;
import com.UCH.UAContentHub.Repository.*;
import com.UCH.UAContentHub.Service.Interface.PostService;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("postService")
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    private LikesRepository likesRepository;

    private UserRepository userRepository;

    private СomplaintRepository complaintRepository;

    @Override
    public Post CreatePost(Post post) {
        if (post.getContent()==null){
            throw new IllegalArgumentException("Опис посту не може бути порожнім");
        }
        return postRepository.save(post);
    }

    @Override
    public Post updatePost(Post post) {
        Post updatedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new IllegalArgumentException("Пост не знайден."));
        if (StringUtils.isEmpty(post.getContent())) {
            throw new IllegalArgumentException("Опис посту не може бути порожнім");
        }
        updatedPost.setContent(post.getContent());
        updatedPost.setPublishDate(LocalDateTime.now());

        return postRepository.save(updatedPost);
    }

    @Override
    //не працює
    public void deletePost(int postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Пост з айді не був знайден: " + postId));
        postRepository.delete(post);
    }

    @Override
    public List<Post> getAllPostsByCreators() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> getPostsBySubscribedCreators(int userId) {
        return postRepository.findBySubscribedCreatorsOrderByPublishDateDesc(userId);
    }

    @Override
    public void likeThePost(int postId, int userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Пост не знайден з айді: " + postId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Юзер з айді не знайден: " + userId));

        if (!likesRepository.existsByPostIdAndUserId(postId, userId)) {
            Likes like = new Likes();
            like.setPost(post);
            like.setUser(user);
            like.setLikeDate(LocalDateTime.now());
            likesRepository.save(like);

        } else {
            throw new IllegalArgumentException("Юзер вже лайкнув цей пост: " + postId);
        }
    }

    @Override
    public void CancelTheLikeForThePost(int postId, int userId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Пост з айді не знайден: " + postId));

        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Юзер з айді не знайден: " + userId));

        Likes like = likesRepository.findByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Лайк з айді поста або  не знайден,: " + postId + " або юзера: " + userId));

        likesRepository.delete(like);

    }

    @Override
    public void reportPost(int postid, int whoComplainedId, String reason) {

            Post reportedPost = postRepository.findById(postid).orElseThrow(()
        -> new IllegalArgumentException("пост  не знайден,: " + postid));

            User complainingUser = userRepository.findById(whoComplainedId)
                    .orElseThrow(() -> new IllegalArgumentException("Користувач не знайден"));

        if (StringUtils.isEmpty(reason)) {
            throw new IllegalArgumentException("Причина не може бути порожньою");
        }

            Complaint newComplaint = new Complaint();
            newComplaint.setPost(reportedPost);
            newComplaint.setUser(complainingUser);
            newComplaint.setReason(reason);
            newComplaint.setStatus(ComplaintStatus.PENDING);
            complaintRepository.save(newComplaint);
    }
    public boolean isPostLikedByUser(int postId, int userId) {
        return likesRepository.existsByPostIdAndUserId(postId, userId);
    }
    public   Post getPostById(int postid){
        return postRepository.findById(postid).orElseThrow(()
                -> new IllegalArgumentException("Пост не знайден з айді: " + postid));
    }
    public List<Post> getPostsByUser(int userId) {
        return postRepository.findByProfileUserId(userId);
    }
}
