package it.epicode.CapstonBackEndAF.controller;

import it.epicode.CapstonBackEndAF.dto.LoginDto;
import it.epicode.CapstonBackEndAF.dto.UserDto;
import it.epicode.CapstonBackEndAF.enumeration.Ruolo;
import it.epicode.CapstonBackEndAF.exception.NotFoundException;
import it.epicode.CapstonBackEndAF.exception.ValidationException;
import it.epicode.CapstonBackEndAF.model.User;
import it.epicode.CapstonBackEndAF.service.AuthService;
import it.epicode.CapstonBackEndAF.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;


    @PostMapping("/auth/register")
    public User register(@RequestBody @Validated UserDto userDto, BindingResult bindingResult) throws ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .reduce("", (s, e) -> s + e));
        }

        userDto.setRuolo(Ruolo.USER);
        return userService.saveUser(userDto);
    }

    @PostMapping ("/auth/login")
    public String login(@RequestBody @Validated LoginDto loginDto, BindingResult bindingResult) throws ValidationException, NotFoundException {
        if(bindingResult.hasErrors()){
            throw new ValidationException(bindingResult.getAllErrors().stream().
                    map(objectError -> objectError.getDefaultMessage()).
                    reduce("", (s,e)->s+e));
        }


        return authService.login(loginDto);
    }
}

