package com.UCH.UAContentHub.Service.Interface;

import com.UCH.UAContentHub.Entity.Profile;
import java.util.List;
import java.util.Set;

public interface ContentService {
    List<Profile> filterByTagsAndRating(Set<String> tagNames, int minRating, int maxRating);
    List<Profile> getConfirmedCreators();
    Profile getProfileById(int id);
}
