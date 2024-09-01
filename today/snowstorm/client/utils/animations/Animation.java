package today.snowstorm.client.utils.animations;

import today.snowstorm.client.utils.other.TimerUtil;

public abstract class Animation {

    public TimerUtil timerUtil = new TimerUtil();
    protected int duration;
    protected double endPoint;
    protected Direction direction;

    public Animation(int ms, double endPoint) {
        this.duration = ms;
        this.endPoint = endPoint;
        this.direction = Direction.FORWARDS;
    }


    public Animation(int ms, double endPoint, Direction direction) {
        this.duration = ms; //Time in milliseconds of how long you want the animation to take.
        this.endPoint = endPoint; //The desired distance for the animated object to go.
        this.direction = direction; //Direction in which the graph is going. If backwards, will start from endPoint and go to 0.
    }

    public boolean finished(Direction direction) {
        return isDone() && this.direction.equals(direction);
    }

    public double getLinearOutput() {
        return 1 - ((timerUtil.getMs() / (double) duration) * endPoint);
    }

    public double getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(double endPoint) {
        this.endPoint = endPoint;
    }

    public void reset() {
        timerUtil.reset();
    }

    public boolean isDone() {
        return timerUtil.stopAfter(duration, false);
    }

    public void changeDirection() {
        if(direction.equals(Direction.FORWARDS)) {
            setDirection(Direction.BACKWARDS);
        } else {
            setDirection(Direction.FORWARDS);
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        if (this.direction != direction) {
            this.direction = direction;
            timerUtil.setMs((long) (System.currentTimeMillis() - (duration - Math.min(duration, timerUtil.getMs()))));
        }
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    protected boolean correctOutput() {
        return false;
    }

    public double getOutput() {
        if (direction == Direction.FORWARDS) {
            if (isDone())
                return endPoint;
            return (getEquation(timerUtil.getMs()) * endPoint);
        } else {
            if (isDone()) return 0;
            if (correctOutput()) {
                double revTime = Math.min(duration, Math.max(0, duration - timerUtil.getMs()));
                return getEquation(revTime) * endPoint;
            } else return (1 - getEquation(timerUtil.getMs())) * endPoint;
        }
    }


    //This is where the animation equation should go, for example, a logistic function. Output should range from 0 - 1.
    //This will take the timer's time as an input, x.
    protected abstract double getEquation(double x);

}