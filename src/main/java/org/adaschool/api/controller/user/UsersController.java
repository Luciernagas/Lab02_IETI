package org.adaschool.api.controller.user;

import org.adaschool.api.exception.UserNotFoundException;
import org.adaschool.api.repository.user.User;
import org.adaschool.api.repository.user.UserDto;
import org.adaschool.api.service.user.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/users/")
public class UsersController {

    private final UsersService usersService;
    public UsersController(@Autowired UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
        User userCreated = usersService.save(new User(userDto));
        URI createdUserUri = URI.create("/v1/users/" + userCreated.getId());
        return ResponseEntity.created(createdUserUri).body(userCreated);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = usersService.all();
        return ResponseEntity.ok(users);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> findById(@PathVariable("id") String id) {
        Optional<User> user = usersService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        throw new UserNotFoundException(id);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@PathVariable("id") String id, @RequestBody UserDto userDto) {
        Optional<User> userOptional = usersService.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.update(userDto);
            usersService.save(user);
            return ResponseEntity.ok(user);
        }
        throw new UserNotFoundException(id);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
        Optional<User> userOptional = usersService.findById(id);
        if (userOptional.isPresent()) {
            usersService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        throw new UserNotFoundException(id);
    }
}
