postings = LOAD 'sample_postings.csv' USING PigStorage(',') AS
    (job_link:chararray, last_processed_time:chararray, got_summary:chararray,
     got_ner:chararray, is_being_worked:chararray, job_title:chararray,
     company:chararray, job_location:chararray, first_seen:chararray,
     search_city:chararray, search_country:chararray, search_position:chararray,
     job_level:chararray, job_type:chararray);

filtered = FILTER postings BY job_title IS NOT NULL AND job_title != 'job_title';
grouped = GROUP filtered BY job_title;
counted = FOREACH grouped GENERATE group AS title, COUNT(filtered.job_link) AS total;
sorted = ORDER counted BY total DESC;
top10 = LIMIT sorted 10;

DUMP top10;