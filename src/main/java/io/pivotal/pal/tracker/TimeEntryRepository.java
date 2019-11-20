package io.pivotal.pal.tracker;

import java.sql.Time;
import java.util.List;

public interface TimeEntryRepository {

    TimeEntry create(TimeEntry timeEntry);

    TimeEntry update(long timeEntryId, TimeEntry timeEntry);

    TimeEntry find(long timeEntryId);

    void delete(long timeEntryId);

    List<TimeEntry> list();





}
