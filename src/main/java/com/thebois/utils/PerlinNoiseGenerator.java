package com.thebois.utils;

/**
 * Generator used to generate Perlin Noise.
 */
public class PerlinNoiseGenerator {

    private int octaves = 1;
    private int currentOctave;
    private double amplitude;
    private double frequencyOffSet;
    private double persistence;
    private int seed;
    private final int variable1 = 57;
    private final int variable2 = 13;
    private final int variable3 = 0x7fffffff;
    private final int variable4 = 1073741824;
    private final int[] primeNumberArray = {
        15731,
        789221,
        1376312589,
        547063007,
        522882781,
        286040101,
        628595861,
        426993577,
        360599501,
        795799423,
        185183639,
        719087363,
        249311663,
        };

    /**
     * Instantiate a Perlin Noise Generator with given arguments.
     *
     * @param octaves     A number equals or greater than 1, used for deciding how many times the
     *                    noise should be added to it self.
     * @param amplitude   The amplitude used to amplify the resulting noise.
     * @param frequency   Used to decide how big the square should be.
     * @param persistence Used to decided much the noise should change per addition. A number over 1
     *                    would increase the change per addition and under 1 would decrease.
     * @param seed        A number used to generate the random table.
     */
    public PerlinNoiseGenerator(final int octaves,
                                final double amplitude,
                                final double frequency,
                                final double persistence,
                                final int seed) {
        setOctaves(octaves);
        this.amplitude = amplitude;
        this.frequencyOffSet = frequency;
        this.persistence = persistence;
        setSeed(seed);
    }

    /**
     * Instantiate a Perlin Noise Generator with default settings equals to 1.
     */
    public PerlinNoiseGenerator() {
        this(1, 1, 1, 1, 1);
    }

    /**
     * Generates a float value with Perlin Noise algorithm given a position.
     *
     * @param coordinateX X coordinate for the position.
     * @param coordinateY Y coordinate for the position.
     *
     * @return The Perlin Noise value.
     */
    public float perlinNoise(final float coordinateX, final float coordinateY) {
        double total = 0;

        for (int i = 0; i < octaves; i++) {
            currentOctave = i;
            final double octaveAmplification = Math.pow(persistence, i);
            final double frequency = this.frequencyOffSet * Math.pow(2, i);

            total = total + interpolateNoise(coordinateX * frequency + seed,
                                             coordinateY * frequency + seed)
                            * octaveAmplification;
        }
        return (float) (total * this.amplitude);
    }

    private double interpolateNoise(final double coordinateX, final double coordinateY) {
        // Convert to integer
        final int integerX = (int) coordinateX;
        final int integerY = (int) coordinateY;

        // Get fractal
        final double fractalX = coordinateX - integerX;
        final double fractalY = coordinateY - integerY;

        // Get gradient Vectors
        final double vector1 = smoothNoise(integerX, integerY);
        final double vector2 = smoothNoise(integerX + 1, integerY);
        final double vector3 = smoothNoise(integerX, integerY + 1);
        final double vector4 = smoothNoise(integerX + 1, integerY + 1);

        // Interpolate Vectors
        final double interpolation1 = interpolate(vector1, vector2, fractalX);
        final double interpolation2 = interpolate(vector3, vector4, fractalX);

        return interpolate(interpolation1, interpolation2, fractalY);
    }

    /**
     * Linear Interpolation between two values.
     *
     * @param value1     The first value to interpolate.
     * @param value2     The second value to interpolate.
     * @param alphaValue A value that scale the interpolation.
     *
     * @return The interpolated value.
     */
    private double interpolate(final double value1, final double value2, final double alphaValue) {
        return value1 + alphaValue * (value2 - value1);
    }

    private double smoothNoise(final int coordinateX, final int coordinateY) {
        // Corners
        final double corner1 = noise(coordinateX - 1, coordinateY - 1);
        final double corner2 = noise(coordinateX - 1, coordinateY + 1);
        final double corner3 = noise(coordinateX + 1, coordinateY - 1);
        final double corner4 = noise(coordinateX + 1, coordinateY + 1);

        final double corners = (corner1 + corner2 + corner3 + corner4) / 16;

        // Sides
        final double side1 = noise(coordinateX - 1, coordinateY);
        final double side2 = noise(coordinateX + 1, coordinateY);
        final double side3 = noise(coordinateX, coordinateY - 1);
        final double side4 = noise(coordinateX, coordinateY + 1);

        final double sides = (side1 + side2 + side3 + side4) / 8;

        // Center
        final double center = noise(coordinateX, coordinateY) / 4;

        return center + sides + corners;
    }

    /**
     * Generates a pseudo random value with given position and current octave.
     *
     * @param coordinateX X coordinate for the position.
     * @param coordinateY Y coordinate for the position.
     *
     * @return The generated noise.
     */
    private double noise(final int coordinateX, final int coordinateY) {
        int temporaryValue1;
        int temporaryValue2;
        final int resetValue = primeNumberArray.length / 3;

        final int primeNumber1 = primeNumberArray[currentOctave % resetValue];
        final int primeNumber2 = primeNumberArray[1 + currentOctave % resetValue];
        final int primeNumber3 = primeNumberArray[2 + currentOctave % resetValue];

        temporaryValue1 = coordinateX + coordinateY * variable1;
        temporaryValue1 = (temporaryValue1 << variable2) ^ temporaryValue1;
        temporaryValue2 = temporaryValue1 * temporaryValue1 * primeNumber1 + primeNumber2;
        temporaryValue2 = temporaryValue1 * temporaryValue2 + primeNumber3;

        temporaryValue1 = temporaryValue2 & variable3;
        // Result = 1 - T1 * V7
        final double result = 1.0f - temporaryValue1 / (double) variable4;

        return result;
    }

    // Getters and Setters

    public int getOctaves() {
        return octaves;
    }

    /**
     * Sets the Octaves setting to given int if int is equals or lager than 1.
     *
     * @param octaves A number equals or greater than 1, used for deciding how many times the *
     *                noise should be added to it self.
     */
    public void setOctaves(final int octaves) {
        if (octaves >= 1) {
            this.octaves = octaves;
        }
    }

    public double getAmplitude() {
        return amplitude;
    }

    /**
     * Sets the amplitude value to given float value.
     *
     * @param amplitude The amplitude used to amplify the resulting noise. This value should not be
     *                  0.
     */
    public void setAmplitude(final double amplitude) {
        this.amplitude = amplitude;
    }

    public double getFrequencyOffSet() {
        return frequencyOffSet;
    }

    /**
     * Sets the frequency value to given float value.
     *
     * @param frequencyOffSet The frequency value is used to decide how one value can change from
     *                        another. This value should be between 0 and 1.
     */
    public void setFrequencyOffSet(final double frequencyOffSet) {
        this.frequencyOffSet = frequencyOffSet;
    }

    public double getPersistence() {
        return persistence;
    }

    /**
     * Sets the persistence value to given float value.
     *
     * @param persistence The persistence value used to decide how much different octaves affect the
     *                    resulting noise. This value should not be 0.
     */
    public void setPersistence(final double persistence) {
        this.persistence = persistence;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(final int seed) {
        this.seed = 2 * seed * seed;
    }

}
