package com.th3.appms.web.rest;

import com.th3.appms.NotiApp;

import com.th3.appms.domain.Imei;
import com.th3.appms.repository.ImeiRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ImeiResource REST controller.
 *
 * @see ImeiResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NotiApp.class)
public class ImeiResourceIntTest {

    private static final String DEFAULT_IMEI = "AAAAAAAAAA";
    private static final String UPDATED_IMEI = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    @Inject
    private ImeiRepository imeiRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restImeiMockMvc;

    private Imei imei;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ImeiResource imeiResource = new ImeiResource();
        ReflectionTestUtils.setField(imeiResource, "imeiRepository", imeiRepository);
        this.restImeiMockMvc = MockMvcBuilders.standaloneSetup(imeiResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Imei createEntity(EntityManager em) {
        Imei imei = new Imei()
                .imei(DEFAULT_IMEI)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE);
        return imei;
    }

    @Before
    public void initTest() {
        imei = createEntity(em);
    }

    @Test
    @Transactional
    public void createImei() throws Exception {
        int databaseSizeBeforeCreate = imeiRepository.findAll().size();

        // Create the Imei

        restImeiMockMvc.perform(post("/api/imeis")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(imei)))
                .andExpect(status().isCreated());

        // Validate the Imei in the database
        List<Imei> imeis = imeiRepository.findAll();
        assertThat(imeis).hasSize(databaseSizeBeforeCreate + 1);
        Imei testImei = imeis.get(imeis.size() - 1);
        assertThat(testImei.getImei()).isEqualTo(DEFAULT_IMEI);
        assertThat(testImei.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testImei.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    public void checkImeiIsRequired() throws Exception {
        int databaseSizeBeforeTest = imeiRepository.findAll().size();
        // set the field null
        imei.setImei(null);

        // Create the Imei, which fails.

        restImeiMockMvc.perform(post("/api/imeis")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(imei)))
                .andExpect(status().isBadRequest());

        List<Imei> imeis = imeiRepository.findAll();
        assertThat(imeis).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = imeiRepository.findAll().size();
        // set the field null
        imei.setEmail(null);

        // Create the Imei, which fails.

        restImeiMockMvc.perform(post("/api/imeis")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(imei)))
                .andExpect(status().isBadRequest());

        List<Imei> imeis = imeiRepository.findAll();
        assertThat(imeis).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = imeiRepository.findAll().size();
        // set the field null
        imei.setPhone(null);

        // Create the Imei, which fails.

        restImeiMockMvc.perform(post("/api/imeis")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(imei)))
                .andExpect(status().isBadRequest());

        List<Imei> imeis = imeiRepository.findAll();
        assertThat(imeis).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllImeis() throws Exception {
        // Initialize the database
        imeiRepository.saveAndFlush(imei);

        // Get all the imeis
        restImeiMockMvc.perform(get("/api/imeis?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(imei.getId().intValue())))
                .andExpect(jsonPath("$.[*].imei").value(hasItem(DEFAULT_IMEI.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())));
    }

    @Test
    @Transactional
    public void getImei() throws Exception {
        // Initialize the database
        imeiRepository.saveAndFlush(imei);

        // Get the imei
        restImeiMockMvc.perform(get("/api/imeis/{id}", imei.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(imei.getId().intValue()))
            .andExpect(jsonPath("$.imei").value(DEFAULT_IMEI.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingImei() throws Exception {
        // Get the imei
        restImeiMockMvc.perform(get("/api/imeis/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateImei() throws Exception {
        // Initialize the database
        imeiRepository.saveAndFlush(imei);
        int databaseSizeBeforeUpdate = imeiRepository.findAll().size();

        // Update the imei
        Imei updatedImei = imeiRepository.findOne(imei.getId());
        updatedImei
                .imei(UPDATED_IMEI)
                .email(UPDATED_EMAIL)
                .phone(UPDATED_PHONE);

        restImeiMockMvc.perform(put("/api/imeis")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedImei)))
                .andExpect(status().isOk());

        // Validate the Imei in the database
        List<Imei> imeis = imeiRepository.findAll();
        assertThat(imeis).hasSize(databaseSizeBeforeUpdate);
        Imei testImei = imeis.get(imeis.size() - 1);
        assertThat(testImei.getImei()).isEqualTo(UPDATED_IMEI);
        assertThat(testImei.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testImei.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void deleteImei() throws Exception {
        // Initialize the database
        imeiRepository.saveAndFlush(imei);
        int databaseSizeBeforeDelete = imeiRepository.findAll().size();

        // Get the imei
        restImeiMockMvc.perform(delete("/api/imeis/{id}", imei.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Imei> imeis = imeiRepository.findAll();
        assertThat(imeis).hasSize(databaseSizeBeforeDelete - 1);
    }
}
