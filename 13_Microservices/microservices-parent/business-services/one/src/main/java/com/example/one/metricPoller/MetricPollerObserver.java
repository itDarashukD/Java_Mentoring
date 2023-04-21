package com.example.one.metricPoller;


import static java.util.stream.Collectors.toList;

import com.netflix.servo.Metric;
import com.netflix.servo.publish.BasicMetricFilter;
import com.netflix.servo.publish.JvmMetricPoller;
import com.netflix.servo.publish.MemoryMetricObserver;
import com.netflix.servo.publish.PollRunnable;
import com.netflix.servo.publish.PollScheduler;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;

@Service
public class MetricPollerObserver {


    public List<List<Metric>> prepareSystemMetrics() {
        MemoryMetricObserver observer = new MemoryMetricObserver();
        PollRunnable pollRunnable =
	       new PollRunnable(new JvmMetricPoller(), new BasicMetricFilter(true), observer);
        PollScheduler.getInstance().start();
        PollScheduler.getInstance().addPoller(pollRunnable, 1, TimeUnit.SECONDS);

        PollScheduler.getInstance().stop();

        return observer.getObservations();
    }

    public void printSystemMetrics(List<List<Metric>> observations) {
        List<String> args =
	       observations.stream()
		      .filter(m -> !m.isEmpty())
		      .flatMap(ms -> ms.stream().map(m -> m.getConfig().getName()))
		      .collect(toList());

        System.out.println(args);
    }


}
