package com.example.springbatchdemo.logger;

import static com.example.springbatchdemo.constants.ApplicationConstants.LOG_AFTER_WRITE;
import static com.example.springbatchdemo.constants.ApplicationConstants.LOG_BEFORE_WRITE;
import static com.example.springbatchdemo.constants.ApplicationConstants.LOG_ERROR_WRITE;

import com.example.springbatchdemo.dao.entities.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;

@Slf4j
public class ChunkSizeLogger implements ItemWriteListener<Person> {

  @Override
  public void beforeWrite(Chunk<? extends Person> items) {
    log.info(LOG_BEFORE_WRITE, items.size());
  }

  @Override
  public void afterWrite(Chunk<? extends Person> items) {
    log.info(LOG_AFTER_WRITE, items.size());
  }

  @Override
  public void onWriteError(Exception exception, Chunk<? extends Person> items) {
    log.error(LOG_ERROR_WRITE, items.size(), exception);
  }
}