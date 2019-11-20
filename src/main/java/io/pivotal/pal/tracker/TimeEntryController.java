package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {

    private final TimeEntryRepository timeEntryRepository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @PostMapping("/time-entries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry createdTimeEntry = timeEntryRepository.create(timeEntryToCreate);
        return new ResponseEntity<>(createdTimeEntry, HttpStatus.CREATED);
    }

    @GetMapping("/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId) {
        HttpStatus status;
        TimeEntry retrievedTimeEntry = timeEntryRepository.find(timeEntryId);

        if (retrievedTimeEntry != null) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(retrievedTimeEntry, status);
    }

    @PutMapping("/time-entries/{timeEntryId}")
    public ResponseEntity update(@PathVariable long timeEntryId, @RequestBody TimeEntry expected) {
        HttpStatus status;
        TimeEntry updatedTimeEntry = timeEntryRepository.update(timeEntryId, expected);

        if (updatedTimeEntry != null) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity(updatedTimeEntry, status);
    }

    @DeleteMapping("/time-entries/{timeEntryId}")
    public ResponseEntity delete(@PathVariable long timeEntryId) {
        timeEntryRepository.delete(timeEntryId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        List<TimeEntry> timeEntryList= timeEntryRepository.list();
    return new ResponseEntity<>(timeEntryList, HttpStatus.OK);
    }
}
