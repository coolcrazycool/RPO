package moneyGrabber.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import moneyGrabber.backend.repositories.ArtistRepository;
import moneyGrabber.backend.models.Artist;
import moneyGrabber.backend.models.Country;
import moneyGrabber.backend.repositories.CountryRepository;
import moneyGrabber.backend.tools.DataValidationException;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class ArtistController {
        @Autowired
        ArtistRepository artistRepository;
        @Autowired
        CountryRepository countryRepository;

        @GetMapping("/artists")
        public Page<Artist> getAllArtists(@RequestParam("page") int page, @RequestParam("limit") int limit) {
                return artistRepository.findAll(PageRequest.of(page,limit, Sort.by(Sort.Direction.ASC, "name")));
        }

        @GetMapping("/artists/{id}")
        public ResponseEntity<Artist> getArtist(@PathVariable(value = "id") Long artistId)
                throws DataValidationException {
                Artist artist = artistRepository.findById(artistId).orElseThrow(() -> new DataValidationException("Художник с таким индексом не найден"));
                return ResponseEntity.ok(artist);
        }

        @GetMapping("/countries/{id}/artists")
        public ResponseEntity<List<Artist>> getCoutryArtists(@PathVariable(value = "id") Long countryId) {
                Optional<Country> cc = countryRepository.findById(countryId);
                if (cc.isPresent())
                {
                        return ResponseEntity.ok(cc.get().artists);
                }
                return ResponseEntity.ok(new ArrayList<Artist>());
        }

//        @PostMapping("/artists")
//        public ResponseEntity<Object> createArtist(@Validated @RequestBody Artist artist) {
//                try {
//                        Optional<Country> cc = countryRepository.findById(artist.country.id);
//                        cc.ifPresent(country -> artist.country = country);
//                        Artist na = artistRepository.save(artist);
//                        return new ResponseEntity<Object>(na, HttpStatus.OK);
//                } catch (Exception ex) {
//                        String error;
//                        if (ex.getMessage().contains("artists.name_UNIQUE"))
//                                error = "artist already exists";
//                        else
//                                error = "undefined error";
//                        Map<String, String> map = new HashMap<>();
//                        map.put("error", error);
//                        return new ResponseEntity<Object>(map, HttpStatus.OK);
//                }
//        }

        @PostMapping("/artists")
        public ResponseEntity<Object> createArtist(@Validated @RequestBody Artist artist) {
                try {
                        Artist na = artistRepository.save(artist);
                        return new ResponseEntity<Object>(na, HttpStatus.OK);
                } catch (Exception ex) {
                        String error;
                                error = "undefined error";
                        Map<String, String> map = new HashMap<>();
                        map.put("error", error);
                        return new ResponseEntity<Object>(map, HttpStatus.OK);
                }
        }


        @PutMapping("/artists/{id}")
        public ResponseEntity<Artist> updateArtist(@PathVariable(value = "id") Long artistId,
                                                     @Validated @RequestBody Artist artistDetails) throws DataValidationException {
                try {
                        Artist artist = artistRepository.findById(artistId).orElseThrow(() -> new DataValidationException("Художник с таким индексом не найден"));
                        artist.name = artistDetails.name;
                        artistRepository.save(artist);
                        return ResponseEntity.ok(artist);
                } catch (Exception ex) {
                        String error;
                        if (ex.getMessage().contains("artists.name_UNIQUE"))
                                throw new DataValidationException("Этот художник уже есть");
                        else
                                throw new DataValidationException("Неизвестная ошибка");
                }
        }

        @PostMapping("/deleteartists")
        public ResponseEntity deleteArtists(@Validated @RequestBody List<Artist> artists) {
                artistRepository.deleteAll(artists);
                return new ResponseEntity(HttpStatus.OK);
        }

        @DeleteMapping("/artists/{id}")
        public Map<String, Boolean> deleteArtist(@PathVariable(value = "id") Long artistId) {
                Optional<Artist> artist = artistRepository.findById(artistId);
                Map<String, Boolean> response = new HashMap<>();
                if (artist.isPresent()) {
                        artistRepository.delete(artist.get());
                        response.put("deleted", Boolean.TRUE);
                }
                else {
                        response.put("deleted", Boolean.FALSE);
                }
                return response;
        }
}
