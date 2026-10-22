package bikeshare;

public record Ride(
        String memberId,
        MemberType memberType,
        String bikeId,
        Station origin,
        Station destination,
        int minutes
) {
    public Ride {
        if (memberId == null || memberId.isBlank()
                || bikeId == null || bikeId.isBlank()
                || memberType == null
                || origin == null || destination == null) {
            throw new IllegalArgumentException("Ride details are required.");
        }
        if (minutes <= 0) {
            throw new IllegalArgumentException("Minutes must be positive.");
        }
    }

    public long fee() {
        return memberType.fee(minutes);
    }
}
