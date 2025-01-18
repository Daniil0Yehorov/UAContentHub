package com.UCH.UAContentHub.Service.Implementation;

import com.UCH.UAContentHub.Entity.Enum.CreatorProfileStatus;
import com.UCH.UAContentHub.Entity.Tags;
import com.UCH.UAContentHub.Repository.ProfileRepository;
import com.UCH.UAContentHub.Repository.TagsRepository;
import com.UCH.UAContentHub.Service.Interface.ContentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.UCH.UAContentHub.Entity.Profile;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ContentServiceImpl implements ContentService {

    private ProfileRepository profileRepository;

    private TagsRepository tagsRepository;

    public List<Profile> getConfirmedCreators() {
        return profileRepository.findByStatus(CreatorProfileStatus.CONFIRMED);
    }

    public List<Profile> filterByTagsAndRating(Set<String> tagNames, int minRating, int maxRating) {
        List<Tags> tags = tagsRepository.findByNameIn(tagNames);

        return profileRepository.findByStatusAndRatingBetween(CreatorProfileStatus.CONFIRMED, minRating, maxRating).stream()
                .filter(profile -> {
                    Set<String> profileTags = profile.getPhs().stream()
                            .map(phs -> phs.getTags().getName())
                            .collect(Collectors.toSet());
                    return profileTags.containsAll(tagNames);
                })
                .collect(Collectors.toList());
    }

    public Profile getProfileById(int id) {
        return profileRepository.findById(id).orElse(null);
    }
}
