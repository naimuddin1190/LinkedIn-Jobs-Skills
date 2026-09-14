postings = LOAD 'sample_postings.csv' USING PigStorage(',') AS
    (job_link:chararray, last_processed_time:chararray, got_summary:chararray,
     got_ner:chararray, is_being_worked:chararray, job_title:chararray,
     company:chararray, job_location:chararray, first_seen:chararray,
     search_city:chararray, search_country:chararray, search_position:chararray,
     job_level:chararray, job_type:chararray);

filtered = FILTER postings BY company IS NOT NULL AND company != 'company';
grouped = GROUP filtered BY company;
counted = FOREACH grouped GENERATE group AS company, COUNT(filtered.job_link) AS total_jobs;
sorted = ORDER counted BY total_jobs DESC;
top10 = LIMIT sorted 10;

DUMP top10;