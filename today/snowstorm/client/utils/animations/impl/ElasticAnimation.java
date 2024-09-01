package today.snowstorm.client.utils.animations.impl;

import today.snowstorm.client.utils.animations.Animation;

public class ElasticAnimation extends Animation {

    private final float easeAmount;
    private final float smooth;
    private final boolean reallyElastic;

    public ElasticAnimation(int ms, double endPoint, float elasticity, float smooth, boolean moreElasticity) {
        super(ms, endPoint);
        this.easeAmount = elasticity;
        this.smooth = smooth;
        this.reallyElastic = moreElasticity;
    }

    @Override
    protected double getEquation(double x) {
        double x1 = Math.pow(x / duration, smooth);
        double elasticity = easeAmount * 0.1f;
        return Math.pow(2, -10 * (reallyElastic ? Math.sqrt(x1) : x1)) * Math.sin((x1 - (elasticity / 4)) * ((2 * Math.PI) / elasticity)) + 1;
    }
}