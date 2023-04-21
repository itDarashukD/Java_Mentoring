package com.example.one.controller;

import com.example.one.metricPoller.MetricPollerObserver;
import com.example.one.timer.ServoTimer;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import com.netflix.servo.Metric;
import com.netflix.servo.monitor.BasicInformational;
import com.netflix.servo.monitor.MonitorConfig;
import com.netflix.servo.monitor.StatsTimer;
import com.netflix.servo.monitor.Stopwatch;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class ConfigPropertiesController {

    @Autowired
    private ServoTimer servoTimer;
    @Autowired
    private MetricPollerObserver observer;

    private final DynamicStringProperty propertyOneWithDynamic
	   = DynamicPropertyFactory.getInstance()
	   .getStringProperty("baeldung.archaius.properties.one", "not found!");

    private final DynamicStringProperty propertyOneWithDynamic1
	   = DynamicPropertyFactory.getInstance()
	   .getStringProperty("foreach1", "not found!");

    private final DynamicStringProperty propertyOneWithDynamic2
	   = DynamicPropertyFactory.getInstance()
	   .getStringProperty("property.from.other.file", "not found!");


    @GetMapping("/property-from-dynamic-management")
    public String getPropertyValue() {
        StatsTimer statsTimer = servoTimer.prepareTimer();
        Stopwatch stopwatch = statsTimer.start();

        List<List<Metric>> lists = observer.prepareSystemMetrics();

        System.out.println(propertyOneWithDynamic.getName() + " : " + propertyOneWithDynamic.getValue());
        System.out.println(propertyOneWithDynamic1.getName() + " : " + propertyOneWithDynamic1.getValue());
        System.out.println(propertyOneWithDynamic2.getName() + " : " + propertyOneWithDynamic2.getValue());

        stopwatch.stop();
        servoTimer.printStatistic(statsTimer);

        observer.printSystemMetrics(lists);

        return "it works";
    }


}
