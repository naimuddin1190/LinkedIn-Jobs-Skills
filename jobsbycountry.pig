postings = LOAD 'sample_postings.csv' USING PigStorage(',') AS
    (job_link:chararray, last_processed_time:chararray, got_summary:chararray,
     got_ner:chararray, is_being_worked:chararray, job_title:chararray,
     company:chararray, job_location:chararray, first_seen:chararray,
     search_city:chararray, search_country:chararray, search_position:chararray,
     job_level:chararray, job_type:chararray);

filtered = FILTER postings BY search_country IS NOT NULL AND search_country != 'search_country';
grouped = GROUP filtered BY search_country;
counted = FOREACH grouped GENERATE group AS country, COUNT(filtered.job_link) AS total;
sorted = ORDER counted BY total DESC;

DUMP sorted;