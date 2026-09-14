package skillcount;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MapperClass extends Mapper<LongWritable, Text, Text, IntWritable> {

    private Text skill = new Text();
    private final static IntWritable one = new IntWritable(1);

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String line = value.toString();

        // Quote-aware split: keeps the quoted skills list as one field
        String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

        if (fields.length < 2) return;

        String skillsField = fields[1].trim();

        if (skillsField.startsWith("\"") && skillsField.endsWith("\"") && skillsField.length() > 1) {
            skillsField = skillsField.substring(1, skillsField.length() - 1);
        }
        skillsField = skillsField.replace("\"\"", "\"");

        String[] skills = skillsField.split(",");

        for (String s : skills) {
            String cleaned = s.trim();
            if (!cleaned.isEmpty()) {
                skill.set(cleaned);
                context.write(skill, one);
            }
        }
    }
}