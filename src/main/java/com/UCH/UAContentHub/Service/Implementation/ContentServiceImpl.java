package com.UCH.UAContentHub.Service.Implementation;

import com.UCH.UAContentHub.Entity.Enum.CreatorProfileStatus;
import com.UCH.UAContentHub.Entity.Tags;
import com.UCH.UAContentHub.Repository.ProfileRepository;
import com.UCH.UAContentHub.Repository.TagsRepository;
import com.UCH.UAContentHub.Service.Interface.ContentService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.UCH.UAContentHub.Entity.Profile;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ContentServiceImpl implements ContentService {
    @Autowired
    private EntityManager entityManager;

    private ProfileRepository profileRepository;

    private TagsRepository tagsRepository;
    @Override
    public List<Profile> getConfirmedCreators() {
        return profileRepository.findByStatus(CreatorProfileStatus.CONFIRMED);
    }

    @Override
    public List<Profile> filterCreators(String name, Set<String> tagNames, int minRating, int maxRating, int minSubscribers, int maxSubscribers) {
        List<Tags> selectedTags = tagsRepository.findByNameIn(tagNames);

        return profileRepository.findByStatus(CreatorProfileStatus.CONFIRMED).stream()
                .filter(p -> name == null || p.getUser().getName().toLowerCase().contains(name.toLowerCase()))
                .filter(p -> p.getRating() >= minRating && p.getRating() <= maxRating)
                .filter(p -> p.getSubscribersCount() >= minSubscribers && p.getSubscribersCount() <= maxSubscribers)
                .filter(p -> {
                    Set<String> profileTags = p.getPhs().stream()
                            .map(phs -> phs.getTags().getName())
                            .collect(Collectors.toSet());
                    return profileTags.containsAll(tagNames);
                })
                .collect(Collectors.toList());
    }
    @Override
    public Profile getProfileById(int id) {
        return profileRepository.findById(id).orElse(null);
    }

    @Override
    public List<Tags> getAllTags() {
        return tagsRepository.findAll();
    }
    @Override
    public List<Profile> getRecommendedCreators(int userId) {
        Query query = entityManager.createNativeQuery("SELECT GetRecommendedCreators(:userId)");
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

        return profileRepository.findAllById(recommendedIds);
    }
    //функція з бд
    /*DELIMITER //

CREATE FUNCTION GetRecommendedCreators(UID INT)
RETURNS JSON DETERMINISTIC
BEGIN
    DECLARE recommended JSON DEFAULT NULL;

    SET recommended = (
        SELECT JSON_ARRAYAGG(id)
        FROM (
            SELECT DISTINCT s_other.CreatorID AS id
            FROM Subscription s_self
            JOIN Subscription s_other ON s_self.UserID = s_other.UserID
            JOIN Profile p ON s_other.CreatorID = p.UserID
            WHERE s_self.CreatorID IN (SELECT CreatorID FROM Subscription WHERE UserID = UID)
              AND s_other.CreatorID NOT IN (SELECT CreatorID FROM Subscription WHERE UserID = UID)
              AND p.Status = 'CONFIRMED'
            ORDER BY RAND()
            LIMIT 5
        ) AS recommended_random
    );

    RETURN COALESCE(recommended, JSON_ARRAY());
END //

DELIMITER //*/

}
