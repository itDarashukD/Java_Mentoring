package org.example.lru;

import com.google.common.cache.LoadingCache;
import org.example.lfu.entity.Model;

import java.util.concurrent.ExecutionException;

public class LruMain {

        public static void main(String[] args) throws ExecutionException, InterruptedException {
            Model model1 = new Model("name1");
            Model model2 = new Model("name2");
            Model model3 = new Model("name3");
            Model model4 = new Model("name4");
            Model model5 = new Model("name5");
            Model model6 = new Model("name6");

            LoadingCache<String, Model> cache = new LruCache().createCache(3L,5L);
            cache.put(model1.getName(), model1);
            cache.put(model2.getName(), model2);
            cache.put(model3.getName(), model3);
            cache.put(model4.getName(), model4);
            cache.put(model5.getName(), model5);
//        cache.put(model6.getName(), model6);

            System.out.println("now");

            cache.asMap().entrySet().stream().forEach(entry -> System.out.println(entry.getKey() + "=" + entry.getValue()));
            ;
            System.out.println(cache.get(model4.getName()));
            System.out.println(cache.get(model4.getName()));
            cache.put(model3.getName(), model3);
            System.out.println(cache.get(model6.getName()));
            System.out.println(cache.get(model6.getName()));
            cache.put(model6.getName(), model6);

            System.out.println(cache.get(model6.getName()));

            System.out.println(cache.stats().evictionCount() + "!!!!!!!!!!!!!!!!!!!");
            System.out.println(cache.stats().averageLoadPenalty() + "!!!!!!!!!!!!!!!!!!!");

            Thread.sleep(5100);

            System.out.println("after 5 second");

            cache.asMap().entrySet().stream().forEach(entry -> System.out.println(entry.getKey() + "=" + entry.getValue()));

        }
    }

