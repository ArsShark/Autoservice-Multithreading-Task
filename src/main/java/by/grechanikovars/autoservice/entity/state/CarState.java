package by.grechanikovars.autoservice.entity.state;

import by.grechanikovars.autoservice.entity.Car;

public interface CarState {

    void onEnter(Car car);

    String getStateName();
}
