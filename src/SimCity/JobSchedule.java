package SimCity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JobSchedule {
	enum scheduleType {start, end, weekendStart, weekendEnd};
	
	Map <scheduleType, Time> schedule = new HashMap<scheduleType, Time>();
	
	public void addSchedule(scheduleType type, Time time){
		schedule.put(type, time);
	}
	
	public Map<scheduleType, Time> getSchedule(){
		return schedule;
	}
	
}
