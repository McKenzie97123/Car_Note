package Manager;

import Class.Event;

public class EventManager {
    private static EventManager instance;
    private Event currentEvent;

    private EventManager() {

    }

    public static synchronized EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }

        return instance;
    }

    public void setCurrentEvent(Event event) {
        this.currentEvent = event;
    }

    public Event getCurrentEvent() {
        return currentEvent;
    }
}
