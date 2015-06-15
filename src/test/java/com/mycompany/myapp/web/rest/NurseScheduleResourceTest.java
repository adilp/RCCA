package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.NurseSchedule;
import com.mycompany.myapp.repository.NurseScheduleRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.joda.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the NurseScheduleResource REST controller.
 *
 * @see NurseScheduleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class NurseScheduleResourceTest {


    private static final LocalDate DEFAULT_SCHEDULE_DATE = new LocalDate(0L);
    private static final LocalDate UPDATED_SCHEDULE_DATE = new LocalDate();

    @Inject
    private NurseScheduleRepository nurseScheduleRepository;

    private MockMvc restNurseScheduleMockMvc;

    private NurseSchedule nurseSchedule;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NurseScheduleResource nurseScheduleResource = new NurseScheduleResource();
        ReflectionTestUtils.setField(nurseScheduleResource, "nurseScheduleRepository", nurseScheduleRepository);
        this.restNurseScheduleMockMvc = MockMvcBuilders.standaloneSetup(nurseScheduleResource).build();
    }

    @Before
    public void initTest() {
        nurseSchedule = new NurseSchedule();
        nurseSchedule.setScheduleDate(DEFAULT_SCHEDULE_DATE);
    }

    @Test
    @Transactional
    public void createNurseSchedule() throws Exception {
        int databaseSizeBeforeCreate = nurseScheduleRepository.findAll().size();

        // Create the NurseSchedule
        restNurseScheduleMockMvc.perform(post("/api/nurseSchedules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(nurseSchedule)))
                .andExpect(status().isCreated());

        // Validate the NurseSchedule in the database
        List<NurseSchedule> nurseSchedules = nurseScheduleRepository.findAll();
        assertThat(nurseSchedules).hasSize(databaseSizeBeforeCreate + 1);
        NurseSchedule testNurseSchedule = nurseSchedules.get(nurseSchedules.size() - 1);
        assertThat(testNurseSchedule.getScheduleDate()).isEqualTo(DEFAULT_SCHEDULE_DATE);
    }

    @Test
    @Transactional
    public void checkScheduleDateIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(nurseScheduleRepository.findAll()).hasSize(0);
        // set the field null
        nurseSchedule.setScheduleDate(null);

        // Create the NurseSchedule, which fails.
        restNurseScheduleMockMvc.perform(post("/api/nurseSchedules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(nurseSchedule)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<NurseSchedule> nurseSchedules = nurseScheduleRepository.findAll();
        assertThat(nurseSchedules).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllNurseSchedules() throws Exception {
        // Initialize the database
        nurseScheduleRepository.saveAndFlush(nurseSchedule);

        // Get all the nurseSchedules
        restNurseScheduleMockMvc.perform(get("/api/nurseSchedules"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(nurseSchedule.getId().intValue())))
                .andExpect(jsonPath("$.[*].scheduleDate").value(hasItem(DEFAULT_SCHEDULE_DATE.toString())));
    }

    @Test
    @Transactional
    public void getNurseSchedule() throws Exception {
        // Initialize the database
        nurseScheduleRepository.saveAndFlush(nurseSchedule);

        // Get the nurseSchedule
        restNurseScheduleMockMvc.perform(get("/api/nurseSchedules/{id}", nurseSchedule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(nurseSchedule.getId().intValue()))
            .andExpect(jsonPath("$.scheduleDate").value(DEFAULT_SCHEDULE_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNurseSchedule() throws Exception {
        // Get the nurseSchedule
        restNurseScheduleMockMvc.perform(get("/api/nurseSchedules/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNurseSchedule() throws Exception {
        // Initialize the database
        nurseScheduleRepository.saveAndFlush(nurseSchedule);

		int databaseSizeBeforeUpdate = nurseScheduleRepository.findAll().size();

        // Update the nurseSchedule
        nurseSchedule.setScheduleDate(UPDATED_SCHEDULE_DATE);
        restNurseScheduleMockMvc.perform(put("/api/nurseSchedules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(nurseSchedule)))
                .andExpect(status().isOk());

        // Validate the NurseSchedule in the database
        List<NurseSchedule> nurseSchedules = nurseScheduleRepository.findAll();
        assertThat(nurseSchedules).hasSize(databaseSizeBeforeUpdate);
        NurseSchedule testNurseSchedule = nurseSchedules.get(nurseSchedules.size() - 1);
        assertThat(testNurseSchedule.getScheduleDate()).isEqualTo(UPDATED_SCHEDULE_DATE);
    }

    @Test
    @Transactional
    public void deleteNurseSchedule() throws Exception {
        // Initialize the database
        nurseScheduleRepository.saveAndFlush(nurseSchedule);

		int databaseSizeBeforeDelete = nurseScheduleRepository.findAll().size();

        // Get the nurseSchedule
        restNurseScheduleMockMvc.perform(delete("/api/nurseSchedules/{id}", nurseSchedule.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<NurseSchedule> nurseSchedules = nurseScheduleRepository.findAll();
        assertThat(nurseSchedules).hasSize(databaseSizeBeforeDelete - 1);
    }
}
