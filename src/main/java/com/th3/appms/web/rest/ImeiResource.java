package com.th3.appms.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.th3.appms.domain.Imei;
import com.th3.appms.domain.NotificationContent;
import com.th3.appms.notification.NotificationSend;
import com.th3.appms.repository.ImeiRepository;
import com.th3.appms.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Imei.
 */
@RestController
@RequestMapping("/api")
public class ImeiResource {

    private final Logger log = LoggerFactory.getLogger(ImeiResource.class);
        
    @Inject
    private ImeiRepository imeiRepository;
    
    @Autowired
    NotificationSend notificationSend;

    /**
     * POST  /imeis : Create a new imei.
     *
     * @param imei the imei to create
     * @return the ResponseEntity with status 201 (Created) and with body the new imei, or with status 400 (Bad Request) if the imei has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/imeis")
    @Timed
    public ResponseEntity<Imei> createImei(@Valid @RequestBody Imei imei) throws URISyntaxException {
        log.debug("REST request to save Imei : {}", imei);
        if (imei.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("imei", "idexists", "A new imei cannot already have an ID")).body(null);
        }
        Imei result = imeiRepository.save(imei);
        return ResponseEntity.created(new URI("/api/imeis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("imei", result.getId().toString()))
            .body(result);
    }

    
    @PostMapping("/imeis/send")
    @Timed
    public ResponseEntity<Boolean> SendMailNoti(@Valid @RequestBody NotificationContent notificationContent) throws URISyntaxException {
        log.debug("REST request to save Imei : {}", notificationContent);

        notificationSend.send(notificationContent);
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityCreationAlert("imei", notificationContent.toString()))
            .body(true);
    }
    
    
    /**
     * PUT  /imeis : Updates an existing imei.
     *
     * @param imei the imei to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated imei,
     * or with status 400 (Bad Request) if the imei is not valid,
     * or with status 500 (Internal Server Error) if the imei couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/imeis")
    @Timed
    public ResponseEntity<Imei> updateImei(@Valid @RequestBody Imei imei) throws URISyntaxException {
        log.debug("REST request to update Imei : {}", imei);
        if (imei.getId() == null) {
            return createImei(imei);
        }
        Imei result = imeiRepository.save(imei);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("imei", imei.getId().toString()))
            .body(result);
    }

    /**
     * GET  /imeis : get all the imeis.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of imeis in body
     */
    @GetMapping("/imeis")
    @Timed
    public List<Imei> getAllImeis() {
        log.debug("REST request to get all Imeis");
        List<Imei> imeis = imeiRepository.findAllWithEagerRelationships();
        return imeis;
    }

    /**
     * GET  /imeis/:id : get the "id" imei.
     *
     * @param id the id of the imei to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the imei, or with status 404 (Not Found)
     */
    @GetMapping("/imeis/{id}")
    @Timed
    public ResponseEntity<Imei> getImei(@PathVariable Long id) {
        log.debug("REST request to get Imei : {}", id);
        Imei imei = imeiRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(imei)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /imeis/:id : delete the "id" imei.
     *
     * @param id the id of the imei to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/imeis/{id}")
    @Timed
    public ResponseEntity<Void> deleteImei(@PathVariable Long id) {
        log.debug("REST request to delete Imei : {}", id);
        imeiRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("imei", id.toString())).build();
    }

}
