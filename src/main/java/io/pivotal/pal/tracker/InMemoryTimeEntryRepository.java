package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private Map<Long, TimeEntry> timeEntryMap;
    private long lastAssignedId;

    public InMemoryTimeEntryRepository() {
        timeEntryMap = new HashMap<>();
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        TimeEntry timeEntryToBeInserted = new TimeEntry(timeEntry);
        timeEntryToBeInserted.setId(++lastAssignedId);
        timeEntryMap.put(lastAssignedId, timeEntryToBeInserted);
        return find(lastAssignedId);
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        return timeEntryMap.get(timeEntryId);
    }

    @Override
    public TimeEntry update(long timeEntryId, TimeEntry timeEntry) {
        TimeEntry timeEntryToBeUpdated = new TimeEntry(timeEntry);
        timeEntryToBeUpdated.setId(timeEntryId);
        timeEntryMap.replace(timeEntryId, timeEntryToBeUpdated);
        return find(timeEntryId);
    }

    @Override
    public void delete(long timeEntryId) {
        timeEntryMap.remove(timeEntryId);
    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<>(timeEntryMap.values());
    }
}
