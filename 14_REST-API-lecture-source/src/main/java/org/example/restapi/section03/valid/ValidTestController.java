package org.example.restapi.section03.valid;

import jakarta.validation.Valid;
import org.example.restapi.section02.responseentity.ResponseMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/valid")
public class ValidTestController {

    private List<UserDTO> users;

    public ValidTestController() {
        users = new ArrayList<>();

        users.add(new UserDTO(1, "user01", "pass01", "너구리", LocalDate.now()));
        users.add(new UserDTO(2, "user02", "pass01", "코알라", LocalDate.now()));
        users.add(new UserDTO(3, "user03", "pass01", "곰", LocalDate.now()));

    }

    @PostMapping("/users")
    public ResponseEntity<?> registUser(@Valid @RequestBody UserDTO newUser) {

        System.out.println("body로 돌아온 userDTO = " + newUser);

        return ResponseEntity.created(URI.create("/valid/users/" + "userNo")).build();
    }

    // 에러상황에 대해 처리할수있는 컨트롤러를 따로 만들것
    @GetMapping("/users/{userNo}")
    public ResponseEntity<?> findUserByNo(@PathVariable int userNo) throws UserNotFoundException {

        //Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        //Body
        List<UserDTO> foundUserList = users.stream().filter(user -> user.getNo() == userNo).toList();

        UserDTO foundUser = null;
        if(foundUserList.size() > 0) {
            // userNo 이 일치하는 회원이 있으면
            foundUser = foundUserList.get(0);
        } else {
            throw new UserNotFoundException("회원 정보를 찾을 수 없습니다.");
        }

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("user", foundUser);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new ResponseMessage(
                                200, "조회 성공", responseMap
                        )
                );
    }

//        2024-09-03T10:09:31.684+09:00  WARN 1128 --- [14_REST-API-lecture-source] [nio-8080-exec-5] .w.s.m.s.DefaultHandlerExceptionResolver :
//        Resolved [org.springframework.web.bind.MethodArgumentNotValidException: Validation failed for argument [0] in public org.springframework
//        .http.ResponseEntity<?> org.example.restapi.section03.valid.ValidTestController.registUser(org.example.restapi.section03.valid.UserDTO):
//        [Field error in object 'userDTO' on field 'id': rejected value [null]; codes [NotNull.userDTO.id,NotNull.id,NotNull.java.lang.String,NotNull];
//        arguments [org.springframework.context.support.DefaultMessageSourceResolvable:
//        codes [userDTO.id,id]; arguments []; default message [id]]; default message [아이디는 반드시 입력되어야 합니다.]] ]

}
