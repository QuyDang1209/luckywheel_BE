package burundi.ilucky.service;

import burundi.ilucky.model.Gift;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GiftService {

    public static Map<String, Gift> gifts;

    static {
        gifts = new HashMap<>();
        gifts.put("10000VND", new Gift("10000VND", "10.000 VND", 10000, "VND",0.01));
        gifts.put("1000VND", new Gift("1000VND", "1.000 VND", 1000, "VND",0.02));
        gifts.put("500VND", new Gift("500VND", "500 VND", 500, "VND",0.03));
        gifts.put("200VND", new Gift("200VND", "200 VND", 200, "VND",0.05));

        //Mete: Mảnh ghép
        gifts.put("SAMSUNG1", new Gift("SAMSUNG1", "Mảnh Samsung 1", 1, "SAMSUNG",0.07));
        gifts.put("SAMSUNG2", new Gift("SAMSUNG2", "Mảnh Samsung 2", 1, "SAMSUNG",0.07));
        gifts.put("SAMSUNG3", new Gift("SAMSUNG3", "Mảnh Samsung 3", 1, "SAMSUNG",0.05));
        gifts.put("SAMSUNG4", new Gift("SAMSUNG4", "Mảnh Samsung 4", 1, "SAMSUNG",0.07));

        //Let: Chữ cái
        gifts.put("L", new Gift("L", "1 Chữ cái \"L\"", 1, "PIECE",0.05));
        gifts.put("I", new Gift("I", "1 Chữ cái \"I\"", 1, "PIECE",0.02));
        gifts.put("T", new Gift("T", "1 Chữ cái \"T\"", 1, "PIECE",0.05));
        gifts.put("E", new Gift("E", "1 Chữ cái \"E\"", 1, "PIECE",0.05));

        gifts.put("SHARE", new Gift("SHARE", "Chia sẻ cho bạn bè để nhận được 1 lượt chơi", 1, "SHARE",0.08));

        gifts.put("5555STARS", new Gift("5555STARS", "5555 Sao", 4, "STARS",0.1));
        gifts.put("555STARS", new Gift("555STARS", "555 Sao", 3, "STARS",0.08));
        gifts.put("55STARS", new Gift("55STARS", "55 Sao", 2, "STARS",0.06));
        gifts.put("5STARS", new Gift("5STARS", "5 Sao", 1, "STARS",0.05));

        gifts.put("UNLUCKY", new Gift("UNLUCKY", "Chúc bạn may mắn lần sau", 1, "UNLUCKY",0.09));
    }

    public static Gift getRandomGift() {
        double totalProbability = gifts.values().stream().mapToDouble(Gift::getPercent).sum();

        // Đảm bảo tổng xác suất không vượt quá 1.0
        if (totalProbability > 1.0) {
            throw new IllegalStateException("Tổng xác suất vượt quá 1.0");
        }

        // Tạo một số ngẫu nhiên trong khoảng [0, 1)
        double rand = Math.random();
        double cumulativeProbability = 0.0;

        // Duyệt qua các phần thưởng và so sánh với tỷ lệ xác suất
        for (Map.Entry<String, Gift> entry : gifts.entrySet()) {
            cumulativeProbability += entry.getValue().getPercent();
            if (rand <= cumulativeProbability) {
                return entry.getValue(); // Trả về Gift khi tỷ lệ xác suất đạt đến
            }
        }

        // Nếu không có quà nào được chọn (thường không xảy ra), trả về null
        return null;
    }
}
