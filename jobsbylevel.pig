postings = LOAD 'sample_postings.csv' USING PigStorage(',') AS
    (job_link:chararray, last_processed_time:chararray, got_summary:chararray,
     got_ner:chararray, is_being_worked:chararray, job_title:chararray,
     company:chararray, job_location:chararray, first_seen:chararray,
     search_city:chararray, search_country:chararray, search_position:chararray,
     job_level:chararray, job_type:chararray);

filtered = FILTER postings BY job_level IS NOT NULL AND job_level != 'job_level';
grouped = GROUP filtered BY job_level;
counted = FOREACH grouped GENERATE group AS level, COUNT(filtered.job_link) AS total;
sorted = ORDER counted BY total DESC;

DUMP sorted;