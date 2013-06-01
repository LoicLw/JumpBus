package au.com.thecommuters.model;

public class BusDataFromRequest {

	private long aimedArrivalTime;
    private String direction;
    private String line;
    private long latestExpArrivalTime;
    
    
	
	public BusDataFromRequest(long aimedArriTime, String direction, String line, long latestExpArrTime) {
		super();
		this.aimedArrivalTime = aimedArriTime;
		this.direction = direction;
		this.line = line;
		this.latestExpArrivalTime = latestExpArrTime;
	}

	

	public long getAimedArrivalTime() {
		return aimedArrivalTime;
	}



	public void setAimedArrivalTime(long aimedArrivalTime) {
		this.aimedArrivalTime = aimedArrivalTime;
	}



	public long getLatestExpArrivalTime() {
		return latestExpArrivalTime;
	}



	public void setLatestExpArrivalTime(long latestExpArrivalTime) {
		this.latestExpArrivalTime = latestExpArrivalTime;
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



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (aimedArrivalTime ^ (aimedArrivalTime >>> 32));
		result = prime * result
				+ ((direction == null) ? 0 : direction.hashCode());
		result = prime * result
				+ (int) (latestExpArrivalTime ^ (latestExpArrivalTime >>> 32));
		result = prime * result + ((line == null) ? 0 : line.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		//if (this == obj)
			//return true;
		//if (obj == null)
			//return false;
	//	if (getClass() != obj.getClass())
			//return false;
		BusDataFromRequest other = (BusDataFromRequest) obj;
		if (aimedArrivalTime != other.aimedArrivalTime)
			return false;
		if (direction == null) {
			if (other.direction != null)
				return false;
		} else if (!direction.equals(other.direction))
			return false;
		if (latestExpArrivalTime != other.latestExpArrivalTime)
			return false;
		if (line == null) {
			if (other.line != null)
				return false;
		} else if (!line.equals(other.line))
			return false;
		return true;
	}
	
	
	

}
