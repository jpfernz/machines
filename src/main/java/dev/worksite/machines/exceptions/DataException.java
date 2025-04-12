package dev.worksite.machines.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DataException extends RuntimeException {
  public DataException(Exception e) {
    log.info("Data Exception: {}", e.getMessage());
  }
}
