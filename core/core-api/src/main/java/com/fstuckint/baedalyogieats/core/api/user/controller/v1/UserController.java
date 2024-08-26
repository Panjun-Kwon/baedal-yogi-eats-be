package com.fstuckint.baedalyogieats.core.api.user.controller.v1;

import com.fstuckint.baedalyogieats.core.api.user.controller.v1.request.LoginDto;
import com.fstuckint.baedalyogieats.core.api.user.controller.v1.request.SignupDto;
import com.fstuckint.baedalyogieats.core.api.user.controller.v1.request.UpdateUserDto;
import com.fstuckint.baedalyogieats.core.api.user.domain.UserService;
import com.fstuckint.baedalyogieats.core.api.user.jwt.JwtUtils;
import com.fstuckint.baedalyogieats.core.api.user.support.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> signUp(@Validated @RequestBody SignupDto signupDto) {
        return userService.signUp(signupDto);
    }

    @PostMapping("/authorization")
    public ResponseEntity<ApiResponse<?>> createToken(@RequestBody LoginDto loginDto) {
        return userService.createToken(loginDto);
    }

    @DeleteMapping("/token")
    public ResponseEntity<ApiResponse<?>> deleteToken(HttpServletRequest request) {
        return userService.deleteToken(request);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getUsers(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime cursor,
                                                   @RequestParam(defaultValue = "10") Integer limit,
                                                   @RequestParam(defaultValue = "id") String sortKey,
                                                   @RequestParam(defaultValue = "ASC") String direction,
                                                   HttpServletRequest request) {
        return userService.getUserList(cursor, limit, sortKey, direction, request);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResponse<?>> getUser(@PathVariable UUID uuid, HttpServletRequest request) {
        return userService.getUser(uuid, request);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ApiResponse<?>> updateUser(@PathVariable UUID uuid, @RequestBody UpdateUserDto updateUserDto, HttpServletRequest request) {
        return userService.updateUser(uuid, updateUserDto, request);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<ApiResponse<?>> deleteUser(@PathVariable UUID uuid) {
        return userService.deleteUser(uuid);
    }
}