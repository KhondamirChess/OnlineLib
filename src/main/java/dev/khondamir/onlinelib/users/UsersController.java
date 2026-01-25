package dev.khondamir.onlinelib.users;

import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UsersController {

    private static final Logger log= LoggerFactory.getLogger(UsersController.class);

    private UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(
            @RequestBody @Valid SignUpRequest signUpRequest
    ){
        log.info("Get request for signup: login={}", signUpRequest.login());
        var user = userService.registerUser(signUpRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserDto(user.id(), user.login()));
    }
}
