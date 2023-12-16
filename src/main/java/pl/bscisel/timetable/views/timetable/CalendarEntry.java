package pl.bscisel.timetable.views.timetable;

import elemental.json.Json;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import org.vaadin.stefan.fullcalendar.Entry;


public class CalendarEntry extends Entry {
    private final JsonObject properties = Json.createObject();

    public CalendarEntry(Long databaseId) {
        super(String.valueOf(databaseId));
    }

    public void setLocation(String location) {
        setCustomProperty("location", location);
    }

    public void setTeacher(String teacher) {
        setCustomProperty("teacher", teacher);
    }

    @Override
    public JsonObject toJson() {
        JsonObject json = super.toJson();
        json.remove("durationEditable");
        json.remove("editable");
        json.remove("startEditable");
        for (String key : properties.keys()) {
            json.put(key, (JsonValue) properties.get(key));
        }
        return json;
    }

    @Override
    public boolean isEditable() {
        if (!properties.hasKey("editable"))
            return false;
        return properties.getBoolean("editable");
    }

    @Override
    public void setEditable(boolean editable) {
        properties.put("editable", editable);
    }

    @Override
    public boolean isStartEditable() {
        if (!properties.hasKey("startEditable"))
            return false;
        return properties.getBoolean("startEditable");
    }

    @Override
    public void setStartEditable(boolean startEditable) {
        properties.put("startEditable", startEditable);
    }

    @Override
    public boolean isDurationEditable() {
        if (!properties.hasKey("durationEditable"))
            return false;
        return properties.getBoolean("durationEditable");
    }

    @Override
    public void setDurationEditable(boolean durationEditable) {
        properties.put("durationEditable", durationEditable);
    }

}