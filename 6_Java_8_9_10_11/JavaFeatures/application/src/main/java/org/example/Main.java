package org.example;

import java.util.ServiceLoader;
import org.example.serviceApi.Service;

public class Main {

    public static void main(String[] args) {
        Iterable<Service> services = ServiceLoader.load(Service.class);
        System.out.println(services);
        for (Service serv : services) {
	   double averageUsersAge = serv.getAverageUsersAge();
        }
    }

}