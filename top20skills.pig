skills = LOAD 'sample_skills.csv' USING PigStorage(',') AS
    (job_link:chararray, job_skills:chararray);

filtered = FILTER skills BY job_skills IS NOT NULL AND job_link != 'job_link';
tokenized = FOREACH filtered GENERATE FLATTEN(TOKENIZE(job_skills, ',')) AS skill;
cleaned = FOREACH tokenized GENERATE TRIM(skill) AS skill;
grouped = GROUP cleaned BY skill;
counted = FOREACH grouped GENERATE group AS skill, COUNT(cleaned) AS total;
sorted = ORDER counted BY total DESC;
top20 = LIMIT sorted 20;

DUMP top20;