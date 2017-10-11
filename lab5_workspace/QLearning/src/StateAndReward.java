public class StateAndReward {

	
	/* State discretization function for the angle controller */
	public static String getStateAngle(double angle, double vx, double vy) {

		/* TODO: IMPLEMENT THIS FUNCTION */
		int direction = discretize(angle, 12, -Math.PI, Math.PI);
		String state = Integer.toString(direction);
		
		return state;
	}

	/* Reward function for the angle controller */
	public static double getRewardAngle(double angle, double vx, double vy) {

		/* TODO: IMPLEMENT THIS FUNCTION */
		
		double reward = 0;
		int dir = discretize(angle, 12, -Math.PI, Math.PI);
		switch (dir)
		{
		case 1:
			reward = 0;
			break;
		case 2:
			reward = 0.1;
			break;
		case 3:
			reward = 0.3;
			break;
		case 4:
			reward = 1;
			break;
		case 5:
			reward = 3;
			break;
		case 6:
			reward = 2.5;
			break;
		case 7:
			reward = 1;
			break;
		case 8:
			reward = 0.3;
			break;
		case 9:
			reward = 0.1;
		case 10:
			reward = 0;
		}
		return reward;
	}

	/* State discretization function for the full hover controller */
	public static String getStateHover(double angle, double vx, double vy) {

		String state = getStateAngle(angle, vx, vy);
		
		if (vy < 0.05 && vy > -0.05)
		{
			state += ":REALLY_PERFECT";
		}
		else if (vy > 5)
		{
			state += ":MAX_DOWN";
		}
		else if (vy > 3)
		{
			state += ":MID_DOWN";
		}
		else if (vy > 1)
		{
			state += ":LOW_DOWN";
		}
		else if (vy > 0.5)
		{
			state += ":MINI_DOWN";
		}
		else if (vy > 0.2)
		{
			state += ":MICRO_DOWN";
		}
		else if (vy > -0.2)
		{
			state += ":PERFECT";
		}
		else if (vy < -5)
		{
			state += ":MAX_UP";
		}
		else if (vy < -3)
		{
			state += ":MID_UP";
		}
		else if (vy < -1)
		{
			state += ":LOW_UP";
		}
		else if (vy < -0.5)
		{
			state += ":MINI_UP";
		}
		else if (vy < -0.2)
		{
			state += ":MICRO_UP";
		}	
		
		return state;
	}

	/* Reward function for the full hover controller */
	public static double getRewardHover(double angle, double vx, double vy) {
		
		double reward = getRewardAngle(angle, vx, vy);
		if (vy < 0.05 && vy > -0.05)
		{
			reward += 4.5;
		}
		else if (vy > 5)
		{
			reward += 0;
		}
		else if (vy > 3)
		{
			reward += 0.05;
		}
		else if (vy > 1)
		{
			reward += 0.1;
		}
		else if (vy > 0.5)
		{
			reward += 0.2;
		}
		else if (vy > 0.2)
		{
			reward += 0.6;
		}
		else if (vy > -0.2)
		{
			reward += 3;
		}
		else if (vy < -5)
		{
			reward += 0;
		}
		else if (vy < -3)
		{
			reward += 0.05;
		}
		else if (vy < -1)
		{
			reward += 0.1;
		}
		else if (vy < -0.5)
		{
			reward += 0.2;
		}
		else if (vy < -0.2)
		{
			reward += 0.6;
		}	
				
		return reward;
	}

	// ///////////////////////////////////////////////////////////
	// discretize() performs a uniform discretization of the
	// value parameter.
	// It returns an integer between 0 and nrValues-1.
	// The min and max parameters are used to specify the interval
	// for the discretization.
	// If the value is lower than min, 0 is returned
	// If the value is higher than min, nrValues-1 is returned
	// otherwise a value between 1 and nrValues-2 is returned.
	//
	// Use discretize2() if you want a discretization method that does
	// not handle values lower than min and higher than max.
	// ///////////////////////////////////////////////////////////
	public static int discretize(double value, int nrValues, double min,
			double max) {
		if (nrValues < 2) {
			return 0;
		}

		double diff = max - min;

		if (value < min) {
			return 0;
		}
		if (value > max) {
			return nrValues - 1;
		}

		double tempValue = value - min;
		double ratio = tempValue / diff;

		return (int) (ratio * (nrValues - 2)) + 1;
	}

	// ///////////////////////////////////////////////////////////
	// discretize2() performs a uniform discretization of the
	// value parameter.
	// It returns an integer between 0 and nrValues-1.
	// The min and max parameters are used to specify the interval
	// for the discretization.
	// If the value is lower than min, 0 is returned
	// If the value is higher than min, nrValues-1 is returned
	// otherwise a value between 0 and nrValues-1 is returned.
	// ///////////////////////////////////////////////////////////
	public static int discretize2(double value, int nrValues, double min,
			double max) {
		double diff = max - min;

		if (value < min) {
			return 0;
		}
		if (value > max) {
			return nrValues - 1;
		}

		double tempValue = value - min;
		double ratio = tempValue / diff;

		return (int) (ratio * nrValues);
	}

}
