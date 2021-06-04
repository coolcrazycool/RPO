package moneyGrabber.backend.controllers;

import moneyGrabber.backend.tools.DataValidationException;
import moneyGrabber.backend.tools.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import moneyGrabber.backend.models.Museum;
import moneyGrabber.backend.models.User;
import moneyGrabber.backend.repositories.ArtistRepository;
import moneyGrabber.backend.repositories.ArtistRepository;
import moneyGrabber.backend.repositories.CountryRepository;
import moneyGrabber.backend.repositories.UserRepository;
import moneyGrabber.backend.repositories.MuseumRepository;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class UserController {
        @Autowired
        ArtistRepository artistRepository;
        @Autowired
        CountryRepository countryRepository;
        @Autowired
        UserRepository userRepository;
        @Autowired
        MuseumRepository museumRepository;


        @GetMapping("/users")
        public List<User> getAllUser() {
                List<User> users= userRepository.findAll();
//                for (int i = 0; i < users.size(); i++) {
//                        users.set(i,"D");
//                }
                return users;
        }

        @PostMapping("/users")
        public ResponseEntity<Object> createUser(@Validated @RequestBody User user) {
                try {
                        User nu = userRepository.save(user);
                        return new ResponseEntity<Object>(nu, HttpStatus.OK);
                } catch (Exception ex) {
                        String error;
                        if (ex.getMessage().contains("user.login_UNIQUE"))
                                error = "user already exists";
                        else
                                error = "undefined error";
                        Map<String, String> map = new HashMap<>();
                        map.put("error", error);
                        return new ResponseEntity<Object>(map, HttpStatus.OK);
                }
        }

        @PostMapping("/users/{id}/addmuseums")
        public ResponseEntity<Object> addMuseums(@PathVariable(value = "id") Long userId,
                                                 @Validated @RequestBody Set<Museum> museums) {
                Optional<User> uu = userRepository.findById(userId);
                int cnt = 0;
                if (uu.isPresent()) {
                        User u = uu.get();
                        for (Museum m : museums) {
                                Optional<Museum> mm = museumRepository.findById(m.id);
                                if (mm.isPresent()) {
                                        u.addMuseum(mm.get());
                                        cnt++;
                                }
                        }
                        userRepository.save(u);
                }
                Map<String, String> response = new HashMap<>();
                response.put("count", String.valueOf(cnt));
                return ResponseEntity.ok(response);
        }

        @PostMapping("/users/{id}/removemuseums")
        public ResponseEntity<Object> removeMuseums(@PathVariable(value = "id") Long userId,
                                                    @Validated @RequestBody Set<Museum> museums) {
                Optional<User> uu = userRepository.findById(userId);
                int cnt = 0;
                if (uu.isPresent()) {
                        User u = uu.get();
                        for (Museum m : u.museums) {
                                u.removeMuseum(m);
                                cnt++;
                        }
                        userRepository.save(u);
                }
                Map<String, String> response = new HashMap<>();
                response.put("count", String.valueOf(cnt));
                return ResponseEntity.ok(response);
        }
        @PutMapping("/users/{id}")
        public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId,
                                                     @Validated @RequestBody User userDetails)
        throws DataValidationException
        {
                try{
                        User user = userRepository.findById(userId)
                                .orElseThrow(() -> new DataValidationException("Пользователь с таким id не найден"));
                        user.email = userDetails.email;
                        String np =userDetails.np;
                        if (np != null && !np.isEmpty()){
                                byte[] b = new byte[32];
                                new Random().nextBytes(b);
                                String salt = new String(Hex.encode(b));
                                user.password = Utils.ComputeHash(np, salt);
                                user.salt = salt;
                        }
                        userRepository.save(user);
                        return ResponseEntity.ok(user);
                }
                catch (Exception ex){
                        String error;
                        if (ex.getMessage().contains("users.email_UNIQUE"))
                                throw new DataValidationException("C такой почтой уже есть");
                        else
                                throw new DataValidationException("Неизвестная ошибка");
                }
        }

}
