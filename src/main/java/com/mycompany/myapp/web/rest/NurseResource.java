package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Nurse;
import com.mycompany.myapp.repository.NurseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Nurse.
 */
@RestController
@RequestMapping("/api")
public class NurseResource {

    private final Logger log = LoggerFactory.getLogger(NurseResource.class);

    @Inject
    private NurseRepository nurseRepository;

    /**
     * POST  /nurses -> Create a new nurse.
     */
    @RequestMapping(value = "/nurses",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Nurse nurse) throws URISyntaxException {
        log.debug("REST request to save Nurse : {}", nurse);
        if (nurse.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new nurse cannot already have an ID").build();
        }
        nurseRepository.save(nurse);
        return ResponseEntity.created(new URI("/api/nurses/" + nurse.getId())).build();
    }

    /**
     * PUT  /nurses -> Updates an existing nurse.
     */
    @RequestMapping(value = "/nurses",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Nurse nurse) throws URISyntaxException {
        log.debug("REST request to update Nurse : {}", nurse);
        if (nurse.getId() == null) {
            return create(nurse);
        }
        nurseRepository.save(nurse);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /nurses -> get all the nurses.
     */
    @RequestMapping(value = "/nurses",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Nurse> getAll() {
        log.debug("REST request to get all Nurses");
        return nurseRepository.findAll();
    }

    /**
     * GET  /nurses/:id -> get the "id" nurse.
     */
    @RequestMapping(value = "/nurses/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Nurse> get(@PathVariable Long id) {
        log.debug("REST request to get Nurse : {}", id);
        return Optional.ofNullable(nurseRepository.findOne(id))
            .map(nurse -> new ResponseEntity<>(
                nurse,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /nurses/:id -> delete the "id" nurse.
     */
    @RequestMapping(value = "/nurses/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Nurse : {}", id);
        nurseRepository.delete(id);
    }
}
