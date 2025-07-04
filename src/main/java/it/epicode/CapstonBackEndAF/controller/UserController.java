package it.epicode.CapstonBackEndAF.controller;

import it.epicode.CapstonBackEndAF.dto.UserDto;
import it.epicode.CapstonBackEndAF.exception.NotFoundException;
import it.epicode.CapstonBackEndAF.exception.ValidationException;
import it.epicode.CapstonBackEndAF.model.User;
import it.epicode.CapstonBackEndAF.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    //Creo un User
    @PostMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public User saveUser (@RequestBody @Validated UserDto userDto, BindingResult bindingResult) throws NotFoundException, ValidationException {
        if(bindingResult.hasErrors()){
            throw new ValidationException(bindingResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage()).
                    reduce("",(e,s)->e+s));
        }
        return userService.saveUser(userDto);
    }


    //Richiedo tutti gli user presenti
    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Page<User> getAllUser(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "id") String sortBy){
        return userService.getAllUser(page, size, sortBy);
    }


    //Richiedo un determinato user tramite l'id
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public User getUser(@PathVariable int id) throws NotFoundException {
        return userService.getUser(id);
    }

    //Modifico un user esistente
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public User updateUser(@PathVariable int id,@RequestBody @Validated UserDto userDto, BindingResult bindingResult) throws NotFoundException, ValidationException {
        if(bindingResult.hasErrors()){
            throw new ValidationException(bindingResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage()).
                    reduce("",(e,s)->e+s));}
        return userService.updateUser(id, userDto);
    }

    //Elimino un user
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void deleteUser(@PathVariable int id) throws NotFoundException{
        userService.deleteUser(id);
    }

    //Verifico quale utente è autenticato
    @GetMapping("/me")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User getLoggedInUser() {
        return userService.getAuthenticatedUser();
    }

}


