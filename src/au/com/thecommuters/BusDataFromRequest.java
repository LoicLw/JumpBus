package au.com.thecommuters;

public class BusDataFromRequest {

	public String time;
    public String direction;
    public String line;	
	
	public BusDataFromRequest(String time, String direction, String line) {
		super();
		this.time = time;
		this.direction = direction;
		this.line = line;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}
	
	
	

}
