package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private JdbcTemplate jdbcTemplate;
    private KeyHolder generatedKeyHolder;
    private final String tableName = "time_entries";

    public JdbcTimeEntryRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        generatedKeyHolder = new GeneratedKeyHolder();
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        String sql = "INSERT INTO " + tableName +
                " (project_id, user_id, date, hours) " +
                " VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(
                (connection) -> {
                    PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    statement.setLong(1, timeEntry.getProjectId());
                    statement.setLong(2, timeEntry.getUserId());
                    statement.setDate(3, Date.valueOf(timeEntry.getDate()));
                    statement.setInt(4, timeEntry.getHours());
                    return statement;
                },
                generatedKeyHolder);

        return find(generatedKeyHolder.getKey().longValue());
    }

    @Override
    public TimeEntry update(long timeEntryId, TimeEntry timeEntry) {
        String sql = "UPDATE " + tableName +
                " SET project_id = ?, user_id = ?, date = ?, hours = ? "+
                "WHERE id = ?";

        jdbcTemplate.update(sql,
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                Date.valueOf(timeEntry.getDate()),
                timeEntry.getHours(),
                timeEntryId
        );

        return find(timeEntryId);
    }

    @Override
    public TimeEntry find(long timeEntryId) {
        String sql = "SELECT * FROM " + tableName +
                    " WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[]{timeEntryId}, extractor);
    }

    @Override
    public void delete(long timeEntryId) {
        String sql = "DELETE FROM " + tableName +
                " WHERE id = ?";

        jdbcTemplate.update(sql, timeEntryId);
    }

    @Override
    public List<TimeEntry> list() {
        String sql = "SELECT * FROM " + tableName;
        return jdbcTemplate.query(sql, mapper);
    }

    private final RowMapper<TimeEntry> mapper = (rs, rowNum) -> {
        return new TimeEntry(
                rs.getLong("id"),
                rs.getLong("project_id"),
                rs.getLong("user_id"),
                rs.getDate("date").toLocalDate(),
                rs.getInt("hours")
        );
    };

    private final ResultSetExtractor<TimeEntry> extractor = (rs) -> rs.next()? mapper.mapRow(rs, 1):null;


}
