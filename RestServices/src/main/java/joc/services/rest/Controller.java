package joc.services.rest;




import joc.Joc;
import joc.Jucator;
import joc.Propunere;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@CrossOrigin
@RestController
@RequestMapping("/joc")
public class Controller {
    private static final String template = "Hello, %s!";


    private UtilsRepository repository = new UtilsRepository();

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return String.format(template, name);
    }


    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public ResponseEntity<?> findUserByUsername(@PathVariable String username) {

        Jucator jucator=repository.findUserByUsername(username);
        if (jucator == null) return new ResponseEntity<String>("User not found!", HttpStatus.NOT_FOUND);
        else return new ResponseEntity<Jucator>(jucator, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Jucator[] findAll() {
        int size = (int) StreamSupport.stream(repository.findAll().spliterator(), false).count();
        Jucator[] result = new Jucator[size];
        result = ((List<Jucator>) repository.findAll()).toArray(result);
        return result;
    }

    @RequestMapping(value = "/path/{idJoc}", method = RequestMethod.GET)
    public ResponseEntity<?> getPropuneri(@PathVariable int idJoc){
        List<String> carti= (repository.findByJoc(idJoc))
                .stream()
                .map(Joc::getJocDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(carti, HttpStatus.OK);
    }

    @RequestMapping(value = "/{idJoc}/{username}", method = RequestMethod.GET)
    public ResponseEntity<?> getByJocAndJucator(@PathVariable int idJoc, @PathVariable String username){
        List<String> propuneri= (repository.findByJocJucator(idJoc,username))
                .stream()
                .map(Propunere::getPropunereDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(propuneri, HttpStatus.OK);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userError(Exception e) {
        return e.getMessage();
    }

}
