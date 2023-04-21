package org.example.server;

import java.lang.reflect.Constructor;

interface Foo<T> {

    T with(String name, Class<T> clazz) throws Exception;
}

interface Model {

    String getName();
}

public class HelloWorld {


    public static void main(String[] args) throws Exception {
        Foo<Model1> bar = new Bar<>();
        Model model = bar.with("Super", Model1.class);
        System.out.println(model.getName());
    }
}

class Bar<M extends Model> implements Foo<M> {

    public M with(String name, Class<M> clazz) throws Exception {
        Constructor<M> declaredConstructor = clazz.getDeclaredConstructor(String.class);
        M m = declaredConstructor.newInstance(name);
        return m;
    }
}

class Model1 implements Model {

    private String name;

    public Model1(String name) {
        this.name = "Model1 name " + name;
    }

    public String getName() {
        return name;
    }
}

class Model2 implements Model {

    private String name;

    public Model2(String name) {
        this.name = "Model2 name " + name;
    }

    public String getName() {
        return name;
    }
}

