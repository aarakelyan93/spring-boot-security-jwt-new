package com.example.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtils {

  public static final ZoneId ZONE_ID = ZoneId.of("UTC");

  /**
   * The method converts localDateTime to epoch milli.
   *
   * @return localDateTime as epoch milli
   */
  public static Long toEpochMilli(LocalDateTime localDateTime) {
    if (Objects.isNull(localDateTime)) {
      return null;
    }

    return localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
  }

  /**
   * Get current date time in milliseconds as UTC.
   *
   * @return current date time in milliseconds as UTC
   */
  public static Long getNow() {
    final var instant = ZonedDateTime.now(ZONE_ID).toInstant();
    return instant.toEpochMilli();
  }

  /**
   * Get current LocalDateTime as UTC.
   */
  public static LocalDateTime currentLocalDateTime() {
    return ZonedDateTime.now(ZONE_ID).toLocalDateTime();
  }

}
