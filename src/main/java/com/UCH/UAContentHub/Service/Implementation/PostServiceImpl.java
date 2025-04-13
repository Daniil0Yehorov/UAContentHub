package com.UCH.UAContentHub.Service.Implementation;

import com.UCH.UAContentHub.Entity.*;
import com.UCH.UAContentHub.Entity.Enum.ComplaintStatus;
import com.UCH.UAContentHub.Repository.*;
import com.UCH.UAContentHub.Service.Interface.PostService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service("postService")
@AllArgsConstructor
public class PostServiceImpl implements PostService {
    @Autowired
    private EntityManager entityManager;

    private PostRepository postRepository;
    private LikesRepository likesRepository;
    private UserRepository userRepository;
    private СomplaintRepository complaintRepository;
    private ImageRepository imageRepository;
    private Post_has_ImageRepository post_has_ImageRepository;

    private static String srcPhotos = "src/main/resources/static/postPhotos/";

    @Override
    public void CreatePost(Post post) {
        if (post.getContent() == null) {
            throw new IllegalArgumentException("Опис посту не може бути порожнім");
        }
        postRepository.save(post);
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
    public void deletePost(int postId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Пост з айді не був знайден: " + postId));
        List<Post_has_Image> postImages = post_has_ImageRepository.findByPostId(postId);
        if (postImages != null && !postImages.isEmpty()) {
        for (Post_has_Image postImage : postImages) {
            String imagePath = srcPhotos + postImage.getImage().getSrc().replace("/postPhotos/",
                    "");
            try {
                Files.deleteIfExists(Paths.get(imagePath));
            } catch (IOException e) {
                throw new IllegalArgumentException("Не вдалося видалити файл: " + imagePath, e);
            }
            imageRepository.delete(postImage.getImage());
        }
        post_has_ImageRepository.deleteAll(postImages);
        }
        postRepository.deletePostById(postId);
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
        Profile profile = reportedPost.getProfile();

        User complainingUser = userRepository.findById(whoComplainedId)
                .orElseThrow(() -> new IllegalArgumentException("Користувач не знайден"));

        if (StringUtils.isEmpty(reason)) {
            throw new IllegalArgumentException("Причина не може бути порожньою");
        }
        if (complaintRepository.existsByUserIdAndPostId(whoComplainedId, postid)) {
            throw new IllegalArgumentException("Ви вже відправили скаргу на цей пост.");
        }
        Complaint newComplaint = new Complaint();
        newComplaint.setPost(reportedPost);
        newComplaint.setProfile(profile);
        newComplaint.setUser(complainingUser);
        newComplaint.setReason(reason);
        newComplaint.setStatus(ComplaintStatus.PENDING);
        newComplaint.setComplaintDate(LocalDateTime.now());
        complaintRepository.save(newComplaint);
    }

    public boolean isPostLikedByUser(int postId, int userId) {
        return likesRepository.existsByPostIdAndUserId(postId, userId);
    }

    @Override
    public Post getPostById(int postid) {
        return postRepository.findById(postid).orElseThrow(()
                -> new IllegalArgumentException("Пост не знайден з айді: " + postid));
    }

    @Override
    public List<Post> getPostsByUserSortedByDate(int userId) {
        return postRepository.findPostsByUserOrderByPublishDateDesc(userId);
    }

    @Override
    public void CreatePostwithImages(Post post, MultipartFile[] images) {
        if (images.length > 3) {
            throw new IllegalArgumentException("Максимальна кількість зображень для посту - 3.");
        }
        CreatePost(post);

        String userLogin = post.getProfile().getUser().getLogin();
        int sequence = 1;

        for (MultipartFile imageFile : images) {
            try {
                String originalFilename = imageFile.getOriginalFilename();

                if (originalFilename == null || originalFilename.isEmpty()) {
                    throw new IllegalArgumentException("Ім'я файлу не може бути порожнім.");
                }

                int lastDotIndex = originalFilename.lastIndexOf('.');
                if (lastDotIndex == -1 || lastDotIndex == originalFilename.length() - 1) {
                    throw new IllegalArgumentException("Файл не має розширення: " + originalFilename);
                }

                String extension = originalFilename.substring(lastDotIndex);

                if (!extension.equalsIgnoreCase(".jpg") &&
                        !extension.equalsIgnoreCase(".jpeg") &&
                        !extension.equalsIgnoreCase(".png")) {
                    throw new IllegalArgumentException("Непідтримуваний формат файлу: " + extension);
                }

                String fileName = userLogin + "_" + post.getId() + "_" + sequence + extension;

                Path path = Paths.get(srcPhotos + fileName);

                Files.write(path, imageFile.getBytes());

                Image image = new Image();
                image.setSrc("/postPhotos/" + fileName);
                imageRepository.save(image);

                Post_has_Image postImage = new Post_has_Image();
                postImage.setPost(post);
                postImage.setImage(image);
                postImage.setSequence(sequence);
                post_has_ImageRepository.save(postImage);

                sequence++;

            } catch (IOException e) {
                throw new IllegalArgumentException("Не вдалося завантажити фотографії", e);
            }
        }
    }
    public boolean hasUserReportedPost(int userId, int postId) {
        return complaintRepository.existsByUserIdAndPostId(userId, postId);
    }
    @Override
    public List<Post> getRecommendedPosts(int userId) {
        Query query = entityManager.createNativeQuery("SELECT GetRecommendedPosts(:userId)");
        query.setParameter("userId", userId);
        String resultJson = (String) query.getSingleResult();

        if (resultJson == null || resultJson.equals("[]")) {
            return List.of();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        List<Integer> recommendedIds;
        try {
            recommendedIds = objectMapper.readValue(resultJson, new TypeReference<List<Integer>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Помилка парсингу JSON", e);
        }

        return postRepository.findAllById(recommendedIds);
    }
    //функція з бд
    /*CREATE FUNCTION GetRecommendedPosts(UID INT)
RETURNS JSON DETERMINISTIC
BEGIN
    DECLARE liked_titles TEXT;
    DECLARE recommended JSON DEFAULT NULL;

    -- Отримуємо всі назви вподобаних постів, розділені пробілами
    SELECT GROUP_CONCAT(title SEPARATOR ' ') INTO liked_titles
    FROM Post
    WHERE id IN (SELECT PostID FROM Likes WHERE UserID = UID);

    IF liked_titles IS NULL THEN
        RETURN JSON_ARRAY(); -- Користувач ще нічого не вподобав
    END IF;

    -- Знаходимо інші пости, схожі за назвою
    SET recommended = (
        SELECT JSON_ARRAYAGG(p.id)
        FROM Post p
        WHERE p.id NOT IN (SELECT PostID FROM Likes WHERE UserID = UID)
          AND (
              MATCH(p.title) AGAINST (SUBSTRING_INDEX(liked_titles, ' ', 1) IN NATURAL LANGUAGE MODE) OR
              MATCH(p.title) AGAINST (SUBSTRING_INDEX(SUBSTRING_INDEX(liked_titles, ' ', 2), ' ', -1) IN NATURAL LANGUAGE MODE)
          )
        LIMIT 5
    );

    RETURN COALESCE(recommended, JSON_ARRAY());
END*/
}