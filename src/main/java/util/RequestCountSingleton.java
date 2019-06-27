package util;

import lombok.Data;

@Data
public class RequestCountSingleton {
    private static RequestCountSingleton instance;
    private Double runtime = 0D;

    private RequestCountSingleton() {
    }

    public static RequestCountSingleton getInstance() {
        if (instance == null) {
            instance = new RequestCountSingleton();
        }
        return instance;
    }

    public void increment(Double runtime) {
        this.runtime += runtime;
    }
}
