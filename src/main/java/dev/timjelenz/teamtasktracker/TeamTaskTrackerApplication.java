package dev.timjelenz.teamtasktracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TeamTaskTrackerApplication {
  /**
   * Start application.
   *
   * @param args the given CLI arguments
   */
  public static void main(final String[] args) {
    SpringApplication.run(TeamTaskTrackerApplication.class, args);
  }
}
