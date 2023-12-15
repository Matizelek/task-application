package com.application.task.processing;

import com.application.task.entity.Task;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TaskProcessor {

    private static final Logger logger = LoggerFactory.getLogger(TaskProcessor.class);

    private final long threadSleep;

    public TaskProcessor(long threadSleep) {
        this.threadSleep = threadSleep;
    }

    public void processTask(final Task task) {
        logger.info("processTask() start with task=" + task + " Thread name " + Thread.currentThread().getName());
        long startTime = System.currentTimeMillis();

        while((System.currentTimeMillis()-startTime)<threadSleep)
        {
            long percentage = (System.currentTimeMillis()-startTime)*100/threadSleep;
            task.updateProgress(percentage);
        }
        int patternSize = task.getPattern().length();

        List<String> splitInput = getSplitInput(task.getInput(), patternSize);

        LevenshteinDistance algorithm = new LevenshteinDistance();
        int typos = patternSize;
        Integer position = null;
        for(int i = 0; i < splitInput.size(); i++){
            Integer result = algorithm.apply(task.getPattern(), splitInput.get(i));
            if(typos > result){
                position = i;
                typos = result;
            }
        }

        if(Objects.isNull(position)){
            task.finishFailed();
        } else {
            task.finishSuccessfully(position, typos);
        }
        logger.info("processTask() finish with task=" + task);
    }

    private static List<String> getSplitInput(final String input, final Integer size){
        List<String> ret = new ArrayList<>();

        for (int start = 0; start <= input.length() - size; start++) {
            ret.add(input.substring(start, Math.min(input.length(), start + size)));
        }
        return ret;
    }
}
