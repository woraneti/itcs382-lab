package bikeshare;

public enum MemberType {
    STUDENT(2), STAFF(3);

    private final int bahtPerMinute;

    MemberType(int bahtPerMinute) {
        this.bahtPerMinute = bahtPerMinute;
    }

    public long fee(int minutes) {
        if (minutes <= 0) {
            throw new IllegalArgumentException("Minutes must be positive.");
        }
        return (long) minutes * bahtPerMinute;
    }
}
