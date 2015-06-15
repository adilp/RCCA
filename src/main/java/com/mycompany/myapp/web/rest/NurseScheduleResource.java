package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.NurseSchedule;
import com.mycompany.myapp.repository.NurseScheduleRepository;
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
 * REST controller for managing NurseSchedule.
 */
@RestController
@RequestMapping("/api")
public class NurseScheduleResource {

    private final Logger log = LoggerFactory.getLogger(NurseScheduleResource.class);

    @Inject
    private NurseScheduleRepository nurseScheduleRepository;

    /**
     * POST  /nurseSchedules -> Create a new nurseSchedule.
     */
    @RequestMapping(value = "/nurseSchedules",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody NurseSchedule nurseSchedule) throws URISyntaxException {
        log.debug("REST request to save NurseSchedule : {}", nurseSchedule);
        if (nurseSchedule.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new nurseSchedule cannot already have an ID").build();
        }
        nurseScheduleRepository.save(nurseSchedule);
        return ResponseEntity.created(new URI("/api/nurseSchedules/" + nurseSchedule.getId())).build();
    }

    /**
     * PUT  /nurseSchedules -> Updates an existing nurseSchedule.
     */
    @RequestMapping(value = "/nurseSchedules",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody NurseSchedule nurseSchedule) throws URISyntaxException {
        log.debug("REST request to update NurseSchedule : {}", nurseSchedule);
        if (nurseSchedule.getId() == null) {
            return create(nurseSchedule);
        }
        nurseScheduleRepository.save(nurseSchedule);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /nurseSchedules -> get all the nurseSchedules.
     */
    @RequestMapping(value = "/nurseSchedules",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<NurseSchedule> getAll() {
        log.debug("REST request to get all NurseSchedules");
        return nurseScheduleRepository.findAll();
    }

    /**
     * GET  /nurseSchedules/:id -> get the "id" nurseSchedule.
     */
    @RequestMapping(value = "/nurseSchedules/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NurseSchedule> get(@PathVariable Long id) {
        log.debug("REST request to get NurseSchedule : {}", id);
        return Optional.ofNullable(nurseScheduleRepository.findOne(id))
            .map(nurseSchedule -> new ResponseEntity<>(
                nurseSchedule,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /nurseSchedules/:id -> delete the "id" nurseSchedule.
     */
    @RequestMapping(value = "/nurseSchedules/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete NurseSchedule : {}", id);
        nurseScheduleRepository.delete(id);
    }
}
