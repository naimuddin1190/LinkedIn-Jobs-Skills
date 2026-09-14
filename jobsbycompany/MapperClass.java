package jobsbycompany;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MapperClass extends Mapper<LongWritable, Text, Text, IntWritable> {

    private Text company = new Text();
    private final static IntWritable one = new IntWritable(1);

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String line = value.toString();

        // Skip header row
        if (line.startsWith("job_link") || line.contains("search_country")) {
            return;
        }

        // Quote-aware split
        String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

        if (fields.length > 6) {
            String companyName = fields[6].trim();

            if (companyName.startsWith("\"") && companyName.endsWith("\"") && companyName.length() > 1) {
                companyName = companyName.substring(1, companyName.length() - 1);
            }
            companyName = companyName.replace("\"\"", "\"").trim();

            if (!companyName.isEmpty()) {
                company.set(companyName);
                context.write(company, one);
            }
        }
    }
}