package burundi.ilucky.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import burundi.ilucky.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	
    private Long id;
    private String username;
    private Date addTime;
    private long totalPlay;
    private long totalStar;
    
//    @JsonProperty("isPremium")
    private boolean isPremium;
    
    @JsonProperty("isWin")
    private boolean isWin;
    private long totalVnd;
    
    
    public UserDTO(User user) {
        this.id = user.getId();
        this.username=user.getUsername();
        this.addTime = user.getAddTime();
        this.totalPlay = user.getTotalPlay();
        this.totalStar = user.getTotalStar();
        this.totalVnd = user.getTotalVnd();
    }
}
