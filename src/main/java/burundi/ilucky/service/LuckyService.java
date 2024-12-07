package burundi.ilucky.service;

import burundi.ilucky.model.Gift;
import burundi.ilucky.model.LuckyHistory;
import burundi.ilucky.model.User;
import burundi.ilucky.model.dto.LuckyHistoryDTO;
import burundi.ilucky.repository.LuckyHistoryRepository;
import burundi.ilucky.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Log4j2
public class LuckyService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LuckyHistoryRepository luckyHistoryRepository;

    public List<LuckyHistory> getHistoriesByUserId(Long userId) {
        return luckyHistoryRepository.findByUserIdOrderByAddTimeDesc(userId);
    }

    public Gift lucky(User user) {
        Gift gift = GiftService.getRandomGift();

        LuckyHistory luckyHistory = new LuckyHistory();
        if(gift.getType().equals("VND")) {
            user.setTotalVnd(user.getTotalVnd() + gift.getNoItem());
        }
        if(gift.getType().equals("STARS")) {
            user.setTotalStar(user.getTotalStar() + gift.getNoItem());
        }

        luckyHistory.setGiftType(gift.getType());
        luckyHistory.setAddTime(new Date());
        luckyHistory.setGiftId(gift.getId());
        luckyHistory.setNoItem(gift.getNoItem());
        luckyHistory.setUser(user);
        luckyHistoryRepository.save(luckyHistory);

        user.setTotalPlay(user.getTotalPlay() - 1);
        userRepository.save(user);

        return gift;
    }
    public Gift lucky() {
        Gift gift = GiftService.getRandomGift();

        LuckyHistory luckyHistory = new LuckyHistory();
        luckyHistory.setGiftType(gift.getType());
        luckyHistory.setAddTime(new Date());
        luckyHistory.setGiftId(gift.getId());
        luckyHistory.setNoItem(gift.getNoItem());
        luckyHistoryRepository.save(luckyHistory);

        return gift;
    }
    public List<LuckyHistoryDTO> convertLuckyHistoriesToDTO(List<LuckyHistory> luckyGiftHistories) {
    	List<LuckyHistoryDTO> luckyGiftHistoriesDTO = new ArrayList<>();
    	
    	for(LuckyHistory item: luckyGiftHistories) {
    		Gift gift = GiftService.gifts.get(item.getGiftId());
    		LuckyHistoryDTO luckyHistoryDTO = new LuckyHistoryDTO(item.getAddTime(), gift);
    		luckyGiftHistoriesDTO.add(luckyHistoryDTO);
    	}
    	
    	return luckyGiftHistoriesDTO;
    }

}
