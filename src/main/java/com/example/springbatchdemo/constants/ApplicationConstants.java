package com.example.springbatchdemo.constants;

public class ApplicationConstants {

  public static final String PEOPLE_INSERT_JOB = "peopleInsertJob";
  public static final String PEOPLE_INSERT_STEP = "peopleInsertStep";
  public static final String PEOPLE_FILE_READER = "cvsReader";
  public static final String FILE_PATH = "people.csv";
  public static final Integer CHUNK_SIZE = 10;
  public static final String[] FIELD_NAMES = {"id", "firstName", "lastName", "birthDate", "email"};
  public static final String DELIMITER = ",";
  public static final String JOB_START_LOG = "Job started : {}";
  public static final String JOB_FAILED_LOG = "Job failed with exception: {} ";
  public static final String LOG_BEFORE_WRITE = "About to write chunk of {} people";
  public static final String LOG_AFTER_WRITE = "Successfully wrote chunk of {} people";
  public static final String LOG_ERROR_WRITE = "Error writing chunk of {} people";

}
