package burundi.ilucky.controller;

import burundi.ilucky.model.User;
import burundi.ilucky.model.dto.UserDTO;
import burundi.ilucky.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequestMapping("/api/user")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/info")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userService.findByUserName(userDetails.getUsername());
            UserDTO userDTO = new UserDTO(user);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            log.warn(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNewUser(@RequestBody User user) {
        try {
            userService.saveUser(user);
            Map<String, Object> response = new HashMap<>();
            response.put("status", 201);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (Exception e){
            log.warn(e);
            Map<String, Object> response = new HashMap<>();
            response.put("status", 404);
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }


    }

    @PostMapping("/deposit_money/{money}")
    public ResponseEntity<?> depositMoneyInAcount(@RequestParam String username, @PathVariable Long money) {
        try {
            User user = userService.findByUserName(username);
            userService.depositMoney(user, money);
            Map<String, Object> response = new HashMap<>();
            response.put("status", 201);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            log.warn(e);
            Map<String, Object> response = new HashMap<>();
            response.put("status", 404);
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/buy_new_spin/{number}")
    public ResponseEntity<?> buyNewSpin(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long number){
        try{
            User user = userService.findByUserName(userDetails.getUsername());
            userService.buyNewSpins(user,number);
            Map<String, Object> response = new HashMap<>();
            response.put("status", 201);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch (Exception e){
            log.warn(e);
            Map<String, Object> response = new HashMap<>();
            response.put("status", 404);
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping
    public ResponseEntity<?> listTop5User(){
        List<UserDTO> userDTOList = userService.findTop5UserHaveMostStar();
        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }
}
