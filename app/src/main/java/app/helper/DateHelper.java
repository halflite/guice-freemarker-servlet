package app.helper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

/** 日時のヘルパー */
@Singleton
public class DateHelper {
  /** logger */
  private final static Logger LOG = LoggerFactory.getLogger(DateHelper.class);

  private final ZoneId zoneId;

  public Date nowDate() {
    Instant instant = LocalDateTime.now().atZone(this.zoneId).toInstant();
    return Date.from(instant);
  }

  @Inject
  public DateHelper(@Named("user.timezone") String tz) {
    LOG.info("{}", tz);
    this.zoneId = ZoneId.of(tz);
  }
}
