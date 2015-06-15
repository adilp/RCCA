package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Nurse;
import com.mycompany.myapp.repository.NurseRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the NurseResource REST controller.
 *
 * @see NurseResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class NurseResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Integer DEFAULT_NURSE_ID = 0;
    private static final Integer UPDATED_NURSE_ID = 1;

    @Inject
    private NurseRepository nurseRepository;

    private MockMvc restNurseMockMvc;

    private Nurse nurse;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NurseResource nurseResource = new NurseResource();
        ReflectionTestUtils.setField(nurseResource, "nurseRepository", nurseRepository);
        this.restNurseMockMvc = MockMvcBuilders.standaloneSetup(nurseResource).build();
    }

    @Before
    public void initTest() {
        nurse = new Nurse();
        nurse.setName(DEFAULT_NAME);
        nurse.setIsActive(DEFAULT_IS_ACTIVE);
        nurse.setNurseId(DEFAULT_NURSE_ID);
    }

    @Test
    @Transactional
    public void createNurse() throws Exception {
        int databaseSizeBeforeCreate = nurseRepository.findAll().size();

        // Create the Nurse
        restNurseMockMvc.perform(post("/api/nurses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(nurse)))
                .andExpect(status().isCreated());

        // Validate the Nurse in the database
        List<Nurse> nurses = nurseRepository.findAll();
        assertThat(nurses).hasSize(databaseSizeBeforeCreate + 1);
        Nurse testNurse = nurses.get(nurses.size() - 1);
        assertThat(testNurse.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testNurse.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testNurse.getNurseId()).isEqualTo(DEFAULT_NURSE_ID);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(nurseRepository.findAll()).hasSize(0);
        // set the field null
        nurse.setName(null);

        // Create the Nurse, which fails.
        restNurseMockMvc.perform(post("/api/nurses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(nurse)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Nurse> nurses = nurseRepository.findAll();
        assertThat(nurses).hasSize(0);
    }

    @Test
    @Transactional
    public void checkNurseIdIsRequired() throws Exception {
        // Validate the database is empty
        assertThat(nurseRepository.findAll()).hasSize(0);
        // set the field null
        nurse.setNurseId(null);

        // Create the Nurse, which fails.
        restNurseMockMvc.perform(post("/api/nurses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(nurse)))
                .andExpect(status().isBadRequest());

        // Validate the database is still empty
        List<Nurse> nurses = nurseRepository.findAll();
        assertThat(nurses).hasSize(0);
    }

    @Test
    @Transactional
    public void getAllNurses() throws Exception {
        // Initialize the database
        nurseRepository.saveAndFlush(nurse);

        // Get all the nurses
        restNurseMockMvc.perform(get("/api/nurses"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(nurse.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
                .andExpect(jsonPath("$.[*].nurseId").value(hasItem(DEFAULT_NURSE_ID)));
    }

    @Test
    @Transactional
    public void getNurse() throws Exception {
        // Initialize the database
        nurseRepository.saveAndFlush(nurse);

        // Get the nurse
        restNurseMockMvc.perform(get("/api/nurses/{id}", nurse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(nurse.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.nurseId").value(DEFAULT_NURSE_ID));
    }

    @Test
    @Transactional
    public void getNonExistingNurse() throws Exception {
        // Get the nurse
        restNurseMockMvc.perform(get("/api/nurses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNurse() throws Exception {
        // Initialize the database
        nurseRepository.saveAndFlush(nurse);

		int databaseSizeBeforeUpdate = nurseRepository.findAll().size();

        // Update the nurse
        nurse.setName(UPDATED_NAME);
        nurse.setIsActive(UPDATED_IS_ACTIVE);
        nurse.setNurseId(UPDATED_NURSE_ID);
        restNurseMockMvc.perform(put("/api/nurses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(nurse)))
                .andExpect(status().isOk());

        // Validate the Nurse in the database
        List<Nurse> nurses = nurseRepository.findAll();
        assertThat(nurses).hasSize(databaseSizeBeforeUpdate);
        Nurse testNurse = nurses.get(nurses.size() - 1);
        assertThat(testNurse.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNurse.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testNurse.getNurseId()).isEqualTo(UPDATED_NURSE_ID);
    }

    @Test
    @Transactional
    public void deleteNurse() throws Exception {
        // Initialize the database
        nurseRepository.saveAndFlush(nurse);

		int databaseSizeBeforeDelete = nurseRepository.findAll().size();

        // Get the nurse
        restNurseMockMvc.perform(delete("/api/nurses/{id}", nurse.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Nurse> nurses = nurseRepository.findAll();
        assertThat(nurses).hasSize(databaseSizeBeforeDelete - 1);
    }
}
