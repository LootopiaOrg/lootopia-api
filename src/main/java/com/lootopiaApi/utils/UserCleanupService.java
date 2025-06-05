package com.lootopiaApi.utils;

import com.lootopiaApi.model.entity.User;
import com.lootopiaApi.repository.*;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class UserCleanupService {
    private final UserRepository userRepository;
    private final EmailConfirmationTokenRepository emailConfirmationTokenRepository;
    private final UserBalanceRepository userBalanceRepository;
    private final ParticipationRepository participationRepository;
    private final HuntRepository huntRepository;
    private final AddressRepository addressRepository;
    private final ProfileImagesRepository profileImagesRepository;

    public UserCleanupService(UserRepository userRepository, EmailConfirmationTokenRepository emailConfirmationTokenRepository, UserBalanceRepository userBalanceRepository, ParticipationRepository participationRepository, HuntRepository huntRepository, AddressRepository addressRepository, ProfileImagesRepository profileImagesRepository) {
        this.userRepository = userRepository;
        this.emailConfirmationTokenRepository = emailConfirmationTokenRepository;
        this.userBalanceRepository = userBalanceRepository;
        this.participationRepository = participationRepository;
        this.huntRepository = huntRepository;
        this.addressRepository = addressRepository;
        this.profileImagesRepository = profileImagesRepository;
    }

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void cleanupInactiveUsers() {
        log.info("Start cleaning up inactive users");

        LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6);
        List<User> toDelete = userRepository.findByActiveFalseAndUpdatedAtBefore(sixMonthsAgo);

        if (!toDelete.isEmpty()){
            List<Long> userIds = toDelete.stream().map(User::getId).toList();

            emailConfirmationTokenRepository.deleteByUserIdIn(userIds);
            userBalanceRepository.deleteByUserIdIn(userIds);
            participationRepository.deleteByPlayerIdIn(userIds);
            huntRepository.clearOrganizerReferences(userIds);
            addressRepository.deleteByUserIdIn(userIds);
            profileImagesRepository.deleteByUserIdIn(userIds);

            userRepository.deleteAll(toDelete);
            log.info("{} users deleted", toDelete.size());
        }else {
            log.info("No users to be deleted");
        }
        
        log.info("Finish cleaning up inactive users");
    }
}
