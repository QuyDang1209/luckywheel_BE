package burundi.ilucky.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import burundi.ilucky.model.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import burundi.ilucky.model.User;
import burundi.ilucky.payload.Response;
import burundi.ilucky.repository.UserRepository;

@Service
public class UserService {
	@Autowired
    private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;


	public User findByUserName(String username) {
		try {
			return userRepository.findByUsername(username);
		} catch (Exception e) {
			return null;
		}
	}

	
	public User saveUser(User user) {
		Date now = new Date();
		user.setAddTime(now);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	public void depositMoney(User user, Long money){
		Date now = new Date();
		user.setTotalVnd(user.getTotalVnd()+money);
		user.setLastUpdate(now);
		userRepository.save(user);
	}
	public void buyNewSpins(User user, Long number){
		if(number*1000 <= user.getTotalVnd()){
			user.setTotalPlay(user.getTotalPlay()+number);
			user.setTotalVnd(user.getTotalVnd()-number*1000);
		}else {
			throw new RuntimeException("Tài khoản không đủ tiền");
		}
		userRepository.save(user);
	}
	public void addNewSpinInNextDay(User user){
		Date now = new Date();
		Date date = user.getLastUpdate();
		if(date == null){
			user.setLastUpdate(now);
			user.setTotalPlay(user.getTotalPlay()+5);
			userRepository.save(user);
		}else {
			LocalDate date1 = now.toInstant()
					.atZone(ZoneId.systemDefault())  // Đặt múi giờ
					.toLocalDate();                 // Chuyển thành LocalDate

			LocalDate date2 = date.toInstant()
					.atZone(ZoneId.systemDefault())  // Đặt múi giờ
					.toLocalDate();
			if(!date1.equals(date2)){
				user.setLastUpdate(now);
				user.setTotalPlay(user.getTotalPlay()+5);
				userRepository.save(user);
			}
		}

	}
	public List<UserDTO> findTop5UserHaveMostStar(){
		Pageable pageable = PageRequest.of(0,5);
		List<User> userS = userRepository.findTop5UserByTotalStar(pageable);
		List<UserDTO> userDTOS = new ArrayList<>();
		for(User u: userS){
			UserDTO userDTO = new UserDTO(u);
			userDTOS.add(userDTO);
		}
		return userDTOS;
	}
}

