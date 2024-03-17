package Manager;

import Class.Car;

public class CarManager {
    private static CarManager instance;
    private Car currentCar;

    private CarManager() {

    }

    public static synchronized CarManager getInstance() {
        if (instance == null) {
            instance = new CarManager();
        }

        return instance;
    }

    public void setCurrentCar(Car car) {
        this.currentCar = car;
    }

    public Car getCurrentCar() {
        return currentCar;
    }
}
