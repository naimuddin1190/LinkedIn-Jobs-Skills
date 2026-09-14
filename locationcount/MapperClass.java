package locationcount;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MapperClass extends Mapper<LongWritable, Text, Text, IntWritable> {

    private Text country = new Text();
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

        if (fields.length > 10) {
            String countryName = fields[10].trim();

            if (countryName.startsWith("\"") && countryName.endsWith("\"") && countryName.length() > 1) {
                countryName = countryName.substring(1, countryName.length() - 1);
            }
            countryName = countryName.replace("\"\"", "\"").trim();

            if (!countryName.isEmpty()) {
                country.set(countryName);
                context.write(country, one);
            }
        }
    }
}