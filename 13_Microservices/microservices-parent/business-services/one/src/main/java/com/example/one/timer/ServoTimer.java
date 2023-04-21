package com.example.one.timer;

import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toMap;

import com.netflix.servo.monitor.Monitor;
import com.netflix.servo.monitor.MonitorConfig;
import com.netflix.servo.monitor.StatsTimer;
import com.netflix.servo.stats.StatsConfig;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ServoTimer {

    static {
        System.setProperty("netflix.servo", "100");
    }

    public StatsTimer prepareTimer() {
        return new StatsTimer(MonitorConfig.builder("test").build(),
	       new StatsConfig.Builder().withComputeFrequencyMillis(2000)
		      .withPercentiles(new double[]{99.0, 95.0, 90.0})
		      .withPublishMax(true)
		      .withPublishMin(true)
		      .withPublishCount(true)
		      .withPublishMean(true)
		      .withPublishStdDev(true)
		      .withPublishVariance(true)
		      .build(),
	       MICROSECONDS);
    }

    public void printStatistic(StatsTimer statsTimer) {
        final Map<String, Number> statistic =
	       statsTimer.getMonitors()
		      .stream()
		      .collect(toMap(monitor -> getMonitorTagValue(monitor, "statistic"),
			     monitor -> (Number) monitor.getValue()));

        System.out.println(statistic);
    }

    private String getMonitorTagValue(Monitor monitor, String tagKey) {
        return monitor.getConfig().getTags().getTag(tagKey).getValue();
    }


}
