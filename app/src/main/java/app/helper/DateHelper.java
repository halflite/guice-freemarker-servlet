package app.helper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 日時のヘルパー */
public class DateHelper {
  /** logger */
  private final static Logger LOG = LoggerFactory.getLogger(DateHelper.class);

  private final ZoneId zoneId;

  /** 現在時刻を返します */
  public Date nowDate() {
    Instant instant = LocalDateTime.now().atZone(this.zoneId).toInstant();
    return Date.from(instant);
  }

  public DateHelper(ZoneId zoneId) {
    LOG.info("zone id: {}", zoneId);
    this.zoneId = zoneId;
  }
}
